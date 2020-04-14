package com.freebds.backend.repository;

import com.freebds.backend.model.Serie;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.freebds.backend.repository.SerieSpecifications.titleContainsIgnoreCase;


public class ReferentialRepository {

    private SerieRepository serieRepository;

    public List<Serie> filter(SerieRepository serieRepository, String title) {
        // Define the specification
        Specification<Serie> serieSpecification = Specification
                .where(title == null ? null : titleContainsIgnoreCase(title));

        List<Serie> series = serieRepository.findAll();
        System.out.println(series.size());
        return series;
    }

}
