package com.freebds.backend.mapper;

import com.freebds.backend.common.web.serie.resources.OriginsResource;

import java.util.List;

public class OriginsMapper {

    public static OriginsResource toResource(List<String> origins) {
        OriginsResource originsResource = new OriginsResource();
        originsResource.setOrigins(origins);
        return originsResource;
    }
}
