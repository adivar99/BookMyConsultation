package com.bmc.notificationservice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public class NotificationServiceApplication {

	public static void main(String[] args) {

        System.out.println("=================================================");
        System.out.println("CONSUMERRRRRR");
        System.out.println("=================================================");

        Properties props = new Properties();

        //Update the IP adress of Kafka server here//

        props.setProperty("bootstrap.servers", "kafka:9092");

        props.setProperty("group.id", "sweethome");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        System.out.println(props);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("message"));
        //Prints the topic subscription list
        Set<String> subscribedTopics = consumer.subscription();
        for(String topic : subscribedTopics) {
        	System.out.println(topic);
        }
        
        
        try {
        	while(true) {
        		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        		for(ConsumerRecord<String, String> record : records) {
                    System.out.println("=================================================");
        			System.out.println(record.value());
                    System.out.println("=================================================");
        		}
        	}
        }finally {
			consumer.close();
		}

	}

}
