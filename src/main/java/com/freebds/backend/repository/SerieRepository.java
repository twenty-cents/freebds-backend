package com.freebds.backend.repository;

import com.freebds.backend.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

    Serie findFirstByExternalId(String externalId);

    List<Serie> findSeriesByExternalId(String externalId);

}
