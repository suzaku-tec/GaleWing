package com.galewings.dto.output;

import com.miragesql.miragesql.annotation.Column;

public class SiteLastFeedDto {

  @Column(name = "uuid")
  public String uuid;

  @Column(name = "lastUpdateDate")
  public String lastUpdateDate;
}
