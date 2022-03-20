package com.shiyisoushu.web.vo;

import cn.hutool.core.util.StrUtil;
import com.shiyisoushu.web.constant.BookSearchConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/12/7 2:33 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchVO {
    private String searchKey;

    private Double num;

    public String getHref() {
        return StrUtil.format(BookSearchConstant.SEARCH_URL, searchKey);
    }


}