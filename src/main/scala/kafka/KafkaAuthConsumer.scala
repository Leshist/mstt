package kafka

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, Extractor, KafkaConsumerActor, Offsets}
import com.lambdaworks.jacks.JacksMapper
import com.typesafe.config.Config
import datastore.PlayerLoginService
import kafka.commands.AuthEvent
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object KafkaAuthConsumer {

  def apply(config: Config): ActorRef = {
    val consumerConf = KafkaConsumer.Conf(
      new StringDeserializer,
      new StringDeserializer,
      groupId = config.getString("kafka.group.id"),
      enableAutoCommit = false,
      autoOffsetReset = OffsetResetStrategy.EARLIEST,
      bootstrapServers = config.getString("kafka.bootstrap.servers"))
      .withConf(config.getConfig("kafka"))

    val actorConf = KafkaConsumerActor.Conf(1.seconds, 3.seconds)
    val db = new PlayerLoginService

    val system = ActorSystem()
    system.actorOf(Props(new KafkaAuthConsumer(consumerConf, actorConf, db)))
  }
}

class KafkaAuthConsumer(
  kafkaConfig: KafkaConsumer.Conf[String, String],
  actorConfig: KafkaConsumerActor.Conf,
  db: PlayerLoginService) extends Actor with ActorLogging {

  val recordsExt: Extractor[Any, ConsumerRecords[String, String]] = ConsumerRecords.extractor[String, String]

  val consumer: ActorRef = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )

  consumer ! Subscribe.ManualOffset(Offsets(Map((new TopicPartition("auth", 0), 1))))

  override def receive: Receive = {
    case recordsExt(records) =>
      val _sender = sender()
      processRecords(records)
      _sender ! Confirm(records.offsets)
  }

  private def processRecords(records: ConsumerRecords[String, String]) = {
    records.pairs.foreach { case (_, value) =>

      val msg: Try[AuthEvent] = Try(JacksMapper.readValue[AuthEvent](value))
      msg match {
        case Success(authEvent) =>
          log.info(s"successfully parsed $authEvent storing into db now")
          db.storePlayerAuthRecord(authEvent.uuid, email = authEvent.email, password = authEvent.password)
        case Failure(e) => log.info(s"failed to parse msg: $value with error $e")
      }
    }
  }
}



