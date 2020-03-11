package com.freebds.backend.service;

import com.freebds.backend.repository.AuthorRepository;

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
        //ScrapAuthorDTO scrapAuthorDTO = authorService.scrapAuthorByUrl(url);

        //assertThat(scrapAuthorDTO.getNickname()).isEqualTo("Greg");
    }
}