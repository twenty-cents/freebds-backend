package com.freebds.backend.common.web.resources;

import com.freebds.backend.model.Library;
import com.freebds.backend.model.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ContextResource {

    private User user;
    private Library library;
    private String context;
    private boolean isValid;
    private String userRole;

}
