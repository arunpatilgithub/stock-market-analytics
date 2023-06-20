package com.ap.stockmarketanalytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService<T> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaService(KafkaProperties kafkaProperties) {
        this.kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties()));
    }

    public <T> void sendMessage(String topic, T message) {
        kafkaTemplate.send(topic, message);
    }

}
