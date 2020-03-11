package com.freebds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "graphicnovel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString(exclude = "graphicNovelAuthors")
public class GraphicNovel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_graphicnovel_id_seq")
    @SequenceGenerator (name = "generator_graphicnovel_id_seq", sequenceName = "graphicnovel_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "title")
    private String title;

    @Column(name = "tome")
    private String tome;

    @Column(name = "num_edition")
    private String numEdition;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "collection")
    private String collection;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(name = "book_format")
    private String format;

    @Column(name = "info_edition")
    private String infoEdition;

    @Column(name = "reedition_url")
    private String reeditionUrl;

    @Column(name = "is_original_edition")
    private Boolean isOriginalEdition;

    @Column(name = "external_id_original_publication")
    private String externalIdOriginalPublication;

    @Column(name = "is_integrale")
    private Boolean isIntegrale;

    @Column(name = "is_broche")
    private Boolean isBroche;

    @JsonIgnore
    @OneToMany(
            mappedBy = "graphicNovel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<GraphicNovelAuthor> graphicNovelAuthors = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "serie_id"))
    private Serie serie;

    @Column(name = "is_scraped")
    private Boolean isScraped;

    @Column(name = "scrap_url")
    private String scrapUrl;

    @Column(name = "graphic_novel_url")
    private String graphicNovel_Url;

    @Column(name = "cover_picture_url")
    private String coverPictureUrl;

    @Column(name = "cover_thumbnail_url")
    private String coverThumbnailUrl;

    @Column(name = "back_cover_picture_url")
    private String backCoverPictureUrl;

    @Column(name = "back_cover_thumbnail_url")
    private String backCoverThumbnailUrl;

    @Column(name = "page_url")
    private String pageUrl;

    @Column(name = "page_thumbnail_url")
    private String pageThumbnailUrl;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

}
