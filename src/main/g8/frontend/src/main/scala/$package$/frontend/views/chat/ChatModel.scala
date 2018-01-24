package $package$.frontend.views.chat

import $package$.shared.model.chat.ChatMessage
import io.udash._

case class ChatModel(msgs: Seq[ChatMessage], msgInput: String, connectionsCount: Int)
object ChatModel extends HasModelPropertyCreator[ChatModel]
