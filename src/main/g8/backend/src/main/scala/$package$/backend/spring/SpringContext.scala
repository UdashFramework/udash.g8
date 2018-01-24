package $package$.backend.spring

import com.avsystem.commons.spring.HoconBeanDefinitionReader
import com.typesafe.config.ConfigFactory
import org.springframework.context.support.GenericApplicationContext

object SpringContext {
  /** Reads system configuration from "beans.conf" file. */
  def createApplicationContext(configName: String): GenericApplicationContext = {
    val ctx = new GenericApplicationContext()
    val bdr = new HoconBeanDefinitionReader(ctx)
    bdr.loadBeanDefinitions(ConfigFactory.load(configName))
    ctx.refresh()
    ctx
  }
}
