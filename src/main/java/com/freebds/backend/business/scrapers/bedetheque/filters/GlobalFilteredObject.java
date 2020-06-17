package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class GlobalFilteredObject {

    private String filter;
    private String globalErrorMessage = "";
    private List<FilteredChronicle> filteredChronicles = new ArrayList<>();
    private String filteredChroniclesMessage = "";
    private List<FilteredNews> filteredNews = new ArrayList<>();
    private String filteredNewsMessage = "";
    private List<FilteredPreview> filteredPreviews = new ArrayList<>();
    private String filteredPreviewsMessage = "";
    private List<FilteredAuthor> filteredAuthors = new ArrayList<>();
    private String filteredAuthorsMessage = "";
    private List<FilteredSerie> filteredSeries = new ArrayList<>();
    private String filteredSeriesMessage = "";
    private List<FilteredSerie> filteredAssociateSeries = new ArrayList<>();
    private String filteredAssociateSeriesMessage = "";
    private List<FilteredGraphicNovel> filteredGraphicNovels = new ArrayList<>();
    private String filteredGraphicNovelsMessage = "";

}
