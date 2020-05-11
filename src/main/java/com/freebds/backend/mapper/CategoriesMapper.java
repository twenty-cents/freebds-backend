package com.freebds.backend.mapper;

import com.freebds.backend.common.web.serie.resources.CategoriesResource;

import java.util.List;

public class CategoriesMapper {

    public static CategoriesResource toResource(List<String> categories) {
        CategoriesResource categoriesResource = new CategoriesResource();
        categoriesResource.setCategories(categories);
        return categoriesResource;
    }
}
