package com.freebds.freebdsbackend.repository;

import com.freebds.freebdsbackend.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

    Serie findFirstByExternalId(String externalId);

}
