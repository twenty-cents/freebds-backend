package com.freebds.backend.business.scrapers.bedetheque.dto;

public class ScrapedAuthorRole {

    private String externalId;
    private String role;
    private String name;
    private String authorUrl;

    public ScrapedAuthorRole() {

    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    @Override
    public String toString() {
        return "ScrapAuthorRole [externalId=" + externalId + ", role=" + role + ", name=" + name + ", authorUrl="
                + authorUrl + "]";
    }

}
