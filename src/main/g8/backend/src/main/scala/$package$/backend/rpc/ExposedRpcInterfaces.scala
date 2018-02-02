package $package$.backend.rpc

import $package$.backend.rpc.auth.AuthEndpoint
import $package$.backend.rpc.i18n.TranslationsEndpoint
import $package$.backend.rpc.secure.SecureEndpoint
import $package$.backend.services.DomainServices
import $package$.shared.model.auth.{UserContext, UserToken}
import $package$.shared.model.SharedExceptions
import $package$.shared.rpc.server.MainServerRPC
import $package$.shared.rpc.server.open.AuthRPC
import $package$.shared.rpc.server.secure.SecureRPC
import io.udash.i18n.RemoteTranslationRPC
import io.udash.rpc._

class ExposedRpcInterfaces(implicit domainServices: DomainServices, clientId: ClientId) extends MainServerRPC {
  // required domain services are implicitly passed to the endpoints
  import domainServices._

  private lazy val authEndpoint: AuthRPC = new AuthEndpoint

  // it caches SecureEndpoint for a single UserToken (UserToken change is not an expected behaviour)
  private var secureEndpointCache: Option[(UserToken, SecureEndpoint)] = None

  private def secureEndpoint(implicit ctx: UserContext): SecureRPC = {
    secureEndpointCache match {
      case Some((token, endpoint)) if token == ctx.token =>
        endpoint
      case None =>
        val endpoint = new SecureEndpoint
        secureEndpointCache = Some((ctx.token, endpoint))
        endpoint
    }
  }

  override def auth(): AuthRPC = authEndpoint

  override def secure(token: UserToken): SecureRPC = {
    authService
      .findUserCtx(token)
      .map(ctx => secureEndpoint(ctx))
      .getOrElse(throw SharedExceptions.UnauthorizedException())
  }

  override def translations(): RemoteTranslationRPC = TranslationsEndpoint
}