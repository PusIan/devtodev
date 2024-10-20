package org.devtodev.consumer.service.impl;

import org.devtodev.consumer.repository.AuthorRepository;
import org.devtodev.consumer.repository.model.Author;
import org.devtodev.consumer.service.AuthorListener;
import org.devtodev.consumer.service.AuthorUpdateEventHandler;
import org.devtodev.consumer.util.DistributedLock;
import org.devtodev.consumer.util.UniqueIdGenerator;
import org.devtodev.dto.AuthorRawDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorListenerImpl implements AuthorListener {
    private final AuthorRepository authorRepository;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final DistributedLock distributedLock;
    private final List<AuthorUpdateEventHandler> authorUpdateEventHandlerList;

    public AuthorListenerImpl(AuthorRepository authorRepository,
                              UniqueIdGenerator uniqueIdGenerator, DistributedLock distributedLock, List<AuthorUpdateEventHandler> authorUpdateEventHandlerList) {
        this.authorRepository = authorRepository;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.distributedLock = distributedLock;
        this.authorUpdateEventHandlerList = authorUpdateEventHandlerList;
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "${devtodev.kafka.topic.input.name}")
    @Override
    public void process(AuthorRawDto authorRawDto) {
        if (authorRepository.getAuthorByAuthorName(authorRawDto.authorName()).isEmpty()) {
            try {
                if (distributedLock.acquireLock(authorRawDto.authorName())) {
                    if (authorRepository.getAuthorByAuthorName(authorRawDto.authorName()).isEmpty()) {
                        Author author = Author.builder()
                                .authorName(authorRawDto.authorName())
                                .id(uniqueIdGenerator.generateUniqueId()).build();
                        authorRepository.save(author);
                    }
                }
            } finally {
                distributedLock.releaseLock(authorRawDto.authorName());
            }
        }
        Author author = authorRepository.getAuthorByAuthorName(authorRawDto.authorName())
                .orElseThrow(() -> new RuntimeException(String.format("author: %s was not saved", authorRawDto.authorName())));
        for (AuthorUpdateEventHandler authorUpdateEventHandler : authorUpdateEventHandlerList) {
            authorUpdateEventHandler.handleAuthorUpdateEvent(author);
        }
    }
}
