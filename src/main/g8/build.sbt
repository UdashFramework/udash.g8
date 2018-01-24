import org.scalajs.jsenv.selenium.SeleniumJSEnv
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities

name := "$name;format="normalize"$"

inThisBuild(Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := Dependencies.versionOfScala,
  organization := "$package$",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-language:implicitConversions",
    "-language:existentials",
    "-language:dynamics",
    "-Xfuture",
    "-Xfatal-warnings",
    "-Xlint:_,-missing-interpolator,-adapted-args"
  ),
))

val TestAndCompileDep = "test->test;compile->compile"

// Custom SBT tasks
val copyAssets = taskKey[Unit]("Copies all assets to the target directory.")
val cssDir = settingKey[File]("Target for `compileCss` task.")
val compileCss = taskKey[Unit]("Compiles CSS files.")
val compileStatics = taskKey[File](
  "Compiles JavaScript files and copies all assets to the target directory."
)
val compileAndOptimizeStatics = taskKey[File](
  "Compiles and optimizes JavaScript files and copies all assets to the target directory."
)

// Settings for JS tests run in browser
val browserCapabilities: DesiredCapabilities = {
  // requires ChromeDriver: https://sites.google.com/a/chromium.org/chromedriver/
  val capabilities = DesiredCapabilities.chrome()
  capabilities.setCapability(ChromeOptions.CAPABILITY, {
    val options = new ChromeOptions()
    options.addArguments("--headless", "--disable-gpu")
    options
  })
  capabilities
}

// Reusable settings for all modules
val commonSettings = Seq(
  moduleName := "$name;format="normalize"$-" + moduleName.value,
)

// Reusable settings for modules compiled to JS
val commonJSSettings = Seq(
  emitSourceMaps in Compile := true,
  // enables scalajs-env-selenium plugin
  Test / jsEnv := new SeleniumJSEnv(browserCapabilities),
)

lazy val root = project.in(file("."))
  .aggregate(sharedJS, sharedJVM, frontend, backend, packager)
  .dependsOn(backend)
  .settings(
    publishArtifact := false,
    Compile / mainClass := Some("$package$.backend.Launcher")
  )

lazy val shared = crossProject
  .crossType(CrossType.Pure).in(file("shared"))
  .settings(commonSettings)
  .jsSettings(commonJSSettings)
  .settings(
    libraryDependencies ++= Dependencies.crossDeps.value,
    libraryDependencies ++= Dependencies.crossTestDeps.value
  )

lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js

val frontendWebContent = "UdashStatics/WebContent"
lazy val frontend = project.in(file("frontend"))
  .enablePlugins(ScalaJSPlugin) // enables Scala.js plugin in this module
  .dependsOn(sharedJS % TestAndCompileDep)
  .settings(commonSettings)
  .settings(commonJSSettings)
  .settings(
    libraryDependencies ++= Dependencies.frontendDeps.value,
    jsDependencies ++= Dependencies.frontendJSDeps.value, // native JS dependencies

    // Make this module executable in JS
    Compile / mainClass := Some("$package$.frontend.JSLauncher"),
    scalaJSUseMainModuleInitializer := true,

    // Implementation of custom tasks defined above
    copyAssets := {
      IO.copyDirectory(
        sourceDirectory.value / "main/assets",
        target.value / frontendWebContent / "assets"
      )
      IO.copyFile(
        sourceDirectory.value / "main/assets/index.html",
        target.value / frontendWebContent / "index.html"
      )
    },

    // Compiles CSS files and put them in the target directory
    cssDir := (Compile / fastOptJS / target).value / frontendWebContent / "styles",
    compileCss := Def.taskDyn {
      val dir = (Compile / cssDir).value
      val path = dir.absolutePath
      dir.mkdirs()
      (backend / Compile / runMain).toTask(s" $package$.backend.css.CssRenderer \$path false")
    }.value,

    // Compiles JS files without full optimizations
    compileStatics := { (Compile / fastOptJS / target).value / "UdashStatics" },
    compileStatics := compileStatics.dependsOn(
      Compile / fastOptJS, Compile / copyAssets, Compile / compileCss
    ).value,

    // Compiles JS files with full optimizations
    compileAndOptimizeStatics := { (Compile / fullOptJS / target).value / "UdashStatics" },
    compileAndOptimizeStatics := compileAndOptimizeStatics.dependsOn(
      Compile / fullOptJS, Compile / copyAssets, Compile / compileCss
    ).value,

    // Target files for Scala.js plugin
    Compile / fastOptJS / artifactPath :=
      (Compile / fastOptJS / target).value /
        frontendWebContent / "scripts" / "frontend.js",
    Compile / fullOptJS / artifactPath :=
      (Compile / fullOptJS / target).value /
        frontendWebContent / "scripts" / "frontend.js",
    Compile / packageJSDependencies / artifactPath :=
      (Compile / packageJSDependencies / target).value /
        frontendWebContent / "scripts" / "frontend-deps.js",
    Compile / packageMinifiedJSDependencies / artifactPath :=
      (Compile / packageMinifiedJSDependencies / target).value /
        frontendWebContent / "scripts" / "frontend-deps.js"
  )

lazy val backend = project.in(file("backend"))
  .dependsOn(sharedJVM % TestAndCompileDep)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Dependencies.backendDeps.value,
    Compile / mainClass := Some("$package$.backend.Launcher"),
  )

lazy val packager = project
  .in(file("packager"))
  .dependsOn(backend)
  .enablePlugins(JavaServerAppPackaging)
  .settings(commonSettings)
  .settings(
    normalizedName := "bootstrapping-test",
    Compile / mainClass := (backend / Compile / mainClass).value,

    // add frontend statics to the package
    Universal / mappings ++= {
      import Path.relativeTo
      val frontendStatics = (frontend / Compile / compileAndOptimizeStatics).value
      (frontendStatics.allPaths --- frontendStatics) pair relativeTo(frontendStatics.getParentFile)
    },
  )