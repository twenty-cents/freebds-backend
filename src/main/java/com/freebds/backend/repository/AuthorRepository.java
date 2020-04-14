package com.freebds.backend.repository;

import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

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
    List<Author> findAuthorsByExternalId(String externalId);

    /**
     * Retrieve all existing authors <code>nationality types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct authors nationality types, ordered by asc
     */
    @Query("SELECT DISTINCT a.nationality FROM Author a ORDER BY a.nationality")
    List<String> findDistinctNationalities();

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

    /**
     * Retrieve all existing authors by multiple criteria
     * @param pageable the page to get
     * @param serieTitle the serie title to get
     * @param serieExternalId the serie external id to get
     * @param categories the serie category to get
     * @param status the serie status to get
     * @param origin the serie origin to get
     * @param language the serie language to get
     * @param graphicNovelTitle the graphic novel title to get
     * @param graphicNovelExternalId the graphic novel external id to get
     * @param publisher the graphic novel publisher to get
     * @param collection the graphic novel collection to get
     * @param isbn the graphic novel ISBN to get
     * @param publicationDateFrom the graphic novel publication date from to get
     * @param publicationDateTo the graphic novel publication date to to get
     * @param lastname the author lastname to get
     * @param firstname the author firstname to get
     * @param nickname the author nickname to get
     * @param authorExternalId the author external id to get
     * @return a page of filtered authors
     */
    @Query(
            value = "SELECT distinct a  FROM Author a " +
                    "  JOIN GraphicNovelAuthor ga ON a.id = ga.author.id " +
                    "  JOIN GraphicNovel g ON ga.graphicNovel.id = g.id  " +
                    "  JOIN Serie s ON g.serie.id = s.id " +
                    // Serie filters
                    "WHERE " +
                    "  (:serieTitle IS NULL OR LOWER(s.title) LIKE %:serieTitle%) " +
                    "  AND (:serieExternalId IS NULL OR s.externalId = :serieExternalId) " +
                    "  AND (:categories IS NULL OR s.categories LIKE %:categories%) " +
                    "  AND (:status IS NULL OR s.status = :status) " +
                    "  AND (:origin IS NULL OR s.origin = :origin) " +
                    "  AND (:language IS NULL OR s.langage = :language) " +
                    // Graphic novel filters
                    "  AND (:graphicNovelTitle IS NULL OR LOWER(g.title) LIKE %:graphicNovelTitle%)" +
                    "  AND (:graphicNovelExternalId IS NULL OR g.externalId = :graphicNovelExternalId) " +
                    "  AND (:publisher IS NULL OR LOWER(g.publisher) LIKE %:publisher%)" +
                    "  AND (:collection IS NULL OR LOWER(g.collection) LIKE %:collection%)" +
                    "  AND (:isbn IS NULL OR g.isbn = :isbn) " +
                    "  AND (g.publicationDate BETWEEN :publicationDateFrom AND :publicationDateTo) " +
                    // Author filters
                    "  AND (:authorExternalId IS NULL OR a.externalId = :authorExternalId) " +
                    "  AND (:lastname IS NULL OR LOWER(a.lastname) LIKE %:lastname%)" +
                    "  AND (:firstname IS NULL OR LOWER(a.firstname) LIKE %:firstname%)" +
                    "  AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%)",
            countQuery = "SELECT COUNT(distinct a) FROM Author a " +
                        "  JOIN GraphicNovelAuthor ga ON a.id = ga.author.id " +
                        "  JOIN GraphicNovel g ON ga.graphicNovel.id = g.id " +
                        "  JOIN Serie s ON g.serie.id = s.id " +
                        // Serie filters
                        "WHERE " +
                        "  (:serieTitle IS NULL OR LOWER(s.title) LIKE %:serieTitle%) " +
                        "  AND (:serieExternalId IS NULL OR s.externalId = :serieExternalId) " +
                        "  AND (:categories IS NULL OR s.categories LIKE %:categories%) " +
                        "  AND (:status IS NULL OR s.status = :status) " +
                        "  AND (:origin IS NULL OR s.origin = :origin) " +
                        "  AND (:language IS NULL OR s.langage = :language) " +
                        // Graphic novel filters
                        "  AND (:graphicNovelTitle IS NULL OR LOWER(g.title) LIKE %:graphicNovelTitle%)" +
                        "  AND (:graphicNovelExternalId IS NULL OR g.externalId = :graphicNovelExternalId) " +
                        "  AND (:publisher IS NULL OR LOWER(g.publisher) LIKE %:publisher%)" +
                        "  AND (:collection IS NULL OR LOWER(g.collection) LIKE %:collection%)" +
                        "  AND (:isbn IS NULL OR g.isbn = :isbn) " +
                        "  AND (g.publicationDate BETWEEN :publicationDateFrom AND :publicationDateTo) " +
                        // Author filters
                        "  AND (:authorExternalId IS NULL OR a.externalId = :authorExternalId) " +
                        "  AND (:lastname IS NULL OR LOWER(a.lastname) LIKE %:lastname%)" +
                        "  AND (:firstname IS NULL OR LOWER(a.firstname) LIKE %:firstname%)" +
                        "  AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%)"
    )
    Page<Author> findBySearchFilters(
            Pageable pageable,
            // Serie filters parameters
            @Param("serieTitle") String serieTitle,
            @Param("serieExternalId") String serieExternalId,
            @Param("categories") String categories,
            @Param("status") String status,
            @Param("origin") String origin,
            @Param("language") String language,
            // Graphic novel filters parameters
            @Param("graphicNovelTitle") String graphicNovelTitle,
            @Param("graphicNovelExternalId") String graphicNovelExternalId,
            @Param("publisher") String publisher,
            @Param("collection") String collection,
            @Param("isbn") String isbn,
            @Param("publicationDateFrom") Date publicationDateFrom,
            @Param("publicationDateTo") Date publicationDateTo,
            // Author filters parameters
            @Param("lastname") String lastname,
            @Param("firstname") String firstname,
            @Param("nickname") String nickname,
            @Param("authorExternalId") String authorExternalId);

    /**
     * Find all authors starting with the given letter
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    Page<Author> findAuthorsByLastnameStartingWithIgnoreCase(@Param("letter") String letter, Pageable pageable);

    /**
     * Find all authors starting with #
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    Page<Author> findAuthorsByLastnameLessThanIgnoreCase(@Param("letter") String letter, Pageable pageable);
}
