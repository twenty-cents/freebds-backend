package com.freebds.backend.service;

import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.SerieRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.freebds.backend.repository.SerieSpecifications.titleContainsIgnoreCase;

@Service
public class ReferentialService {

    private SerieRepository serieRepository;

    public ReferentialService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }


    public List<Serie> filter(String title) {
        // Define the specification
        Specification<Serie> serieSpecification = Specification
                .where(title == null ? null : titleContainsIgnoreCase(title));

        List<Serie> series = serieRepository.findAll();
        System.out.println(series.size());
        return series;
    }

}
