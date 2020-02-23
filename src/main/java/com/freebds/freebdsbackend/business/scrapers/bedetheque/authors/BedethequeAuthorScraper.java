package com.freebds.freebdsbackend.business.scrapers.bedetheque.authors;

import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthor;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthorUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BedethequeAuthorScraper {
    private static final String BEDETHEQUE_AUTHOR_PREFIX_URL = "https://www.bedetheque.com/auteur-";

    /**
     * Scrap all authors starting with a letter <n> from http://wwww.bedetheque.com
     * @param bedethequeAuthorsUrl
     * @return
     * @throws IOException
     */
    public List<ScrapedAuthorUrl> scrapAuthorUrlsByLetter(String bedethequeAuthorsUrl) throws IOException {
        // Load all authors starting with the letter
        Document doc = Jsoup.connect(bedethequeAuthorsUrl).maxBodySize(0).userAgent("Mozilla").get();

        // Retrieve all authors from the html page
        List<ScrapedAuthorUrl> scrapedAuthorUrls = new ArrayList<>();
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            if (linkHref.contains(BEDETHEQUE_AUTHOR_PREFIX_URL)) {
                // Define a new author
                ScrapedAuthorUrl scrapedAuthorUrl = new ScrapedAuthorUrl();
                scrapedAuthorUrl.setName(link.text());
                scrapedAuthorUrl.setUrl(linkHref);
                // Add him to the author list
                scrapedAuthorUrls.add(scrapedAuthorUrl);
            }
        }

        return scrapedAuthorUrls;
    }

    /**
     * Scrap an author from http://wwww.bedetheque.com
     * @param scrapedAuthorUrl
     * @return
     * @throws IOException
     */
    public ScrapedAuthor scrapAuthor(ScrapedAuthorUrl scrapedAuthorUrl) throws IOException {
        // Load author
        Document doc = Jsoup.connect(scrapedAuthorUrl.getUrl()).maxBodySize(0).userAgent("Mozilla").get();

        ScrapedAuthor scrapedAuthor = new ScrapedAuthor();
        // Scrap author informations
        scrapedAuthor.setId(scrap(doc, "ul.auteur-info li:contains(Identifiant)", null));
        scrapedAuthor.setLastname(scrap(doc, "ul.auteur-info li:contains(Nom) > span", null));
        scrapedAuthor.setFirstname(scrap(doc, "ul.auteur-info li:contains(Prénom) > span", null));
        scrapedAuthor.setNickname(scrap(doc, "ul.auteur-info li:contains(Pseudo)", null));
        scrapedAuthor.setBirthdate(scrap(doc, "ul.auteur-info li:contains(Naissance)", null));
        scrapedAuthor.setDeceaseDate(scrap(doc, "ul.auteur-info li:contains(Décès)", null));
        scrapedAuthor.setNationality(scrap(doc, "ul.auteur-info li:contains(Naissance) > span.pays-auteur", null)
                .replace('(', ' ')
                .replace(')', ' ')
                .trim());
        scrapedAuthor.setSiteUrl(scrap(doc, "ul.auteur-info li:contains(Site) > a", "href"));
        scrapedAuthor.setBiography(scrap(doc, "p.bio", null));
        scrapedAuthor.setPic_url(scrap(doc, "div.auteur-image > a", "href"));
        scrapedAuthor.setThumb_url(scrap(doc, "div.auteur-image > a > img", "src"));
        scrapedAuthor.setAuthorUrl(scrapedAuthorUrl.getUrl());

        // Generic author
        if(scrapedAuthor.getId() == "") {
            scrapedAuthor.setId(scrapedAuthorUrl.getUrl().split("-")[1]);
            scrapedAuthor.setNickname(scrapedAuthorUrl.getName());
            scrapedAuthor.setLastname(scrapedAuthorUrl.getName());
        }
        return scrapedAuthor;
    }

    /**
     * Scrap an unique value from the DOM
     * @param doc
     * @param query
     * @param attribute
     * @return
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
}
