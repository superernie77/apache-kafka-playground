package com.test.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProducer {

	private static String topicName = "test-topic";

	private KafkaProducer<String, String> kafkaProducer;
	
	private static final Logger LOG = LoggerFactory.getLogger(MessageProducer.class);
	
	private Callback callback = (metadata, exception) -> {
		if (exception != null) {
			LOG.error("Exception sending message {}", exception.getMessage(), exception);
		} else {
			LOG.info("Partition: {}, offset: {}",metadata.partition(),metadata.offset());
		}
	};

	public MessageProducer(Map<String, Object> propsMap) {
		kafkaProducer = new KafkaProducer<String, String>(propsMap);
	}

	private static Map<String, Object> propsMap() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return props;

	}
	
	public void publishMessageAsync(String key, String value) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, key, value);
		kafkaProducer.send(record, callback);
	}

	public void publishMessage(String key, String value) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, key, value);
		try {
			RecordMetadata metadata =  kafkaProducer.send(record).get();
			LOG.info("Message {} sent with key {}.", record.value(), record.key());
			LOG.info("Partition: {}, offset: {}",metadata.partition(),metadata.offset());
		} catch (Exception  e) {
			LOG.error("Exception sending message {}", e.getMessage(), e);
		} 
	}

	public static void main(String[] args) {
		
		if (args.length > 0) {
			topicName = args[0];
		}
		
		MessageProducer producer = new MessageProducer(propsMap());
		producer.publishMessage(null, "Test");
		
		producer.publishMessageAsync(null, "Test Async");;
	}

}
