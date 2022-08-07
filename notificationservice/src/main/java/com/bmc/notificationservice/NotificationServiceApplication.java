package com.bmc.notificationservice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
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

        props.setProperty("bootstrap.servers", "ec2-3-84-59-47.compute-1.amazonaws.com:9092");

        props.setProperty("group.id", "bookMyConsultation");
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
                if (records.count() == 0) {
                    continue;
                }
        		for(ConsumerRecord<String, String> record : records) {
                    System.out.println("=================================================");
        			System.out.println("RECORD: "+record.key()+" = "+record.value());
                    System.out.println("=================================================");
                    if(record.key() == "doctorCreate") {
                        // Send verification email
                    } else if(record.key() == "doctorApproval") {
                        // Send email
                    } else if(record.key() == "userCreate") {
                        // Send verification email
                    } else if(record.key() == "setAppointment") {
                        // Send email
                    } else if(record.key() == "prescription") {
                        // Send email
                    }
        		}
        	}
        }finally {
			consumer.close();
		}

	}

}
