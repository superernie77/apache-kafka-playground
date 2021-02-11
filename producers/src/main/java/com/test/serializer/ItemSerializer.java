package com.test.serializer;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.domain.Item;

public class ItemSerializer implements Serializer<Item>{
	
	private static final Logger logger = LoggerFactory.getLogger(ItemSerializer.class);

	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public byte[] serialize(String arg0, Item item) {
		try {
			return mapper.writeValueAsBytes(item);
		} catch (JsonProcessingException e) {
			logger.error("Error during serialization : {}", item, e);
			return null;
		}
	}

}
