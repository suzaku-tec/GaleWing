package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NavigationEndpoint {

  public String clickTrackingParams;

  public CommandMetadata commandMetadata;

  public WatchEndpoint watchEndpoint;
}
