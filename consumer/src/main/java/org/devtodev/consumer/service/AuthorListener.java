package org.devtodev.consumer.service;

import org.devtodev.dto.AuthorRawDto;

public interface AuthorListener {
    void process(AuthorRawDto authorRawDto);
}
