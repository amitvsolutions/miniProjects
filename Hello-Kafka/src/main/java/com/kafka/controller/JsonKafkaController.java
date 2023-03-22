package com.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.dto.User;
import com.kafka.producer.JsonKafkaProducer;

@RestController
@RequestMapping("/v1/kafka")
public class JsonKafkaController {
	
	@Autowired
	private JsonKafkaProducer kafkaProducer;
	
	@PostMapping("/publish")
	public ResponseEntity<String> publish(@RequestBody User user){
		kafkaProducer.sendJsonMessage(user);
		return ResponseEntity.ok("Json message sent to Kafka-Topic.");
	}

}
