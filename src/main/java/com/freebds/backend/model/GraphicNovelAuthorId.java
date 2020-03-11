package com.freebds.backend.model;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@ToString
public class GraphicNovelAuthorId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "graphicnovel_id")
    private Long graphicNovelId;

    @Column(name = "author_id")
    private Long authorId;

    private String role;

    public GraphicNovelAuthorId() {
    }

    public GraphicNovelAuthorId(Long graphicNovelId, Long authorId, String role) {
        super();
        this.graphicNovelId = graphicNovelId;
        this.authorId = authorId;
        this.role = role;
    }

    public Long getGraphicNovelId() {
        return graphicNovelId;
    }

    public void setGraphicNovelId(Long graphicNovelId) {
        this.graphicNovelId = graphicNovelId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphicNovelAuthorId that = (GraphicNovelAuthorId) o;
        return graphicNovelId.equals(that.graphicNovelId) &&
                authorId.equals(that.authorId) &&
                role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graphicNovelId, authorId, role);
    }
}
