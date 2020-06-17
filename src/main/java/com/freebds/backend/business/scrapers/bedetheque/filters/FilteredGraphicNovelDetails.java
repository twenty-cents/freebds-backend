package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FilteredGraphicNovelDetails {

    private String flagUrl;
    private String serieTitle;
    private String tome;
    private String numEdition;
    private String title;
    private String coverUrl;
    private String publicationDate;
    private String url;
}
