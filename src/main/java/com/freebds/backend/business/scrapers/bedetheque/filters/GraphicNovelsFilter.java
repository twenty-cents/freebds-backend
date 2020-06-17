package com.freebds.backend.business.scrapers.bedetheque.filters;

import lombok.extern.java.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log
public class GraphicNovelsFilter {

    //@Value("${bedetheque.urls.graphicNovelsSearch}")
    private String BEDETHEQUE_GRAPHIC_NOVELS_SEARCH_URL = "https://www.bedetheque.com/search/albums";
    //@Value("${bedetheque.urls.autocomplete.categories}")
    private String BEDETHEQUE_AUTOCOMPLETE_CATEGORIES_URL = "https://www.bedetheque.com/ajax/styles";
    private String BEDETHEQUE_AUTOCOMPLETE_SERIES_URL = "https://www.bedetheque.com/ajax/series";
    private String BEDETHEQUE_AUTOCOMPLETE_PUBLISHERS_URL = "https://www.bedetheque.com/ajax/editeurs";
    private String BEDETHEQUE_AUTOCOMPLETE_COLLECTIONS_URL = "https://www.bedetheque.com/ajax/collections";
    private String BEDETHEQUE_AUTOCOMPLETE_AUTHORS_URL = "https://www.bedetheque.com/ajax/auteurs";

    /**
     * Find graphic novels by graphic novels search filter
     * @param filter the filter
     * @return the filtered results
     * @throws IOException
     */
    public GraphicNovelsFilteredObject filter(String rechIdSerie, String rechIdAuteur, String rechSerie, String rechTitre,
                                              String rechEditeur, String rechCollection, String rechStyle, String rechAuteur,
                                              String rechISBN, String rechParution, String rechOrigine, String rechLangue,
                                              String rechMotCle, String rechDLDeb, String rechDLFin, String rechCoteMin,
                                              String rechCoteMax, String rechEO) {

        GraphicNovelsFilteredObject graphicNovelsFilteredObject = new GraphicNovelsFilteredObject();
        //globalFilteredObject.setFilter(filter);

        // Get the page
        Connection.Response response = null;
        try {
            response = Jsoup.connect(BEDETHEQUE_GRAPHIC_NOVELS_SEARCH_URL)
                    .maxBodySize(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36")
                    .referrer(BEDETHEQUE_GRAPHIC_NOVELS_SEARCH_URL)
                    .method(Connection.Method.GET)
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("connection", "keep-alive")
                    .data("RechIdSerie", rechIdSerie)
                    .data("RechIdAuteur", rechIdAuteur)
                    .data("RechSerie", rechSerie)
                    .data("RechTitre", rechTitre)
                    .data("RechEditeur", rechEditeur)
                    .data("RechCollection", rechCollection)
                    .data("RechStyle", rechStyle)
                    .data("RechAuteur", rechAuteur)
                    .data("RechISBN", rechISBN)
                    .data("RechParution", rechParution)
                    .data("RechOrigine", rechOrigine)
                    .data("RechLangue", rechLangue)
                    .data("RechMotCle", rechMotCle)
                    .data("RechDLDeb", rechDLDeb)
                    .data("RechDLFin", rechDLFin)
                    .data("RechCoteMin", rechCoteMin)
                    .data("RechCoteMax", rechCoteMax)
                    .data("RechEO", rechEO)
                    .execute();

                    Document doc = null;
                    try {
                        doc = response.parse();
                    } catch (IOException e) {
                        graphicNovelsFilteredObject.setFilteredGraphicNovelsMessage(
                                "Erreur de décodage de la page HTML, les résultats de la recherche n'ont pas être analysés : " + response.url().toString());
                    }

                    // Extract results
                    getGraphicNovels(doc.selectFirst("div.widget-line-title"), graphicNovelsFilteredObject);

        } catch (IOException e) {
            graphicNovelsFilteredObject.setFilteredGraphicNovelsMessage("La recherche renvoie plus de 5000 albums, veuillez affiner vos critères de recherche.");
        }

        return graphicNovelsFilteredObject;
    }

    /**
     * Get autocomplete on series titles
     * @param term the filter
     * @return a JSON String {id: "62375", label: "Gast", value: "Gast", desc: "skin/flags/France.png"}
     */
    public String autocompleteSeries(String term) {
        return autocomplete(BEDETHEQUE_AUTOCOMPLETE_SERIES_URL, term);
    }

    /**
     * Get autocomplete on series publishers
     * @param term the filter
     * @return a JSON String {id: "Dupond", label: "Dupond", value: "Dupond"}
     */
    public String autocompletePublishers(String term) {
        return autocomplete(BEDETHEQUE_AUTOCOMPLETE_PUBLISHERS_URL, term);
    }

    /**
     * Get autocomplete on graphic novels collections
     * @param term the filter
     * @return a JSON String {id: "Dupond", label: "Dupond", value: "Dupond"}
     */
    public String autocompleteCollections(String term) {
        return autocomplete(BEDETHEQUE_AUTOCOMPLETE_COLLECTIONS_URL, term);
    }

    /**
     * Get autocomplete on serie categories
     * @param term the filter
     * @return a JSON String {'id'='' , 'label'='', 'value'=''}
     */
    public String autocompleteCategories(String term) {
        return autocomplete(BEDETHEQUE_AUTOCOMPLETE_CATEGORIES_URL, term);
    }

    /**
     * Get autocomplete on author names
     * @param term the filter
     * @return a JSON String {'id'='' , 'label'='', 'value'=''}
     */
    public String autocompleteAuthors(String term) {
        return autocomplete(BEDETHEQUE_AUTOCOMPLETE_AUTHORS_URL, term);
    }

    /**
     * Get graphic novels
     * @param title the title tag
     * @return a list of graphic novels
     */
    private GraphicNovelsFilteredObject getGraphicNovels(Element title, GraphicNovelsFilteredObject graphicNovelsFilteredObject) {
        List<FilteredGraphicNovelDetails> filteredGraphicNovelDetails = new ArrayList<>();
        // Second sibling => ul or p if nothing or too much elements found
        Element ulElement = title.nextElementSibling();
        // Add all graphic novels
        if(ulElement.is("ul")) {
            List<Element> liElements = ulElement.select("li");
            for(Element e : liElements) {
                // Add a graphic novel in the result list
                FilteredGraphicNovelDetails filteredGraphicNovelDetail = FilteredGraphicNovelDetails.builder()
                        .flagUrl(e.selectFirst("span.ico > img").attr("src"))
                        .serieTitle(e.selectFirst("span.serie").text().trim())
                        .tome(e.selectFirst("span.num").text().trim())
                        .numEdition(e.selectFirst("span.numa").text().trim())
                        .title(e.selectFirst("span.titre").text().trim())
                        .publicationDate(e.selectFirst("span.dl").text().trim())
                        .url(e.selectFirst("a").attr("href"))
                        .coverUrl(e.selectFirst("a").attr("rel"))
                        .build();
                filteredGraphicNovelDetails.add(filteredGraphicNovelDetail);
            }
            graphicNovelsFilteredObject.setFilteredGraphicNovelDetails(filteredGraphicNovelDetails);
        }

        if(ulElement.is("span.erreur")) {
            graphicNovelsFilteredObject.setFilteredGraphicNovelsMessage(ulElement.text().trim());
        }
        return graphicNovelsFilteredObject;
    }

    /**
     *
     * @param term
     * @return
     */
    private String autocomplete(String url, String term) {
        // Get the page
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36")
                    .referrer(BEDETHEQUE_GRAPHIC_NOVELS_SEARCH_URL)
                    .method(Connection.Method.GET)
                    .header("accept", "application/json, text/javascript, */*; q=0.01")
                    .header("connection", "keep-alive")
                    .data("term", term)
                    .execute();
        } catch (IOException e) {
            log.warning("autocomplete connection error on url " + url);;
        }

        if(response.statusCode() != 200) {
            log.warning("autocomplete communication error");
        }

        String autocomplete = (response.body().equals("riendutout") ? "" : response.body());
        return autocomplete;
    }
}
