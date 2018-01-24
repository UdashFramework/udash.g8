package $package$.frontend.views.chat

import java.util.Date

import $package$.frontend.routing.ChatState
import $package$.frontend.services.UserContextService
import $package$.frontend.services.rpc.NotificationsCenter
import $package$.shared.model.auth.Permission
import $package$.shared.model.chat.ChatMessage
import $package$.shared.rpc.server.secured.chat.ChatRPC
import io.udash._
import io.udash.auth.AuthRequires

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ChatPresenter(
  model: ModelProperty[ChatModel],
  chatRpc: ChatRPC,
  userService: UserContextService,
  notificationsCenter: NotificationsCenter
)(
  implicit ec: ExecutionContext
) extends Presenter[ChatState.type] with AuthRequires {

  requireAuthenticated()(userService.getCurrentContext)

  private val msgCallback = notificationsCenter.onNewMsg { case msg =>
    model.subSeq(_.msgs).append(msg)
  }

  private val connectionsCallback = notificationsCenter.onConnectionsCountChange { case count =>
    model.subProp(_.connectionsCount).set(count)
  }

  override def handleState(state: ChatState.type): Unit = {
    if (hasReadAccess) {
      chatRpc.latestMessages().onComplete {
        case Success(msgs) =>
          model.subSeq(_.msgs).prepend(msgs: _*)
        case Failure(ex) =>
          model.subSeq(_.msgs).set(Seq(ChatMessage(s"ERROR: \${ex.getMessage}", "System", new Date)))
      }
    } else {
      model.subSeq(_.msgs).set(Seq(ChatMessage(s"You don't have access to read the messages.", "System", new Date)))
    }

    chatRpc.connectedClientsCount().onComplete {
      case Success(count) =>
        model.subProp(_.connectionsCount).set(count)
      case Failure(ex) =>
        model.subProp(_.connectionsCount).set(-1)
        model.subSeq(_.msgs).set(Seq(ChatMessage(s"ERROR: \${ex.getMessage}", "System", new Date)))
    }
  }

  override def onClose(): Unit = {
    // remove callbacks from NotificationsCenter before exit
    msgCallback.cancel()
    connectionsCallback.cancel()
  }

  def sendMsg(): Unit = {
    val msgProperty = model.subProp(_.msgInput)
    val msg = msgProperty.get.trim
    msgProperty.set("")
    if (msg.nonEmpty) {
      chatRpc.sendMsg(msg)
    }
  }

  def hasReadAccess: Boolean =
    userService.currentContext.exists(_.has(Permission.ChatRead))

  def hasWriteAccess: Boolean =
    userService.currentContext.exists(_.has(Permission.ChatWrite))

  def hasActiveNotificationsCenterCallbacks: Boolean =
    msgCallback.isActive && connectionsCallback.isActive
}
