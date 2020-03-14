package com.freebds.backend.mapper;

import com.freebds.backend.dto.AuthorDTO;
import com.freebds.backend.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mappings({
        @Mapping(target="birthdate", source = "author.birthdate", dateFormat = "dd-MM-yyyy", defaultValue = ""),
        @Mapping(target="deceaseDate", source = "author.deceaseDate", dateFormat = "dd-MM-yyyy", defaultValue = "")
    })
    AuthorDTO authorToAuthorDTO(Author author);

    Author authorDTOToAuthor(AuthorDTO authorDTO);
}
