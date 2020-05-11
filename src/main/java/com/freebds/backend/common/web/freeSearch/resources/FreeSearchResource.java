package com.freebds.backend.common.web.freeSearch.resources;

import com.freebds.backend.common.web.author.resources.AuthorResource;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelResource;
import com.freebds.backend.common.web.serie.resources.SerieResource;
import lombok.*;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FreeSearchResource {

    private Page<SerieResource> series;
    private Page<GraphicNovelResource> graphicNovels;
    private Page<AuthorResource> authors;
}
