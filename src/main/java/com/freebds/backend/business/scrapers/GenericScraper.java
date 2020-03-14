package com.freebds.backend.business.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GenericScraper {

    public GenericScraper() {
    }

    public Document load(String url) throws IOException {
        // First try
        try {
            return Jsoup.connect(url).maxBodySize(0).userAgent("Mozilla").get();
        } catch (IOException e) {
            // Second try, after 10 sec
            // And throw IOException if http access error
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
            }
            return Jsoup.connect(url).maxBodySize(0).userAgent("Mozilla").get();
        }
    }
}
