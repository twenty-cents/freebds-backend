package com.freebds.freebdsbackend.service;

import com.freebds.freebdsbackend.business.scrapers.bedetheque.dto.ScrapedAuthor;
import com.freebds.freebdsbackend.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@RunWith(MockitoJUnitRunner.class)
class AuthorServiceImplTest {

    //@Mock
    AuthorRepository authorRepository;

    //@InjectMocks
    AuthorServiceImpl authorService;

    // https://www.freecodecamp.org/news/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772/
    //@Test
    void scrapAuthorByUrl() {
        String url = "https://www.bedetheque.com/auteur-77-BD-Greg.html";
        ScrapedAuthor scrapedAuthor = authorService.scrapAuthorByUrl(url);

        assertThat(scrapedAuthor.getNickname()).isEqualTo("Greg");
    }
}