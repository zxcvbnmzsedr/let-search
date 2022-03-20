package com.shiyisoushu.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchResultModel {
    private String site;

    private String url;

    private String title;

    private String summary;

    private String coverUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchResultModel model = (SearchResultModel) o;
        return Objects.equals(url, model.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
