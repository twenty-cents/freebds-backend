package com.freebds.backend.common.web.user.resources;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ProfileUpdateResponse {

    private Long id;
    private String email;
    private String avatar;
    private String firstname;
    private String lastname;
}
