package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.filters.GlobalFilteredObject;
import com.freebds.backend.business.scrapers.bedetheque.filters.GraphicNovelsFilteredObject;
import org.springframework.stereotype.Service;

@Service
public interface BedethequeService {

    GlobalFilteredObject filter(String term);

    GraphicNovelsFilteredObject filterGraphicNovels(String rechIdSerie, String rechIdAuteur, String rechSerie, String rechTitre,
                                       String rechEditeur, String rechCollection, String rechStyle, String rechAuteur,
                                       String rechISBN, String rechParution, String rechOrigine, String rechLangue,
                                       String rechMotCle, String rechDLDeb, String rechDLFin, String rechCoteMin,
                                       String rechCoteMax, String rechEO);

    String autocompleteSeries(String term);

    String autocompletePublishers(String term);

    String autocompleteCollections(String term);

    String autocompleteCategories(String term);

    String autocompleteAuthors(String term);
}
