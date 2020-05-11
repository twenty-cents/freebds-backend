package com.freebds.backend.common.web.serie.resources;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CategoriesResource {
    private List<String> categories;
}
