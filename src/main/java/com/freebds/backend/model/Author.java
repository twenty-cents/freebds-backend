package com.freebds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "graphicNovelAuthors")
public class Author {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_author_id_seq")
    @SequenceGenerator (name = "generator_author_id_seq", sequenceName = "author_id_seq", allocationSize = 1)
    @Column(name="id")
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "decease_date")
    private Date deceaseDate;

    @Column(name = "biography", columnDefinition = "TEXT")
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

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

    @JsonIgnore
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<GraphicNovelAuthor> graphicNovelAuthors = new HashSet<>();

}
