package com.shiyisoushu.engine.engines;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.shiyisoushu.engine.model.SearchResultModel;
import com.shiyisoushu.engine.BaseEngine;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 鸠摩搜书
 */
@Service
public class JiumoEngine extends BaseEngine {

    String domain1 = "https://www.jiumodiary.com";
    String domain2 = "https://www2.jiumodiary.com";
    String domain5 = "https://www5.jiumodiary.com";

    @Override
    public Set<SearchResultModel> search(String searchKey) {
        Map<String, Object> initHubsParam = new HashMap<>();
        initHubsParam.put("q", searchKey);
        String hubId = JSONObject.parseObject(HttpUtil.post(domain1 + "/init_hubs.php", initHubsParam)).getString("id");

        Set<SearchResultModel> result = new HashSet<>();
        result.addAll(request(domain1, hubId));
        result.addAll(request(domain2, hubId));
        result.addAll(request(domain5, hubId));
        return result;
    }

    @Override
    public String getName() {
        return "jiumo";
    }

    private List<SearchResultModel> request(String domain, String hubId) {
        Map<String, Object> fetchParam = new HashMap<>();
        fetchParam.put("id", hubId);
        fetchParam.put("set", 0);
        JSONObject searchResult = JSONObject.parseObject(HttpUtil.post(domain + "/ajax_fetch_hubs.php", fetchParam));
        return searchResult.getJSONArray("sources")
                .stream()
                .map(a -> ((JSONObject) a))
                .filter(a -> a.getString("view_type").endsWith("view_normal"))
                .flatMap(a -> a.getJSONObject("details").getJSONArray("data").stream())
                .map(a -> ((JSONObject) a))
                .map(a -> {
                    SearchResultModel model = new SearchResultModel();
                    String title = a.getString("title");
                    model.setTitle(title);
                    String url = a.getString("link");
                    model.setUrl(url);
                    String summary = a.getString("des");
                    model.setSummary(summary);
                    model.setSite(getDomainName(URLUtil.toURI(url)));
                    return model;
                })
                .collect(Collectors.toList());
    }
}
