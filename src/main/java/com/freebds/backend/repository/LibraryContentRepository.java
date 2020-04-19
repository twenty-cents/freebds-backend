package com.freebds.backend.repository;

import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.LibraryContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface LibraryContentRepository extends JpaRepository<LibraryContent, Long> {

    //@Query("SELECT c.isFavorite, c. FROM LibraryContent  c WHERE c.graphicNovel.id = :graphicNovelId")
    Optional<LibraryContent> findFirstByGraphicNovel_Id(@Param("graphicNovelId") Long graphicNovelId);

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
     * Find all missing graphic novels from a serie within a library
     * @param libraryId : the id library to scan
     * @param serieId the id serie to get
     * @param pageable : the page to get
     * @return a page of graphic novels
     */
    @Query(
            value = "SELECT DISTINCT g FROM GraphicNovel g " +
                    "  JOIN LibrarySerieContent ls ON g.id = ls.serie.id " +
                    "WHERE ls.library.id = :libraryId AND ls.serie.id = :serieId " +
                    "  AND g.id NOT IN ( " +
                    "    SELECT lg.id FROM LibraryContent lg )",
            countQuery = "SELECT COUNT(DISTINCT g) FROM GraphicNovel g " +
                    "  JOIN LibrarySerieContent ls ON g.id = ls.serie.id " +
                    "WHERE ls.library.id = :libraryId AND ls.serie.id = :serieId " +
                    "  AND g.id NOT IN ( " +
                    "    SELECT lg.id FROM LibraryContent lg )"
    )
    Page<GraphicNovel> findMissingGraphicNovelsFromLibraryBySerie(@Param("libraryId") Long libraryId, @Param("serieId") Long serieId, Pageable pageable);

    /**
     * Retrieve all existing graphic novels by multiple criteria
     * @param pageable the page to get
     * @param libraryId the library id to get
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
            value = "SELECT distinct g FROM GraphicNovel g " +
                    "  JOIN Serie s ON g.serie.id = s.id " +
                    "  JOIN LibraryContent lc ON lc.graphicNovel.id = g.id " +
                    "  JOIN GraphicNovelAuthor ga ON g.id = ga.graphicNovel.id " +
                    "  JOIN Author a ON ga.author.id = a.id " +
                    // Serie filters
                    "WHERE lc.library.id = :libraryId " +
                    "  AND (:serieTitle IS NULL OR LOWER(s.title) LIKE %:serieTitle%) " +
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
                    "  AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%)" +
                    "  AND (:nationality IS NULL OR a.nationality = :nationality)",
            countQuery = "SELECT COUNT(distinct g) FROM GraphicNovel g " +
                    "  JOIN Serie s ON g.serie.id = s.id " +
                    "  JOIN LibraryContent lc ON lc.graphicNovel.id = g.id " +
                    "  JOIN GraphicNovelAuthor ga ON g.id = ga.graphicNovel.id " +
                    "  JOIN Author a ON ga.author.id = a.id " +
                    // Serie filters
                    "WHERE lc.library.id = :libraryId " +
                    "  AND (:serieTitle IS NULL OR LOWER(s.title) LIKE %:serieTitle%) " +
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
                    "  AND (:nickname IS NULL OR LOWER(a.nickname) LIKE %:nickname%)" +
                    "  AND (:nationality IS NULL OR a.nationality = :nationality)"
    )
    Page<GraphicNovel> findBySearchFilters(
            Pageable pageable,
            @Param("libraryId") Long libraryId,
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
}
