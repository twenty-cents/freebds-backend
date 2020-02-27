package com.freebds.freebdsbackend.business.scrapers.bedetheque.graphicnovels;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BedethequeGraphicNovelScraperTest {

    @Test
    void retrieveAlbums() throws IOException {
        String url = "https://www.bedetheque.com/serie-59-BD-Asterix__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        bedethequeGraphicNovelScraper.retrieveAlbums(url);
    }

    @Test
    void retrieveAlbumsWithReleaseDate() throws IOException {
        String url = "https://www.bedetheque.com/serie-68754-BD-Ne-regarde-pas-derriere-toi__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        bedethequeGraphicNovelScraper.retrieveAlbums(url);
    }
}