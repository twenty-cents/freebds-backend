package com.freebds.backend.mapper;

import com.freebds.backend.common.web.EntityDTOMapper;
import com.freebds.backend.dto.GraphicNovelDTO;
import com.freebds.backend.model.GraphicNovel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface GraphicNovelMapper extends EntityDTOMapper<GraphicNovel, GraphicNovelDTO> {

    GraphicNovelMapper INSTANCE = Mappers.getMapper(GraphicNovelMapper.class);

    GraphicNovelDTO toDTO(GraphicNovel graphicNovel);

    GraphicNovel toEntity(GraphicNovelDTO graphicNovelDTO);

}
