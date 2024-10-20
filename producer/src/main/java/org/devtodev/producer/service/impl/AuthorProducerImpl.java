package org.devtodev.producer.service.impl;

import org.devtodev.dto.AuthorRawDto;
import org.devtodev.producer.service.AuthorProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthorProducerImpl implements AuthorProducer {
    private final KafkaTemplate<String, AuthorRawDto> kafkaTemplate;
    @Value("${devtodev.kafka.topic.input.name}")
    private String topic;

    public AuthorProducerImpl(KafkaTemplate<String, AuthorRawDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(AuthorRawDto authorRawDto) {
        kafkaTemplate.send(this.topic, authorRawDto);
    }
}
