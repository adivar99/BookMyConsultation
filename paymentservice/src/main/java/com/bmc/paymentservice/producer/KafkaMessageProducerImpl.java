package com.bmc.paymentservice.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class KafkaMessageProducerImpl implements KafkaMessageProducer{

    @Value("${url.service.kafka}")
    private String kafkaIp;

    @Override
    public void publish(String topic, String key, String value) throws IOException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaIp+":9092");
        properties.put("acks","all");
        properties.put("retries", 0);
        properties.put("linger.ms",0);
        properties.put("partitioner.class","org.apache.kafka.clients.producer.internals.DefaultPartitioner");
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("request.timeout.ms",30000);
        properties.put("timeout.ms",30000);
        properties.put("max.in.flight.requests.per.connection", 5);
        properties.put("retry.backoff.ms",5);

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        // System.out.println(producer.metrics());

        producer.send(new ProducerRecord<String, String>(topic, key, value));
        producer.close();
    }
}
