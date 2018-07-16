import com.typesafe.config.{Config, ConfigFactory}
import kafka.KafkaAuthConsumer

import scala.io.StdIn


object StartConsumerAndDb extends App {
  val config: Config = ConfigFactory.load()
  // wait for kafka to start
  Thread.sleep(config.getLong("appstart.wait"))

  val consumer = KafkaAuthConsumer(config)
  StdIn.readLine()
}
