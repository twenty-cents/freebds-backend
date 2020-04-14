package com.freebds.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "Origin", description = "A serie origin scraped from http://www.bedetheque.com")
public class OriginDTO {

    private List<String> origins;
}
