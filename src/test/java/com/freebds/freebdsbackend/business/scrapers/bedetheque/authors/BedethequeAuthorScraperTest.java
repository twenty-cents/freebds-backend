package com.freebds.freebdsbackend.business.scrapers.bedetheque.authors;

import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthor;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthorUrl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BedethequeAuthorScraperTest {

    @Test
    void scrapAuthorUrlsByLetter() {
        // Given author list : all authors starting with the letter A
        String url = "https://www.bedetheque.com/liste_auteurs_BD_A.html";

        // Scrap authors list
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();
        List<ScrapedAuthorUrl> scrapedAuthorUrlList = null;
        try {
            scrapedAuthorUrlList = bedethequeAuthorScraper.scrapAuthorUrlsByLetter(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tests
        assertThat(scrapedAuthorUrlList.size()).isGreaterThanOrEqualTo(1);
        assertThat(scrapedAuthorUrlList.get(0).getUrl()).isEqualToIgnoringCase("https://www.bedetheque.com/auteur-13335-BD-A-Ming.html");
        assertThat(scrapedAuthorUrlList.get(0).getName()).isEqualToIgnoringCase("A Ming");
    }

    @Test
    void scrapAuthor() {
        // Given author to scrap
        ScrapedAuthorUrl scrapedAuthorUrl = new ScrapedAuthorUrl();
        scrapedAuthorUrl.setUrl("https://www.bedetheque.com/auteur-77-BD-Greg.html");
        scrapedAuthorUrl.setName("Greg");

        // Scrap author
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();
        ScrapedAuthor scrapedAuthor = null;
        try {
            scrapedAuthor = bedethequeAuthorScraper.scrapAuthor(scrapedAuthorUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tests
        assertThat(scrapedAuthor.getId()).isEqualTo("77");
        assertThat(scrapedAuthor.getNickname()).isEqualTo("Greg");
        assertThat(scrapedAuthor.getLastname()).isEqualToIgnoringCase("regnier");
        assertThat(scrapedAuthor.getFirstname()).isEqualToIgnoringCase("michel");
        assertThat(scrapedAuthor.getBirthdate()).isEqualTo("05/05/1931");
        assertThat(scrapedAuthor.getDeceaseDate()).isEqualTo("29/10/1999");
        assertThat(scrapedAuthor.getNationality()).isEqualToIgnoringCase("belgique");
        assertThat(scrapedAuthor.getSiteUrl()).isEqualToIgnoringCase("http://michel.greg.free.fr");
        assertThat(scrapedAuthor.getPic_url()).isEqualToIgnoringCase("https://www.bedetheque.com/media/Photos/Photo_77.jpg");
        assertThat(scrapedAuthor.getBiography()).isGreaterThan("");
        assertThat(scrapedAuthor.getAuthorUrl()).isEqualToIgnoringCase("https://www.bedetheque.com/auteur-77-BD-Greg.html");
    }
}