package com.galewings.dto.wordNews.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsDto {
    private String summary;
    private String image;
    private String author;
    private Long id;
    private String text;
    private String title;

    @JsonProperty("publish_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    private String url;
    private List<String> authors;
    private String video;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String value) {
        this.summary = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String value) {
        this.image = value;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String value) {
        this.author = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date value) {
        this.publishDate = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> value) {
        this.authors = value;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String value) {
        this.video = value;
    }
}
