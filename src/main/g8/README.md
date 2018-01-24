# My Udash App

Thank you for choosing Udash! Your Udash Starter Pack is ready to compile and run. 
It contains simple chat application, which presents example usage of the most important 
Udash features, e.g., properties, bidirectional RPC, 
[Bootstrap](https://getbootstrap.com/docs/3.3/) Components, i18n, etc.

## Compile and run

The project configuration is using [SBT](https://www.scala-sbt.org/). We assume that 
you have installed SBT on your system, so when you type `sbt sbtVersion` in your console, 
you should see something like:

```sbtshell
[info] $sbt_version$
```

In our opinion it's the most convenient to keep compilation of the backend and the frontend modules separated. 
This configuration follows this rule, but feel free to change it as you like. 
The compilation of the whole project can be triggered with the following command: 
`sbt compile compileStatics run`. 

* `compile` - compiles all sources with standard Scala compiler.
* `compileStatics` - compiles all static resources of the frontend module, it depends on:
  * `copyAssets` - copies all assets from `frontend/src/main/assets` to the target directory,
  * `compileCss` - starts `CssRenderer` from the backend module and creates CSS files in the target directory,
  * `fastOptJS` - compiles Scala sources to the JavaScript file.
* `compileAndOptimizeStatics` - it's similar to `compileStatics`, but it depends on `fullOptJS`, so it creates minimized JS files.
* `run` - starts main class of the backend module - it runs Jetty server and you can open your application on [http://127.0.0.1:8080](http://127.0.0.1:8080).

The JVM launcher waits for user input and then closes the server, so you can tap Enter to stop it.

When you work on your frontend application, you can keep server running in a SBT session. 
At the same time you can run `sbt ~compileStatics` in another console and get a fresh frontend
build on every sources change. It will also render the CSS files every time.

## The Starter Pack content 

When you open the page in your browser you should see the login page form. In the top-right corner 
you can change the page language. Below you can type user credentials (e.g. `test` and `test`; 
as described in the tooltip next to the "Username" field - credentials are defined in the `application.conf` file)
and go to the chat window. 

On the chat view you can type, send and read messages. You should try to open another browser window and 
check that messages and connections count refresh automatically. Server notifies authenticated clients
about these events via []Server -> Client RPC](http://guide.udash.io/#/rpc/server-client) notification. 
On every login you receive a random set of permissions, so sometimes you might not be able to 
send or read messages.   

The application consists of three modules: `shared`, `frontend` and `backend`. In every module you 
can find another README describing its content. 

Briefly:
* `shared` - contains a global model and a login of the application, it also keeps CSS styles definition and RPC interfaces;
* `backend` - holds the server-side logic, it uses [Spring](https://spring.io/) for DI and [Jetty](https://www.eclipse.org/jetty/) as the servlets container;
* `frontend` -  keeps views and the client's application logic. 

In every module you can find tests based on [ScalaTest](http://www.scalatest.org/) and [ScalaMock](http://scalamock.org/).
The `frontend` and `shared` modules use [scalajs-env-selenium](https://github.com/scala-js/scala-js-env-selenium) in order
to run the tests compiled to JavaScript in a web browser. Take a look at `browserCapabilities` in `build.sbt` - 
it configures the tests to run in Google Chrome with headless mode. 
Make sure you have installed [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/).  

This configuration embraces [SBT Native Packager](https://github.com/sbt/sbt-native-packager) 
in the `packager` module to provide easy deployment process. In this module you can find a customized 
application's configuration. With `sbt packager/universal:packageBin` you can create a zip archive with 
your application and nice starting scripts. Take a look at the 
[SBT Native Packager](https://github.com/sbt/sbt-native-packager) docs for more details. 

## What's next?

Look around this codebase. You can find more detailed READMEs inside the modules directories. 
The code itself is also documented, so we hope you will understand it without problems.
We recommend to start from the `shared` module. 

You should also take a look at [Udash Developers Guide](http://guide.udash.io/). If you have any questions,
you can ask for help on ours [Gitter channel](https://gitter.im/UdashFramework/udash-core).

