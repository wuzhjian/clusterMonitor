package monitor.kafka

import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}


/**
  * 手动实现一个KafkaSink,并实例化producer，
  * 将数据发送到对应kafka的topic
  * @param createProducer
  * @tparam K
  * @tparam V
  */
class KafkaSink[K,V](createProducer:() => KafkaProducer[K,V]) extends Serializable {
  // 创建一个生产真
  lazy val producer = createProducer()

  def send(topic: String, key: K, value: V) : Future[RecordMetadata] = {
    producer.send(new ProducerRecord[K, V](topic, key, value))
  }

  def send(topic: String, value: V): Future[RecordMetadata] = {
    producer.send(new ProducerRecord[K, V](topic, value))
  }
}

object KafkaSink{
  import scala.collection.JavaConversions._

  def apply[K, V](config: Map[String, Object]): KafkaSink[K,V] = {
    val createProducerFunc = ()=>{
      val producer = new KafkaProducer[K,V](config)
      sys.addShutdownHook{
        producer.close()
      }
      producer
    }
    // 返回一个新的producer
    new KafkaSink(createProducerFunc)
  }

  def apply[K,V](config: java.util.Properties): KafkaSink[K,V] = apply(config.toMap)
}


