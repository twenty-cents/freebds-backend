package com.freebds.backend.mapper;

import com.freebds.backend.common.web.serie.resources.StatusResource;

import java.util.List;

public class StatusMapper {

    public static StatusResource toResource(List<String> status) {
        StatusResource statusResource = new StatusResource();
        statusResource.setStatus(status);
        return statusResource;
    }
}
