package $package$.shared.model.chat

import java.util.Date

import com.avsystem.commons.serialization.HasGenCodec

/** Contains everything what is needed to display message in GUI chat window. */
case class ChatMessage(text: String, author: String, created: Date)
object ChatMessage extends HasGenCodec[ChatMessage]
