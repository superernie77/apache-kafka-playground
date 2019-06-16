Navigate to [KAFKA HOME]\bin\windows directory.

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