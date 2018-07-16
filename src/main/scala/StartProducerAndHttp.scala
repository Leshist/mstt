import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import http.AuthHttp
import com.typesafe.config.{Config, ConfigFactory}
import kafka.KafkaAuthProducerService
import scala.concurrent.ExecutionContext.Implicits.global

import scala.io.StdIn


object StartProducerAndHttp extends App {
  val config: Config = ConfigFactory.load()

  // wait for kafka to start
  Thread.sleep(config.getLong("appstart.wait"))

  implicit def myRejectionHandler = RejectionHandler.newBuilder()
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val kafkaService = KafkaAuthProducerService(config)
  val authHttp = new AuthHttp(kafkaService)
  val route = authHttp.route

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
