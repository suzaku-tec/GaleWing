package com.example.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

import java.io.Serializable;

@SuppressWarnings("unused")
@Table(name = "feed")
public class Feed implements Serializable {
    @Override
    public String toString() {
        return "Feed{" +
                "title='" + title + '\'' +
                ", uuid='" + uuid + '\'' +
                ", link='" + link + '\'' +
                ", uri='" + uri + '\'' +
                ", type='" + type + '\'' +
                ", author='" + author + '\'' +
                ", comments='" + comments + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                '}';
    }

    @Column(name = "title")
    public String title;

    @Column(name = "uuid")
    public String uuid;

    @Column(name = "link")
    public String link;

    @Column(name = "uri")
    public String uri;

    @Column(name = "type")
    public String type;

    @Column(name = "author")
    public String author;

    @Column(name = "comments")
    public String comments;

    @Column(name = "publishedDate")
    public String publishedDate;

    public String getTitle() {
        return title;
    }

    public String getUuid() {
        return uuid;
    }

    public String getLink() {
        return link;
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public String getComments() {
        return comments;
    }

    public String getPublishedDate() {
        return publishedDate;
    }


}