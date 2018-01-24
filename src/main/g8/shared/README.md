# Shared (JVM/JS)

The sources defined in this module are cross-compiled to Bytecode and JavaScript. The `backend` and `frontend`
modules depend on this one, so everything you define here can be used in both the server and the client code.

You can find four main packages in the sources of this module:
* `$package$.shared.css` - definition of application stylesheets.
* `$package$.shared.i18n` - definition of translations used in the application.
* `$package$.shared.model` - model classes which are useful in the RPC communication.
* `$package$.shared.rpc` - RPC interfaces for bidirectional client-server communication.

## CSS

The definition of CSS styles in Udash is based on the [ScalaCSS](https://github.com/japgolly/scalacss) library.
It provides type-safe stylesheets definition with automatic class names generation. Unfortunately ScalaCSS 
generates a lot of JavaScript code and significantly reduces application start-up performance. 
Udash provides tools for server-side CSS rendering with type-safe class references. 
It allows to keep benefits of ScalaCSS DSL and decrease JS size and initialization time. 

You can find more details regarding stylesheets definitions in 
[Udash Guide](http://guide.udash.io/#/frontend/templates) and [ScalaCSS docs](https://github.com/japgolly/scalacss).

## I18n

Udash provides tools for a type-safe translations management. Besides simple translation key declaration, you can 
define the count and the types of arguments passed to the translation. 

```scala
// notice that this translation takes one Int argument
val connections = key1[Int]("chat.connections")
val inputPlaceholder = key("chat.input.placeholder")
```

You can find more details regarding translations in [Udash Guide](http://guide.udash.io/#/ext/i18n).

## Model

With Scala.js you can share your data model classes between the client and the server application. In this case 
we cross-compile classes describing user's context and permissions, chat message and exceptions used for RPC 
calls failures.

The `SharedExceptions` class extends `ExceptionCodecRegistry` - it provides `GenCodec`s for the shared exceptions, so
these can be serialized and passed to the client as a result of the RPC call. 

Udash Guide describes [the auth utilities](http://guide.udash.io/#/ext/authorization) used in `UserContext` and 
[the exceptions handling in RPC](http://guide.udash.io/#/rpc/client-server).

## RPC

Interfaces are the most important part of the Udash RPC system. Thanks to the cross compilation, 
they make client-server communication easy to develop and maintain. 

You can find two types of RPC interfaces in Udash:
* RPC - the RPC interface exposed by the server-side - `MainServerRPC`.
* Client RPC - the RPC interface exposed by the client-side - `MainClientRPC`.

Methods exposed by the RPC interface can be divided into three groups:
* Calls - methods returning `Future[T]` where `T` is a serializable type (a client RPC interface cannot expose these methods).
* Fires - methods with a return type `Unit`, there is no guarantee that your request will be received by a recipient.
* Getters - methods returning another RPC interface, calling this method does not send anything over network.

Both call and fire methods are asynchronous. Call will complete a returned Future when response is received. 
Fire is a *fire&forget* method, there is no acknowledgement that the request reached its recipient.

The RPC system uses some macro-generated code. To keep the JavaScript code as small as possible and make compilation faster, 
for each RPC interface create a companion object extending the `RPCCompanion` class from the RPC framework you use. 
The RPC framework describes supported RPC methods and serialization methods. 
In this case we use `DefaultClientUdashRPCFramework` for the client interfaces and 
`DefaultServerUdashRPCFramework` for the server API. 

Read more about RPC interfaces in [Udash Guide](http://guide.udash.io/#/rpc/interfaces).

## What's next?

Now you are familiar with the shared parts of code. It's time to take a look at the `backend` module.  