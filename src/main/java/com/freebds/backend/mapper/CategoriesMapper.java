package com.freebds.backend.mapper;

import com.freebds.backend.dto.CategoriesDTO;

import java.util.List;

public class CategoriesMapper {

    public static CategoriesDTO toDTO(List<String> categories) {
        CategoriesDTO categoriesDTO = new CategoriesDTO();
        categoriesDTO.setCategories(categories);
        return categoriesDTO;
    }
}
