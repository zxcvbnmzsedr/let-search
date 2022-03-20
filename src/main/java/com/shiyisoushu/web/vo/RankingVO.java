package com.shiyisoushu.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/12/6 7:49 下午
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RankingVO<T> {
    /**
     * 榜单名称
     */
    private String name;

    /**
     * 榜单数据列表
     */
    private Collection<T> items;
}