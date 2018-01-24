package $package$.shared.rpc.client.chat

import $package$.shared.model.chat.ChatMessage
import io.udash.rpc._

@RPC
trait ChatNotificationsRPC {
  /** Notification about new message registered on server side. */
  def newMessage(msg: ChatMessage): Unit
  /** Notification about authenticated connections count change. */
  def connectionsCountUpdate(count: Int): Unit
}

object ChatNotificationsRPC extends DefaultClientUdashRPCFramework.RPCCompanion[ChatNotificationsRPC]
