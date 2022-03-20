package com.shiyisoushu.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户搜索事件
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/11/9 3:12 下午
 */
@AllArgsConstructor
@Getter
public class UserSearchEvent {
    private final String searchKey;
}