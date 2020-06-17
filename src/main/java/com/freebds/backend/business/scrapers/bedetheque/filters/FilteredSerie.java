package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FilteredSerie {

    private String flagUrl;
    private String title;
    private String category;
    private String url;
}
