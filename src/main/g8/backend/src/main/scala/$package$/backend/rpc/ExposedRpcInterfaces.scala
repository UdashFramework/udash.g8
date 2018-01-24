package $package$.backend.rpc

import $package$.backend.rpc.auth.AuthEndpoint
import $package$.backend.rpc.i18n.TranslationsEndpoint
import $package$.backend.rpc.secured.SecuredEndpoint
import $package$.backend.services.DomainServices
import $package$.shared.model.auth.{UserContext, UserToken}
import $package$.shared.model.SharedExceptions
import $package$.shared.rpc.server.MainServerRPC
import $package$.shared.rpc.server.open.AuthRPC
import $package$.shared.rpc.server.secured.SecuredRPC
import io.udash.i18n.RemoteTranslationRPC
import io.udash.rpc._

class ExposedRpcInterfaces(implicit domainServices: DomainServices, clientId: ClientId) extends MainServerRPC {
  // required domain services are implicitly passed to the endpoints
  import domainServices._

  private lazy val authEndpoint: AuthRPC = new AuthEndpoint

  // it caches SecuredEndpoint for a single UserToken (UserToken change is not an expected behaviour)
  private var securedEndpointCache: Option[(UserToken, SecuredEndpoint)] = None
  private def securedEndpoint(implicit ctx: UserContext): SecuredRPC = {
    securedEndpointCache match {
      case Some((token, endpoint)) if token == ctx.token =>
        endpoint
      case None =>
        val endpoint = new SecuredEndpoint
        securedEndpointCache = Some((ctx.token, endpoint))
        endpoint
    }
  }

  override def auth(): AuthRPC = authEndpoint

  override def secured(token: UserToken): SecuredRPC = {
    authService
      .findUserCtx(token)
      .map(ctx => securedEndpoint(ctx))
      .getOrElse(throw SharedExceptions.UnauthorizedException())
  }

  override def translations(): RemoteTranslationRPC = TranslationsEndpoint
}