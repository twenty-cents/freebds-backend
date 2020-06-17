package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FilteredChronicle {

    private String name;
    private String ratingLabel;
    private String ratingUrl;
    private String url;
}
