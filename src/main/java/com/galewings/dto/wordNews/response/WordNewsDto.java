package com.galewings.dto.wordNews.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WordNewsDto {
    private String country;

    @JsonProperty("top_news")
    private List<TopNewDto> topNews;
    private String language;

    public String getCountry() {
        return country;
    }

    public void setCountry(String value) {
        this.country = value;
    }

    public List<TopNewDto> getTopNews() {
        return topNews;
    }

    public void setTopNews(List<TopNewDto> value) {
        this.topNews = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String value) {
        this.language = value;
    }
}
