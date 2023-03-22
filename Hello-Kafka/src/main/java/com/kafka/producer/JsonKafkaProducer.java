package com.kafka.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.kafka.dto.User;

@Service
public class JsonKafkaProducer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaProducer.class);
	
	@Autowired
	private KafkaTemplate<String, User> kafkaTemplate;
	
	public void sendJsonMessage(User data) {
		LOGGER.info(String.format("Message sent -> %s", data.toString()));
		Message<User> msg = MessageBuilder
						.withPayload(data)
						.setHeader(KafkaHeaders.TOPIC, "boMaster_Json").build();
		
		kafkaTemplate.send(msg);		
	}

}
