package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;

public class Stack {

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

  @Column(name = "stackDate")
  public String stackDate;

}
