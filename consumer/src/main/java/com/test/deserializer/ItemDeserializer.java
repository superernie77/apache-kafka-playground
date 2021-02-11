package com.test.deserializer;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.domain.Item;

public class ItemDeserializer implements Deserializer<Item> {

	private static final Logger logger = LoggerFactory.getLogger(ItemDeserializer.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public Item deserialize(String arg0, byte[] arg1) {
		try {
			return mapper.readValue(arg1, Item.class);
		} catch (IOException e) {
			logger.error("Error in deserialize: {}", arg1, e);
			return null;
		}
	}

}
