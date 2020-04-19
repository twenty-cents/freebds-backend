package com.freebds.backend.controller;

import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.dto.AuthorDTO;
import com.freebds.backend.dto.NationalitiesDTO;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.AuthorMapper;
import com.freebds.backend.mapper.NationalitiesMapper;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.service.AuthorService;
import com.freebds.backend.service.ContextService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/authors", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthorController {

    private final ContextService contextService;
    private final AuthorService authorService;

    /**
     * Retrieve all authors
     *
     * @param pageable the page to get
     * @return a page of authors
     */
    @GetMapping("")
    public Page<AuthorDTO> getAuthors(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable){
        // Get a page of authors
        Page<Author> authors = authorService.getAuthors(pageable);
        // Convert authors in authorDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return authors.map(author -> AuthorMapper.INSTANCE.toDTO(author));
    }

    /**
     * Retrieve all graphic novels from an author
     *
     * @param pageable the page to get
     * @param id the author id to get
     * @return a page of graphic novels from the author
     * @see com.freebds.backend.exception.freebdsApiExceptionHandler#handleEntityNotFoundException(EntityNotFoundException ex) for Exception handling
     */
    @GetMapping("/{id}/graphic-novels")
    public ResponseEntity<Page<GraphicNovel>> getGraphicNovelsByAuthorId(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable Long id){
        return ResponseEntity.ok(authorService.getGraphicNovelsByAuthorId(pageable, id));
    }

    /**
     * Get an author by Id
     *
     * @param id the author id to get
     * @return an author
     * @see com.freebds.backend.exception.freebdsApiExceptionHandler#handleEntityNotFoundException(EntityNotFoundException ex) for Exception handling
     */
    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable Long id){
        return AuthorMapper.INSTANCE.toDTO(authorService.getAuthorById(id));
    }

    /**
     * Retrieve all existing nationalities <code>nationality</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct author's nationalities, ordered by asc
     */
    @GetMapping("/nationalities")
    public NationalitiesDTO getDistinctNationalities() {
        return NationalitiesMapper.toDTO(this.authorService.getDistinctNationalities());
    }

    /**
     * Retrieve all authors starting with the given letter
     * @param context the context to get
     * @param libraryId the library to get
     * @param titleStartingWith the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    @GetMapping("/letter")
    Page<AuthorDTO> getAuthorsByLastnameStartingWith(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'context'") @Valid @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @Valid @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId,
            @ApiParam(value = "Query param for 'titleStartingWith'") @Valid @RequestParam(value = "titleStartingWith", required = false) String titleStartingWith
    ){
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        if(titleStartingWith == null)
            titleStartingWith = "";
        Page<Author> authors = authorService.getAuthorsByLastnameStartingWith(contextResource, titleStartingWith, pageable);
        return authors.map(author -> AuthorMapper.INSTANCE.toDTO(author));
    }

    /**
     * Count authors
     * @return the count
     */
    @GetMapping("/count")
    public Long count() {
        return this.authorService.count();
    }

}
