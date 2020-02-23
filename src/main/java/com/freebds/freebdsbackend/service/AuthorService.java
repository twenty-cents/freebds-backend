package com.freebds.freebdsbackend.service;

import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthor;
import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthorUrl;
import com.freebds.freebdsbackend.model.Author;

import java.util.List;

public interface AuthorService {

    Author addAuthor();

    Author updateAuthor();

    Author getAuthorById();

    Author getAuthorByExternalId();

    boolean deleteAuthorById(long id);


    ScrapedAuthor scrapAuthorById(String externalId);

    ScrapedAuthor scrapAuthorByUrl(String url);

    List<ScrapedAuthorUrl> scrapAuthorsByLetter(String letter);

}
