package com.freebds.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "Categories", description = "The author's nationalities list scraped from http://www.bedetheque.com")
public class NationalitiesDTO {

    private List<String> nationalities;
}
