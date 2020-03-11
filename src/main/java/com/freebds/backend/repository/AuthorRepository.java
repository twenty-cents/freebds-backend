package com.freebds.backend.repository;

import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Get the first author found by external id
     * Note : no author should have more than one external id
     * @sse com.freebds.backend.repository.AuthorRepository#findAuthorByExternalId(String externakId) for a complete list of authors filtered by external id.
     *
     * @param externalId the author external id to search
     * @return an author
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    Author findFirstByExternalId(String externalId);

    /**
     * Get an author by external id
     *
     * @param externalId the author external id to search
     * @return an author
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    List<Author> findAuthorByExternalId(String externalId);

    /**
     * Get all graphic novels from an author
     *
     * @param page the page to get
     * @param authorId the author id to search
     * @return a list of graphic novels
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    @Query("SELECT gn FROM GraphicNovel gn WHERE gn.id IN ( " +
            "SELECT ga.graphicNovel.id FROM GraphicNovelAuthor ga WHERE ga.author.id = :author_id )")
    Page<GraphicNovel> findGraphicNovelAuthorByAuthor(Pageable page, @Param("author_id") Long authorId);

    /**
     * Retrieve all authors filtered by lastname, firstname, nickname, nationality
     *
     * @param page the page to get
     * @param lastname the author lastname to get
     * @param firstname the author firstname to get
     * @param nickname the author nickname to get
     * @param nationality the author nationality to get
     * @return a page of filtered authors
     */
    Page<Author> findAuthorsByLastnameIgnoreCaseContainingAndFirstnameIgnoreCaseContainingAndNicknameIgnoreCaseContainingAndNationalityIgnoreCaseContaining(
            Pageable page, String lastname, String firstname, String nickname, String nationality);

}
