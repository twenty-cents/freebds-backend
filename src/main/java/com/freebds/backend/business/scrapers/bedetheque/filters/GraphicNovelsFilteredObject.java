package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class GraphicNovelsFilteredObject {

    private String filter;
    private List<FilteredGraphicNovelDetails> filteredGraphicNovelDetails = new ArrayList<>();
    private String filteredGraphicNovelsMessage = "";
}
