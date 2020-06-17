package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.filters.GlobalFilter;
import com.freebds.backend.business.scrapers.bedetheque.filters.GlobalFilteredObject;
import com.freebds.backend.business.scrapers.bedetheque.filters.GraphicNovelsFilter;
import com.freebds.backend.business.scrapers.bedetheque.filters.GraphicNovelsFilteredObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BedethequeServiceImpl implements BedethequeService {

    private static GlobalFilter globalFilter = new GlobalFilter();
    private static GraphicNovelsFilter graphicNovelsFilter = new GraphicNovelsFilter();

    @Override
    public GlobalFilteredObject filter(String term) {
        return globalFilter.filter(term);
    }

    @Override
    public GraphicNovelsFilteredObject filterGraphicNovels(String rechIdSerie, String rechIdAuteur, String rechSerie, String rechTitre,
                                              String rechEditeur, String rechCollection, String rechStyle, String rechAuteur,
                                              String rechISBN, String rechParution, String rechOrigine, String rechLangue,
                                              String rechMotCle, String rechDLDeb, String rechDLFin, String rechCoteMin,
                                              String rechCoteMax, String rechEO) {

        return graphicNovelsFilter.filter(rechIdSerie, rechIdAuteur, rechSerie, rechTitre,
                rechEditeur, rechCollection, rechStyle, rechAuteur,
                rechISBN, rechParution, rechOrigine, rechLangue,
                rechMotCle, rechDLDeb, rechDLFin, rechCoteMin,
                rechCoteMax, rechEO);
    }

    /**
     * Get autocomplete on series titles
     * @param term the filter
     * @return a JSON String {id: "62375", label: "Gast", value: "Gast", desc: "skin/flags/France.png"}
     */
    @Override
    public String autocompleteSeries(String term) {
        return graphicNovelsFilter.autocompleteSeries(term);
    }

    /**
     * Get autocomplete on series publishers
     * @param term the filter
     * @return a JSON String {id: "Dupond", label: "Dupond", value: "Dupond"}
     */
    @Override
    public String autocompletePublishers(String term) {
        return graphicNovelsFilter.autocompletePublishers(term);
    }

    /**
     * Get autocomplete on graphic novels collections
     * @param term the filter
     * @return a JSON String {id: "Dupond", label: "Dupond", value: "Dupond"}
     */
    @Override
    public String autocompleteCollections(String term) {
        return graphicNovelsFilter.autocompleteCollections(term);
    }

    /**
     * Get autocomplete on serie categories
     * @param term the filter
     * @return a JSON String {'id'='' , 'label'='', 'value'=''}
     */
    @Override
    public String autocompleteCategories(String term) {
        return graphicNovelsFilter.autocompleteCategories(term);
    }

    /**
     * Get autocomplete on author names
     * @param term the filter
     * @return a JSON String {'id'='' , 'label'='', 'value'=''}
     */
    @Override
    public String autocompleteAuthors(String term) {
        return graphicNovelsFilter.autocompleteAuthors(term);
    }

}
