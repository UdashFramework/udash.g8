package $package$.backend.rpc.secure

import $package$.backend.rpc.secure.chat.ChatEndpoint
import $package$.backend.services.DomainServices
import $package$.shared.model.auth.UserContext
import $package$.shared.rpc.server.secure.SecureRPC
import $package$.shared.rpc.server.secure.chat.ChatRPC

class SecureEndpoint(implicit domainServices: DomainServices, ctx: UserContext) extends SecureRPC {
  import domainServices._

  lazy val chatEndpoint = new ChatEndpoint

  override def chat(): ChatRPC = chatEndpoint
}
