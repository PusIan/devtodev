package org.devtodev.consumer.repository;

import org.devtodev.consumer.repository.model.Author;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Cacheable(value = "author", key = "#authorName", unless = "#result == null")
    Optional<Author> getAuthorByAuthorName(String authorName);

    @Cacheable(value = "author", key = "#id", unless = "#result == null")
    Optional<Author> getAuthorById(UUID id);
}
