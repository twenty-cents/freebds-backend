package com.freebds.backend.common.web.author.resources;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthorResource {

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
