package com.freebds.backend.repository;

import com.freebds.backend.model.LibrarySerieContent;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface LibrarySerieContentRepository extends JpaRepository<LibrarySerieContent, Long> {

    /**
     * Check if a serie allready exists in a collection
     * @param serieId : the serie's id to check
     * @param libraryId : the library's id to check
     * @return : the count of series found (should always be one if everything is ok)
     */
    @Query("SELECT COUNT(s) FROM LibrarySerieContent s WHERE s.serie.id = :serieId AND s.library.id = :libraryId")
    Long checkIfExist(@Param("serieId") Long serieId, @Param("libraryId") Long libraryId);

    @Query("SELECT ls FROM LibrarySerieContent ls WHERE ls.library.id = :libraryId AND ls.serie.id = :serieId")
    List<LibrarySerieContent> findLibrarySerie(@Param("libraryId") Long libraryId, @Param("serieId") Long serieId);

    /**
     * Find series within a library
     * @param libraryId : the library to scan
     * @param titleStartingWith : the title to get
     * @param pageable : the page to get
     * @return a page of series
     */
    @Query(
            value =
                    "SELECT DISTINCT s FROM LibrarySerieContent ls " +
                    "  INNER JOIN Serie s ON ls.serie.id = s.id " +
                    "WHERE ls.library.id = :libraryId " +
                    "  AND (s.title IS NULL OR LOWER(s.title) LIKE :titleStartingWith%) ",
            countQuery =
                    "SELECT COUNT(s) FROM LibrarySerieContent ls " +
                    "  INNER JOIN Serie s ON ls.serie.id = s.id " +
                    "WHERE ls.library.id = :libraryId " +
                    "  AND (s.title IS NULL OR LOWER(s.title) LIKE :titleStartingWith%) "
    )
    Page<Serie> findAllSeries(@Param("libraryId") Long libraryId, @Param("titleStartingWith") String titleStartingWith, Pageable pageable);

    /**
     * Retrieve all existing authors by multiple criteria
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
     * @return a page of filtered series
     */
    @Query(
            value = "SELECT distinct s  FROM Serie s " +
                    "  JOIN LibrarySerieContent ls ON ls.serie.id = s.id " +
                    "  JOIN GraphicNovel g ON g.serie.id = s.id  " +
                    "  JOIN GraphicNovelAuthor ga ON g.id = ga.graphicNovel.id " +
                    "  JOIN Author a ON ga.author.id = a.id " +
                    // Serie filters
                    "WHERE ls.library.id = :libraryId " +
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
            countQuery = "SELECT COUNT(distinct s)  FROM Serie s " +
                    "  JOIN LibrarySerieContent ls ON ls.serie.id = s.id " +
                    "  JOIN GraphicNovel g ON g.serie.id = s.id  " +
                    "  JOIN GraphicNovelAuthor ga ON g.id = ga.graphicNovel.id " +
                    "  JOIN Author a ON ga.author.id = a.id " +
                    // Serie filters
                    "WHERE ls.library.id = :libraryId " +
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
    Page<Serie> findBySearchFilters(
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
