package com.freebds.backend.common.web.library.resources;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class LibraryResource {
   private Long libraryId;
   private String name;
   private String description;
   private String userRole;
   private Boolean isActive;
}
