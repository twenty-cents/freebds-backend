package com.freebds.backend.common.web.libraryContent.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class LibraryContentUpdateRequest {

    private Long libraryId;
    private Long libraryContentId;
    private boolean isFavorite;
    @JsonProperty
    private boolean isPaper;
    @JsonProperty
    private boolean isNumeric;
    @JsonProperty
    private boolean isWishlist;
}
