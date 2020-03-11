package com.freebds.backend.business.scrapers.bedetheque.authors;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.GenericScrapAuthor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BedethequeAuthorScraperTest {

    @Test
    void scrapAuthorUrlsByLetter() {
        // Given author list : all authors starting with the letter A
        String url = "A";

        // Scrap authors list
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();
        List<GenericAuthorUrl> scrapedAuthorUrlList = null;
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
        GenericAuthorUrl scrapedAuthorUrl = new GenericAuthorUrl();
        scrapedAuthorUrl.setUrl("https://www.bedetheque.com/auteur-77-BD-Greg.html");
        scrapedAuthorUrl.setName("Greg");

        // Scrap author
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();
        GenericScrapAuthor scrapAuthorDTO = null;
        try {
            scrapAuthorDTO = bedethequeAuthorScraper.scrapAuthor(scrapedAuthorUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tests
        assertThat(scrapAuthorDTO.getId()).isEqualTo("77");
        assertThat(scrapAuthorDTO.getNickname()).isEqualTo("Greg");
        assertThat(scrapAuthorDTO.getLastname()).isEqualToIgnoringCase("regnier");
        assertThat(scrapAuthorDTO.getFirstname()).isEqualToIgnoringCase("michel");
        assertThat(scrapAuthorDTO.getBirthdate()).isEqualTo("05/05/1931");
        assertThat(scrapAuthorDTO.getDeceaseDate()).isEqualTo("29/10/1999");
        assertThat(scrapAuthorDTO.getNationality()).isEqualToIgnoringCase("belgique");
        assertThat(scrapAuthorDTO.getSiteUrl()).isEqualToIgnoringCase("http://michel.greg.free.fr");
        assertThat(scrapAuthorDTO.getPic_url()).isEqualToIgnoringCase("https://www.bedetheque.com/media/Photos/Photo_77.jpg");
        assertThat(scrapAuthorDTO.getBiography()).isGreaterThan("");
        assertThat(scrapAuthorDTO.getAuthorUrl()).isEqualToIgnoringCase("https://www.bedetheque.com/auteur-77-BD-Greg.html");
    }
}