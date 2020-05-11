package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.common.web.author.resources.AuthorResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface AuthorService {

    Author getAuthorById(Long id);

    Page<AuthorResource> getAuthors(Pageable page);

    Page<GraphicNovel> getGraphicNovelsByAuthorId(Pageable page, Long authorId);

    Author getAuthorByExternalId(String externalId) throws EntityNotFoundException;

    /**
     * Retrieve all existing nationalities <code>nationality</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct author's nationalities, ordered by asc
     */
    List<String> getDistinctNationalities();

    Page<Author> getFilteredAuthors(Pageable pageable, String lastname, String firstname, String nickname, String nationality);

    Author scrapAuthor(GenericAuthorUrl authorUrl) throws IOException;

    void scrapNewAuthorsInDb() throws IOException, InterruptedException;

    /**
     * Retrieve all authors starting with the given letter
     * @param contextResource the context to get
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    Page<AuthorResource> getAuthorsByLastnameStartingWith(ContextResource contextResource, String letter, Pageable pageable);

}
