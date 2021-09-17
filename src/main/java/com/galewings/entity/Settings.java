package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

@Table(name = "setting")
public class Settings {

  @Column(name = "id")
  public String id;

  @Column(name = "setting")
  public String setting;

  @Column(name = "overview")
  public String overview;

  @Column(name = "description")
  public String description;

}
