package com.freebds.backend.service;

import com.freebds.backend.repository.SerieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SerieServiceImplTest {

    @Mock
    private SerieRepository serieRepository;


    private SerieService serieService;


    @Test
    void scrapSerie() {

    }


}