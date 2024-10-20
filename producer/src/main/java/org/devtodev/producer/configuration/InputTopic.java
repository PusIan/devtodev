package org.devtodev.producer.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputTopic {
    @Value("${devtodev.kafka.topic.input.name}")
    private String topic;

    @Value("${devtodev.kafka.topic.input.numberofpartitions}")
    private int numberOfPartitions;

    @Value("${devtodev.kafka.topic.input.replicationfactor}")
    private int replicationFactor;

    @Bean
    public NewTopic topic1() {
        return new NewTopic(topic, numberOfPartitions, (short) replicationFactor);
    }
}
