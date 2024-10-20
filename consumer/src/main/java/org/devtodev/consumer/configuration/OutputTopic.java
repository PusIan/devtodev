package org.devtodev.consumer.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutputTopic {
    @Value("${devtodev.kafka.topic.output.name}")
    private String topic;

    @Value("${devtodev.kafka.topic.output.numberofpartitions}")
    private int numberOfPartitions;

    @Value("${devtodev.kafka.topic.output.replicationfactor}")
    private int replicationFactor;

    @Bean
    public NewTopic topic1() {
        return new NewTopic(topic, numberOfPartitions, (short) replicationFactor);
    }
}
