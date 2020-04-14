package com.freebds.backend.mapper;

import com.freebds.backend.dto.StatusDTO;

import java.util.List;

public class StatusMapper {

    public static StatusDTO toDTO(List<String> status) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus(status);
        return statusDTO;
    }
}
