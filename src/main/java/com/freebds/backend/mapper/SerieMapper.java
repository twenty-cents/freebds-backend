package com.freebds.backend.mapper;

import com.freebds.backend.common.web.serie.resources.SerieResource;
import com.freebds.backend.model.Serie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface SerieMapper {

    SerieMapper INSTANCE = Mappers.getMapper(SerieMapper.class);


    @Mappings({
            @Mapping(target="siteUrl", source = "serie.siteUrl", defaultValue = ""),
            @Mapping(target="pageThumbnailUrl", source = "serie.pageThumbnailUrl", defaultValue = ""),
            @Mapping(target="pageUrl", source = "serie.pageUrl", defaultValue = "")
    })

    SerieResource toResource(Serie serie);

}
