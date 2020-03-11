package com.freebds.backend.repository;

import com.freebds.backend.model.GraphicNovelAuthor;
import com.freebds.backend.model.GraphicNovelAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraphicNovelAuthorRepository  extends JpaRepository<GraphicNovelAuthor, GraphicNovelAuthorId> {

    /**
     * Check if a role allready exists on a tuple graphic novel / author
     * @param graphicNovelId the graphic novel
     * @param authorId the author id
     * @param role the role to check
     * @return the row if this association allready exists
     */
    @Query("SELECT g FROM GraphicNovelAuthor g " +
            "WHERE g.graphicNovel.id = :graphicnovel_id " +
            "AND g.author.id = :author_id " +
            "AND g.role = :role")
    List<GraphicNovelAuthor> isRoleExist
            (@Param("graphicnovel_id") Long graphicNovelId,
             @Param("author_id") Long authorId,
             @Param("role") String role);

}
