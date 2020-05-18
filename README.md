Get your lates Kafka distribution from here:
```
https://kafka.apache.org/downloads
```
Unzip your downloaded tar.gz to a folder of your choice.
Next, navigate to [KAFKA HOME]\bin\windows directory.
For Windows users, use Powershell not CMD to execute the following commands. CMD has a maximum command length, which is too small to execute the zookeper start Java command.


Start Zookeeper:
```
zookeeper-server-start.bat ..\..\config\zookeeper.properties
```

Start Kafka Broker:
```
kafka-server-start.bat ..\..\config\server.properties
```

Create a topic:
```
kafka-topics.bat --create --topic <topic-name> -zookeeper localhost:2181 --replication-factor 1 --partitions 1.
```

Create consumer for topic:
```
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic <topic-name> --from-beginning
```