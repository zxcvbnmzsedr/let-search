
package com.shiyisoushu.repo.es.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class BookCrawlerResponseModel {
    @Id
    private String id;

    private String siteName;

    private String site;

    private String url;
    @Field(analyzer = "ik_max_word")
    private String title;
    @Field(analyzer = "ik_smart")
    private String summary;

    private String coverUrl;

    private Date date;
    /**
     * 是否有下载链接
     */
    private Boolean hasDownload;
}
