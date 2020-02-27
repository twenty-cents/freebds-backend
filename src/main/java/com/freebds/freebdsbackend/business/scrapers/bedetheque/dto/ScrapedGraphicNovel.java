package com.freebds.freebdsbackend.business.scrapers.bedetheque.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScrapedGraphicNovel {

    private String externalId;
    private String tome;
    private String numEdition;
    private String title;
    private List<ScrapedAuthorRole> authors = new ArrayList<>();
    private String publicationDate;
    private String releaseDate;
    private String publisher;
    public String collection;
    private String isbn;
    private int totalPages;
    private String format;
    private boolean isOriginalPublication;
    private boolean isIntegrale;
    private boolean isBroche;
    private String infoEdition;
    private String reeditionUrl;
    private String externalIdOriginalPublication;
    private String coverPictureUrl;
    private String coverThumbnailUrl;
    private String backCoverPictureUrl;
    private String backCoverThumbnailUrl;
    private String pagePictureUrl;
    private String pageThumbnailUrl;
    private String albumUrl;

    public ScrapedGraphicNovel() {
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTome() {
        return tome;
    }

    public void setTome(String tome) {
        this.tome = tome;
    }

    public String getNumEdition() {
        return numEdition;
    }

    public void setNumEdition(String numEdition) {
        this.numEdition = numEdition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScrapedAuthorRole> getAuthors() {
        return authors;
    }

    public void setAuthors(List<ScrapedAuthorRole> authors) {
        this.authors = authors;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isIntegrale() {
        return isIntegrale;
    }

    public void setIntegrale(boolean integrale) {
        isIntegrale = integrale;
    }

    public boolean isBroche() {
        return isBroche;
    }

    public void setBroche(boolean broche) {
        isBroche = broche;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isOriginalPublication() {
        return isOriginalPublication;
    }

    public void setOriginalPublication(boolean originalPublication) {
        isOriginalPublication = originalPublication;
    }

    public String getInfoEdition() {
        return infoEdition;
    }

    public void setInfoEdition(String infoEdition) {
        this.infoEdition = infoEdition;
    }

    public String getReeditionUrl() {
        return reeditionUrl;
    }

    public void setReeditionUrl(String reeditionUrl) {
        this.reeditionUrl = reeditionUrl;
    }

    public String getExternalIdOriginalPublication() {
        return externalIdOriginalPublication;
    }

    public void setExternalIdOriginalPublication(String externalIdOriginalPublication) {
        this.externalIdOriginalPublication = externalIdOriginalPublication;
    }

    public String getCoverPictureUrl() {
        return coverPictureUrl;
    }

    public void setCoverPictureUrl(String coverPictureUrl) {
        this.coverPictureUrl = coverPictureUrl;
    }

    public String getCoverThumbnailUrl() {
        return coverThumbnailUrl;
    }

    public void setCoverThumbnailUrl(String coverThumbnailUrl) {
        this.coverThumbnailUrl = coverThumbnailUrl;
    }

    public String getBackCoverPictureUrl() {
        return backCoverPictureUrl;
    }

    public void setBackCoverPictureUrl(String backCoverPictureUrl) {
        this.backCoverPictureUrl = backCoverPictureUrl;
    }

    public String getBackCoverThumbnailUrl() {
        return backCoverThumbnailUrl;
    }

    public void setBackCoverThumbnailUrl(String backCoverThumbnailUrl) {
        this.backCoverThumbnailUrl = backCoverThumbnailUrl;
    }

    public String getPagePictureUrl() {
        return pagePictureUrl;
    }

    public void setPagePictureUrl(String pagePictureUrl) {
        this.pagePictureUrl = pagePictureUrl;
    }

    public String getPageThumbnailUrl() {
        return pageThumbnailUrl;
    }

    public void setPageThumbnailUrl(String pageThumbnailUrl) {
        this.pageThumbnailUrl = pageThumbnailUrl;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public void addAuthor(ScrapedAuthorRole scrapedAuthorRole) {
        authors.add(scrapedAuthorRole);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScrapedGraphicNovel that = (ScrapedGraphicNovel) o;
        return totalPages == that.totalPages &&
                isOriginalPublication == that.isOriginalPublication &&
                isIntegrale == that.isIntegrale &&
                isBroche == that.isBroche &&
                Objects.equals(externalId, that.externalId) &&
                Objects.equals(tome, that.tome) &&
                Objects.equals(numEdition, that.numEdition) &&
                Objects.equals(title, that.title) &&
                Objects.equals(publicationDate, that.publicationDate) &&
                Objects.equals(releaseDate, that.releaseDate) &&
                Objects.equals(publisher, that.publisher) &&
                Objects.equals(collection, that.collection) &&
                Objects.equals(isbn, that.isbn) &&
                Objects.equals(format, that.format) &&
                Objects.equals(infoEdition, that.infoEdition) &&
                Objects.equals(reeditionUrl, that.reeditionUrl) &&
                Objects.equals(externalIdOriginalPublication, that.externalIdOriginalPublication) &&
                Objects.equals(coverPictureUrl, that.coverPictureUrl) &&
                Objects.equals(coverThumbnailUrl, that.coverThumbnailUrl) &&
                Objects.equals(backCoverPictureUrl, that.backCoverPictureUrl) &&
                Objects.equals(backCoverThumbnailUrl, that.backCoverThumbnailUrl) &&
                Objects.equals(pagePictureUrl, that.pagePictureUrl) &&
                Objects.equals(pageThumbnailUrl, that.pageThumbnailUrl) &&
                Objects.equals(albumUrl, that.albumUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId, tome, numEdition, title, publicationDate, releaseDate, publisher, collection, isbn, totalPages, format, isOriginalPublication, isIntegrale, isBroche, infoEdition, reeditionUrl, externalIdOriginalPublication, coverPictureUrl, coverThumbnailUrl, backCoverPictureUrl, backCoverThumbnailUrl, pagePictureUrl, pageThumbnailUrl, albumUrl);
    }

    @Override
    public String toString() {
        return "ScrapedGraphicNovel{" +
                "externalId='" + externalId + '\'' +
                ", tome='" + tome + '\'' +
                ", numEdition='" + numEdition + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", publicationDate='" + publicationDate + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", publisher='" + publisher + '\'' +
                ", collection='" + collection + '\'' +
                ", isbn='" + isbn + '\'' +
                ", totalPages=" + totalPages +
                ", format='" + format + '\'' +
                ", isOriginalPublication=" + isOriginalPublication +
                ", isIntegrale=" + isIntegrale +
                ", isBroche=" + isBroche +
                ", infoEdition='" + infoEdition + '\'' +
                ", reeditionUrl='" + reeditionUrl + '\'' +
                ", externalIdOriginalPublication='" + externalIdOriginalPublication + '\'' +
                ", coverPictureUrl='" + coverPictureUrl + '\'' +
                ", coverThumbnailUrl='" + coverThumbnailUrl + '\'' +
                ", backCoverPictureUrl='" + backCoverPictureUrl + '\'' +
                ", backCoverThumbnailUrl='" + backCoverThumbnailUrl + '\'' +
                ", pagePictureUrl='" + pagePictureUrl + '\'' +
                ", pageThumbnailUrl='" + pageThumbnailUrl + '\'' +
                ", albumUrl='" + albumUrl + '\'' +
                '}';
    }
}
