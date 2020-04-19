package com.freebds.backend.controller;

import com.freebds.backend.common.web.requests.LibraryGraphicNovelFormatRequest;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.service.LibraryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/graphic-novels-library", produces = { MediaType.APPLICATION_JSON_VALUE })
@CrossOrigin("*")
@Validated
@Slf4j
@RequiredArgsConstructor
public class LibraryGraphicNovelController {

    private final LibraryContentService libraryContentService;

    @PostMapping("/{graphicNovelId}")
    public LibraryContent addToLibrary(@PathVariable Long graphicNovelId, @RequestBody LibraryGraphicNovelFormatRequest libraryGraphicNovelFormatRequest) {
        return this.libraryContentService.addGraphicNovelToCollection(graphicNovelId, libraryGraphicNovelFormatRequest.getFormat());
    }

}
