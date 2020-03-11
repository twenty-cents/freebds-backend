package com.freebds.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
