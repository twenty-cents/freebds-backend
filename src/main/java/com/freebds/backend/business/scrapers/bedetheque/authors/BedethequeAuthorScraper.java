package com.freebds.backend.business.scrapers.bedetheque.authors;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.GenericScrapAuthor;
import com.freebds.backend.model.Author;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class BedethequeAuthorScraper {

    private static final String BEDETHEQUE_AUTHORS_LIST_BY_LETTER = "https://www.bedetheque.com/liste_auteurs_BD_0.html";
    private static final String BEDETHEQUE_AUTHOR_PREFIX_URL = "https://www.bedetheque.com/auteur-";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Scrap all authors starting with a letter <n> from http://wwww.bedetheque.com
     *
     * @param letter the letter from which the author list should be scraped
     * @return a list of scraped author urls
     * @throws IOException in case of connection error to the url authors page
     */
    public List<GenericAuthorUrl> scrapAuthorUrlsByLetter(String letter) throws IOException {
        // Build authors url
        String bedethequeAuthorUrlListByLetter = BEDETHEQUE_AUTHORS_LIST_BY_LETTER;
        bedethequeAuthorUrlListByLetter = bedethequeAuthorUrlListByLetter.replaceFirst("0", letter);

        // Load all authors starting with the letter
        Document doc = Jsoup.connect(bedethequeAuthorUrlListByLetter).maxBodySize(0).userAgent("Mozilla").get();

        // Retrieve all authors from the html page
        List<GenericAuthorUrl> genericAuthorUrls = new ArrayList<>();
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            if (linkHref.contains(BEDETHEQUE_AUTHOR_PREFIX_URL)) {
                // Define a new author
                GenericAuthorUrl authorUrl = new GenericAuthorUrl();
                authorUrl.setName(link.text());
                authorUrl.setUrl(linkHref);
                // Add him to the author list
                genericAuthorUrls.add(authorUrl);
            }
        }

        return genericAuthorUrls;
    }

    /**
     * Scrap an author from http://wwww.bedetheque.com
     *
     * @param authorUrl the scraped author url to get
     * @return a generic scraped author from http://wwww.bedetheque.com
     * @throws IOException in case of connection error to the url author page
     */
    public GenericScrapAuthor scrapAuthor(GenericAuthorUrl authorUrl) throws IOException {
        // Load author
        Document doc = Jsoup.connect(authorUrl.getUrl()).maxBodySize(0).userAgent("Mozilla").get();

        GenericScrapAuthor genericScrapAuthor = new GenericScrapAuthor();
        // Scrap author informations
        genericScrapAuthor.setId(scrap(doc, "ul.auteur-info li:contains(Identifiant)", null));
        genericScrapAuthor.setLastname(scrap(doc, "ul.auteur-info li:contains(Nom) > span", null));
        genericScrapAuthor.setFirstname(scrap(doc, "ul.auteur-info li:contains(Prénom) > span", null));
        genericScrapAuthor.setNickname(scrap(doc, "ul.auteur-info li:contains(Pseudo)", null));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            genericScrapAuthor.setBirthdate(LocalDate.parse(scrapAndClean(doc, "ul.auteur-info li:contains(Naissance)", null, "le"), formatter));
        } catch (DateTimeParseException e) {
            genericScrapAuthor.setBirthdate(null);
        }

        try {
            genericScrapAuthor.setDeceaseDate(LocalDate.parse(scrapAndClean(doc, "ul.auteur-info li:contains(Décès)", null, "le"), formatter));
        } catch (DateTimeParseException e) {
            genericScrapAuthor.setDeceaseDate(null);
        }

        genericScrapAuthor.setNationality(scrap(doc, "ul.auteur-info li:contains(Naissance) > span.pays-auteur", null)
                .replace('(', ' ')
                .replace(')', ' ')
                .trim());
        genericScrapAuthor.setSiteUrl(scrap(doc, "ul.auteur-info li:contains(Site) > a", "href"));
        genericScrapAuthor.setBiography(scrap(doc, "p.bio", null));
        genericScrapAuthor.setPic_url(scrap(doc, "div.auteur-image > a", "href"));
        genericScrapAuthor.setThumb_url(scrap(doc, "div.auteur-image > a > img", "src"));
        genericScrapAuthor.setAuthorUrl(authorUrl.getUrl());

        // Generic author
        if(genericScrapAuthor.getId() == "") {
            genericScrapAuthor.setId(authorUrl.getUrl().split("-")[1]);
            genericScrapAuthor.setNickname(authorUrl.getName());
            genericScrapAuthor.setLastname(authorUrl.getName());
        }

        // Date
        genericScrapAuthor.setCreationDate(LocalDateTime.now());
        genericScrapAuthor.setCreationUser("SCRAPER_BEDETHEQUE_V1");
        genericScrapAuthor.setLastUpdateDate(LocalDateTime.now());
        genericScrapAuthor.setLastUpdateUser("SCRAPER_BEDETHEQUE_V1");

        return genericScrapAuthor;
    }

    /**
     * Convert a scraped author to an author entity
     *
     * @param genericScrapAuthor the scraped author to convert
     * @return a author entity
     */
    public Author toAuthor(GenericScrapAuthor genericScrapAuthor) {
        Author author = new Author();
        author.setExternalId(genericScrapAuthor.getId());
        author.setLastname(genericScrapAuthor.getLastname());
        author.setFirstname(genericScrapAuthor.getFirstname());
        author.setNickname(genericScrapAuthor.getNickname());
        author.setNationality(genericScrapAuthor.getNationality());
        author.setBirthdate(genericScrapAuthor.getBirthdate());
        author.setDeceaseDate(genericScrapAuthor.getDeceaseDate());
        author.setBiography(genericScrapAuthor.getBiography());
        author.setSiteUrl(genericScrapAuthor.getSiteUrl());
        author.setPhotoUrl(genericScrapAuthor.getPic_url());
        author.setScrapUrl(genericScrapAuthor.getAuthorUrl());
        author.setIsScraped(true);
        author.setCreationDate(genericScrapAuthor.getCreationDate());
        author.setCreationUser(genericScrapAuthor.getCreationUser());
        author.setLastUpdateDate(genericScrapAuthor.getLastUpdateDate());
        author.setLastUpdateUser(genericScrapAuthor.getLastUpdateUser());

        return author;
    }

    /**
     * Scrap an unique value from the DOM
     *
     * @param doc the document to scrap
     * @param query the query to apply to extract values
     * @param attribute optional parameter. If not null, search the attribute in the given query to extract values
     * @return a scraped value
     */
    private static String scrap(Document doc, String query, String attribute){
        String res = "";
        Element element = doc.selectFirst(query);
        if(element != null){
            if(attribute == null) {
                res = element.ownText().trim();
            } else {
                res = element.attr(attribute);
            }
        }
        return res;
    }

    /**
     * Scrap an unique value from the DOM
     *
     * @param doc the document to scrap
     * @param query the query to apply to extract values
     * @param attribute optional parameter. If not null, search the attribute in the given query to extract values
     * @param textToRemove the text to remove from the extract value
     * @return a scraped value
     */
    private static String scrapAndClean(Document doc, String query, String attribute, String textToRemove){
        String preprocessedText = scrap(doc, query, attribute);

        return preprocessedText.replaceAll(textToRemove, "").trim();

    }
}
