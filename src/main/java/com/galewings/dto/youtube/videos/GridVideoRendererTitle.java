package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridVideoRendererTitle {

  public List<Runs> runs;
}
