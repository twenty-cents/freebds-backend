package com.freebds.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "Categories", description = "The categories serie list scraped from http://www.bedetheque.com")
public class CategoriesDTO {

    private List<String> categories;
}
