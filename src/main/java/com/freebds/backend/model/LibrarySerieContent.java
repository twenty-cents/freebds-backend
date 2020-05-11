package com.freebds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "library_serie_content")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude= {"reviews"})
@ToString
public class LibrarySerieContent {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_library_serie_content_id_seq")
    @SequenceGenerator (name = "generator_library_serie_content_id_seq", sequenceName = "library_serie_content_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_id"), unique = false)
    private Library library;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "serie_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "serie_id"), unique = false)
    private Serie serie;

    @Column(name = "is_favorite")
    private  Boolean isFavorite;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

    @JsonIgnore
    @OneToMany(
            mappedBy = "librarySerieContent",
            orphanRemoval = true
    )
    private List<LibraryContent> libraryContents = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "librarySerieContent")
    private List<Review> reviews;

}
