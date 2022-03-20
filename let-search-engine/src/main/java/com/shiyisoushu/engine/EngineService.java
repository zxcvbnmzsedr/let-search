package com.shiyisoushu.engine;

import com.shiyisoushu.core.event.UserSearchEvent;
import com.shiyisoushu.engine.model.SearchResultModel;
import com.shiyisoushu.repo.es.BookCrawlerResponseRepo;
import com.shiyisoushu.repo.es.model.BookCrawlerResponseModel;
import com.shiyisoushu.repo.mysql.WebsiteRepo;
import com.shiyisoushu.repo.mysql.model.WebsiteModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EngineService {
    private static final String ENGINE_KEY = ":j_dupeFilter";
    @Autowired
    private List<BaseEngine> baseEngines;
    @Autowired
    private RedisTemplate<String, String> template;
    @Autowired
    private WebsiteRepo websiteRepo;
    @Autowired
    private BookCrawlerResponseRepo bookCrawlerResponseRepo;


    public void startSearch(String key) {
        List<WebsiteModel> disableWebsite = websiteRepo.findByEnableEquals(false);
        List<String> disableSite = disableWebsite.stream().map(WebsiteModel::getSite).collect(Collectors.toList());
        baseEngines.parallelStream()
                // 筛选重复key
                .filter(a -> !Objects.equals(template.opsForSet().add(a.getName() + ENGINE_KEY, key), 0L))
                // 进行搜索
                .flatMap(a -> {
                    log.info("engine: {},searchKey:{}", a.getName(), key);
                    return a.search(key).stream();
                })
                .filter(a -> !disableSite.contains(a.getSite()))
                // 存储到website发现索引
                .map(this::saveWebsiteToMysql)
                // 发布到es
                .forEach(this::saveEs);

    }

    private SearchResultModel saveWebsiteToMysql(SearchResultModel a) {
        Long add = template.opsForSet().add("website:cache", a.getSite());
        if (!Objects.equals(add, 0L)) {
            WebsiteModel siteInfo = websiteRepo.findBySite(a.getSite());
            if (siteInfo == null) {
                siteInfo = new WebsiteModel();
                siteInfo.setBoost(1F);
                siteInfo.setEnable(true);
                siteInfo.setSite(a.getSite());
                websiteRepo.save(siteInfo);
            }
        }

        return a;
    }

    private void saveEs(SearchResultModel a) {
        BookCrawlerResponseModel responseModel = new BookCrawlerResponseModel();
        responseModel.setCoverUrl(a.getCoverUrl());
        responseModel.setSite(a.getSite());
        responseModel.setSummary(a.getSummary());
        responseModel.setUrl(a.getUrl());
        responseModel.setTitle(a.getTitle());
        bookCrawlerResponseRepo.save(responseModel);
    }

    @EventListener
    @Async
    public void listener(UserSearchEvent userSearchEvent) {
        this.startSearch(userSearchEvent.getSearchKey());
    }
}
