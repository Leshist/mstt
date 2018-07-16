package http

import kafka.KafkaAuthProducerService
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

class AuthHttp(kafka: KafkaAuthProducerService) {
  def route: Route = (path("auth") & get & parameters('email, 'password)) { (email, password) =>
    complete(createPlayerAuthUUID(email, password))
  }
  def createPlayerAuthUUID(email: String, password: String): String = kafka.publishAuthEvent(email, password)
}
