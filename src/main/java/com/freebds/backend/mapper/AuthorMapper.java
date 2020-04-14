package com.freebds.backend.mapper;

import com.freebds.backend.dto.AuthorDTO;
import com.freebds.backend.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO toDTO(Author author);

    Author toEntity(AuthorDTO authorDTO);
}
