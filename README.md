Get your latest Kafka distribution here:
```
https://kafka.apache.org/downloads
```
Unzip your downloaded tar.gz to a folder of your choice.
Next, navigate to [KAFKA HOME]\bin\windows directory.
For Windows users, use Powershell not CMD to execute the following commands. CMD has a maximum command length, which is too small to execute the zookeper start Java command.


Start Zookeeper:
```
.\zookeeper-server-start.bat ..\..\config\zookeeper.properties
```
Next add these two lines to the Kafka the server.properties:
```
listeners=PLAINTEXT://:9092
auto.create.topics.enable=false 
```
Start Kafka Broker:
```
.\kafka-server-start.bat ..\..\config\server.properties
```

Create a topic:
```
.\kafka-topics.bat --create --topic <topic-name> -zookeeper localhost:2181 --replication-factor 1 --partitions 1
```
Create Console-Producer for Topic:
```
.\kafka-console-producer.bat --broker-list localhost:9092 --topic <topic-name>
```
or with key:
```
.\kafka-console-producer.bat --broker-list localhost:9092 --topic <topic-name> --property "key.separator=-" --property "parse.key=true"
```
Create consumer for topic:
```
.\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic <topic-name> --from-beginning
```
or with key:
```
.\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic <topic-name> --from-beginning -property "key.separator= - " --property "print.key=true"
```

Command to List all topics:
```
./kafka-topics.sh --zookeeper localhost:2181 --list
```
Command to List consumers:
```
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```