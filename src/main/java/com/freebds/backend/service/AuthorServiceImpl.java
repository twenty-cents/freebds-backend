package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.GenericScrapAuthor;
import com.freebds.backend.business.scrapers.bedetheque.authors.BedethequeAuthorScraper;
import com.freebds.backend.common.web.author.resources.AuthorResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.AuthorMapper;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.repository.AuthorRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {


    private final AuthorRepository authorRepository;
    private final SerieRepository serieRepository;
    private final LibraryService libraryService;

    /**
     * Get an author by id
     *
     * @param id the author id to get
     * @return an author
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    @Override
    public Author getAuthorById(Long id) throws EntityNotFoundException {
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        if(optionalAuthor.isPresent()){
            return optionalAuthor.get();
        } else{
            throw new EntityNotFoundException(id, "Author");
        }
    }

    /**
     * Authors list retrieval.
     *
     * @param page the page  to get
     * @return a page object with authors
     */
    @Override
    public Page<AuthorResource> getAuthors(Pageable page) {
        Page<Author> authors = authorRepository.findAll(page);
        return authors.map(author -> AuthorMapper.INSTANCE.toResource(author));
    }

    /**
     * Get all graphic novels from an author
     *
     * @param page the page to get
     * @param authorId the author id to get
     * @return a list of graphic novels
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    @Override
    public Page<GraphicNovel> getGraphicNovelsByAuthorId(Pageable page, Long authorId) throws EntityNotFoundException {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if(optionalAuthor.isPresent()){
            Page<GraphicNovel> graphicNovels = authorRepository.findGraphicNovelAuthorByAuthor(page, authorId);
            return graphicNovels;
        }else{
            throw new EntityNotFoundException(authorId, "Author");
        }
    }

    /**
     * Get an author by external id
     *
     * @param externalId the author external id to get
     * @return an author
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    @Override
    public Author getAuthorByExternalId(String externalId) throws EntityNotFoundException {
        List<Author> authors = authorRepository.findAuthorsByExternalId(externalId);

        if(authors.size() == 0){
            throw new EntityNotFoundException(externalId, "Author");
        } else {
            return authors.get(0);
        }
    }

    /**
     * Retrieve all existing nationalities <code>nationality</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct author's nationalities, ordered by asc
     */
    @Override
    public List<String> getDistinctNationalities() {
        return this.authorRepository.findDistinctNationalities();
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
    @Override
    @Deprecated
    public Page<Author> getFilteredAuthors(Pageable pageable, String lastname, String firstname, String nickname, String nationality) {
        return authorRepository.findAuthorsByLastnameIgnoreCaseContainingAndFirstnameIgnoreCaseContainingAndNicknameIgnoreCaseContainingAndNationalityIgnoreCaseContaining(pageable, lastname, firstname, nickname, nationality);
    }

    /**
     * Scrap an author from https://www.bedetheque.com and save it into the db
     *
     * @param genericAuthorUrl the scrap author url to get
     * @return the scraped author entity
     * @throws IOException in case of connection error to the url author page
     */
    @Override
    public Author scrapAuthor(GenericAuthorUrl genericAuthorUrl) throws IOException {
        // Instantiate authors Scraper
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();
        // Scrap author
        GenericScrapAuthor genericScrapAuthor = bedethequeAuthorScraper.scrapAuthor(genericAuthorUrl);
        // Save author
        Author author = bedethequeAuthorScraper.toAuthor(genericScrapAuthor);
        author = authorRepository.saveAndFlush(author);

        return author;
    }

    /**
     * Scrap all new authors from https://wwww.bedetheque.com an add them into the db
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void scrapNewAuthorsInDb() throws IOException, InterruptedException {
        // Instantiate authors Scraper
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();

        // Step 1 - Get all existing authors urls from http://www.bedetheque.com
        List<GenericAuthorUrl> genericAuthorUrls = new ArrayList<>();
        char[] letters = new String("0ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        for(char letter : letters){
            List<GenericAuthorUrl> scrapedAuthorUrls = bedethequeAuthorScraper.scrapAuthorUrlsByLetter(String.valueOf(letter));
            genericAuthorUrls.addAll(scrapedAuthorUrls);
        }

        // Step 2 - Parse authors and add the unknown ones into the db
        for (GenericAuthorUrl genericAuthorUrl : genericAuthorUrls) {

            // Extract URL and author external id
            String externalId = genericAuthorUrl.getUrl().split("-")[1];

            // Existing author in db?
            Author author = authorRepository.findFirstByExternalId(externalId);
            // If not, scrap and save it into the db
            if(author == null){
                // Scrap author
                this.scrapAuthor(genericAuthorUrl);
                // Wait to prevent scraped site to associate the scraping as a DDoS attack
                // TODO : délai d'attente entre deux requêtes http à déclarer en @Value
                Thread.sleep(1000);
            }
        }

    }

    /**
     * Find all authors starting with #
     * @param contextResource the context to get
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    @Override
    public Page<AuthorResource> getAuthorsByLastnameStartingWith(ContextResource contextResource, String letter, Pageable pageable) {
        Page<Author> authors;
        if(contextResource.getContext().equals("referential")) {
            if(letter.equals("#"))
                authors = this.authorRepository.findAuthorsByLastnameLessThanIgnoreCase("a", pageable);
            else
                authors = this.authorRepository.findAuthorsByLastnameStartingWithIgnoreCase(letter, pageable);
        } else {
            if(letter != null)
                letter = letter.toLowerCase();
            if(letter.equals("#"))
                authors = this.authorRepository.findAuthorsFromLibraryByLastnameLessThanIgnoreCase(contextResource.getLibrary().getId(),"a", pageable);
            else
                authors = this.authorRepository.findAuthorsFromLibraryByLastnameStartingWithIgnoreCase(contextResource.getLibrary().getId(),letter, pageable);
        }

        return authors.map(author -> AuthorMapper.INSTANCE.toResource(author));
    }

}
