package $package$.backend.server

import $package$.backend.rpc.ExposedRpcInterfaces
import $package$.backend.services.DomainServices
import $package$.shared.model.SharedExceptions
import $package$.shared.rpc.server.MainServerRPC
import io.udash.rpc._
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}

import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationServer(val port: Int, resourceBase: String, domainServices: DomainServices) {
  private val server = new Server(port)
  private val contextHandler = new ServletContextHandler
  private val appHolder = createAppHolder()
  private val atmosphereHolder = createAtmosphereHolder()

  contextHandler.setSessionHandler(new SessionHandler)
  contextHandler.getSessionHandler.addEventListener(new org.atmosphere.cpr.SessionSupport())
  contextHandler.addServlet(atmosphereHolder, "/atm/*")
  contextHandler.addServlet(appHolder, "/*")
  server.setHandler(contextHandler)

  def start(): Unit = server.start()
  def stop(): Unit = server.stop()

  private def createAtmosphereHolder() = {
    val config = new DefaultAtmosphereServiceConfig((clientId) =>
      // interfaces are cached per user connection
      new DefaultExposesServerRPC[MainServerRPC](
        new ExposedRpcInterfaces()(domainServices, clientId)
      )
    )

    // notify ClientsService about new or closed connections
    config.onNewConnection { case id => domainServices.rpcClientsService.registerConnection(id) }
    config.onClosedConnection { case id => domainServices.rpcClientsService.unregisterConnection(id) }

    val framework = new DefaultAtmosphereFramework(
      config,
      // this is registry with custom exceptions support
      exceptionsRegistry = new SharedExceptions
    )

    val atmosphereHolder = new ServletHolder(new RpcServlet(framework))
    atmosphereHolder.setAsyncSupported(true)
    atmosphereHolder
  }

  private def createAppHolder() = {
    val appHolder = new ServletHolder(new DefaultServlet)
    appHolder.setAsyncSupported(true)
    appHolder.setInitParameter("resourceBase", resourceBase)
    appHolder
  }
}
