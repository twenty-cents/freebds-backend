package com.freebds.backend.business.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GenericScraper {

    // TODO : @Value
    private static final int delayBetweenTwoScraps = 1000;

    public GenericScraper() {
    }

    /**
     * Load a http page
     *
     * The default delay between two scraps is defined by the parameter <code>delayBetweenTwoScraps</code>
     * If the loading fail, a second try is executed after 10 seconds.
     * If the second try fail, an IO Exception is raised
     *
     * @param url the url to load
     * @return the url content loaded
     * @throws IOException in case of access error
     */
    public Document load(String url) throws IOException {
        // Default delay between two scraping
        try {
            Thread.sleep(delayBetweenTwoScraps);
        } catch (InterruptedException ex) {
        }

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
