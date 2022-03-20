
package com.shiyisoushu.web.service;

import com.shiyisoushu.core.event.UserSearchEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 搜索词排行
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/11/9 3:08 下午
 */
@Service
public class SearchKeyRanking {
    private static final String SEARCH_KEY = "search:ranking:key";

    @Autowired
    private RedisTemplate<String, String> template;

    @EventListener
    @Async
    public void listenUserSearch(UserSearchEvent userSearchEvent) {
        addSearchKey(userSearchEvent.getSearchKey());
    }

    /**
     * 新增搜索的键
     *
     * @param searchKey searchKey
     */
    private void addSearchKey(String searchKey) {
        template.opsForZSet().incrementScore(SEARCH_KEY, searchKey, 1);
    }

    /**
     * 获取前十个搜索词
     *
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> top(int limit) {
        return template.opsForZSet().reverseRangeWithScores(SEARCH_KEY, 0, limit);

    }
}