package com.freebds.backend.business.scrapers.bedetheque.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScrapedSerie {

    private String externalId;
    private String title;
    private String category;
    private String status;
    private String origin;
    private String langage;
    private String synopsys;
    private String pictureUrl;
    private String pictureThbUrl;
    private String scrapUrl;
    private LocalDateTime creationDate;
    private String creationUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;

    private List<ScrapedGraphicNovel> scrapedGraphicNovels = new ArrayList<>();

    public ScrapedSerie() {
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLangage() {
        return langage;
    }

    public void setLangage(String langage) {
        this.langage = langage;
    }

    public String getSynopsys() {
        return synopsys;
    }

    public void setSynopsys(String synopsys) {
        this.synopsys = synopsys;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureThbUrl() {
        return pictureThbUrl;
    }

    public void setPictureThbUrl(String pictureThbUrl) {
        this.pictureThbUrl = pictureThbUrl;
    }

    public String getScrapUrl() {
        return scrapUrl;
    }

    public void setScrapUrl(String scrapUrl) {
        this.scrapUrl = scrapUrl;
    }

    public List<ScrapedGraphicNovel> getScrapedGraphicNovels() {
        return scrapedGraphicNovels;
    }

    public void setScrapedGraphicNovels(List<ScrapedGraphicNovel> scrapedGraphicNovels) {
        this.scrapedGraphicNovels = scrapedGraphicNovels;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public void addGraphicNovel(ScrapedGraphicNovel scrapedGraphicNovel) {
        scrapedGraphicNovels.add(scrapedGraphicNovel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScrapedSerie that = (ScrapedSerie) o;
        return Objects.equals(externalId, that.externalId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(category, that.category) &&
                Objects.equals(status, that.status) &&
                Objects.equals(origin, that.origin) &&
                Objects.equals(langage, that.langage) &&
                Objects.equals(synopsys, that.synopsys) &&
                Objects.equals(pictureUrl, that.pictureUrl) &&
                Objects.equals(pictureThbUrl, that.pictureThbUrl) &&
                Objects.equals(scrapUrl, that.scrapUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId, title, category, status, origin, langage, synopsys, pictureUrl, pictureThbUrl, scrapUrl);
    }

    @Override
    public String toString() {
        return "ScrapedSerie{" +
                "externalId='" + externalId + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", origin='" + origin + '\'' +
                ", langage='" + langage + '\'' +
                ", synopsys='" + synopsys + '\'' +
                ", picture_url='" + pictureUrl + '\'' +
                ", pictureThbUrl='" + pictureThbUrl + '\'' +
                ", scrapUrl='" + scrapUrl + '\'' +
                ", scrapedGraphicNovels=" + scrapedGraphicNovels +
                '}';
    }
}
