package com.freebds.backend.common.web.graphicNovel.resources;

import java.time.LocalDateTime;

public interface ReviewResource {

    Long getId();
    Long getUserId();
    Long getLibrarySerieContentId();
    Long getLibraryContentId();
    int getRating();
    String getComment();
    LocalDateTime getLastUpdateDate();
    String getLastUpdateUser();
}
