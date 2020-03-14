package com.freebds.backend.repository;

import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.GraphicNovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraphicNovelRepository extends JpaRepository<GraphicNovel, Long> {

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
     * @param graphicNovel the graphic novel to save
     * @return the graphic novel saved
     */
    GraphicNovel saveAndFlush(GraphicNovel graphicNovel);

    @Query("SELECT g FROM GraphicNovel g WHERE g.serie.id = :id ORDER BY g.tome")
    List<GraphicNovel> findGraphicNovelsBySerieIdOrdeOrderByTome(@Param("id") Long serieId);

}
