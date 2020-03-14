package com.freebds.backend.repository;

import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    @Query("SELECT DISTINCT lower(s.categories) FROM Serie s ORDER BY lower(s.categories)")
    List<String> findDistinctCategories();

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

}
