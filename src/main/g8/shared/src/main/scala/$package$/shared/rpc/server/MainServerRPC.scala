package $package$.shared.rpc.server

import $package$.shared.model.auth.UserToken
import $package$.shared.rpc.server.open.AuthRPC
import $package$.shared.rpc.server.secure.SecureRPC
import io.udash.i18n._
import io.udash.rpc._

trait MainServerRPC {
  /** Returns an RPC for authentication. */
  def auth(): AuthRPC

  /** Verifies provided UserToken and returns a [[$package$.shared.rpc.server.secure.SecureRPC]] if the token is valid. */
  def secure(token: UserToken): SecureRPC

  /** Returns an RPC serving translations from the server resources. */
  def translations(): RemoteTranslationRPC
}

object MainServerRPC extends DefaultServerUdashRPCFramework.RPCCompanion[MainServerRPC]