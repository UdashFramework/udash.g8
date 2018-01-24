package $package$.shared.rpc.server

import $package$.shared.model.auth.UserToken
import $package$.shared.rpc.server.open.AuthRPC
import $package$.shared.rpc.server.secured.SecuredRPC
import io.udash.i18n._
import io.udash.rpc._

@RPC
trait MainServerRPC {
  /** Returns AuthRPC implementation. */
  def auth(): AuthRPC
  /** Verifies provided UserToken and returns SecuredRPC if token is valid. */
  def secured(token: UserToken): SecuredRPC
  /** Returns interface serving translations from server resources. */
  def translations(): RemoteTranslationRPC
}

object MainServerRPC extends DefaultServerUdashRPCFramework.RPCCompanion[MainServerRPC]