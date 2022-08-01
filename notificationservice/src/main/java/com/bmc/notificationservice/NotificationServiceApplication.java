package com.bmc.notificationservice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;

@SpringBootApplication
public class NotificationServiceApplication {

    @Autowired
    static SesEmailVerificationService verifyEmail;

    @Autowired
    static ModelMapper modelMapper;

    public static void main(String[] args) {

        
        System.out.println("=================================================");
        System.out.println("CONSUMERRRRRR");
        System.out.println("=================================================");

        Properties props = new Properties();

        //Update the IP adress of Kafka server here//

        props.setProperty("bootstrap.servers", "ec2-3-82-201-210.compute-1.amazonaws.com:9092");

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
                    if(record.key().contains(':')) {
                        String keyCategory = record.key().split(":",0)[0];
                        String keyEmail = record.key().split(":",0)[1];
                        System.out.println("KAFKA KEY: "+keyCategory+" for email: "+keyEmail);
                        if(keyCategory == "doctorCreate") {
                            // Send verification email
                            verifyEmail.verifyEmail(keyEmail);
                        } else if(keyCategory == "doctorApproval") {
                            // Send email
                            // verifyEmail.sendEmail(keyEmail);
                            Doctor doc = modelMapper.map(record.value(), Doctor.class);
                            try {
                                verifyEmail.sendEmail(doc);
                            } catch (IOException | TemplateException | MessagingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if(keyCategory == "userCreate") {
                            // Send verification email
                            verifyEmail.verifyEmail(keyEmail);
                        } else if(keyCategory == "setAppointment") {
                            // Send email
                            verifyEmail.sendSimpleMessage(keyEmail, )
                        } else if(keyCategory == "prescription") {
                            // Send email
                        }
                    }
        		}
        	}
        }finally {
			consumer.close();
		}

	}

}
