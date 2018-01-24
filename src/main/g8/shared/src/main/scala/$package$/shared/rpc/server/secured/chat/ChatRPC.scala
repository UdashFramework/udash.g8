package $package$.shared.rpc.server.secured.chat

import $package$.shared.model.chat.ChatMessage
import io.udash.rpc._

import scala.concurrent.Future

@RPC
trait ChatRPC {
  /** Registers new message on server. */
  def sendMsg(msg: String): Future[Unit]
  /** Returns a list of registered messages. */
  def latestMessages(): Future[Seq[ChatMessage]]
  /** Returns a count of the currently authenticated users. */
  def connectedClientsCount(): Future[Int]
}

object ChatRPC extends DefaultServerUdashRPCFramework.RPCCompanion[ChatRPC]
