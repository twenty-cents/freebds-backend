package com.freebds.backend.common.web.graphicNovel.resources;

import java.sql.Date;

public interface GraphicNovelMinimumResource {

    Long getId();
    String getTome();
    String getNumEdition();
    String getTitle();
    Date getPublicationDate();
}
