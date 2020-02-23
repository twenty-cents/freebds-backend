package com.freebds.freebdsbackend.business.scrapers.bedetheque.dto;

import java.util.Objects;

public class ScrapedAuthor {

    private String id;
    private String lastname;
    private String firstname;
    private String nickname;
    private String nationality;
    private String birthdate;
    private String deceaseDate;
    private String biography;
    private String siteUrl;
    private String pic_url;
    private String thumb_url;
    private String authorUrl;

    public ScrapedAuthor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDeceaseDate() {
        return deceaseDate;
    }

    public void setDeceaseDate(String deceaseDate) {
        this.deceaseDate = deceaseDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScrapedAuthor that = (ScrapedAuthor) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(nationality, that.nationality) &&
                Objects.equals(birthdate, that.birthdate) &&
                Objects.equals(deceaseDate, that.deceaseDate) &&
                Objects.equals(biography, that.biography) &&
                Objects.equals(siteUrl, that.siteUrl) &&
                Objects.equals(pic_url, that.pic_url) &&
                Objects.equals(thumb_url, that.thumb_url) &&
                Objects.equals(authorUrl, that.authorUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname, nickname, nationality, birthdate, deceaseDate, biography, siteUrl, pic_url, thumb_url, authorUrl);
    }

    @Override
    public String toString() {
        return "ScrapedAuthor{" +
                "id='" + id + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", nationality='" + nationality + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", deceaseDate='" + deceaseDate + '\'' +
                ", biography='" + biography + '\'' +
                ", siteUrl='" + siteUrl + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", thumb_url='" + thumb_url + '\'' +
                ", authorUrl='" + authorUrl + '\'' +
                '}';
    }
}

