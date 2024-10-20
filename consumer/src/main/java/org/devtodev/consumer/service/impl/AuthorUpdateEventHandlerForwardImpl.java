package org.devtodev.consumer.service.impl;

import org.devtodev.consumer.mapper.AuthorMapper;
import org.devtodev.consumer.repository.model.Author;
import org.devtodev.consumer.service.AuthorUpdateEventHandler;
import org.devtodev.dto.AuthorFullDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthorUpdateEventHandlerForwardImpl implements AuthorUpdateEventHandler {
    private final KafkaTemplate<String, AuthorFullDto> kafkaTemplate;
    private final AuthorMapper authorMapper;
    @Value("${devtodev.kafka.topic.output.name}")
    private String topic;

    public AuthorUpdateEventHandlerForwardImpl(KafkaTemplate<String, AuthorFullDto> kafkaTemplate, AuthorMapper authorMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.authorMapper = authorMapper;
    }

    @Override
    public void handleAuthorUpdateEvent(Author author) {
        AuthorFullDto authorFullDto = authorMapper.toAuthorFullDto(author);
        kafkaTemplate.send(topic, authorFullDto);
    }
}
