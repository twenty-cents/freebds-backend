package com.freebds.backend.repository;

import com.freebds.backend.common.web.resources.AuthorRoleResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
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
public interface GraphicNovelRepository extends JpaRepository<GraphicNovel, Long>, JpaSpecificationExecutor<GraphicNovel> {

    /**
     * Get the first graphic novel found by external id
     * Note : no graphic novel should have more than one external id
     * @sse com.freebds.backend.repository.GraphicNovelRepository#findGraphicNovelsByExternalId(String externalId) for a complete list of GraphicNovels filtered by external id.
     *
     * @param externalId the graphic novel external id to search
     * @return a graphic novel
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    GraphicNovel findFirstByExternalId(String externalId);

    /**
     * Get an/some graphic novel(s) by external id
     *
     * @param externalId the graphic novel external id to search
     * @return a list with the graphic novel found
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    List<GraphicNovel> findGraphicNovelsByExternalId(String externalId);

    /**
     * Save and commit a graphic novel into the db
     *
     * param graphicNovel the graphic novel to save
     * return the graphic novel saved

    GraphicNovel saveAndFlush(GraphicNovel graphicNovel);
     */

    /**
     *
     * @param graphicNovelId
     * @return
     * https://stackoverflow.com/questions/29082749/spring-data-jpa-map-the-native-query-result-to-non-entity-pojo
     */
    @Query("SELECT " +
            "  a.id as id, " +
            "  a.lastname as lastname, " +
            "  a.firstname as firstname, " +
            "  a.nickname as nickname, " +
            "  ga.role as role " +
            "FROM Author a " +
            "  JOIN GraphicNovelAuthor ga ON a.id = ga.author.id AND ga.graphicNovel.id = :graphicNovelId " +
            "ORDER BY ga.role, a.nickname, a.lastname, a.firstname"
    )
    List<AuthorRoleResource> findAuthorRolesById(@Param("graphicNovelId") Long graphicNovelId);


    @Query("SELECT g FROM GraphicNovel g WHERE g.serie.id = :id ORDER BY g.tome")
    List<GraphicNovel> findGraphicNovelsBySerieIdOrderByTome(@Param("id") Long serieId);


    /**
     * Retrieve all graphic novels from a serie
     * @param pageable
     * @param serie
     * @return
     */
    Page<GraphicNovel> findGraphicNovelsBySerieEquals(Pageable pageable, Serie serie);

    /**
     * Retrieve all existing graphic novels by multiple criteria
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
     * @return a page of filtered graphic novels
     */
    @Query(
            value = "SELECT distinct g FROM GraphicNovel g JOIN Serie s ON s.id = g.serie.id" +
                    // Serie filters
                    "  WHERE (:serieTitle IS NULL OR LOWER(s.title) LIKE %:serieTitle%) " +
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
                    "  AND g.id IN ( " +
                    "    SELECT ga.graphicNovel.id FROM GraphicNovelAuthor ga JOIN Author a ON ga.author.id = a.id " +
                    // Author filters
                    "    WHERE (:authorExternalId IS NULL OR a.externalId = :authorExternalId) " +
                    "    AND (:lastname IS NULL OR LOWER(a.lastname) LIKE %:lastname%)" +
                    "    AND (:firstname IS NULL OR LOWER(a.firstname) LIKE %:firstname%)" +
                    "    AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%))",
            countQuery = "SELECT COUNT(distinct g) FROM GraphicNovel g JOIN Serie s ON s.id = g.serie.id" +
                    // Serie filters
                    "  WHERE (:serieTitle IS NULL OR LOWER(s.title) LIKE %:serieTitle%) " +
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
                    "  AND g.id IN ( " +
                    "    SELECT ga.graphicNovel.id FROM GraphicNovelAuthor ga JOIN Author a ON ga.author.id = a.id " +
                    // Author filters
                    "    WHERE (:authorExternalId IS NULL OR a.externalId = :authorExternalId) " +
                    "    AND (:lastname IS NULL OR LOWER(a.lastname) LIKE %:lastname%)" +
                    "    AND (:firstname IS NULL OR LOWER(a.firstname) LIKE %:firstname%)" +
                    "    AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%))"
    )
    Page<GraphicNovel> findBySearchFilters(
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

}
