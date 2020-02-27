package com.freebds.freebdsbackend.business.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GenericScraper {

    public GenericScraper() {
    }

    public Document load(String url) throws IOException {
        return Jsoup.connect(url).maxBodySize(0).userAgent("Mozilla").get();
    }
}
