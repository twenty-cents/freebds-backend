package com.freebds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "library_content")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude= {"reviews"})
@ToString(exclude = {"library", "graphicNovel", "reviews"})
public class LibraryContent {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_library_content_id_seq")
    @SequenceGenerator (name = "generator_library_content_id_seq", sequenceName = "library_content_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_id"))
    private Library library;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_serie_content_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "LibrarySerieContent_id"))
    private LibrarySerieContent librarySerieContent;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "graphicnovel_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "graphicnovel_id"))
    private GraphicNovel graphicNovel;

    @Column(name = "is_favorite")
    private  Boolean isFavorite;

    @Column(name = "is_physical")
    private Boolean isPhysical;

    @Column(name = "is_numeric")
    private Boolean isNumeric;

    @Column(name = "is_wishlist")
    private Boolean isWishlist;

    @Column(name = "local_storage")
    private String localStorage;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "libraryContent")
    private List<Review> reviews;
}
