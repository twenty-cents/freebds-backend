package com.freebds.backend.repository;

//@AllArgsConstructor
public class SerieSpecificationRepositoryImpl implements SerieSpecificationRepository {

    //@Autowired
//    private final SerieRepository serieRepository;
/**
//    @Override
    public List<Serie> filterByTitle(String title) {
        // Define the specification
        Specification<Serie> serieSpecification = Specification
                .where(title == null ? null : titleContainsIgnoreCase(title));

        List<Serie> series = serieRepository.findAll();
        System.out.println(series.size());
        return series;
    }

//    @Override
    public List<Serie> findByFilters(
            String title, String externalId, String categories, String status, String origin, String language,
            String graphicNovelTitle, String graphicNovelTExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo, boolean republications,
            String nickname) {

        boolean hasSubqueryGraphicNovel = false;
        if(publisher != null || collection != null ||isbn != null || publicationDateFrom != null || publicationDateTo != null)
            hasSubqueryGraphicNovel = true;

        // Define the specification
        Specification<Serie> serieSpecification = Specification
                .where(title == null ? null : titleContainsIgnoreCase(title)
                .and(externalId == null ? null : externalIdEquals(externalId))
                .and(categories == null ? null : categoriesConstainsIgnoreCase(categories))
                .and(status == null ? null : statusEquals(status))
                .and(origin == null ? null : originEquals(origin))
                .and(language == null ? null : languageEquals(language))
                //.and(hasSubqueryGraphicNovel ? graphicNovelIn(graphicNovelTitle, graphicNovelTExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo, republications) : null)
                //.and(nickname == null ? null : authorIn(nickname, "", ""))
                );

        List<Serie> series = serieRepository.findAll();
        System.out.println(series.size());
        return series;
    }
*/
}
