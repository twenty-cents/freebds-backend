package com.freebds.backend.common.web.dashboard.resources;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class GlobalCountsResource {

    private Long referentialSeriesCount;
    private Long referentialGraphicNovelsCount;
    private Long referentialAuthorsCount;
}
