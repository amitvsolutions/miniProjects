package com.kafka;

public class KafkaDemo {

	public static void main(String[] args) {
		
		// Step-1: https://kafka.apache.org/quickstart [Install, Start, IDE-Plugin*]
		// CMD> .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
		// CMD> .\bin\windows\kafka-server-start.bat .\config\server.properties
		System.out.println("Step-1: Zoopker and Kafka server started.");
		
		// Step-2: Create Topic
		// KafkaProducer --uses---> KafkaTemplate to send message to ---> TOPIC
		// CMD> .\bin\windows\kafka-console-consumer.bat --topic boMaster --from-beginning --bootstrap-server localhost:9092
		System.out.println("Step-2: Create Kafka-Topic, and send message to Topic using Producer/KafkaTemplate.");
		
		// Step-3: Publish/Producer [KAFKA has broker(servers) --has--> Topic --has--> (partition) ]
		
		// Step-4: Consumer
		

	}

}
// https://www.youtube.com/watch?v=yYTLUWnJftYhttps://www.youtube.com/watch?v=yYTLUWnJftY
// GIT: https://github.com/RameshMF/springboot-kafka-course/tree/main/springboot-kafka-tutorial

// Demo-1_Prod/Consumer: https://www.geeksforgeeks.org/spring-boot-kafka-producer-example/
// Demo-2: https://www.geeksforgeeks.org/spring-boot-consume-message-through-kafka-save-into-elasticsearch-and-plot-into-grafana/
// Demo-3: https://www.geeksforgeeks.org/how-to-create-apache-kafka-consumer-with-conduktor/

