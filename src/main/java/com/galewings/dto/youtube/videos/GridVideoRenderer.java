package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridVideoRenderer {

  public String videoId;

  public Thumbnail thumbnail;

  public GridVideoRendererTitle title;

  public SimpleText publishedTimeText;

  public SimpleText viewCountText;

  public NavigationEndpoint navigationEndpoint;

}
