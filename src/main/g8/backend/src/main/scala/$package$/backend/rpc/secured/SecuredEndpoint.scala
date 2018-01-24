package $package$.backend.rpc.secured

import $package$.backend.rpc.secured.chat.ChatEndpoint
import $package$.backend.services.DomainServices
import $package$.shared.model.auth.UserContext
import $package$.shared.rpc.server.secured.SecuredRPC
import $package$.shared.rpc.server.secured.chat.ChatRPC

class SecuredEndpoint(implicit domainServices: DomainServices, ctx: UserContext) extends SecuredRPC {
  import domainServices._

  lazy val chatEndpoint = new ChatEndpoint

  override def chat(): ChatRPC = chatEndpoint
}
