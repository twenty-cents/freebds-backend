package com.freebds.backend.controller;

import com.freebds.backend.dto.AuthorDTO;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.AuthorMapper;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.service.AuthorService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
//@RequestMapping(value = "/api/authors")
@RequestMapping(value = "/api/authors", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AuthorController {

    private AuthorService authorService;

    // TODO : Déclarer les variables de Pageable dans application-properties -> @Value

    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    /**
     * Retrieve all authors
     *
     * @param pageable the page to get
     * @return a page of authors
     */
    @GetMapping("")
    @PageableAsQueryParam
    public Page<AuthorDTO> getAuthors(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) @Parameter(hidden=true) Pageable pageable){
        // Get a page of authors
        Page<Author> authors = authorService.getAuthors(pageable);
        // Convert authors in authorDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return authors.map(author -> AuthorMapper.INSTANCE.authorToAuthorDTO(author));
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
    @PageableAsQueryParam
    public ResponseEntity<Page<GraphicNovel>> getGraphicNovelsByAuthorId(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) @Parameter(hidden=true) Pageable pageable,
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
        return AuthorMapper.INSTANCE.authorToAuthorDTO(authorService.getAuthorById(id));
    }

    /**
     * Retrieve all authors filtered by lastname, firstname, nickname, nationality
     *
     * @param pageable the page to get
     * @param lastname the author lastname to get
     * @param firstname the author firstname to get
     * @param nickname the author nickname to get
     * @param nationality the author nationality to get
     * @return a page of filtered authors
     */
    @GetMapping("/filter")
    @PageableAsQueryParam
    public Page<AuthorDTO> getFilteredAuthors(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) @Parameter(hidden=true) Pageable pageable,
            @ApiParam(value = "Query param for 'lastname'") @Valid @RequestParam(value = "lastname", defaultValue = "") String lastname,
            @ApiParam(value = "Query param for 'firstname'") @Valid @RequestParam(value = "firstname", defaultValue = "") String firstname,
            @ApiParam(value = "Query param for 'nickname'") @Valid @RequestParam(value = "nickname", defaultValue = "") String nickname,
            @ApiParam(value = "Query param for 'nationality'") @Valid @RequestParam(value = "nationality", defaultValue = "") String nationality
    ){
        Page<Author> authors = authorService.getFilteredAuthors(pageable, lastname, firstname, nickname, nationality);
        // Convert authors in authorDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return authors.map(author -> AuthorMapper.INSTANCE.authorToAuthorDTO(author));
    }

}
