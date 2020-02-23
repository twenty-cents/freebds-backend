package com.freebds.freebdsbackend.repository;

import com.freebds.freebdsbackend.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findFirstByExternalId(String externalId);

}
