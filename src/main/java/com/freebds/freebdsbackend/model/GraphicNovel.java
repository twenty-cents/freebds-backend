package com.freebds.freebdsbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "graphicnovel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString
public class GraphicNovel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_graphicnovel_id_seq")
    @SequenceGenerator (name = "generator_graphicnovel_id_seq", sequenceName = "graphicnovel_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "external_id", length = 10)
    private String externalId;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "tome", length = 10)
    private String tome;

    @Column(name = "num_edition", length = 10)
    private String numEdition;

    @Column(name = "publisher", length = 50)
    private String publisher;

    @Column(name = "collection", length = 50)
    private String collection;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(name = "info_edition", length = 500)
    private String infoEdition;

    @Column(name = "is_original_edition")
    private Boolean isOriginalEdition;

    @Column(name = "is_integrale")
    private Boolean isIntegrale;

    @OneToMany(
            mappedBy = "graphicNovel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GraphicNovelAuthor> authors = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "serie_id"))
    private Serie serie;

    @Column(name = "is_scraped")
    private Boolean isScraped;

    @Column(name = "scrap_url")
    private String scrapUrl;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "creation_user", length = 30)
    private String creationUser;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "last_update_user", length = 30)
    private String lastUpdateUser;

}
