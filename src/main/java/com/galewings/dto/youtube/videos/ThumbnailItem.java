package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThumbnailItem {

  public String url;

  public BigDecimal width;

  public BigDecimal height;
}
