package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchEndpointSupportedOnesieConfig {

  public Html5PlaybackOnesieConfig html5PlaybackOnesieConfig;
}
