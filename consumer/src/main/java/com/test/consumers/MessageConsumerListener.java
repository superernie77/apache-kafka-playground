package com.test.consumers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.listeners.ConsumerRebalanceListenerImpl;


public class MessageConsumerListener {

	private static final Logger logger = LoggerFactory.getLogger(MessageConsumerListener.class);

	private KafkaConsumer<String, String> kafkaConsumer;

	private String topicName = "test-topic";
	
	private Map<TopicPartition, OffsetAndMetadata > offsetmap = new HashMap<>();

	public MessageConsumerListener(Map<String, Object> propsMap) {
		kafkaConsumer = new KafkaConsumer<>(propsMap);
	}

	public static Map<String, Object> buildConsumerProperties() {
		Map<String, Object> propsMap = new HashMap<String, Object>();

		propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
		propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "messageconsumer");
		
		// max interval without poll before a rebalance is triggered. Default is 5min
		propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "5000");
		
		// enable manual commit -> done in listener when partition changes
		propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		
		return propsMap;
	}

	public void pollKafka() {
		kafkaConsumer.subscribe(List.of(topicName), new ConsumerRebalanceListenerImpl(kafkaConsumer));
		Duration timeOut = Duration.of(100, ChronoUnit.MILLIS);
		try {
			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(timeOut);
				records.forEach((record) -> {
					logger.info("Consumer Record Key is {} and the value is {} and the partition is {}", record.key(),
							record.value(), record.partition());
					
					// save processed records in map
					offsetmap.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset()+1, null));
				});
				kafkaConsumer.commitSync();
			}
		} catch (Exception ex) {
			logger.error("exception during poll.");
		} finally {
			kafkaConsumer.close();
		}

	}

	public static void main(String[] args) {
		MessageConsumerListener messageConsumer = new MessageConsumerListener(buildConsumerProperties());
		messageConsumer.pollKafka();
	}

}


