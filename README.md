# kafka
a sample about spring cloud stream on kafka.

## shell
nohup consul/consul agent -dev -client=0.0.0.0 &

nohup kafka_2.12-2.3.0/bin/zookeeper-server-start.sh kafka_2.12-2.3.0/config/zookeeper.properties &

nohup kafka_2.12-2.3.0/bin/kafka-server-start.sh kafka_2.12-2.3.0/config/server.properties &

## start command
nohup java -jar gateway-0.0.1-SNAPSHOT.jar --server.port=5000 --spring.profiles.active=prod &

----
nohup java -jar producer-0.0.1-SNAPSHOT.jar --server.port=5100 --spring.cloud.stream.bindings.output.producer.partitionKeyExpression=payload.id --spring.cloud.stream.bindings.output.producer.partitionCount=2 --spring.profiles.active=prod &

nohup java -jar producer-0.0.1-SNAPSHOT.jar --server.port=5101 --spring.cloud.stream.bindings.output.producer.partitionKeyExpression=payload.id --spring.cloud.stream.bindings.output.producer.partitionCount=2 --spring.profiles.active=prod &

----
nohup java -jar consumer-0.0.1-SNAPSHOT.jar --server.port=5200 --spring.cloud.stream.bindings.input.group=group1 --spring.cloud.stream.bindings.input.consumer.partitioned=true --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=0 --spring.profiles.active=prod &

nohup java -jar consumer-0.0.1-SNAPSHOT.jar --server.port=5201 --spring.cloud.stream.bindings.input.group=group1 --spring.cloud.stream.bindings.input.consumer.partitioned=true --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=1 --spring.profiles.active=prod &

----
nohup java -jar consumer-0.0.1-SNAPSHOT.jar --server.port=5202 --spring.cloud.stream.bindings.input.group=group2 --spring.cloud.stream.bindings.input.consumer.partitioned=true --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=0 --spring.profiles.active=prod &

nohup java -jar consumer-0.0.1-SNAPSHOT.jar --server.port=5203 --spring.cloud.stream.bindings.input.group=group2 --spring.cloud.stream.bindings.input.consumer.partitioned=true --spring.cloud.stream.instanceCount=2 --spring.cloud.stream.instanceIndex=1 --spring.profiles.active=prod &

## redis clear command
redis-cli keys "com.nordsoft.streams.log*" |xargs redis-cli del

