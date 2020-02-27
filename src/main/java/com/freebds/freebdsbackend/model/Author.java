package com.freebds.freebdsbackend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_author_id_seq")
    @SequenceGenerator (name = "generator_author_id_seq", sequenceName = "author_id_seq", allocationSize = 1)
    @Column(name="id")
    private Long id;

    @Column(name = "external_id", length = 10, nullable = false)
    private String externalId;

    @Column(length = 50)
    private String lastname;

    @Column(length = 50)
    private String firstname;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 50)
    private String nationality;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "decease_date")
    private LocalDate deceaseDate;

    @Column(length = 10000)
    private String biography;

    @Column(name = "site_url")
    private String siteUrl;

    @Column(name ="photo_url")
    private String photoUrl;

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

    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GraphicNovelAuthor> graphicNovels = new ArrayList<>();

}
