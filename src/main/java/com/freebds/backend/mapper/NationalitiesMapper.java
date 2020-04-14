package com.freebds.backend.mapper;

import com.freebds.backend.dto.NationalitiesDTO;

import java.util.List;

public class NationalitiesMapper {

    public static NationalitiesDTO toDTO(List<String> nationalities) {
        NationalitiesDTO nationalitiesDTO = new NationalitiesDTO();
        nationalitiesDTO.setNationalities(nationalities);
        return nationalitiesDTO;
    }
}
