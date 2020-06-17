package com.freebds.backend.business.scrapers.bedetheque.filters;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlobalFilter {

    //@Value("${bedetheque.urls.globalSearch}")
    private String BEDETHEQUE_GLOBAL_SEARCH_URL = "https://www.bedetheque.com/search/tout";

    /**
     * Find series, graphic novels, authors, news, chronicles, previews by Global search filter
     * @param filter the filter
     * @return the filtered results
     */
    public GlobalFilteredObject filter(String filter) {

        GlobalFilteredObject globalFilteredObject = new GlobalFilteredObject();
        globalFilteredObject.setFilter(filter);

        // Get the page
        Connection.Response response = null;
        try {
            response = Jsoup.connect(BEDETHEQUE_GLOBAL_SEARCH_URL)
                    .maxBodySize(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36")
                    .referrer(BEDETHEQUE_GLOBAL_SEARCH_URL)
                    .method(Connection.Method.GET)
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("connection", "keep-alive")
                    .data("RechWhere", "1")
                    .data("RechTexte", filter)
                    .execute();

            Document doc = response.parse();

            // Extract results
            List<Element> results = doc.select("div.search-line");
            for (Element e : results) {
                String title = e.selectFirst("h3").ownText().trim();
                // Get chronicle
                if(title.contains("chronique")) {
                    globalFilteredObject = getChronicles(e, globalFilteredObject);
                }
                // Get news
                if(title.contains("news")){
                    globalFilteredObject = getNews(e, globalFilteredObject);
                }
                // Get previews
                if(title.contains("preview")) {
                    globalFilteredObject = getPreviews(e, globalFilteredObject);
                }
                // Get authors
                if(title.contains("auteur")){
                    globalFilteredObject = getAuthors(e, globalFilteredObject);
                }
                // Get series
                if(title.contains("série") && title.contains("mot clé") == false) {
                    globalFilteredObject = getSeries(e, globalFilteredObject);
                }
                // Get associate series
                if(title.contains("série") && title.contains("mot clé")) {
                    globalFilteredObject = getAssociateSeries(e, globalFilteredObject);
                }
                // Get albums
                if(title.contains("album")) {
                    globalFilteredObject = getGraphicNovels(e, globalFilteredObject);
                }
            }

        } catch (IOException e) {
            globalFilteredObject.setGlobalErrorMessage("Erreur de communication avec le serveur pour l'url : " + response.url().toString());
        }

        return globalFilteredObject;
    }

    /**
     * Get Chronicles
     * @param title the title tag
     * @return a list of chronicles
     */
    private GlobalFilteredObject getChronicles(Element title, GlobalFilteredObject globalFilteredObject) {
        List<FilteredChronicle> filteredChronicles = new ArrayList<>();
        // Fisrt sibling => div.clear
        // Second sibling => ul or p if nothing or too much elements found
        Element list = title.nextElementSibling().nextElementSibling();
        // Add all chronicles
        if(list.is("ul")) {
            List<Element> liEls = list.select("li");
            for(Element e : liEls) {
                // Get rating
                Element ratingElement = e.selectFirst("span.count > img");
                String ratingLabel = (ratingElement != null ? ratingElement.attr("title").trim() : "");
                String ratingUrl = (ratingElement != null ? ratingElement.attr("src") : "");
                // Add a chronicle in the result list
                FilteredChronicle filteredChronicle = FilteredChronicle.builder()
                        .name(e.selectFirst("a > span").text().trim())
                        .ratingLabel(ratingLabel)
                        .ratingUrl(ratingUrl)
                        .url(e.selectFirst("a").attr("href"))
                        .build();
                filteredChronicles.add(filteredChronicle);
            }
            globalFilteredObject.setFilteredChronicles(filteredChronicles);
        }
        // Too much results
        if(list.is("p")) {
            globalFilteredObject.setFilteredChroniclesMessage(list.text().trim());
        }

        return globalFilteredObject;
    }

    /**
     * Get news
     * @param title the title tag
     * @return a list of news
     */
    private GlobalFilteredObject getNews(Element title, GlobalFilteredObject globalFilteredObject) {
        List<FilteredNews> filteredNews = new ArrayList<>();
        // Fisrt sibling => div.clear
        // Second sibling => ul or p if nothing or too much elements found
        Element list = title.nextElementSibling().nextElementSibling();
        // Add all news
        if(list.is("ul")) {
            List<Element> liEls = list.select("li");
            for(Element e : liEls) {
                // Get origin
                Element originElement = e.selectFirst("span.count");
                String origin = (originElement != null ? originElement.text().trim() : "");
                // Add a news in the result list
                FilteredNews filteredNew = FilteredNews.builder()
                        .title(e.selectFirst("a > span").text().trim())
                        .origin(origin)
                        .url(e.selectFirst("a").attr("href"))
                        .build();
                filteredNews.add(filteredNew);
            }
            globalFilteredObject.setFilteredNews(filteredNews);
        }
        // Too much results
        if(list.is("p")) {
            globalFilteredObject.setFilteredNewsMessage(list.text().trim());
        }
        return globalFilteredObject;
    }

    /**
     * Get previews
     * @param title the title tag
     * @return a list of previews
     */
    private GlobalFilteredObject getPreviews(Element title, GlobalFilteredObject globalFilteredObject) {
        List<FilteredPreview> filteredPreviews = new ArrayList<>();
        // Fisrt sibling => div.clear
        // Second sibling => ul or p if nothing or too much elements found
        Element list = title.nextElementSibling().nextElementSibling();
        // Add all previews
        if(list.is("ul")) {
            List<Element> liEls = list.select("li");
            for(Element e : liEls) {
                // Get origin
                Element pagesCountElement = e.selectFirst("span.count");
                String pagesCount = (pagesCountElement != null ? pagesCountElement.text().trim() : "");
                // Add a preview in the result list
                FilteredPreview filteredPreview = FilteredPreview.builder()
                        .title(e.selectFirst("a > span").text().trim())
                        .pagesCount(pagesCount)
                        .url(e.selectFirst("a").attr("href"))
                        .build();
                filteredPreviews.add(filteredPreview);
            }
            globalFilteredObject.setFilteredPreviews(filteredPreviews);
        }
        // Too much results
        if(list.is("p")) {
            globalFilteredObject.setFilteredPreviewsMessage(list.text().trim());
        }
        return globalFilteredObject;
    }

    /**
     * Extract all authors
     * @param title the title tag
     * @return a list of authors
     */
    private GlobalFilteredObject getAuthors(Element title, GlobalFilteredObject globalFilteredObject) {
        List<FilteredAuthor> filteredAuthors = new ArrayList<>();
        // Fisrt sibling => div.clear
        // Second sibling => ul or p if nothing or too much elements found
        Element list = title.nextElementSibling().nextElementSibling();
        // Add all authors
        if(list.is("ul")) {
            List<Element> liEls = list.select("li");
            for(Element e : liEls) {
                // Get nationality
                Element nationalityElement = e.selectFirst("span.count");
                String nationality = (nationalityElement != null ? nationalityElement.text().trim() : "");
                // Add an author in the result list
                FilteredAuthor filteredAuthor = FilteredAuthor.builder()
                        .name(e.selectFirst("a > span").text().trim())
                        .nationality(nationality)
                        .url(e.selectFirst("a").attr("href"))
                        .build();
                filteredAuthors.add(filteredAuthor);
            }
            globalFilteredObject.setFilteredAuthors(filteredAuthors);
        }
        // Too much results
        if(list.is("p")) {
            globalFilteredObject.setFilteredAuthorsMessage(list.text().trim());
        }
        return globalFilteredObject;
    }

    /**
     * Get series
     * @param title the title tag
     * @return a list of series
     */
    private GlobalFilteredObject getSeries(Element title, GlobalFilteredObject globalFilteredObject) {
        List<FilteredSerie> filteredSeries = new ArrayList<>();
        // Fisrt sibling => div.clear
        // Second sibling => ul or p if nothing or too much elements found
        Element list = title.nextElementSibling().nextElementSibling();
        // Add all series
        if(list.is("ul")) {
            List<Element> liElements = list.select("li");
            for(Element e : liElements) {
                // Get category
                Element categoryElement = e.selectFirst("span.count");
                String category = (categoryElement != null ? categoryElement.text().trim() : "");
                // Add a serie in the result list
                FilteredSerie filteredSerie = FilteredSerie.builder()
                        .flagUrl(e.selectFirst("span.ico > img").attr("src"))
                        .title(e.selectFirst("a > span").text().trim())
                        .category(category)
                        .url(e.selectFirst("a").attr("href"))
                        .build();
                filteredSeries.add(filteredSerie);
            }
            globalFilteredObject.setFilteredSeries(filteredSeries);
        }
        // Too much results
        if(list.is("p")) {
            globalFilteredObject.setFilteredSeriesMessage(list.text().trim());
        }
        return globalFilteredObject;
    }

    /**
     * Get associate series
     * @param title the title tag
     * @return a list of series
     */
    private GlobalFilteredObject getAssociateSeries(Element title, GlobalFilteredObject globalFilteredObject) {
        List<FilteredSerie> filteredSeries = new ArrayList<>();
        // Fisrt sibling => div.clear
        // Second sibling => ul or p if nothing or too much elements found
        Element list = title.nextElementSibling().nextElementSibling();
        // Add all series
        if(list.is("ul")) {
            List<Element> liElements = list.select("li");
            for(Element e : liElements) {
                // Get category
                Element categoryElement = e.selectFirst("span.count");
                String category = (categoryElement != null ? categoryElement.text().trim() : "");
                // Add a serie in the result list
                FilteredSerie filteredSerie = FilteredSerie.builder()
                        .flagUrl(e.selectFirst("span.ico > img").attr("src"))
                        .title(e.selectFirst("a > span").text().trim())
                        .category(category)
                        .url(e.selectFirst("a").attr("href"))
                        .build();
                filteredSeries.add(filteredSerie);
            }
            globalFilteredObject.setFilteredAssociateSeries(filteredSeries);
        }
        // Too much results
        if(list.is("p")) {
            globalFilteredObject.setFilteredAssociateSeriesMessage(list.text().trim());
        }
        return globalFilteredObject;
    }

    /**
     * Get graphic novels
     * @param title the title tag
     * @return a list of graphic novels
     */
    private GlobalFilteredObject getGraphicNovels(Element title, GlobalFilteredObject globalFilteredObject) {
        List<FilteredGraphicNovel> filteredGraphicNovels = new ArrayList<>();
        // Fisrt sibling => div.clear
        // Second sibling => ul or p if nothing or too much elements found
        Element list = title.nextElementSibling().nextElementSibling();
        // Add all graphic novels
        if(list.is("ul")) {
            List<Element> liElements = list.select("li");
            for(Element e : liElements) {
                // Get publication date
                Element publicationDateElement = e.selectFirst("span.count");
                String publicationDate = (publicationDateElement != null ? publicationDateElement.text().trim() : "");
                // Add a graphic novel in the result list
                FilteredGraphicNovel filteredGraphicNovel = FilteredGraphicNovel.builder()
                        .flagUrl(e.selectFirst("span.ico > img").attr("src"))
                        .title(e.selectFirst("a > span").text().trim())
                        .publicationDate(publicationDate)
                        .url(e.selectFirst("a").attr("href"))
                        .build();
                filteredGraphicNovels.add(filteredGraphicNovel);
            }
            globalFilteredObject.setFilteredGraphicNovels(filteredGraphicNovels);
        }
        // Too much results
        if(list.is("p")) {
            globalFilteredObject.setFilteredGraphicNovelsMessage(list.text().trim());
        }
        return globalFilteredObject;
    }

}
