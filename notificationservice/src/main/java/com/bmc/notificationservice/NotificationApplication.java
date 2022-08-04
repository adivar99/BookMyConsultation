package com.bmc.notificationservice;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationApplication implements CommandLineRunner {

    @Autowired
    ModelMapper modelMapper;

    private final SesEmailVerificationService verifyEmail;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=================================================");
        System.out.println("CONSUMERRRRRR");
        System.out.println("=================================================");

        Properties props = new Properties();

        //Update the IP adress of Kafka server here//
        props.setProperty("bootstrap.servers", "ec2-52-90-136-134.compute-1.amazonaws.com:9092");

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
        for (String topic : subscribedTopics) {
            System.out.println(topic);
        }


        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                if (records.count() == 0) {
                    continue;
                }
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("=================================================");
                    System.out.println("RECORD: " + record.key() + " = " + record.value());
                    System.out.println("=================================================");
                    if (record.key().contains(":")) {
                        String keyCategory = record.key().split(":", 0)[0];
                        String keyEmail = record.key().split(":", 0)[1];
                        System.out.println("KAFKA KEY: " + keyCategory + " for email: " + keyEmail);
                        System.out.println(Objects.equals(keyCategory, "doctorApproval"));
                        if (Objects.equals(keyCategory, "doctorCreate")) {
                            // Send verification email
                            verifyEmail.init("verify");
                            verifyEmail.verifyEmail(keyEmail);
                        } else if (Objects.equals(keyCategory, "doctorApproval")) {
                            // Send email
                            verifyEmail.init("send");
                            Doctor doc = modelMapper.map(record.value(), Doctor.class);
                            try {
                                System.out.println("Trying to send email: "+doc.toString());
                                verifyEmail.sendSimpleMessage(keyEmail,"Welcome email", record.value());
                            } catch (MessagingException e) {
                                System.out.println("Email Error: " + e.getMessage());
                            }
                        } else if (Objects.equals(keyCategory, "userCreate")) {
                            // Send verification email
                            verifyEmail.init("verify");
                            verifyEmail.verifyEmail(keyEmail);
                        } else if (Objects.equals(keyCategory, "setAppointment")) {
                            // Send email
                            verifyEmail.init("send");
                            try {
                                verifyEmail.sendSimpleMessage(keyEmail, "Set Appointment", record.value());
                            } catch (MessagingException m) {
                                System.out.println("Email Error: " + m.getMessage());
                            }
                        } else if (Objects.equals(keyCategory, "prescription")) {
                            // Send email
                            // Send email
                            verifyEmail.init("send");
                            try {
                                verifyEmail.sendSimpleMessage(keyEmail, "Prescription", record.value());
                            } catch (MessagingException m) {
                                System.out.println("Email Error: " + m.getMessage());
                            }
                        }
                    }
                }
            }
        } finally {
            consumer.close();
        }

    }
}
