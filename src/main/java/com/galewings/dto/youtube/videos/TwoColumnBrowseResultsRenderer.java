package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwoColumnBrowseResultsRenderer {

  public List<Tab> tabs = Collections.emptyList();

}
