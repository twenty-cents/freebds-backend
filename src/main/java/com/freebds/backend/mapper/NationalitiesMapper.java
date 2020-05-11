package com.freebds.backend.mapper;

import com.freebds.backend.common.web.serie.resources.NationalitiesResource;

import java.util.List;

public class NationalitiesMapper {

    public static NationalitiesResource toResource(List<String> nationalities) {
        NationalitiesResource nationalitiesResource = new NationalitiesResource();
        nationalitiesResource.setNationalities(nationalities);
        return nationalitiesResource;
    }

}
