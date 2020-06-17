package com.freebds.backend.business.scrapers.bedetheque.filters;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalFilterTest {

    @Test
    void filter() throws IOException {
        GlobalFilter globalFilter = new GlobalFilter();
        globalFilter.filter("gaston");

    }
}