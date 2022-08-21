package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;

public class Category {

  @Column(name = "uuid")
  public String uuid;

  @Column(name = "name")
  public String name;

  @Column(name = "description")
  public String description;
}
