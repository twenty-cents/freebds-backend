package com.freebds.backend.mapper;

import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelResource;
import com.freebds.backend.model.GraphicNovel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface GraphicNovelMapper {

    GraphicNovelMapper INSTANCE = Mappers.getMapper(GraphicNovelMapper.class);

    GraphicNovelResource toResource(GraphicNovel graphicNovel);

}
