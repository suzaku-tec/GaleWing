package com.galewings.dto.newsapi.response;

import java.io.Serializable;
import java.util.List;

public class NewsApiResponseDto implements Serializable {
    private String status;
    private int totalResults;
    private List<ArticlesDto> articles;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setArticles(List<ArticlesDto> articles) {
        this.articles = articles;
    }

    public List<ArticlesDto> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return
                "ResponseDTO{" +
                        "status = '" + status + '\'' +
                        ",totalResults = '" + totalResults + '\'' +
                        ",articles = '" + articles + '\'' +
                        "}";
    }
}