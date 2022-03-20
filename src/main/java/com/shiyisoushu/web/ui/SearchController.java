package com.shiyisoushu.web.ui;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.shiyisoushu.core.event.UserSearchEvent;
import com.shiyisoushu.repo.es.model.BookCrawlerResponseModel;
import com.shiyisoushu.repo.mysql.model.WebsiteModel;
import com.shiyisoushu.web.base.Result;
import com.shiyisoushu.web.constant.BookSearchConstant;
import com.shiyisoushu.web.vo.BookSearchResponseVO;

import com.shiyisoushu.web.service.WebsiteService;
import com.shiyisoushu.web.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexBoost;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/11/19 7:04 下午
 */
@RequestMapping("/search")
@RestController
@Slf4j
public class SearchController {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ElasticsearchRestTemplate template;
    @Autowired
    private WebsiteService websiteService;

    /**
     * 第三个版本的搜索
     *
     * @return 搜索结果
     */
    @GetMapping("/v3")
    public Result searchV3(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                           @RequestParam(value = "q") String q) {
        publisher.publishEvent(new UserSearchEvent(q));

        HighlightBuilder builder = new HighlightBuilder();
        builder.field("summary", 50, 2);
        builder.field("title", 50, 2);
        List<WebsiteModel> websiteList = websiteService.findEnableWebsite();
        System.out.println(JSONObject.toJSONString(websiteList.stream().map(WebsiteModel::getSite).map(a -> "site_" + a).collect(Collectors.toList())));
        Map<String, WebsiteModel> siteList = websiteService.findEnableWebsite().stream().collect(Collectors.toMap(WebsiteModel::getSite, a -> a));
        String[] indexNames = websiteList.stream().map(WebsiteModel::getSite).toArray(String[]::new);
        IndexCoordinates index = IndexCoordinates.of("site_all_together");
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery().should(matchPhraseQuery("title", q).boost(20)).should(matchPhraseQuery("summary", q)))
                .withHighlightBuilder(builder)
                .withMinScore(10)
                .withIndicesBoost(websiteList.stream().filter(a -> a.getBoost() > 1).map(a -> new IndexBoost("site_" + a.getSite(), a.getBoost())).collect(Collectors.toList()))
                .withPageable(PageRequest.of(pageNo, 20))
                .withFilter(termsQuery("site", indexNames))
                .build();
        SearchHits<BookCrawlerResponseModel> searchHits = template
                .search(query, BookCrawlerResponseModel.class, index);
        List<BookSearchResponseVO> map = searchHits.stream().map(a -> {
            BookSearchResponseVO var = JacksonUtils.convertValue(a.getContent(), BookSearchResponseVO.class);
            var.setSummary(StrUtil.sub(a.getContent().getSummary(), 0, 100));
            var.setSiteFavicon(BookSearchConstant.STATIC_HOST + siteList.getOrDefault(a.getContent().getSite(), new WebsiteModel()).getFavicon());
            List<String> title = a.getHighlightField("title");
            List<String> summary = a.getHighlightField("summary");
            if (CollUtil.isNotEmpty(title)) {
                var.setTitle(StrUtil.join("......", title));
            }
            if (CollUtil.isNotEmpty(summary)) {
                var.setSummary(StrUtil.join("......", summary));
            }
            if (StrUtil.isNotBlank(var.getCoverUrl())) {
                if (!var.getCoverUrl().startsWith("http")) {
                    var.setCoverUrl(BookSearchConstant.STATIC_HOST + "/" + var.getCoverUrl());
                }
            }
            return var;
        }).collect(Collectors.toList());
        Page<BookSearchResponseVO> result = new PageImpl<>(map);

        return Result.ok(result);
    }

}