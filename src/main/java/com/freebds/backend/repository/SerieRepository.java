package com.freebds.backend.repository;

import com.freebds.backend.common.web.dashboard.resources.PeriodicityCountResource;
import com.freebds.backend.common.web.serie.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.dashboard.resources.SeriesOriginCounterResource;
import com.freebds.backend.common.web.dashboard.resources.SeriesStatusCounterResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

//JpaSpecificationExecutor<Serie>, SerieSpecificationRepository
@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

    /**
     * Get the first serie found by external id
     * Note : no serie should have more than one external id
     * @sse com.freebds.backend.repository.SerieRepository#findSeriesByExternalId(String externakId) for a complete list of series filtered by external id.
     *
     * @param externalId the serie external id to search
     * @return a serie
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    Serie findFirstByExternalId(String externalId);

    /**
     * Get an/some serie(s) by external id
     *
     * @param externalId the serie external id to search
     * @return a serie
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    List<Serie> findSeriesByExternalId(String externalId);

    /**
     * Retrieve all existing series <code>origin types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series origin types, ordered by asc
     */
    @Query("SELECT DISTINCT s.origin FROM Serie s ORDER BY s.origin")
    List<String> findDistinctOrigins();

    /**
     * Retrieve all existing series <code>status types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series status types, ordered by asc
     */
    @Query("SELECT DISTINCT s.status FROM Serie s ORDER BY s.status")
    List<String> findDistinctStatus();

    /**
     * Retrieve all existing series <code>category types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series category types, ordered by asc
     */
    @Query("SELECT DISTINCT s.categories FROM Serie s ORDER BY s.categories")
    List<String> findDistinctCategories();

    /**
     * Retrieve all existing series <code>language types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series language types, ordered by asc
     */
    @Query("SELECT DISTINCT s.langage FROM Serie s ORDER BY s.langage")
    List<String> findDistinctLanguages();

    /**
     * Retrieve all series, filtered by title, origin, status and category
     *
     * @param pageable the page to get
     * @param title the title to get
     * @param origin the origin to get
     * @param status the status to get
     * @param categories the category to get
     * @return a page of filtered series
     */
    Page<Serie> findSeriesByTitleIgnoreCaseContainingAndOriginIgnoreCaseContainingAndStatusIgnoreCaseContainingAndCategoriesIgnoreCaseContaining(
            Pageable pageable, String title, String origin, String status, String categories
    );

    /**
     * Find Series by multiple criteria
     * @param pageable
     * @param serieTitle
     * @param serieExternalId
     * @param categories
     * @param status
     * @param origin
     * @param language
     * @param graphicNovelTitle
     * @param graphicNovelExternalId
     * @param publisher
     * @param collection
     * @param isbn
     * @param publicationDateFrom
     * @param publicationDateTo
     * @param lastname
     * @param firstname
     * @param nickname
     * @param authorExternalId
      * @param nationality the author nationality to get
     * @return
     */
    @Query(
            value = "SELECT distinct s FROM Serie s JOIN GraphicNovel g ON s.id = g.serie.id" +
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
            countQuery = "SELECT COUNT(distinct s) FROM Serie s JOIN GraphicNovel g ON s.id = g.serie.id" +
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
    Page<Serie> findBySearchFilters(
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
     * Find all series from an author (scenario, dessin, couleurs roles only)
     * @param authorId the author id to get
     * @return the list of author's series
     */
    @Query("SELECT distinct s  FROM Serie s " +
            "  JOIN GraphicNovel g ON s.id = g.serie.id  " +
            "  JOIN GraphicNovelAuthor ga ON ga.graphicNovel.id = g.id  " +
            "  JOIN Author a ON a.id = ga.author.id and a.id = :authorId " +
            "WHERE ga.role IN ('Scénario', 'Dessin', 'Couleurs') " +
            "ORDER BY s.title "
    )
    @Deprecated
    List<Serie> findMainSeriesByAuthor(@Param("authorId") Long authorId);

    /**
     * Find all series by author's roles
     * @param authorId the author id to get
     * @return the list of author's series
     */
    @Query(
            value =
            "select distinct rq1.id, rq1.title, rq1.role, rq1.publicationDateFrom, rq2.publicationDateTo from " +
            "( " +
            "        select distinct s.id, s.title, ga.role as role, min(g.publication_date) as publicationDateFrom FROM graphicnovel g " +
            "        JOIN serie s ON s.id = g.serie_id " +
            "        JOIN graphicNovel_author ga ON ga.graphicNovel_id = g.id " +
            "        JOIN author a ON a.id = ga.author_id and a.id = :authorId " +
            "        GROUP BY 1, 2, 3 " +
            ") rq1 " +
            "inner join " +
            "        ( " +
            "                SELECT distinct s.id, s.title, ga.role as role, max(g.publication_date) as publicationDateTo FROM graphicnovel g " +
            "                JOIN serie s ON s.id = g.serie_id " +
            "                JOIN graphicNovel_author ga ON ga.graphicNovel_id = g.id " +
            "                JOIN author a ON a.id = ga.author_id and a.id = :authorId " +
            "                GROUP BY 1, 2, 3 " +
            "        ) rq2 " +
            "on rq1.id = rq2.id and rq1.title = rq2.title and rq1.role = rq2.role " +
            "order by rq1.title, rq1.role "
            , nativeQuery = true
    )
    List<AuthorRolesBySerieResource> findSeriesByAuthorRoles(@Param("authorId") Long authorId);

    /**
     * Find all series by author's roles (from library)
     * @param authorId the author id to get
     * @return the list of author's series
     */
    @Query(
            value =
                    "select distinct rq1.id, rq1.title, rq1.role, rq1.publicationDateFrom, rq2.publicationDateTo from " +
                            "( " +
                            "        select distinct s.id, s.title, ga.role as role, min(g.publication_date) as publicationDateFrom FROM graphicnovel g " +
                            "        JOIN serie s ON s.id = g.serie_id " +
                            "        JOIN graphicNovel_author ga ON ga.graphicNovel_id = g.id " +
                            "        JOIN author a ON a.id = ga.author_id and a.id = :authorId " +
                            "        JOIN library_content lc ON g.id = lc.graphicnovel_id AND lc.library_id = :libraryId " +
                            "        GROUP BY 1, 2, 3 " +
                            ") rq1 " +
                            "inner join " +
                            "        ( " +
                            "                SELECT distinct s.id, s.title, ga.role as role, max(g.publication_date) as publicationDateTo FROM graphicnovel g " +
                            "                JOIN serie s ON s.id = g.serie_id " +
                            "                JOIN graphicNovel_author ga ON ga.graphicNovel_id = g.id " +
                            "                JOIN author a ON a.id = ga.author_id and a.id = :authorId " +
                            "                JOIN library_content lc ON g.id = lc.graphicnovel_id AND lc.library_id = :libraryId " +
                            "                GROUP BY 1, 2, 3 " +
                            "        ) rq2 " +
                            "on rq1.id = rq2.id and rq1.title = rq2.title and rq1.role = rq2.role " +
                            "order by rq1.title, rq1.role "
            , nativeQuery = true
    )
    List<AuthorRolesBySerieResource> findSeriesFromLibraryByAuthorRoles(@Param("libraryId") Long libraryId, @Param("authorId") Long authorId);

    /**
     * Find all series starting with the given letter
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of series
     */
    Page<Serie> findSeriesByTitleStartingWithIgnoreCase(@Param("letter") String letter, Pageable pageable);

    /**
     * Find all series starting with #
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of series
     */
    Page<Serie> findSeriesByTitleLessThanIgnoreCase(@Param("letter") String letter, Pageable pageable);


    /**
     * Count series by origin
     * @return
     */
    @Query(
            value = "SELECT s.origin AS name, COUNT(*) as y FROM Serie s GROUP BY s.origin ORDER BY 2",
            nativeQuery = true
    )
    List<SeriesOriginCounterResource> countSeriesByOrigin();


    /**
     * Count series by status
     * @return
     */
    @Query(
            value = "SELECT s.status AS status, COUNT(*) as count FROM Serie s GROUP BY s.status ORDER BY 2",
            nativeQuery = true
    )
    List<SeriesStatusCounterResource> countSeriesByStatus();


    @Query("SELECT g FROM GraphicNovel g WHERE " +
            "  g.publicationDate BETWEEN :publicationDateFrom AND :publicationDateTo"
    )
    @Deprecated
    Page<GraphicNovel> findByPublicationDates(
            Pageable pageable,
            @Param("publicationDateFrom") Date publicationDateFrom,
            @Param("publicationDateTo") Date publicationDateTo);

    /**
     * Find series within a library
     * @param libraryId : the library to scan
     * @param titleStartingWith : the title to get
     * @param pageable : the page to get
     * @return a page of series
     */
    @Query(
            value =
                    "SELECT DISTINCT s FROM Serie s " +
                            "  INNER JOIN LibrarySerieContent ls ON ls.serie.id = s.id " +
                            "WHERE ls.library.id = :libraryId " +
                            "  AND (s.title IS NULL OR LOWER(s.title) LIKE :titleStartingWith%) ",
            countQuery =
                    "SELECT COUNT(s) FROM Serie s " +
                            "  INNER JOIN LibrarySerieContent ls ON ls.serie.id = s.id " +
                            "WHERE ls.library.id = :libraryId " +
                            "  AND (s.title IS NULL OR LOWER(s.title) LIKE :titleStartingWith%) "
    )
    Page<Serie> findSeriesFromLibraryByTitleStartingWithIgnoreCase(@Param("libraryId") Long libraryId, @Param("titleStartingWith") String titleStartingWith, Pageable pageable);

    /**
     * Find series within a library starting with #
     * @param libraryId : the library to scan
     * @param titleStartingWith : the title to get
     * @param pageable : the page to get
     * @return a page of series
     */
    @Query(
            value =
                    "SELECT DISTINCT s FROM Serie s " +
                            "  INNER JOIN LibrarySerieContent ls ON ls.serie.id = s.id " +
                            "WHERE ls.library.id = :libraryId " +
                            "  AND (s.title IS NULL OR LOWER(s.title) < :titleStartingWith) ",
            countQuery =
                    "SELECT COUNT(s) FROM Serie s " +
                            "  INNER JOIN LibrarySerieContent ls ON ls.serie.id = s.id " +
                            "WHERE ls.library.id = :libraryId " +
                            "  AND (s.title IS NULL OR LOWER(s.title) < :titleStartingWith) "
    )
    Page<Serie> findSeriesFromLibraryByTitleLessThanIgnoreCase(@Param("libraryId") Long libraryId, @Param("titleStartingWith") String titleStartingWith, Pageable pageable);

    @Query(
            value = "select " +
                    "  to_char(g.publication_date, 'yyyy-MM') as periodicity, " +
                    "  count(distinct g.serie_id) as count " +
                    "from graphicnovel g " +
                    "where " +
                    "  g.publication_date > date_trunc('month', current_date - interval '13' month) " +
                    "  and g.publication_date <= date_trunc('month', current_date - interval '1' month) " +
                    "group by 1 " +
                    "union " +
                    "select " +
                    "  '0001-01' as periodicity, " +
                    "  count(distinct g.serie_id) as count " +
                    "from graphicnovel g " +
                    "where " +
                    "  g.publication_date <= date_trunc('month', current_date - interval '13' month) " +
                    "group by 1 " +
                    "order by 1 asc",
            nativeQuery = true
    )
    List<PeriodicityCountResource> countPublicationsByMonth();

}
