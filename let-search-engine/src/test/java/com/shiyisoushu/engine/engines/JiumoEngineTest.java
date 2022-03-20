package com.shiyisoushu.engine.engines;

import com.shiyisoushu.engine.model.SearchResultModel;
import com.shiyisoushu.engine.BaseEngine;
import org.junit.Test;

import java.util.Set;

public class JiumoEngineTest {

    @Test
    public void test() {
        BaseEngine jiumoEngine = new JiumoEngine();
        Set<SearchResultModel> result = jiumoEngine.search("香蜜之软玉生香");
        result.forEach(System.out::println);
    }
}