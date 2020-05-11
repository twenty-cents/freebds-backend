package com.freebds.backend.common.web.dashboard.resources;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class PublicationsGrowthResource {

    private List<String> periods;
    private List<Long> graphicNovels;
}
