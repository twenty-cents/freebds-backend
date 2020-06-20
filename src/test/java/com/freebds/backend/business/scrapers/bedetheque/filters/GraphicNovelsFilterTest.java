package com.freebds.backend.business.scrapers.bedetheque.filters;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GraphicNovelsFilterTest {

    @Test
    void filter() throws IOException {
        GraphicNovelsFilter graphicNovelsFilter = new GraphicNovelsFilter();
        GraphicNovelsFilteredObject graphicNovelsFilteredObject = graphicNovelsFilter.filter("19097", "", "", "", "", "",
                "", "","", "", "", "", "",
                "", "", "", "", "0");
        // Test
        assertThat(graphicNovelsFilteredObject.getFilteredGraphicNovelDetails().size()).isEqualTo(1);
    }

    @Test
    void autocompleteCategories() throws IOException {
        GraphicNovelsFilter graphicNovelsFilter = new GraphicNovelsFilter();
        String autocomplete = graphicNovelsFilter.autocompleteCategories("Humour noir, Roman graphique");
        // Test
        assertThat(autocomplete).isEqualTo("[{\"id\":\"Humour noir, Roman graphique\",\"label\":\"Humour noir, Roman graphique\",\"value\":\"Humour noir, Roman graphique\"}]");
    }

    @Test
    void autocompleteAuthors() throws IOException {
        GraphicNovelsFilter graphicNovelsFilter = new GraphicNovelsFilter();
        String autocomplete = graphicNovelsFilter.autocompleteAuthors("Franquin, Isabelle");
        // Test
        assertThat(autocomplete).isEqualTo("[{\"id\":\"34832\",\"label\":\"Franquin, Isabelle\",\"value\":\"Franquin, Isabelle\"}]");
    }
}