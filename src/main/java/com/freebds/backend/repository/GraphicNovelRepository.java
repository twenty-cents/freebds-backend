package com.freebds.backend.repository;

import com.freebds.backend.model.GraphicNovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphicNovelRepository extends JpaRepository<GraphicNovel, Long> {

    GraphicNovel findFirstByExternalId(String externalId);

}
