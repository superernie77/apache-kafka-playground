package se77.kafka.producer;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProducerExample {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProducerExample.class);

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> myProducer = new KafkaProducer<String, String>(properties);

		try {
			for (int i = 1; i < 250; i++) {
				ProducerRecord<String, String> record = new ProducerRecord<String, String>("my-topic", "Message Value : " + Integer.toString(i));
				LOG.info("Sending Record:  {}", record);
				myProducer.send(record);
			}
		} catch (Exception ex) {
			LOG.error("Exception sending message to Kafka broker. ",ex);
		} finally {
			myProducer.close();
		}
	}

}