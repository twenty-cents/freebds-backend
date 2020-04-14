package com.freebds.backend.common.web.resources;

import java.sql.Date;

public interface AuthorRolesBySerieResource {

    Long getId();
    String getTitle();
    String getRole();
    Date getPublicationDateFrom();
    Date getPublicationDateTo();
}
