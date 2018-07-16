package kafka

import java.time.Instant
import java.util.UUID

import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import com.typesafe.config.Config
import org.apache.kafka.common.serialization.StringSerializer
import serializer.JsonSerializer
import kafka.commands.AuthEvent

// our kafka producer, produces AuthEvents for topic from config
class KafkaAuthProducerService(config: Config) {

  private val topic = config.getString("kafka.topic")

  private val producer = KafkaProducer(
    KafkaProducer.Conf(
      bootstrapServers = config.getString("kafka.bootstrap.servers"),
      keySerializer = new StringSerializer,
      valueSerializer = new JsonSerializer[AuthEvent])
  )


  def publishAuthEvent(email: String, password: String): String = {
    val uuid = genUUID
    val ts = Instant.now().toEpochMilli

    producer.send(KafkaProducerRecord(
      topic,
      uuid.toString,
      AuthEvent(ts, uuid, email = email, password = password)))

    uuid.toString
  }

  private def genUUID: UUID = UUID.randomUUID()

  def close(): Unit = producer.close()
}

object KafkaAuthProducerService {
  def apply(config: Config): KafkaAuthProducerService = new KafkaAuthProducerService(config)
}


