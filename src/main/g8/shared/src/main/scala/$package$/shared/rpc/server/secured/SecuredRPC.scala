package $package$.shared.rpc.server.secured

import $package$.shared.rpc.server.secured.chat.ChatRPC
import io.udash.rpc._

@RPC
trait SecuredRPC {
  def chat(): ChatRPC
}

object SecuredRPC extends DefaultServerUdashRPCFramework.RPCCompanion[SecuredRPC]
