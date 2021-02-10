package com.test.listeners;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.consumers.MessageConsumer;

public class ConsumerRebalanceListenerImpl implements ConsumerRebalanceListener {

	private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

	private KafkaConsumer<String, String> consumer;

	public ConsumerRebalanceListenerImpl(KafkaConsumer<String, String> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		logger.info("onPartAssigned {}", partitions);
		consumer.seekToBeginning(partitions); // read all available messages when assigned newly
	}

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		logger.info("onPartRevoked {}", partitions);
		consumer.commitSync();
		logger.info("Offset Commited");

	}

}
