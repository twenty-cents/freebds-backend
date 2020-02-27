package com.freebds.freebdsbackend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "library_serie_content")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString
public class LibrarySerieContent {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_library_serie_content_id_seq")
    @SequenceGenerator (name = "generator_library_serie_content_id_seq", sequenceName = "library_serie_content_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_id"))
    private Library library;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "serie_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "serie_id"))
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "librarySerieContent")
    private List<Review> reviews;

}
