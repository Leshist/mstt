package datastore

import java.util.UUID
import java.util.Date

import io.getquill.{LowerCase, MysqlAsyncContext}

import scala.concurrent.ExecutionContext.Implicits.global

class PlayerLoginService {
  private val db = new MysqlAsyncContext[LowerCase]("db")
  import db._

  case class PlayerAuth(email: String, password: String, UUID: String)

  def storePlayerAuthRecord(uuid: UUID, email: String, password: String): Unit = {
    val record = PlayerAuth(email = email, password = password, uuid.toString)
    db.run(query[PlayerAuth].insert(lift(record)))
  }

  def close(): Unit = db.close()
}
