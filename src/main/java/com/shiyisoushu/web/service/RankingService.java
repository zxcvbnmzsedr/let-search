package com.shiyisoushu.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/12/6 7:32 下午
 */
@Service
@Slf4j
public class RankingService {
    @Autowired
    private SearchKeyRanking searchKeyRanking;

    /**
     * 搜索排名
     */
    public Set<ZSetOperations.TypedTuple<String>> topSearchKey() {
        return searchKeyRanking.top(11);
    }
}