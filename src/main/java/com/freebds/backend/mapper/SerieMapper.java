package com.freebds.backend.mapper;

import com.freebds.backend.common.web.EntityDTOMapper;
import com.freebds.backend.dto.SerieDTO;
import com.freebds.backend.model.Serie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SerieMapper extends EntityDTOMapper<Serie, SerieDTO> {

    SerieMapper INSTANCE = Mappers.getMapper(SerieMapper.class);


    @Mappings({
            @Mapping(target="siteUrl", source = "serie.siteUrl", defaultValue = ""),
            @Mapping(target="pageThumbnailUrl", source = "serie.pageThumbnailUrl", defaultValue = ""),
            @Mapping(target="pageUrl", source = "serie.pageUrl", defaultValue = "")
    })
    SerieDTO toDto(Serie serie);

    Serie toEntity(SerieDTO serieDTO);

}
