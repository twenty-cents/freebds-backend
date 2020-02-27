package com.freebds.freebdsbackend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "serie")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString
public class Serie {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_serie_id_seq")
    @SequenceGenerator (name = "generator_serie_id_seq", sequenceName = "serie_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "langage")
    private String langage;

    @Column(name = "status")
    private String status;

    @Column(name = "origin")
    private String origin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "serie_category",
            joinColumns = {@JoinColumn(name = "serie_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "serie_id")) },
            inverseJoinColumns = { @JoinColumn(name = "category_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "category_id")) })
    private Set<Category> categories = new HashSet<>();

    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "page_url")
    private String pageUrl;

    @Column(name = "page_thb_url")
    private String pageThumbnailUrl;

    @OneToMany(
            mappedBy = "serie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GraphicNovel> graphicNovels = new ArrayList<>();

    @Column(name = "is_scraped")
    private Boolean isScraped;

    @Column(name = "scrap_url")
    private String scrapUrl;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

}
