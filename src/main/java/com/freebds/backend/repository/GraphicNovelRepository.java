package com.freebds.backend.repository;

import com.freebds.backend.common.web.dashboard.resources.PeriodicityCountResource;
import com.freebds.backend.common.web.dashboard.resources.PublicationsByMonthGroupByOriginResource;
import com.freebds.backend.common.web.author.resources.AuthorRoleResource;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelMinimumResource;
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
     * @param nationality the author nationality to get
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
                    "    AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%)" +
                    "    AND (:nationality IS NULL OR a.nationality = :nationality))",
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
                    "    AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%)" +
                    "    AND (:nationality IS NULL OR a.nationality = :nationality))"
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
            @Param("authorExternalId") String authorExternalId,
            @Param("nationality") String nationality);

    /**
     * Find all graphic novels from a serie within a library
     * @param libraryId : the id library to scan
     * @param serieId the id serie to get
     * @param pageable : the page to get
     * @return a page of graphic novels
     */
    @Query(
            value = "SELECT DISTINCT g FROM GraphicNovel g " +
                    "  JOIN LibraryContent lg ON g.id = lg.graphicNovel.id " +
                    "WHERE lg.library.id = :libraryId AND g.serie.id = :serieId",
            countQuery = "SELECT COUNT(DISTINCT g) FROM GraphicNovel g " +
                    "  JOIN LibraryContent lg ON g.id = lg.graphicNovel.id " +
                    "WHERE lg.library.id = :libraryId AND g.serie.id = :serieId"
    )
    Page<GraphicNovel> findGraphicNovelsFromLibraryBySerie(@Param("libraryId") Long libraryId, @Param("serieId") Long serieId, Pageable pageable);

    /**
     * Find missing graphic novels by serie from a library
     * @param libraryId
     * @param serieId
     * @return
     */
    @Query("SELECT " +
            "  g.id as id," +
            "  g.tome as tome," +
            "  g.numEdition as numEdition," +
            "  g.title as title," +
            "  g.publicationDate as publicationDate " +
            "FROM GraphicNovel g " +
            "WHERE g.serie.id = :serieId " +
            "  AND g.id NOT IN ( " +
            "    SELECT lc.graphicNovel.id FROM LibraryContent lc " +
            "    WHERE lc.library.id = :libraryId ) " +
            "ORDER BY g.publicationDate")
    List<GraphicNovelMinimumResource> findMissingGraphicNovelsFromLibraryBySerie(@Param("libraryId") Long libraryId, @Param("serieId") Long serieId);

    /**
     * Find some graphic novels by a ISBN
     * @param isbn
     * @return
     */
    @Query("SELECT g FROM GraphicNovel g WHERE g.isbn = :isbn ORDER BY g.serie.id, g.tome, g.numEdition")
    List<GraphicNovel> findGraphicNovelsByISBN(@Param("isbn") String isbn);

    /**
     * Find publications count by origin by month
     * @return the statistics result resource
     */
    @Query(
            value = "SELECT " +
                    "  to_char(g.publication_date, 'yyyy-MM') as month, " +
                    "  s.origin as origin, " +
                    "  count(*) as count " +
                    "FROM graphicnovel g " +
                    "INNER JOIN serie s ON g.serie_id = s.id " +
                    "WHERE " +
                    "  g.publication_date > :dateFrom  " +
                    "  AND g.publication_date <= :dateTo  " +
                    "GROUP BY 1, 2 " +
                    "ORDER BY 1 ASC, 2 ",
            nativeQuery = true)
    List<PublicationsByMonthGroupByOriginResource> findPublicationsByMonthGroupByOrigin(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);


    /**
     * Find publications growth by month
     * @return the statistics result resource
     */
    @Query(
            value = "select " +
                    "  to_char(g.publication_date, 'yyyy-MM') as periodicity, " +
                    "  count(*) as count " +
                    "from graphicnovel g " +
                    "where\n" +
                    "  g.publication_date > :dateFrom  " +
                    "  and g.publication_date <= :dateTo " +
                    "group by 1  " +
                    "union " +
                    "select " +
                    "  '0001-01' as periodicity, " +
                    "  count(*) as count " +
                    "from graphicnovel g " +
                    "where\n" +
                    "  g.publication_date <= :dateTo " +
                    "group by 1 " +
                    "order by 1 asc",
            nativeQuery = true
    )
    List<PeriodicityCountResource> countPublicationsByMonth(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

}
