package com.shiyisoushu.engine;

import com.shiyisoushu.engine.model.SearchResultModel;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class BaseEngine {
    private final static Set<String> PublicSuffixSet = new HashSet<>(
            Arrays.asList("com|org|net|gov|edu|co|tv|mobi|info|asia|xxx|onion|cn|com.cn|edu.cn|gov.cn|net.cn|org.cn|jp|kr|tw|com.hk|hk|com.hk|org.hk|se|com.se|org.se"
                    .split("\\|")));

    private static final Pattern IP_PATTERN = Pattern.compile("(\\d{1,3}\\.){3}(\\d{1,3})");

    /**
     * 搜索
     *
     * @param searchKey 搜索的Key
     * @return 搜索结果
     */
    public abstract Set<SearchResultModel> search(String searchKey);


    /**
     * 获取引擎名称
     */
    public abstract String getName();


    public String getDomainName(URI url) {
        String host = url.getHost();
        if (host.endsWith(".")) {
            host = host.substring(0, host.length() - 1);
        }
        if (IP_PATTERN.matcher(host).matches()) {
            return host;
        }

        int index = 0;
        String candidate = host;
        while (index >= 0) {
            index = candidate.indexOf('.');
            String subCandidate = candidate.substring(index + 1);
            if (PublicSuffixSet.contains(subCandidate)) {
                return candidate;
            }
            candidate = subCandidate;
        }
        return candidate;
    }
}
