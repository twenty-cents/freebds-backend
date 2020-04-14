package com.freebds.backend.mapper;

import com.freebds.backend.dto.LanguageDTO;

import java.util.List;

public class LanguageMapper {

    public static LanguageDTO toDTO(List<String> languages) {
        LanguageDTO languageDTO = new LanguageDTO();
        languageDTO.setLanguages(languages);
        return languageDTO;
    }
}
