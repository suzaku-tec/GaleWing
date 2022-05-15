package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebCommandMetadata {

  public String url;

  public String webPageType;

  public BigDecimal rootVe;

  public String apiUrl;

}
