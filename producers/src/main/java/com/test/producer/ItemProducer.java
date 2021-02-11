package com.test.producer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.domain.Item;
import com.test.serializer.ItemSerializer;

public class ItemProducer {

	private static String topicName = "item-topic";

	private KafkaProducer<Integer, Item> itemProducer;
	
	private static final Logger LOG = LoggerFactory.getLogger(ItemProducer.class);
	
	private Callback callback = (metadata, exception) -> {
		if (exception != null) {
			LOG.error("Exception sending message {}", exception.getMessage(), exception);
		} else {
			LOG.info("Partition: {}, offset: {}",metadata.partition(),metadata.offset());
		}
	};

	public ItemProducer(Map<String, Object> propsMap) {
		itemProducer = new KafkaProducer<>(propsMap);
	}
	
	 public void close(){
	        itemProducer.close();
	    }

	public static Map<String, Object> propsMap() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ItemSerializer.class.getName());
		
		// 0, 1 or all. 
		// 0 = i don't care if this worked
		// 1 = leader must have gotten the record
		// all = all replicas need to be in sync (exact number in broker setting min.insync.replicas)
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		
		props.put(ProducerConfig.RETRIES_CONFIG, 10); // number of retries
		props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000); // retry every 3000ms
		return props;

	}
	
	public void publishMessageAsync(Item value) {
		ProducerRecord<Integer, Item> record = new ProducerRecord<Integer, Item>(topicName, value.getId(), value);
		itemProducer.send(record, callback);
	}

	public void publishMessage(Item value) {
		ProducerRecord<Integer, Item> record = new ProducerRecord<>(topicName, value.getId(), value);
		try {
			RecordMetadata metadata =  itemProducer.send(record).get();
			LOG.info("Message {} sent with key {}.", record.value(), record.key());
			LOG.info("Partition: {}, offset: {}",metadata.partition(),metadata.offset());
		} catch (Exception  e) {
			LOG.error("Exception sending message {}", e.getMessage(), e);
		} 
	}

	public static void main(String[] args) throws InterruptedException {
		if (args.length > 0) {
			topicName = args[0];
		}
		
		ItemProducer producer = new ItemProducer(propsMap());
		
		Item item1 = new Item(1,"Test", 5d);
		Item item2 = new Item(2,"Test2", 50d);
		List.of(item1,item2).forEach( item -> {
			producer.publishMessage(item);	
		});
	}

}
