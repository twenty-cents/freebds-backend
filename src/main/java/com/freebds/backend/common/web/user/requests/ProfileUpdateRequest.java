package com.freebds.backend.common.web.user.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ProfileUpdateRequest {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String avatar;
}
