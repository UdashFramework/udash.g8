package $package$.shared.rpc.server.open

import $package$.shared.model.auth.UserContext
import io.udash.rpc._

import scala.concurrent.Future

@RPC
trait AuthRPC {
  /** Verifies provided username and password.
    * Returns a UserContext if provided data is valid. */
  def login(username: String, password: String): Future[UserContext]
}

object AuthRPC extends DefaultServerUdashRPCFramework.RPCCompanion[AuthRPC]
