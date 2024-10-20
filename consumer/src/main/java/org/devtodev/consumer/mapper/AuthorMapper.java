package org.devtodev.consumer.mapper;

import org.devtodev.consumer.repository.model.Author;
import org.devtodev.dto.AuthorFullDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    public AuthorFullDto toAuthorFullDto(Author author);
}
