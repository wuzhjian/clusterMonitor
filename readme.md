```$xslt

bin/kafka-topics.sh --create --zookeeper wuzhjian-System-Product-Name:2181 --topic spark-kafka-demo --partitions 1 --replication-factor 1

bin/kafka-topics.sh --create --zookeeper wuzhjian-System-Product-Name:2181 --partitions 1 --replication-factor 1 --topic spark-sink-test


生产者

bin/kafka-console-producer.sh --broker-list wuzhjian-System-Product-Name:9092 --topic spark-kafka-demo

消费者
bin/kafka-console-consumer.sh --bootstrap-server wuzhjian-System-Product-Name:9092 --from-beginning --topic spark-sink-test


测试数据
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}
{"date_dt": "201808081823","relater_name": "make"}


./bin/spark-submit --master spark://wuzhjian-System-Product-Name:7077 --deploy-mode client \
 --executor-memory 2g --total-executor-cores 2  --driver-memory 2G  \
 --class monitor.stream.SparkKafkaDemo /opt/test/monitor-1.0-SNAPSHOT.jar

```