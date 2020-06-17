package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FilteredGraphicNovel {

    private String flagUrl;
    private String title;
    private String publicationDate;
    private String url;
}
