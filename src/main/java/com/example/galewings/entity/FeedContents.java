package com.example.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

@Table(name = "feed_contents")
public class FeedContents {

  @Column(name = "uri")
  public String uri;

  @Column(name = "value")
  public String value;

  @Column(name = "type")
  public String type;
}
