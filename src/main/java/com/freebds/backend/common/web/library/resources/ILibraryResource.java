package com.freebds.backend.common.web.library.resources;

public interface ILibraryResource {

    Long getLibraryId();
    String getName();
    String getDescription();
    String getUserRole();
    String getIsActive();
}
