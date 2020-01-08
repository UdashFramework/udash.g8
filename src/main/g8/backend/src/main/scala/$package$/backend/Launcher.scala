package $package$.backend

import java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory}
import $package$.backend.server.ApplicationServer
import $package$.backend.services.{AuthService, ChatService, DomainServices, RpcClientsService}
import io.udash.logging.CrossLogging

import scala.io.StdIn

object Launcher extends CrossLogging {
  def main(args: Array[String]): Unit = {
    val startTime = System.nanoTime

    val config: Config = ConfigFactory.load()
    val rpcClientsService: RpcClientsService = new RpcClientsService(RpcClientsService.defaultSendToClientFactory)
    val authService: AuthService = new AuthService(config.getStringList("auth.users"))
    val chatService: ChatService = new ChatService(rpcClientsService)
    val server = new ApplicationServer(
      config.getInt("server.port"),
      config.getString("server.statics"),
      new DomainServices()(authService, chatService, rpcClientsService)
    )
    server.start()

    val duration: Long = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - startTime)
    logger.info(s"Application started in \${duration}s.")

    // wait for user input and then stop the server
    logger.info("Click `Enter` to close application...")
    StdIn.readLine()
    logger.info("Stopping application")
    server.stop()
  }
}
