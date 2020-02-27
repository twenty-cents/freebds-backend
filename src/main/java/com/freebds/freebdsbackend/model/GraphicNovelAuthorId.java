package com.freebds.freebdsbackend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GraphicNovelAuthorId implements Serializable {

    @Column(name = "graphicnovel_id")
    private Long graphicNovelId;

    @Column(name = "author_id")
    private Long authorId;

    public GraphicNovelAuthorId() {
    }

    public GraphicNovelAuthorId(Long graphicNovelId, Long authorId) {
        this.graphicNovelId = graphicNovelId;
        this.authorId = authorId;
    }
}
