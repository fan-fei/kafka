# kafka
a sample about spring cloud stream on kafka.

## start command
java -jar  ~/Documents/IdeaProjects/kafka/gateway/target/gateway-0.0.1-SNAPSHOT.jar --server.port=5000  

----
java -jar  ~/Documents/IdeaProjects/kafka/producer/target/producer-0.0.1-SNAPSHOT.jar --server.port=5100  --spring.cloud.stream.bindings.output.producer.partitionKeyExpression=payload.id --spring.cloud.stream.bindings.output.producer.partitionCount=2

java -jar  ~/Documents/IdeaProjects/kafka/producer/target/producer-0.0.1-SNAPSHOT.jar --server.port=5101  --spring.cloud.stream.bindings.output.producer.partitionKeyExpression=payload.id --spring.cloud.stream.bindings.output.producer.partitionCount=2

----
java -jar  ~/Documents/IdeaProjects/kafka/consumer/target/consumer-0.0.1-SNAPSHOT.jar --server.port=5200  --spring.cloud.stream.bindings.input.group=group1 --spring.cloud.stream.bindings.input.consumer.partitioned=true  --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=0

java -jar  ~/Documents/IdeaProjects/kafka/consumer/target/consumer-0.0.1-SNAPSHOT.jar --server.port=5201  --spring.cloud.stream.bindings.input.group=group1 --spring.cloud.stream.bindings.input.consumer.partitioned=true  --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=1

----
java -jar  ~/Documents/IdeaProjects/kafka/consumer/target/consumer-0.0.1-SNAPSHOT.jar --server.port=5202  --spring.cloud.stream.bindings.input.group=group2 --spring.cloud.stream.bindings.input.consumer.partitioned=true  --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=0

java -jar  ~/Documents/IdeaProjects/kafka/consumer/target/consumer-0.0.1-SNAPSHOT.jar --server.port=5203  --spring.cloud.stream.bindings.input.group=group2 --spring.cloud.stream.bindings.input.consumer.partitioned=true  --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=1

## redis clear command
redis-cli keys "com.nordsoft.streams.log*" |xargs redis-cli del

## shell
nohup /usr/local/services/kafka_2.12-2.3.0/bin/zookeeper-server-start.sh /usr/local/services/kafka_2.12-2.3.0/config/zookeeper.properties &
nohup /usr/local/services/kafka_2.12-2.3.0/bin/kafka-server-start.sh /usr/local/services/kafka_2.12-2.3.0/config/server.properties &