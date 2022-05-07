package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TabRenderer {

  public Endpoint endpoint;

  public String title;

  public Boolean selected;

  public TabRendererContent content;

  public String trackingParams;
}
