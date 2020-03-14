package com.freebds.backend.service;

import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.GraphicNovelAuthor;
import com.freebds.backend.repository.GraphicNovelAuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphicNovelAuthorServiceImpl implements GraphicNovelAuthorService {

    private GraphicNovelAuthorRepository graphicNovelAuthorRepository;

    public GraphicNovelAuthorServiceImpl(GraphicNovelAuthorRepository graphicNovelAuthorRepository) {
        this.graphicNovelAuthorRepository = graphicNovelAuthorRepository;
    }

    /**
     * Check if a role already exists on a tuple graphic novel / author
     * @param graphicNovelId the graphic novel
     * @param authorId the author id
     * @param role the role to check
     * @return the row if this association already exists
     */
    @Override
    public List<GraphicNovelAuthor> isRoleExist(Long graphicNovelId, Long authorId, String role) {
        return this.graphicNovelAuthorRepository.isRoleExist(graphicNovelId, authorId, role);
    }

    /**
     * Associate an author role for a graphic novel
     *
     * @param graphicNovel the graphic novel to associate
     * @param author the author to associate
     * @param role the role to define
     * @return the author/graphic novel association
     */
    @Override
    public GraphicNovelAuthor associate(GraphicNovel graphicNovel, Author author, String role) {
        List<GraphicNovelAuthor> graphicNovelAuthors = this.isRoleExist(graphicNovel.getId(), author.getId(), role);
        if(graphicNovelAuthors.size() == 0) {
            GraphicNovelAuthor graphicNovelAuthor = new GraphicNovelAuthor();
            graphicNovelAuthor.setGraphicNovel(graphicNovel);
            graphicNovelAuthor.setAuthor(author);
            graphicNovelAuthor.setRole(role);
            return this.graphicNovelAuthorRepository.saveAndFlush(graphicNovelAuthor);
        } else {
            return graphicNovelAuthors.get(0);
        }
    }

}
