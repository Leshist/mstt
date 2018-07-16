package kafka.commands

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath
import play.api.libs.json.Reads._

case class AuthEvent(
  timestamp: Long,
  uuid: UUID,
  email: String,
  password: String) {
  require(timestamp > 0)
}

object AuthEvent {
  implicit val AuthEventFormat = (
    (JsPath \ "timestamp").format[Long](min(0L)) and
      (JsPath \ "uuid").format[UUID] and
      (JsPath \ "email").format[String] and
      (JsPath \ "password").format[String]
    ) (AuthEvent.apply, unlift(AuthEvent.unapply))
}
