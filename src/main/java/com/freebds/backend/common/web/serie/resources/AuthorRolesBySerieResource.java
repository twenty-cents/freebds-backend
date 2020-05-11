package com.freebds.backend.common.web.serie.resources;

import java.sql.Date;

public interface AuthorRolesBySerieResource {

    Long getId();
    String getTitle();
    String getRole();
    Date getPublicationDateFrom();
    Date getPublicationDateTo();
}
