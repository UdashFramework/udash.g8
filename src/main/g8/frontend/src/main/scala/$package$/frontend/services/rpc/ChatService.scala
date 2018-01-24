package $package$.frontend.services.rpc

import $package$.shared.model.chat.ChatMessage
import $package$.shared.rpc.client.chat.ChatNotificationsRPC
import io.udash.utils.CallbacksHandler

class ChatService(
  msgListeners: CallbacksHandler[ChatMessage],
  connectionsListeners: CallbacksHandler[Int]
) extends ChatNotificationsRPC {
  override def newMessage(msg: ChatMessage): Unit =
    msgListeners.fire(msg)

  override def connectionsCountUpdate(count: Int): Unit =
    connectionsListeners.fire(count)
}
