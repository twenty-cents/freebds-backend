package com.freebds.freebdsbackend.service;

import com.freebds.freebdsbackend.business.scrapers.bedetheque.authors.BedethequeAuthorScraper;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthor;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthorUrl;
import com.freebds.freebdsbackend.model.Author;
import com.freebds.freebdsbackend.repository.AuthorRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

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

    /**
     * Import a scraped author into the database
     *
     * @param scrapedAuthor : the author to import
     * @return the author added into the database
     */
    @Override
    public Author importAuthor(ScrapedAuthor scrapedAuthor) {
        Author author = new Author();
        author.setExternalId(scrapedAuthor.getId());
        author.setNickname(scrapedAuthor.getNickname());
        author.setLastname(scrapedAuthor.getLastname());
        author.setFirstname(scrapedAuthor.getFirstname());
        author.setNationality(scrapedAuthor.getNationality());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        try {
            author.setBirthdate(LocalDate.parse(scrapedAuthor.getBirthdate(), formatter));
        } catch (DateTimeParseException e) {
            author.setBirthdate(null);
        }

        try {
            author.setDeceaseDate(LocalDate.parse(scrapedAuthor.getDeceaseDate(), formatter));
        } catch (DateTimeParseException e) {
            author.setDeceaseDate(null);
        }

        author.setBiography(scrapedAuthor.getBiography());
        author.setSiteUrl(scrapedAuthor.getSiteUrl());
        author.setScrapUrl(scrapedAuthor.getAuthorUrl());

        return authorRepository.save(author);
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
