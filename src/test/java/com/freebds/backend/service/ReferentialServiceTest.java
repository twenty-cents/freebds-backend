package com.freebds.backend.service;

import com.freebds.backend.model.Serie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static com.freebds.backend.repository.SerieSpecifications.titleContainsIgnoreCase;

@ExtendWith(MockitoExtension.class)
class ReferentialServiceTest {

    @Autowired
    private ReferentialService referentialService;

    //@Mock
    //SerieRepository serieRepository;

    //@BeforeEach
    //public  void SetUp() {
    //    this.referentialService = new ReferentialService(serieRepository);
    //}

    @Test
    void filter() {
        // given
        Serie serie = Serie.builder()
                .title("Gaston")
                .build();

        List<Serie> expectedSeries = Arrays.asList(serie);
        Specification<Serie> serieSpecification = Specification
                .where(serie.getTitle() == null ? null : titleContainsIgnoreCase(serie.getTitle()));
        //doReturn(expectedSeries).when(serieRepository).findAll(serieSpecification);

        // when
        List<Serie> actualSeries = referentialService.filter(null);
        for(Serie s : actualSeries) {
            System.out.println(s.toString());
        }
        // then
        //assertThat(actualSeries).isEqualTo(expectedSeries);
    }
}