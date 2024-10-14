package com.galewings.dto.wordNews.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopNewDto {
    private List<NewsDto> news;

    public List<NewsDto> getNews() {
        return news;
    }

    public void setNews(List<NewsDto> value) {
        this.news = value;
    }
}
