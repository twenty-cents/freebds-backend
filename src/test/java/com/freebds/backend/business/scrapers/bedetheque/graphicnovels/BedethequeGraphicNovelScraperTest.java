package com.freebds.backend.business.scrapers.bedetheque.graphicnovels;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BedethequeGraphicNovelScraperTest {

    @Test
    void retrieveAlbums() throws IOException {
        String url = "https://www.bedetheque.com/serie-59-BD-Asterix__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(37).getTitle()).isEqualTo("La Fille de Vercingétorix");
    }

    @Test
    void retrieveAlbumsWithReleaseDate() throws IOException {
        String url = "https://www.bedetheque.com/serie-68754-BD-Ne-regarde-pas-derriere-toi__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getReleaseDate()).isEqualTo("21/02/2020");
    }

    @Test
    void retrieveAlbumsWithPublicationDateAndParution() throws IOException {
        String url = "https://www.bedetheque.com/serie-67280-BD-Pauvre-humanite__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getPublicationDate()).isEqualTo("05/2019");
        assertThat(sc.get(0).getReleaseDate()).isEqualTo("01/06/2019");
    }

    @Test
    void retrieveAlbumsWithLongNumEdition() throws IOException {
        String url = "https://www.bedetheque.com/serie-19097-BD-Graine-de-Pro-l-album__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getTome()).isEqualTo("");
        assertThat(sc.get(0).getNumEdition()).isEqualTo("");
        assertThat(sc.get(0).getTitle()).isEqualTo("L'album des lauréats du concours B.D scolaire 85.93");
    }

    @Test
    void retrieveAlbumsWithoutSousCouv() throws IOException {
        String url = "https://www.bedetheque.com/serie-12544-BD-Viva-Patata__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getBackCoverPictureUrl()).isEqualTo(null);
    }

    @Test
    void retrieveAlbumsWithoutTome() throws IOException {
        String url = "https://www.bedetheque.com/serie-29776-BD-Atar-Gull-ou-le-destin-d-un-esclave-modele__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getTome()).isEqualTo("");
    }

    @Test
    void retrieveAlbumsWithTitleQuotes() throws IOException {
        String url = "https://www.bedetheque.com/serie-1343-BD-Et-patati-et-patata__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getTitle()).isEqualTo(" \"Et patati, et patata...\"");
    }

    @Test
    void retrieveAlbumsWithTomeAndPoint() throws IOException {
        String url = "https://www.bedetheque.com/serie-29734-BD-20-sur-l-esprit-de-la-foret__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getTitle()).isEqualTo("-20% sur l'esprit de la forêt");
    }

    @Test
    void retrieveAlbumsWithTomeAndPointAndTitleStartWithPoint() throws IOException {
        String url = "https://www.bedetheque.com/serie-57019-BD-Et-les-bebes-viennent-de-Saturne__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getTitle()).isEqualTo("... Et les bébés viennent de Saturne !");
    }

    @Test
    void retrieveAlbumsWithTome() throws IOException {
        String url = "https://www.bedetheque.com/serie-41252-BD-Reversal__10000.html";
        BedethequeGraphicNovelScraper bedethequeGraphicNovelScraper = new BedethequeGraphicNovelScraper();
        List<ScrapedGraphicNovel> sc = bedethequeGraphicNovelScraper.retrieveAlbums(url);
        // Test
        assertThat(sc.get(0).getTitle()).isEqualTo("Tome 1");
    }

}