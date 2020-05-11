package com.freebds.backend.mapper;

import com.freebds.backend.common.web.serie.resources.LanguageResource;

import java.util.List;

public class LanguageMapper {

    public static LanguageResource toResource(List<String> languages) {
        LanguageResource languageResource = new LanguageResource();
        languageResource.setLanguages(languages);
        return languageResource;
    }
}
