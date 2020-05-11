package com.freebds.backend.common.web.graphicNovel.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class SetReviewRequest {

    private String context;
    private Long libraryId;

    private Long reviewId;
    private Long librarySerieId;
    private Long libraryContentId;
    private Long graphicNovelId;
    private int rating;
    private String comment;
}
