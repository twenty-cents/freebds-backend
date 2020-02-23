package com.freebds.freebdsbackend.controller;

import com.freebds.freebdsbackend.model.Author;
import com.freebds.freebdsbackend.repository.AuthorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    /**
     * List all authors
     * @return
     */
    @GetMapping()
    public ResponseEntity<List> listAuthors(){
        return ResponseEntity.ok(authorRepository.findAll());
    }

    /**
     * Create an author
     * @param author
     * @return
     */
    @PostMapping()
    public ResponseEntity<Author> create(@RequestBody Author author){
        author = authorRepository.saveAndFlush(author);
        return ResponseEntity.ok(author);
    }

    /**
     * Update an author
     * @param newAuthor
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@RequestBody Author newAuthor, @PathVariable Long id){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()){
            author.get().setLastname(newAuthor.getLastname());
            author.get().setFirstname(newAuthor.getFirstname());
            author.get().setNickname(newAuthor.getNickname());
            author.get().setNationality(newAuthor.getNationality());
            author.get().setBirthdate(newAuthor.getBirthdate());
            author.get().setDeceaseDate(newAuthor.getDeceaseDate());
            author.get().setBiography(newAuthor.getBiography());
            author.get().setSiteUrl(newAuthor.getSiteUrl());
            author.get().setPhotoUrl(newAuthor.getPhotoUrl());
            author.get().setScrapUrl(newAuthor.getScrapUrl());
            return ResponseEntity.ok(author.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get an author by Id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()) {
            return ResponseEntity.ok(author.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id){
        if(authorRepository.existsById(id)){
            authorRepository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

}
