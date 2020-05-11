package com.freebds.backend.mapper;

import com.freebds.backend.common.web.author.resources.AuthorResource;
import com.freebds.backend.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorResource toResource(Author author);
}
