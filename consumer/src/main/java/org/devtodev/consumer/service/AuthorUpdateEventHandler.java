package org.devtodev.consumer.service;

import org.devtodev.consumer.repository.model.Author;

public interface AuthorUpdateEventHandler {
    void handleAuthorUpdateEvent(Author author);
}
