package $package$.frontend.services

import $package$.shared.model.auth.UserContext
import $package$.shared.model.SharedExceptions
import $package$.shared.rpc.server.MainServerRPC
import $package$.shared.rpc.server.secured.SecuredRPC

import scala.concurrent.{ExecutionContext, Future}

/** Holds an active `UserContext` and provides access to the `SecuredRPC` with a current `UserToken`. */
class UserContextService(rpc: MainServerRPC)(implicit ec: ExecutionContext) {
  private var userContext: Option[UserContext] = None

  def currentContext: Option[UserContext] = userContext
  def getCurrentContext: UserContext = userContext.getOrElse(throw new SharedExceptions.UnauthorizedException)

  /** Sends login request and saves returned context. */
  def login(username: String, password: String): Future[UserContext] = {
    if (userContext.isDefined) Future.successful(userContext.get)
    else {
      rpc.auth().login(username, password).map { ctx =>
        userContext = Some(ctx)
        ctx
      }
    }
  }

  /** Provides access to SecuredRPC with current UserToken. */
  def securedRpc(): Option[SecuredRPC] =
    userContext.map(ctx => rpc.secured(ctx.token))
}
