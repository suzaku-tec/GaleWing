package com.galewings.dto.newsapi.response;

import java.io.Serializable;

public class ArticlesDto implements Serializable {
    private SourceDto source;
    private Object author;
    private String title;
    private String description;
    private String url;
    private Object urlToImage;
    private String publishedAt;
    private String content;

    public void setSource(SourceDto source) {
        this.source = source;
    }

    public SourceDto getSource() {
        return source;
    }

    public void setAuthor(Object author) {
        this.author = author;
    }

    public Object getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrlToImage(Object urlToImage) {
        this.urlToImage = urlToImage;
    }

    public Object getUrlToImage() {
        return urlToImage;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return
                "ArticlesDTO{" +
                        "source = '" + source + '\'' +
                        ",author = '" + author + '\'' +
                        ",title = '" + title + '\'' +
                        ",description = '" + description + '\'' +
                        ",url = '" + url + '\'' +
                        ",urlToImage = '" + urlToImage + '\'' +
                        ",publishedAt = '" + publishedAt + '\'' +
                        ",content = '" + content + '\'' +
                        "}";
    }
}