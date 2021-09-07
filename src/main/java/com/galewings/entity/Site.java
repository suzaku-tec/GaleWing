package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

@SuppressWarnings("unused")
@Table(name = "site")
public class Site {

  @Column(name = "uuid")
  public String uuid;

  @Column(name = "title")
  public String title;

  @Column(name = "htmlUrl")
  public String htmlUrl;

  @Column(name = "xmlUrl")
  public String xmlUrl;
}
