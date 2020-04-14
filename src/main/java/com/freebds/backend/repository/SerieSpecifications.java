package com.freebds.backend.repository;

import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.GraphicNovelAuthor;
import com.freebds.backend.model.Serie;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

// Since this class will only contain static methods that return a Specification, we can mark this class as final
public final class SerieSpecifications {

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
     * Filter series by title contains ignore case
     * @param expression the title to get
     * @return a Serie JPA Specification
     */
    public static Specification<Serie> titleContainsIgnoreCase(String expression) {
        return (root, cq, cb) -> {
            return cb.like(cb.lower(root.get("title")), contains(expression, true));
        };
    }

    /**
     * Filter series by external id
     * @param expression the external id to get
     * @return a Serie JPA Specification
     */
    public static Specification<Serie> externalIdEquals(String expression) {
        return (root, cq, cb) -> {
            return cb.equal(root.get("externalId"), expression);
        };
    }

    /**
     * Filter series by categories contains ignore case
     * @param expression the categories to get
     * @return a Serie JPA Specification
     */
    public static Specification<Serie> categoriesConstainsIgnoreCase(String expression) {
        return (root, cq, cb) -> {
            return cb.like(cb.lower(root.get("categories")), contains(expression, true));
        };
    }

    /**
     * Filter series by status
     * @param expression the status to get
     * @return a Serie JPA Specification
     */
    public static Specification<Serie> statusEquals(String expression) {
        return (root, cq, cb) -> {
            return cb.equal(root.get("status"), expression);
        };
    }

    /**
     * Filter series by origin
     * @param expression the origin to get
     * @return a Serie JPA Specification
     */
    public static Specification<Serie> originEquals(String expression) {
        return (root, cq, cb) -> {
            return cb.equal(root.get("origin"), expression);
        };
    }

    /**
     * Filter series by language
     * @param expression the language to get
     * @return a Serie JPA Specification
     */
    public static Specification<Serie> languageEquals(String expression) {
        return (root, cq, cb) -> {
            return cb.equal(root.get("langage"), expression);
        };
    }

    /**
     * Filter series by graphic novels content
     * @param title
     * @param externalId
     * @param publisher
     * @param collection
     * @param isbn
     * @param publicationDateFrom
     * @param publicationDateTo
     * @param republications
     * @return
     */
    public static Specification<Serie> graphicNovelIn(
            String title, String externalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo, boolean republications
            ) {
            //String authorName, String authorExternalId, String authorNationality) {
        return (root, cq, cb) -> {
            // Build the subquery attachment point
            Subquery<GraphicNovel> subquery = cq.subquery(GraphicNovel.class);
            Root<GraphicNovel> subroot = subquery.from(GraphicNovel.class);

            // Set the graphic novels predicates
            List<Predicate> predicateList = new ArrayList<Predicate>();
            // Set the serie / graphic novel join predicate
            predicateList.add(cb.equal(subroot.get("serie").get("id"), root.get("id")));

            // Set the optional title predicate
            if(title != null) {
                predicateList.add(cb.like(cb.lower(subroot.get("title")), contains(title, true)));
            }

            // Set the optional external id predicate
            if(externalId != null) {
                predicateList.add(cb.equal(cb.lower(subroot.get("externalId")), externalId));
            }

            // Set the optional publisher predicate
            if(publisher != null) {
                predicateList.add(cb.equal(cb.lower(subroot.get("publisher")), publisher.toLowerCase()));
            }

            // Set the optional collection predicate
            if(collection != null) {
                predicateList.add(cb.equal(cb.lower(subroot.get("collection")), collection.toLowerCase()));
            }

            // Set the optional ISBN predicate
            if(isbn != null) {
                predicateList.add(cb.equal(cb.lower(subroot.get("isbn")), isbn.toLowerCase()));
            }

            // Set the optional Publication Date from...to predicate
            if(publicationDateFrom != null && publicationDateTo != null) {
                predicateList.add(cb.between(subroot.get("publicationDate"), publicationDateFrom, publicationDateTo));
            }

            // Set the optional republications
            if(republications == false) {
                predicateList.add(cb.greaterThan(subroot.get("reeditionUrl"), ""));
            }

            // Set author predicates
/*            if(authorName != null ||authorExternalId != null || authorNationality != null) {
                // Build the subquery attachment point
                Subquery<Author> subqueryAuthors = cq.subquery(Author.class);
                Root<Author> subrootAuthor = subqueryAuthors.from(Author.class);
                // Set the graphic novel / authors join predicate
                predicateList.add(cb.equal(subroot.get("serie").get("id"), subroot.get("id")));
            }
*/
            // Transform the predicate list into an array to build the final subquery
            Predicate[] predicates = new Predicate[predicateList.size()];
            int i = 0;
            for(Predicate p : predicateList){
                predicates[i++] = p;
            }

            return cb.exists(subquery.select(subroot).where(predicates));
        };
    }

    public static Specification<Serie> authorIn(String name, String externalId, String nationality) {
        return (root, cq, cb) -> {

            // Build the subquery attachment point
            Subquery<GraphicNovel> graphicNovelSubquery = cq.subquery(GraphicNovel.class);
            Root<GraphicNovel> graphicNovelRoot = graphicNovelSubquery.from(GraphicNovel.class);

            CriteriaQuery<GraphicNovelAuthor> graphicNovelAuthorCriteriaQuery = cb.createQuery(GraphicNovelAuthor.class);
            Root<GraphicNovelAuthor> graphicNovelAuthorRoot = graphicNovelAuthorCriteriaQuery.from(GraphicNovelAuthor.class);

            CriteriaQuery<Author> authorCriteriaQuery = cb.createQuery(Author.class);
            Root<Author> authorRoot = authorCriteriaQuery.from(Author.class);

            // Set the graphic novels predicates
            List<Predicate> predicateList = new ArrayList<Predicate>();

            // Set the serie / graphic novel / Author join predicate
            predicateList.add(cb.equal(graphicNovelRoot.get("serie").get("id"), root.get("id")));
            predicateList.add(cb.equal(graphicNovelAuthorRoot.get("graphicNovel").get("id"), graphicNovelRoot.get("id")));
            predicateList.add(cb.equal(graphicNovelAuthorRoot.get("author").get("id"), authorRoot.get("id")));

            // name
            if(name != null) {
                predicateList.add(cb.like(authorRoot.get("nickname"), contains(name, true)));
            }


            // Transform the predicate list into an array to build the final subquery
            Predicate[] predicates = new Predicate[predicateList.size()];
            int i = 0;
            for(Predicate p : predicateList){
                predicates[i++] = p;
            }

            return cb.exists(graphicNovelSubquery.select(graphicNovelRoot).where(predicates));
        };
    }

    /**
     * Build the contains constraint value
     * @param expression the constraint value to build
     * @param isIgnoreCase boolean to define if the constraint is case sensitive or not
     * @return the constraint value
     */
    private static String contains(String expression, boolean isIgnoreCase) {
        if(isIgnoreCase)
            return MessageFormat.format("%{0}%", expression).toLowerCase();
        else
            return MessageFormat.format("%{0}%", expression);
    }
}
