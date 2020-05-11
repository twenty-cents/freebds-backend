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
public class PublicationsByOriginResource {

    private List<String> periods;
    private List<Long> europa;
    private List<Long> usa;
    private List<Long> asia;
    private List<Long> other;
}
