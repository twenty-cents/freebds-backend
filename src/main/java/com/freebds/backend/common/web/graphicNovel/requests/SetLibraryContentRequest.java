package com.freebds.backend.common.web.graphicNovel.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class SetLibraryContentRequest {

    private String context;
    private Long libraryId;
    private Long librarySerieId;
    private Long libraryContentId;
    private Long graphicNovelId;
    private Long reviewId;
    private boolean isPaper;
    private boolean isNumeric;
    private boolean isWishlist;
    private String comment;
}
