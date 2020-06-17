package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FilteredNews {

    private String title;
    private String origin;
    private String url;
}
