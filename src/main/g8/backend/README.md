# Backend (JVM)

The code from this module is responsible for handling clients' RPC requests, serving static files 
and rendering CSS files.

You can find five main packages in the sources of this module:
* `$package$.backend.css` - the code rendering the styles defined in the `shared` module.
* `$package$.backend.rpc` - an implementation of the server RPC interfaces.
* `$package$.backend.server` - the code setting up [Jetty](https://www.eclipse.org/jetty/) with required servlets.
* `$package$.backend.services` - services encapsulating the main business logic of the application.
* `$package$.backend.spring` - utilities handling [Spring](https://spring.io/) dependency injection.

## RPC and Services

The main responsibility of the server application is to handle RPC calls from client applications. 
The implementation is separated into two layers:
* RPC endpoints - created separately for each client connection which are a direct implementation of the RPC interfaces;
* services - created once as beans in the [Spring](https://spring.io/) application context.

The endpoints are a good place to resolve `UserContext` or verify user's permissions. The services usually 
contain business logic reusable between endpoints and other entry points of the app e.g. REST API.

Read more about the RPC interfaces in the [Udash Guide](http://guide.udash.io/#/rpc).

## Server and configuration

This application uses [Spring](https://spring.io/) for dependency injection with an extension from 
[AVSystem Commons](https://github.com/AVSystem/scala-commons), which allows us to write configuration
using the [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) format. The configuration files are localized 
in the resource bundles stored in `backend/src/main/resources`. This directory also contains translation bundles and basic 
[Logback](https://logback.qos.ch/) configuration. 

The application configuration is separated into three files:
* `application.conf` - application configuration variables e.g. the user list or the web server port.
* `beans.conf` - the main configuration file which contains web server bean definition.
* `services.conf` - definition of services beans.

As you can see in the `Launcher` object, it loads configuration from `beans.conf` and starts `ApplicationServer`. 
The `ApplicationServer` class creates two servlets: the first serves static files like compiled JS or CSS sources, 
the second is responsible for handling WebSocket connections from the client applications. The aforementioned 
servlets are registered in a [Jetty](https://www.eclipse.org/jetty/) server. 

Read more about bootstrapping the backend application in [Udash Guide](http://guide.udash.io/#/bootstrapping/backend).

## CSS

As mentioned in the `shared` module's README, the styles definitions are not compiled into JavaScript code. 
You have to render it with the JVM code and serve as static file - it makes the client's JavaScript source 
much smaller and faster.  

The class `CssRenderer` is started from the `compileCss` SBT task. You don't have to restart application server
to refresh them, just run `sbt compileCss` (or `compileStatics` which depends on `compileCss`) 
and refresh the page in the browser. 

Read more about Udash CSS in [Udash Guide](http://guide.udash.io/#/frontend/templates).

## What's next?

Now you are familiar with the `backend` module. It's time to take a look at the client application in the 
`frontend` module.