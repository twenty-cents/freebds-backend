package com.freebds.backend.common.web.graphicNovel.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Rating {

    private String context;
    private Long libraryId;
    private Long librarySerieId;
    private Long libraryContentId;
    private Long graphicNovelId;
    private Long reviewId;
    private int rating;
    private String comment;
}
