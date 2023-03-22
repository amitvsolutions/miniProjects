package com.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
	
	@Bean
	public NewTopic boMasterTopic() {
		return TopicBuilder.name("boMaster").build();
	}
	
	@Bean
	public NewTopic boMasterJsonTopic() {
		return TopicBuilder.name("boMaster_Json").build();
	}

}
