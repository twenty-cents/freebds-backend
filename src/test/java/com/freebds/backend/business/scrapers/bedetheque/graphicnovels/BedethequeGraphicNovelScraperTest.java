package com.freebds.backend.business.scrapers.bedetheque.graphicnovels;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class BedethequeGraphicNovelScraperTest {

    @Test
    void retrieveAlbums() throws IOException {
        String url = "https://www.bedetheque.com/serie-59-BD-Asterix__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithReleaseDate() throws IOException {
        String url = "https://www.bedetheque.com/serie-68754-BD-Ne-regarde-pas-derriere-toi__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithPublicationDateAndParution() throws IOException {
        String url = "https://www.bedetheque.com/serie-67280-BD-Pauvre-humanite__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithLongNumEdition() throws IOException {
        String url = "https://www.bedetheque.com/serie-19097-BD-Graine-de-Pro-l-album__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithoutSousCouv() throws IOException {
        String url = "https://www.bedetheque.com/serie-12544-BD-Viva-Patata__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithoutTome() throws IOException {
        String url = "https://www.bedetheque.com/serie-29776-BD-Atar-Gull-ou-le-destin-d-un-esclave-modele__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithTitleQuotes() throws IOException {
        String url = "https://www.bedetheque.com/serie-1343-BD-Et-patati-et-patata__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithTomeAndPoint() throws IOException {
        String url = "https://www.bedetheque.com/serie-29734-BD-20-sur-l-esprit-de-la-foret__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithTomeAndPointAndTitleStartWithPoint() throws IOException {
        String url = "https://www.bedetheque.com/serie-57019-BD-Et-les-bebes-viennent-de-Saturne__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

    @Test
    void retrieveAlbumsWithTome() throws IOException {
        String url = "https://www.bedetheque.com/serie-41252-BD-Reversal__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        for(ScrapedGraphicNovel s : sc)
            System.out.println(s.toString());
    }

}