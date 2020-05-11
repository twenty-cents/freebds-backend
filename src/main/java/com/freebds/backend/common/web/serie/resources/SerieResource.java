package com.freebds.backend.common.web.serie.resources;

import com.freebds.backend.model.LibrarySerieContent;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SerieResource {

    private Long id;
    private String externalId;
    private String title;
    private String langage;
    private String status;
    private String origin;
    private String categories;
    private String synopsys;
    private String siteUrl;
    private String pageUrl;
    private String pageThumbnailUrl;
    private LibrarySerieContent librarySerieContent;
}
