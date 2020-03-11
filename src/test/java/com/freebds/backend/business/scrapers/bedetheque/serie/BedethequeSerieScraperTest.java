package com.freebds.backend.business.scrapers.bedetheque.serie;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerie;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BedethequeSerieScraperTest {

    @Test
    void listByLetter() {
    }

    @Test
    void listByTitle() {
    }

    @Test
    void scrap() throws IOException {
        // Given serie to scrap
        String url = "https://www.bedetheque.com/serie-59-BD-Asterix__10000.html";

        // Scrap the serie
        BedethequeSerieScraper bedethequeSerieScraper = new BedethequeSerieScraper();
        ScrapedSerie scrapedSerie = bedethequeSerieScraper.scrap(url);

        // Tests
        assertThat(scrapedSerie.getTitle()).isEqualToIgnoringCase("astérix");
        assertThat(scrapedSerie.getCategory()).isEqualToIgnoringCase("humour");
        assertThat(scrapedSerie.getStatus()).isEqualToIgnoringCase("série en cours");
        assertThat(scrapedSerie.getOrigin()).isEqualToIgnoringCase("europe");
        assertThat(scrapedSerie.getLangage()).isEqualToIgnoringCase("français");
        //System.out.println(scrapedSerie);
    }
}