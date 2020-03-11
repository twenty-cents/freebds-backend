package com.freebds.backend.batch.scrapers.bedetheque.authors;

import com.freebds.backend.business.scrapers.bedetheque.authors.BedethequeAuthorScraper;
import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Configuration
//@EnableBatchProcessing
//@Component
public class AuthorScraperBatchConfiguration {

//    @Autowired
//    public JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    public StepBuilderFactory stepBuilderFactory;
//
//    @Autowired
//    public AuthorRepository authorRepository;
//
//    @Bean
//    public Step gensStep() {
//        RepositoryItemReader reader = new RepositoryItemReader();
//        reader.setRepository(authorRepository);
//
//        RepositoryItemWriter writer = new RepositoryItemWriter();
//        writer.setRepository(empRepo);
//        writer.setMethodName("save");
//        return steps.get("genEmployeesStep")
//                .<Employee, Employee> chunk(10)
//                .reader(new ListItemReader<Employee>(IntStream.range(0, 100)
//                        .mapToObj(i -> new Employee(null, "foo-" + i))
//                        .collect(Collectors.toList())))
//                .writer(writer)
//                .build();
//    }


    private ItemReader<String> setAuthorsFromBedethequeReader() {
        String[] letters = new String("0-A-B-C-D-E-F-G-H-I-J-K-L-M-N-O-P-Q-R-S-T-U-V-W-X-Y-Z").split("-");
        return new ListItemReader<String>(Arrays.asList(letters));
    }

    private List<GenericAuthorUrl> process(String letter) {
        // Instantiate authors Scraper
        BedethequeAuthorScraper bedethequeAuthorScraper = new BedethequeAuthorScraper();
        List<GenericAuthorUrl> authorUrlsDTO = new ArrayList<>();
        try {
            authorUrlsDTO = bedethequeAuthorScraper.scrapAuthorUrlsByLetter(String.valueOf(letter));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authorUrlsDTO;
    }

    //private ItemWriter<AuthorUrlDTO> setAuthorsFromBedethequeWriter()
}
