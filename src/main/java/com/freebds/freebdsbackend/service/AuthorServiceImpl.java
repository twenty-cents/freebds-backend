package com.freebds.freebdsbackend.service;

import com.freebds.freebdsbackend.business.scrapers.bedetheque.authors.BedethequeAuthorScraper;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthor;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthorUrl;
import com.freebds.freebdsbackend.model.Author;

import java.io.IOException;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    @Override
    public Author addAuthor() {
        return null;
    }

    @Override
    public Author updateAuthor() {
        return null;
    }

    @Override
    public Author getAuthorById() {
        return null;
    }

    @Override
    public Author getAuthorByExternalId() {
        return null;
    }

    @Override
    public boolean deleteAuthorById(long id) {
        return false;
    }

    @Override
    public ScrapedAuthor scrapAuthorById(String externalId) {
        return null;
    }

    @Override
    public ScrapedAuthor scrapAuthorByUrl(String url) {
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();
        ScrapedAuthorUrl scrapedAuthorUrl = new ScrapedAuthorUrl();
        scrapedAuthorUrl.setUrl(url);
        ScrapedAuthor scrapedAuthor = null;
        try {
            scrapedAuthor = bedethequeAuthorScraper.scrapAuthor(scrapedAuthorUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scrapedAuthor;
    }

    @Override
    public List<ScrapedAuthorUrl> scrapAuthorsByLetter(String letter) {
        return null;
    }
}
