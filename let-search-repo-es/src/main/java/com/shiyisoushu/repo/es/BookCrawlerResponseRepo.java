package com.shiyisoushu.repo.es;

import com.shiyisoushu.repo.es.model.BookCrawlerResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class BookCrawlerResponseRepo {
    @Autowired
    public ElasticsearchOperations elasticsearchOperations;

    public String save(BookCrawlerResponseModel model) {
        model.setId(model.getUrl());
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(model.getUrl())
                .withObject(model)
                .build();
        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of("site_all_together"));
    }
}
