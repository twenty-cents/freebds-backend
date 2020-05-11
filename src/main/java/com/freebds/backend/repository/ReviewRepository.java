package com.freebds.backend.repository;

import com.freebds.backend.common.web.graphicNovel.resources.ReviewResource;
import com.freebds.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT " +
            "r.id as id, " +
            "r.user.id as userId, " +
            "r.librarySerieContent.id as librarySerieContentId, " +
            "r.libraryContent.id as libraryContentId, " +
            "r.score as rating, " +
            "r.comment as comment," +
            "r.lastUpdateDate as lastUpdateDate, " +
            "r.lastUpdateUser as lastUpdateUser " +
            "FROM Review r " +
            "WHERE r.librarySerieContent.id = :librarySerieId " +
            "  AND r.libraryContent.id = :libraryContentId")
    List<ReviewResource> findReviewsByLibraryContent(
            @Param("librarySerieId") Long librarySerieId,
            @Param("libraryContentId") Long libraryContentId);
}
