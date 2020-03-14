package com.freebds.backend.business.scrapers.bedetheque.serie;

import com.freebds.backend.business.scrapers.GenericScraper;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerie;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerieUrl;
import com.freebds.backend.business.scrapers.bedetheque.graphicnovels.BedethequeGraphicNovelScraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BedethequeSerieScraper extends GenericScraper {

    private final String BEDETHEQUE_SERIE_LIST_BY_LETTER_URL = "https://www.bedetheque.com/bandes_dessinees_%s.html";
    private final String BEDETHEQUE_SERIE_PREFIX_URL = "https://www.bedetheque.com/serie-";
    private final String BEDETHEQUE_MULTI_SEARCH_URL = "https://www.bedetheque.com/search/tout?RechTexte=%s&RechWhere=7";
    private static final int delayBetweenTwoScraps = 1000;

    public BedethequeSerieScraper() {
    }

    /**
     * List all existing series from http://www.bedetheque.com
     *
     * @return the url list of all found series
     * @throws IOException in case of connection error to the site http://www.bedetheque.com
     */
    public List<ScrapedSerieUrl> listAll() throws IOException {
        // Get all existing authors urls from http://www.bedetheque.com
        List<ScrapedSerieUrl> scrapedSerieUrls = new ArrayList<>();
        String[] letters = new String("0-A-B-C-D-E-F-G-H-I-J-K-L-M-N-O-P-Q-R-S-T-U-V-W-X-Y-Z").split("-");
        for(String letter : letters){
            List<ScrapedSerieUrl> sc = this.listByLetter(letter);
            scrapedSerieUrls.addAll(sc);
        }

        return scrapedSerieUrls;
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
                if(link.text().startsWith("(AUT") || link.text().startsWith("(DOC)") || link.text().startsWith("(Catalogues)")) {

                } else
                //if(link.text().matches("(\\(AUT\\))|(\\(Catalogues\\))|(\\(DOC\\))") == false)
                    {
                        // Update serie url to return the whole graphic novels
                        linkHref = linkHref.replaceAll(".html", "__10000.html");
                        series.add(new ScrapedSerieUrl(series.size(), link.text(), linkHref));
                    //System.out.println(series.size() + " - " + link.text() + " - " + linkHref);
                }
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
                //System.out.println(series.size() + " - " + link.text() + " - " + linkHref);
            }
        }

        return series;
    }

    /**
     * Scrap a serie
     * @param url
     * @return
     * @throws IOException
     */
    public ScrapedSerie scrap(String url) throws IOException {
        Document doc = this.load(url);

        System.out.print("Série à scraper=" + url);

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

        // Date
        scrapedSerie.setCreationDate(LocalDateTime.now());
        scrapedSerie.setCreationUser("SCRAPER_BEDETHEQUE_V1");
        scrapedSerie.setLastUpdateDate(LocalDateTime.now());
        scrapedSerie.setLastUpdateUser("SCRAPER_BEDETHEQUE_V1");

        // TODO : délai d'attente entre deux requêtes http à déclarer en @Value
        try {
            Thread.sleep(delayBetweenTwoScraps);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        String res = null;
        try {
            res = doc.select("ul.serie-info li:contains(Identifiant)").first().ownText();
        } catch (Exception e) {

        }
        return res;
    }

    private String retrieveTitle(Document doc) {
        String res = null;
        try {
            res = doc.getElementsByTag("h1").first().text();
        } catch (Exception e) {

        }
        return res;
    }

    private String retrieveCategory(Document doc) {
        String res = null;
        try {
            res = doc.select("ul.serie-info li:contains(Genre) > span").first().ownText();
        } catch (Exception e) {

        }
        return res;
    }

    private String retrieveStatus(Document doc) {
        String res = null;
        try {
            res = doc.select("ul.serie-info li:contains(Parution) > span").first().ownText();
        } catch (Exception e) {

        }
        return res;
    }

    private String retrieveOrigin(Document doc) {
        String res = null;
        try {
            res = doc.select("ul.serie-info li:contains(Origine)").first().ownText();
        } catch (Exception e) {

        }
        return res;
    }

    private String retrieveLangage(Document doc) {
        String res = null;
        try {
            res = doc.select("ul.serie-info li:contains(Langue)").first().ownText();
        } catch (Exception e) {

        }
        return res;
    }

    private String retrieveSynopsys(Document doc) {
        String res = null;
        try {
            res = doc.select("div.single-content.serie > p").first().ownText();
        } catch (Exception e) {

        }
        return res;
    }

    private String retrievePictureUrl(Document doc) {
        String res = null;
        try {
            res = doc.select("div.serie-image > a").first().attr("href");
        } catch (Exception e) {

        }
        return res;
    }

    private String retrieveThumbnailUrl(Document doc) {
        String res = null;
        try {
            res = doc.select("div.serie-image > a > img").first().attr("src");
        } catch (Exception e) {

        }
        return res;
    }
}
