package com.freebds.backend.business.scrapers.bedetheque.filters;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class GraphicNovelsFilterTest {

    @Test
    void filter() throws IOException {
        GraphicNovelsFilter graphicNovelsFilter = new GraphicNovelsFilter();
        graphicNovelsFilter.filter("", "", "", "", "", "",
                "", "","", "", "", "Anglais", "",
                "", "", "", "", "0");
    }

    @Test
    void autocompleteCategories() throws IOException {
        GraphicNovelsFilter graphicNovelsFilter = new GraphicNovelsFilter();
        graphicNovelsFilter.autocompleteCategories("humo");
    }

    @Test
    void autocompleteAuthors() throws IOException {
        GraphicNovelsFilter graphicNovelsFilter = new GraphicNovelsFilter();
        System.out.println(graphicNovelsFilter.autocompleteAuthors("franq"));
    }
}