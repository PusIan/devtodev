package org.devtodev.producer.service;

import org.devtodev.dto.AuthorRawDto;

public interface AuthorProducer {
    void send(AuthorRawDto authorRawDto);
}
