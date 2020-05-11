package com.freebds.backend.common.web.freeSearch.requests;

import com.freebds.backend.common.web.context.resources.ContextResource;
import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class FilterCriteriaRequest {

    // Common parameters
    private ContextResource contextResource;
    private Long libraryId;
    // Serie filters parameters
    private String serieTitle;
    private String serieExternalId;
    private String categories;
    private String status;
    private String origin;
    private String language;
    // Graphic novel filters parameters
    private String graphicNovelTitle;
    private String graphicNovelExternalId;
    private String publisher;
    private String collection;
    private String isbn;
    private Date publicationDateFrom;
    private Date publicationDateTo;
    // Author filters parameters
    private String lastname;
    private String firstname;
    private String nickname;
    private String authorExternalId;
    private String nationality;
}
