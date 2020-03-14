package com.freebds.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "Author", description = "An author description scraped from http://www.bedetheque.com")
public class AuthorDTO {

    private Long id;
    private String externalId;
    private String lastname;
    private String firstname;
    private String nickname;
    private String nationality;
    private String birthdate;
    private String deceaseDate;
    private String biography;
    private String siteUrl;
    private String photoUrl;
}
