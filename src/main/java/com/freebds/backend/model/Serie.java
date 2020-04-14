package com.freebds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "serie")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//@EqualsAndHashCode
@ToString(exclude = {"graphicNovels"})
public class Serie {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_serie_id_seq")
    @SequenceGenerator (name = "generator_serie_id_seq", sequenceName = "serie_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "title")
    private String title;

    @Column(name = "langage")
    private String langage;

    @Column(name = "status")
    private String status;

    @Column(name = "origin")
    private String origin;

    @Column(name = "categories", columnDefinition = "TEXT")
    private String categories;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name= "serie_category",
//            joinColumns = {@JoinColumn(name = "serie_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "serie_id")) },
//            inverseJoinColumns = { @JoinColumn(name = "category_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "category_id")) })
//    private Set<Category> categories = new HashSet<>();

    @Column(name = "synopsys", columnDefinition = "TEXT")
    private String synopsys;

    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "page_url")
    private String pageUrl;

    @Column(name = "page_thb_url")
    private String pageThumbnailUrl;

    @JsonIgnore
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

    // TODO : @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    // TODO : @CreatedBy
    @Column(name = "creation_user")
    private String creationUser;

    // TODO : @LastModifiedDate
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    // TODO : @LastModifiedBy
    @Column(name = "last_update_user")
    private String lastUpdateUser;

    @Override
    public boolean equals(Object o) {
        System.out.println("Serie equals=" + o.toString());
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Serie serie = (Serie) o;
        return id.equals(serie.id) &&
                Objects.equals(externalId, serie.externalId) &&
                Objects.equals(title, serie.title) &&
                Objects.equals(langage, serie.langage) &&
                Objects.equals(status, serie.status) &&
                Objects.equals(origin, serie.origin) &&
                Objects.equals(categories, serie.categories) &&
                Objects.equals(synopsys, serie.synopsys) &&
                Objects.equals(siteUrl, serie.siteUrl) &&
                Objects.equals(pageUrl, serie.pageUrl) &&
                Objects.equals(pageThumbnailUrl, serie.pageThumbnailUrl) &&
                Objects.equals(isScraped, serie.isScraped) &&
                Objects.equals(scrapUrl, serie.scrapUrl) &&
                Objects.equals(creationDate, serie.creationDate) &&
                Objects.equals(creationUser, serie.creationUser) &&
                Objects.equals(lastUpdateDate, serie.lastUpdateDate) &&
                Objects.equals(lastUpdateUser, serie.lastUpdateUser);
    }

    @Override
    public int hashCode() {
        System.out.println("Serie hashcode=" + this.toString());
        return Objects.hash(id, externalId, title, langage, status, origin, categories, synopsys, siteUrl, pageUrl, pageThumbnailUrl, isScraped, scrapUrl, creationDate, creationUser, lastUpdateDate, lastUpdateUser);
    }
}
