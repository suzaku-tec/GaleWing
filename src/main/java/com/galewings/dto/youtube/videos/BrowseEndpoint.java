package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrowseEndpoint {

  public String browseId;

  public String params;

  public String canonicalBaseUrl;
}
