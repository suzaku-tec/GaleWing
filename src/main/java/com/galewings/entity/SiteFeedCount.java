package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;

public class SiteFeedCount {

  @Column(name = "uuid")
  public String uuid;

  @Column(name = "title")
  public String title;

  @Column(name = "count")
  public int count;

  @Column(name = "faviconBase64")
  public String faviconBase64;
}
