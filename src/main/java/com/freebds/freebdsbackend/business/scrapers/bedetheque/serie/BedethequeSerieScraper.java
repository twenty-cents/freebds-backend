package com.freebds.freebdsbackend.business.scrapers.bedetheque.serie;

import com.freebds.freebdsbackend.business.scrapers.GenericScraper;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedSerie;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedSerieUrl;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.graphicnovels.BedethequeGraphicNovelScraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BedethequeSerieScraper extends GenericScraper {

    private final String BEDETHEQUE_SERIE_LIST_BY_LETTER_URL = "https://www.bedetheque.com/bandes_dessinees_%s.html";
    private final String BEDETHEQUE_SERIE_PREFIX_URL = "https://www.bedetheque.com/serie-";
    private final String BEDETHEQUE_MULTI_SEARCH_URL = "https://www.bedetheque.com/search/tout?RechTexte=%s&RechWhere=7";

    public BedethequeSerieScraper() {
    }

    /**
     * List all series starting with the letter
     * @param letter
     * @return Serie collection
     */
    public List<ScrapedSerieUrl> listByLetter(String letter) throws IOException {
        List<ScrapedSerieUrl> series = new ArrayList<ScrapedSerieUrl>();

        // Load all series starting with the letter
        Document doc = this.load(String.format(BEDETHEQUE_SERIE_LIST_BY_LETTER_URL, letter));

        // Retrieve all series from the html page
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            if (linkHref.contains(BEDETHEQUE_SERIE_PREFIX_URL)) {
                series.add(new ScrapedSerieUrl(series.size(), link.text(), linkHref));
                System.out.println(series.size() + " - " + link.text() + " - " + linkHref);
            }
        }

        return series;
    }

    /**
     *
     * @param title
     * @return
     * @throws IOException
     */
    public List<ScrapedSerieUrl> listByTitle(String title) throws IOException{
        List<ScrapedSerieUrl> series = new ArrayList<ScrapedSerieUrl>();
        title = title.toLowerCase();

        // Load all series starting with the letter
        Document doc = this.load(String.format(BEDETHEQUE_MULTI_SEARCH_URL, title));

        // Retrieve all series from the html page
        //Elements div = doc.select("div.line-title, search-line":contains(série))
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            System.out.println(series.size() + " - " + link.text() + " - " + linkHref);
            if (linkHref.contains(BEDETHEQUE_SERIE_PREFIX_URL) && link.text().toLowerCase().contains(title) ) {
                series.add(new ScrapedSerieUrl(series.size(), link.text(), linkHref));
                System.out.println(series.size() + " - " + link.text() + " - " + linkHref);
            }
        }

        return series;
    }

    /**
     * Retrieve a serie
     * @param url
     * @return
     * @throws IOException
     */
    public ScrapedSerie retrieve(String url) throws IOException {
        Document doc = this.load(url);

        ScrapedSerie scrapedSerie = new ScrapedSerie();

        scrapedSerie.setExternalId(retrieveExternalId(doc));
        scrapedSerie.setTitle(retrieveTitle(doc));
        scrapedSerie.setCategory(retrieveCategory(doc));
        scrapedSerie.setStatus(retrieveStatus(doc));
        scrapedSerie.setOrigin(retrieveOrigin(doc));
        scrapedSerie.setLangage(retrieveLangage(doc));
        scrapedSerie.setSynopsys(retrieveSynopsys(doc));
        scrapedSerie.setPictureUrl(retrievePictureUrl(doc));
        scrapedSerie.setPictureThbUrl(retrieveThumbnailUrl(doc));
        scrapedSerie.setScrapUrl(url);
        scrapedSerie.setScrapedGraphicNovels(retrieveGraphicNovels(doc));
        return scrapedSerie;
    }

    private List<ScrapedGraphicNovel> retrieveGraphicNovels(Document doc) {
        List<ScrapedGraphicNovel> scrapedGraphicNovels = new ArrayList<ScrapedGraphicNovel>();
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();

        Elements eAlbums = doc.select("ul.liste-albums li[itemtype='https://schema.org/Book']");

        for(Element li : eAlbums) {
            scrapedGraphicNovels.add(bedethequeGraphicNovelScraper.scrap(li));
        }

        return scrapedGraphicNovels;
    }

    private String retrieveExternalId(Document doc) {
        return doc.select("ul.serie-info li:contains(Identifiant)").first().ownText();
    }

    private String retrieveTitle(Document doc) {
        return doc.getElementsByTag("h1").first().text();
    }

    private String retrieveCategory(Document doc) {
        return doc.select("ul.serie-info li:contains(Genre) > span").first().ownText();
    }

    private String retrieveStatus(Document doc) {
        return doc.select("ul.serie-info li:contains(Parution) > span").first().ownText();
    }

    private String retrieveOrigin(Document doc) {
        return doc.select("ul.serie-info li:contains(Origine)").first().ownText();
    }

    private String retrieveLangage(Document doc) {
        return doc.select("ul.serie-info li:contains(Langue)").first().ownText();
    }

    private String retrieveSynopsys(Document doc) {
        return doc.select("div.single-content.serie > p").first().ownText();
    }

    private String retrievePictureUrl(Document doc) {
        return doc.select("div.serie-image > a").first().attr("href");
    }

    private String retrieveThumbnailUrl(Document doc) {
        return doc.select("div.serie-image > a > img").first().attr("src");
    }
}
