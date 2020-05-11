package com.freebds.backend.common.web.graphicNovel.resources;

import com.freebds.backend.common.web.author.resources.AuthorRoleResource;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.model.Serie;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GraphicNovelResource {

    private Long id;
    private String externalId;
    private String graphicNovel_Url;
    private String tome;
    private String numEdition;
    private String title;
    private String publisher;
    private String collection;
    private String publicationDate;
    private String releaseDate;
    private String isbn;
    private Integer totalPages;;
    private String format;
    private String infoEdition;
    private Boolean isOriginalEdition;
    private String externalIdOriginalPublication;
    private Boolean isIntegrale;
    private Boolean isBroche;
    private String coverPictureUrl;
    private String coverThumbnailUrl;
    private String backCoverPictureUrl;
    private String backCoverThumbnailUrl;
    private String pageUrl;
    private String pageThumbnailUrl;
    private Serie serie;

    private List<AuthorRoleResource> authorRoles;
    private LibraryContent libraryContent;
    private List<ReviewResource> reviews;
}
