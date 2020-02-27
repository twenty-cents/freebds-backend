package com.freebds.freebdsbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "graphicnovel_author")
public class GraphicNovelAuthor {

    @EmbeddedId
    private GraphicNovelAuthorId id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("graphicNovelId")
    @JoinColumn(name = "graphicnovel_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "graphicnovel_id"))
    private GraphicNovel graphicNovel;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorId")
    @JoinColumn(name = "author_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "author_id"))
    private Author author;

    @Column(name = "role", length = 50, nullable = false)
    private String role;

    public GraphicNovelAuthor() {
    }

    public GraphicNovelAuthor(GraphicNovel graphicNovel, Author author) {
        this.graphicNovel = graphicNovel;
        this.author = author;
        this.id = new GraphicNovelAuthorId(graphicNovel.getId(), author.getId());
    }

    public GraphicNovelAuthorId getId() {
        return id;
    }

    public void setId(GraphicNovelAuthorId id) {
        this.id = id;
    }

    public GraphicNovel getGraphicNovel() {
        return graphicNovel;
    }

    public void setGraphicNovel(GraphicNovel graphicNovel) {
        this.graphicNovel = graphicNovel;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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
        GraphicNovelAuthor that = (GraphicNovelAuthor) o;
        return id.equals(that.id) &&
                graphicNovel.equals(that.graphicNovel) &&
                author.equals(that.author) &&
                role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, graphicNovel, author, role);
    }

    @Override
    public String toString() {
        return "GraphicNovelAuthor{" +
                "id=" + id +
                ", graphicNovel=" + graphicNovel +
                ", author=" + author +
                ", role='" + role + '\'' +
                '}';
    }
}
