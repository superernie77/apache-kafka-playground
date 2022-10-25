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
./kafka-topics.bat --zookeeper localhost:2181 --list
```
Command to List consumers:
```
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```
View log File on windows:
```
./kafka-run-class.bat kafka.tools.DumpLogSegments --deep-iteration --files c:/tmp/kafka-logs/<topic-name>-0/00000000000000000000.log
```
How to start a new server instance:
1. copy server.properties under new name (e.g. server-2.properties)
2. Change properties in new file: log.dirs, port number for listener and broker.id
3 Start server with new properties file

How to show topics + partions + in-sync-replicas (ISR)
```
./kafka-topics.bat --zookeeper localhost:2181 -- describe
```
or partitions for a single topic:
```
./kafka-topics.bat --zookeeper localhost:2181 -- describe --topic [topicname]
```


Some interesting facts about Kafka that shouldn't be forgotten:
- Kafka is not a messaging system
- Clients pull records from Kafka
- Order of the messages is guaranteed
- Records are persisted on the harddrive of the broker in a log file
- Records are immutable
- Consumers in the same groups work on the same topic (horizontal scaling)
- Consumer in different groups get all records from a topic
