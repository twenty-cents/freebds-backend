package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.GenericScrapAuthor;
import com.freebds.backend.business.scrapers.bedetheque.authors.BedethequeAuthorScraper;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final SerieRepository serieRepository;

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
    public Page<Author> getAuthors(Pageable page) {
        return authorRepository.findAll(page);
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
    @Override
    public Page<Author> findBySearchFilters(
            Pageable pageable,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId ) throws CollectionItemNotFoundException {

        System.out.println(serieTitle);
        // Check parameters validity
        //--------------------------
        // Set all optional contains searches to lower case for SQL compliance research
        serieTitle = (serieTitle == null ? null : serieTitle.toLowerCase());
        graphicNovelTitle = (graphicNovelTitle == null ? null : graphicNovelTitle.toLowerCase());
        publisher = (publisher == null ? null : publisher.toLowerCase());
        collection = (collection == null ? null : collection.toLowerCase());
        lastname = (lastname == null ? null : lastname.toLowerCase());
        firstname = (firstname == null ? null : firstname.toLowerCase());
        nickname = (nickname == null ? null : nickname.toLowerCase());

        // Check if selected values from combo box lists exist in DB
        isItemExists(categories, serieRepository.findDistinctCategories(), "Categories", true);
        isItemExists(status, serieRepository.findDistinctStatus(), "Status", true);
        isItemExists(origin, serieRepository.findDistinctOrigins(), "Origin", true);
        isItemExists(language, serieRepository.findDistinctLanguages(), "Languages", true);

        Page<Author> authors = this.authorRepository
                .findBySearchFilters(
                        pageable,
                        serieTitle, serieExternalId, categories, status, origin, language,
                        graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                        lastname, firstname, nickname, authorExternalId
                );

        return authors;
    }

    /**
     * Find all authors starting with #
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    @Override
    public Page<Author> getAuthorsByLastnameStartingWith(String letter, Pageable pageable) {
        if(letter.equals("0"))
            return this.authorRepository.findAuthorsByLastnameLessThanIgnoreCase("a", pageable);
        else
            return this.authorRepository.findAuthorsByLastnameStartingWithIgnoreCase(letter, pageable);
    }

    /**
     * Count authors
     * @return the count
     */
    @Override
    public Long count() {
        return this.authorRepository.count();
    }

    public static boolean isItemExists(String item, List<String> collection, String collectionName, boolean isThrowable) {
        // Check if selected values from combo box lists exist in DB
        boolean result = true;
        if(item != null ) {
            if(! collection.contains(item.toLowerCase())) {
                result = false;
            }
        }

        if(isThrowable && result == false) {
            throw new CollectionItemNotFoundException(item, collectionName);
        }

        return result;
    }
}
