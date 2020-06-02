package com.test.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class MessageProducer {

	private String topicName = "test-topic";

	private KafkaProducer<String, String> kafkaProducer;

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

	public void publishMessage(String key, String value) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, key, value);
		try {
			RecordMetadata metadata =  kafkaProducer.send(record).get();
			System.out.println("Partition: "+metadata.partition()+", offset:"+metadata.offset());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		MessageProducer producer = new MessageProducer(propsMap());
		
		producer.publishMessage(null, "Test");
		
	}

}
