import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import org.scalajs.jsdependencies.sbtplugin.JSDependenciesPlugin.autoImport._
import sbt._

object Dependencies {
  val versionOfScala = "$scala_version$"

  // Udash
  val udashVersion = "$udash_version$"
  val udashJQueryVersion = "3.2.0"

  // Backend
  val jettyVersion = "9.4.51.v20230217"
  val logbackVersion = "1.3.5"
  val typesafeConfigVersion = "1.4.2"

  // JS dependencies
$if(macrotask_executor.truthy)$
  val macrotaskExecutorVersion = "1.1.1"
$endif$
  val bootstrapVersion = "4.1.3"

  // Testing
  val scalatestVersion = "3.2.15"
  val scalamockVersion = "5.2.0"

  // Dependencies for both frontend and backend
  // Those have to be cross-compilable
  val crossDeps = Def.setting(Seq(
    "io.udash" %%% "udash-rpc" % udashVersion,
    "io.udash" %%% "udash-rest" % udashVersion,
    "io.udash" %%% "udash-i18n" % udashVersion,
    "io.udash" %%% "udash-css" % udashVersion,
    "io.udash" %%% "udash-auth" % udashVersion,
  ))

  // Dependencies compiled to JavaScript code
  val frontendDeps = Def.setting(Seq(
$if(macrotask_executor.truthy)$
    "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion,
$endif$

    "io.udash" %%% "udash-rpc" % udashVersion,
    "io.udash" %%% "udash-i18n" % udashVersion,
    "io.udash" %%% "udash-css" % udashVersion,
    "io.udash" %%% "udash-auth" % udashVersion,

    // type-safe wrapper for Twitter Bootstrap
    "io.udash" %%% "udash-bootstrap4" % udashVersion,
    // type-safe wrapper for jQuery
    "io.udash" %%% "udash-jquery" % udashJQueryVersion,
  ))

  // JavaScript libraries dependencies
  // Those will be added into frontend-deps.js
  val frontendJSDeps = Def.setting(Seq(
    // "jquery.js" is provided by "udash-jquery" dependency
    "org.webjars" % "bootstrap" % bootstrapVersion / "js/bootstrap.bundle.js"
      minified "js/bootstrap.bundle.min.js" dependsOn "jquery.js",
  ))

  // Dependencies for JVM part of code
  val backendDeps = Def.setting(Seq(
    "io.udash" %% "udash-rpc" % udashVersion,
    "io.udash" %% "udash-rest" % udashVersion,
    "io.udash" %% "udash-i18n" % udashVersion,
    "io.udash" %% "udash-css" % udashVersion,

    "org.eclipse.jetty" % "jetty-server" % jettyVersion,
    "org.eclipse.jetty" % "jetty-rewrite" % jettyVersion,
    "org.eclipse.jetty.websocket" % "websocket-server" % jettyVersion,

    "com.typesafe" % "config" % typesafeConfigVersion,

    // server logging backend
    "ch.qos.logback" % "logback-classic" % logbackVersion,
  ))

  // Test dependencies
  val crossTestDeps = Def.setting(Seq(
    "org.scalatest" %%% "scalatest" % scalatestVersion,
    "org.scalamock" %%% "scalamock" % scalamockVersion
  ).map(_ % Test))
}
