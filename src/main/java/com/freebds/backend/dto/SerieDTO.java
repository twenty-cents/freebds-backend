package com.freebds.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "Serie", description = "A serie description scraped from http://www.bedetheque.com")
public class SerieDTO {

    private Long id;
    private String externalId;
    private String title;
    private String langage;
    private String status;
    private String origin;
    private String categories;
    private String synopsys;
    private String siteUrl;
    private String pageUrl;
    private String pageThumbnailUrl;

}
