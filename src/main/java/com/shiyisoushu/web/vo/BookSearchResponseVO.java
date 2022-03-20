package com.shiyisoushu.web.vo;

import lombok.Data;

/**
 * 图书搜索结果
 */
@Data
public class BookSearchResponseVO {
    private String id;

    private String siteName;

    private String siteFavicon;

    private String url;

    private String title;

    private String summary;

    private String coverUrl;

}
