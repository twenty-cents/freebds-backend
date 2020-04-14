package com.freebds.backend.controller;

import com.freebds.backend.dto.AuthorDTO;
import com.freebds.backend.dto.NationalitiesDTO;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.AuthorMapper;
import com.freebds.backend.mapper.NationalitiesMapper;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.service.AuthorService;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;

@RestController
@RequestMapping(value = "/api/authors", produces = { MediaType.APPLICATION_JSON_VALUE })
@CrossOrigin("*")
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
    public Page<AuthorDTO> getFilteredAuthors(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'lastname'") @Valid @RequestParam(value = "lastname", defaultValue = "") String lastname,
            @ApiParam(value = "Query param for 'firstname'") @Valid @RequestParam(value = "firstname", defaultValue = "") String firstname,
            @ApiParam(value = "Query param for 'nickname'") @Valid @RequestParam(value = "nickname", defaultValue = "") String nickname,
            @ApiParam(value = "Query param for 'nationality'") @Valid @RequestParam(value = "nationality", defaultValue = "") String nationality
    ){
        Page<Author> authors = authorService.getFilteredAuthors(pageable, lastname, firstname, nickname, nationality);
        // Convert authors in authorDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return authors.map(author -> AuthorMapper.INSTANCE.toDTO(author));
    }

    /**
     * Retrieve all existing authors by multiple criteria
     * @param pageable the page to get
     * @param serieTitle the serie title to get
     * @param serieExternalId the serie external id to get
     * @param categories the serie category to get
     * @param status the serie status to get
     * @param origin the serie origin to get
     * @param language the serie language to get
     * @param graphicNovelTitle the graphic novel title to get
     * @param graphicNovelExternalId the graphic novel external id to get
     * @param publisher the graphic novel publisher to get
     * @param collection the graphic novel collection to get
     * @param isbn the graphic novel ISBN to get
     * @param publicationDateFrom the graphic novel publication date from to get
     * @param publicationDateTo the graphic novel publication date to to get
     * @param lastname the author lastname to get
     * @param firstname the author firstname to get
     * @param nickname the author nickname to get
     * @param authorExternalId the author external id to get
     * @return a page of filtered authors
     * @throws CollectionItemNotFoundException in case of invalid selected value request
     */
    @GetMapping("/search")
    public Page<Author> getFilteredAuthors (
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'serietitle'") @Valid @RequestParam(value = "serietitle", required = false) String serieTitle,
            @ApiParam(value = "Query param for 'serieexternalId'") @Valid @RequestParam(value = "serieexternalId", required = false) String serieExternalId,
            @ApiParam(value = "Query param for 'origin'") @Valid @RequestParam(value = "origin", required = false) String origin,
            @ApiParam(value = "Query param for 'status'") @Valid @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Query param for 'categories'") @Valid @RequestParam(value = "categories", required = false) String categories,
            @ApiParam(value = "Query param for 'language'") @Valid @RequestParam(value = "language", required = false) String language,
            @ApiParam(value = "Query param for 'graphicnoveltitle'") @Valid @RequestParam(value = "graphicnoveltitle", required = false) String graphicNovelTitle,
            @ApiParam(value = "Query param for 'graphicnovelexternalid'") @Valid @RequestParam(value = "graphicnovelexternalid", required = false) String graphicNovelExternalId,
            @ApiParam(value = "Query param for 'publisher'") @Valid @RequestParam(value = "publisher", required = false) String publisher,
            @ApiParam(value = "Query param for 'collection'") @Valid @RequestParam(value = "collection", required = false) String collection,
            @ApiParam(value = "Query param for 'isbn'") @Valid @RequestParam(value = "isbn", required = false) String isbn,
            @ApiParam(value = "Query param for 'publicationdatefrom'") @Valid @RequestParam(value = "publicationdatefrom", required = false, defaultValue = "0001-01-01") Date publicationDateFrom,
            @ApiParam(value = "Query param for 'publicationdateto'") @Valid @RequestParam(value = "publicationdateto", required = false, defaultValue = "9999-01-01") Date publicationDateTo,
            @ApiParam(value = "Query param for 'lastname'") @Valid @RequestParam(value = "lastname", required = false) String lastname,
            @ApiParam(value = "Query param for 'firstname'") @Valid @RequestParam(value = "firstname", required = false) String firstname,
            @ApiParam(value = "Query param for 'nickname'") @Valid @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam(value = "Query param for 'authorexternalid'") @Valid @RequestParam(value = "authorexternalid", required = false) String authorExternalId
    ) {
        // Call the associated service
        Page<Author> authors = authorService.findBySearchFilters(
                pageable,
                serieTitle, serieExternalId, categories, status, origin, language,
                graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                lastname, firstname, nickname, authorExternalId
        );

        //return authors.map(author -> AuthorMapper.INSTANCE.toDTO(author));
        return authors;
    }

    /**
     * Retrieve all authors starting with the given letter
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    @GetMapping("/letters/{letter}")
    Page<AuthorDTO> getAuthorsByLastnameStartingWith(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable String letter
    ){
        Page<Author> authors = authorService.getAuthorsByLastnameStartingWith(letter, pageable);
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
