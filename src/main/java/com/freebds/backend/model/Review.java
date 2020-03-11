package com.freebds.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_review_id_seq")
    @SequenceGenerator(name = "generator_review_id_seq", sequenceName = "review_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_serie_content_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_serie_content_id"))
    private LibrarySerieContent librarySerieContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_content_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_content_id"))
    private LibraryContent libraryContent;
}
