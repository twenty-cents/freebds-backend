package com.freebds.backend.service;

import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.GraphicNovelAuthor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GraphicNovelAuthorService {

    /**
     * Check if a role already exists on a tuple graphic novel / author
     * @param graphicNovelId the graphic novel
     * @param authorId the author id
     * @param role the role to check
     * @return the row if this association already exists
     */
    List<GraphicNovelAuthor> isRoleExist(Long graphicNovelId, Long authorId, String role);

    /**
     * Associate an author role for a graphic novel
     *
     * @param graphicNovel the graphic novel to associate
     * @param author the author to associate
     * @param role the role to define
     * @return the author/graphic novel association
     */
    GraphicNovelAuthor associate(GraphicNovel graphicNovel, Author author, String role);
}
