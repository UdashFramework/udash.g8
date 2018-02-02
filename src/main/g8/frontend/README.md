# Frontend (JS)

This module contains all the code which is compiled to JavaScript and served as client's application.

The code is separated into three packages:
* `$package$.frontend.routing` - contains all the code related to the frontend application routing.
* `$package$.frontend.services` - business logic of the client's application.
* `$package$.frontend.views` - code describing views and their behaviour.

The root package (`$package$.frontend`) contains also `JSLauncher` and `ApplicationContext`. The former 
initializes the whole application inside `div` with `id = application`. The latter keeps references 
to the global services instances, yet we use it only in the class resolving `ViewFactories` 
for the current application state in order to stay away from global variables.

Read more about bootstrapping the frontend application in [Udash Guide](http://guide.udash.io/#/bootstrapping/frontend).

## Services

The services keep separated the business logic of the client's application from the rest of the code. 
`UserContextService` holds an active `UserContext` and provides access to the `SecureRPC` with 
a current `UserToken`. `TranslationsService` keeps entities responsible for translations: 
`TranslationProvider` and `LangProperty` (more about i18n in [Udash Guide](http://guide.udash.io/#/ext/i18n)). 
`RPCService` implements the `MainClientRPC` interface and is responsible for handling notifications from the server.
`NotificationsCenter` allows the other system components to register a callback and receive notification
about new message or connection.  

Read more about server notifications in [Udash Guide](http://guide.udash.io/#/rpc/server-client).

## States, Routing and ViewFactories

A Udash application is based on states. The application state describes the created `ViewFactories`
structure and is determined by a URL. The URL is resolved to a `RoutingState` on every change. 
States tend to form a nested hierarchy.  With `ContainerState` and `FinalState` you can express 
the place of a state in the hierarchy.

In `RoutingRegistryDef` you have to assign URLs to one of the application states. 
`StatesToViewFactoryDef` resolves `ViewFactory` based on the provided `State`, it's also a good place
to inject dependencies from `ApplicationContext` to the view.

Read more about routing in [Udash Guide](http://guide.udash.io/#/frontend/routing).

## Views

A single page in Udash application is based on four elements:
* Model - based on the [Properties](http://guide.udash.io/#/frontend/properties) mechanism, it provides one and two-ways bindings to DOM elements.
* View - and creates a [Scalatags](https://github.com/lihaoyi/scalatags) template with a method getting a child view to render.
* Presenter - it should contain a business logic of the related view. It also handles application state changes.
* ViewFactory - responsible for creating a model, a view and a presenter. 

Read more about views modelling in [Udash Guide](http://guide.udash.io/#/frontend/mvp).

As you might noticed, the main model of the view is usually a `ModelProperty`. This kind of property
allows you to represent your data model hierarchically with a case classes, standard classes and traits.
In order to use a type `T` as a model hierarchy template, you have to create an implicit `ModelPropertyCreator[T]` instance 
for this type. The easiest way is to create companion object of this type extending `HasModelPropertyCreator[T]`.
You can also define an implicit val and assign to it the result of `ModelPropertyCreator.materialize[T]`.

Read more about properties in [Udash Guide](http://guide.udash.io/#/frontend/properties).

## What's next?

Now you probably read all the READMEs in this application. Look around this codebase. 
The code itself is documented, so we hope you will understand it without problems.

You should also take a look at [Udash Developers Guide](http://guide.udash.io/). If you have any questions,
you can ask for help on ours [Gitter channel](https://gitter.im/UdashFramework/udash-core).