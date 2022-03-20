package com.shiyisoushu.web.ui;

import com.shiyisoushu.web.base.Result;
import com.shiyisoushu.web.service.RankingService;
import com.shiyisoushu.web.vo.BookSearchVO;
import com.shiyisoushu.web.vo.RankingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获取排行
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/12/6 7:47 下午
 */
@RestController
@RequestMapping("/ranking")
public class RankingController {
    @Autowired
    private RankingService rankingService;

    /**
     * 热门搜索词
     */
    @GetMapping("/searchKey")
    public Result<RankingVO<BookSearchVO>> searchKey() {
        Set<ZSetOperations.TypedTuple<String>> webBooks = rankingService.topSearchKey();
        return Result.ok(new RankingVO<>("热门搜索",
                        webBooks.stream()
                                .map(a -> new BookSearchVO(a.getValue(), a.getScore()))
                                .collect(Collectors.toList())
                )
        );
    }
}