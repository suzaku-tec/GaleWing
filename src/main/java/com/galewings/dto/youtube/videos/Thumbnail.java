package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Thumbnail {

  public List<ThumbnailItem> thumbnails;
}
