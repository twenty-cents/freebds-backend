package com.freebds.backend.repository;

import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.GraphicNovelAuthor;
import com.freebds.backend.model.Serie;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.MessageFormat;

public final class AuthorSpecifications {

    public static Specification<Author> getAuthorsByCriteria(String serieTitle) {
        return new Specification<Author>() {
            @Override
            public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.distinct(true);
                Join<Author, GraphicNovelAuthor> authorGraphicNovelAuthorJoin = root.join("graphicNovelAuthors");
                Join<GraphicNovelAuthor, GraphicNovel> graphicNovelAuthorGraphicNovelJoin = authorGraphicNovelAuthorJoin.join("graphicNovel");
                Join<GraphicNovel, Serie> graphicNovelSerieJoin = graphicNovelAuthorGraphicNovelJoin.join("serie");
                return cb.like(graphicNovelSerieJoin.get("title"), contains(serieTitle, true));
            }
        };
    }

    /**
     * Filter series by title contains
     * @param expression the title to get
     * @return a Serie JPA Specification
     */
    public static Specification<Serie> titleContains(String expression) {
        return (root, cq, cb) -> {
            return cb.like(root.get("title"), contains(expression, false));
        };
    }


    /**
     * Build the contains constraint value
     * @param expression the constraint value to build
     * @param isIgnoreCase boolean to define if the constraint is case sensitive or not
     * @return the constraint value
     */
    private static String contains(String expression, boolean isIgnoreCase) {
        System.out.println(expression);
        if(isIgnoreCase)
            return MessageFormat.format("%{0}%", expression).toLowerCase();
        else
            return MessageFormat.format("%{0}%", expression);
    }
}
