package com.test.consumers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.tools.packager.Log;

public class MessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

	private KafkaConsumer<String, String> kafkaConsumer;

	private String topicName = "test-topic";

	public MessageConsumer(Map<String, Object> propsMap) {
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
		
		// milliseconds until messages are marked as commited between pools
		// if this is too big, messages might be procressed twice if consumer crashes
		propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 10000);

		return propsMap;
	}

	public void pollKafka() {
		kafkaConsumer.subscribe(List.of(topicName));
		Duration timeOut = Duration.of(100, ChronoUnit.MILLIS);
		try {
			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(timeOut);
				records.forEach((record) -> {
					logger.info("Consumer Record Key is {} and the value is {} and the partition is {}", record.key(),
							record.value(), record.partition());
				});
			}
		} catch (Exception ex) {
			logger.error("exception during poll.");
		} finally {
			kafkaConsumer.close();
		}

	}

	public static void main(String[] args) {
		MessageConsumer messageConsumer = new MessageConsumer(buildConsumerProperties());
		messageConsumer.pollKafka();
	}

}


