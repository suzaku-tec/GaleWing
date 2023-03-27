package com.galewings.entity;

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
        ", author='" + author + '\'' +
        ", comments='" + comments + '\'' +
        ", publishedDate='" + publishedDate + '\'' +
        ", opened='" + opened + '\'' +
        ", readed='" + readed + '\'' +
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

  @Column(name = "author")
  public String author;

  @Column(name = "comments")
  public String comments;

  @Column(name = "publishedDate")
  public String publishedDate;

  @Column(name = "opened")
  public boolean opened;

  @Column(name = "readed")
  public boolean readed;

  @Column(name = "imageUrl")
  public String imageUrl;

  @Column(name = "contentTerxt")
  public String contentTerxt;

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

  public String getAuthor() {
    return author;
  }

  public String getComments() {
    return comments;
  }

  public String getPublishedDate() {
    return publishedDate;
  }

  public boolean getOpened() {
    return this.opened;
  }

  public boolean getReaded() {
    return readed;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getContentTerxt() {
    return contentTerxt;
  }
}
