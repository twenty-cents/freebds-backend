package com.freebds.backend.common.web.library.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class UserLibraryStatusRequest {
    private Long userId;
    private String firstname;
    private String lastname;
    private String status;
}
