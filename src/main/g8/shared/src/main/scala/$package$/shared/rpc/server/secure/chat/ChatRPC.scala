package $package$.shared.rpc.server.secure.chat

import $package$.shared.model.chat.ChatMessage
import io.udash.rpc._

import scala.concurrent.Future

trait ChatRPC {
  /** Registers a new message on server. */
  def sendMsg(msg: String): Future[Unit]

  /** Returns a list of registered messages. */
  def latestMessages(): Future[Seq[ChatMessage]]

  /** Returns the number of the currently authenticated users. */
  def connectedClientsCount(): Future[Int]
}

object ChatRPC extends DefaultServerUdashRPCFramework.RPCCompanion[ChatRPC]
