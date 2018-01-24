package $package$.backend

import java.util.concurrent.TimeUnit

import $package$.backend.server.ApplicationServer
import $package$.backend.spring.SpringContext
import io.udash.logging.CrossLogging

import scala.io.StdIn

object Launcher extends CrossLogging {
  def main(args: Array[String]): Unit = {
    val startTime = System.nanoTime

    val ctx = SpringContext.createApplicationContext("beans.conf")
    val server = ctx.getBean(classOf[ApplicationServer])
    server.start()

    val duration: Long = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - startTime)
    logger.info(s"Application started in \${duration}s.")

    // wait for user input and then stop the server
    logger.info("Click `Enter` to close application...")
    StdIn.readLine()
    server.stop()
  }
}