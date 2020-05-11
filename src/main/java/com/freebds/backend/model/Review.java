package com.freebds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
//@EqualsAndHashCode(exclude = {"librarySerieContent, libraryContent, user"})
@ToString(exclude = {"librarySerieContent, libraryContent, user"})
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_serie_content_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_serie_content_id"))
    private LibrarySerieContent librarySerieContent;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_content_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_content_id"))
    private LibraryContent libraryContent;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_id"))
    private User user;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;
}
