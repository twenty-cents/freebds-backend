package com.freebds.backend.common.web.dashboard.resources;

public interface PublicationsByMonthGroupByOriginResource {

    String getMonth();
    String getOrigin();
    Long getCount();
}
