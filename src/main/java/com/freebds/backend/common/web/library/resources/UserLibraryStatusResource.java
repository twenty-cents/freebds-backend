package com.freebds.backend.common.web.library.resources;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class UserLibraryStatusResource {
    private Long userId;
    private String firstname;
    private String lastname;
    private String status;
}
