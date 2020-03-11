package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthorService {

    Author getAuthorById(Long id);

    Page getAuthors(Pageable page);

    Page<GraphicNovel> getGraphicNovelsByAuthorId(Pageable page, Long authorId);

    Author getAuthorByExternalId(String externalId);

    Page<Author> getFilteredAuthors(Pageable pageable, String lastname, String firstname, String nickname, String nationality);

    Author scrapAuthor(GenericAuthorUrl authorUrl) throws IOException;

    void scrapNewAuthorsInDb() throws IOException, InterruptedException;

}
