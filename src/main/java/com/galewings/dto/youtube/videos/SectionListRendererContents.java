package com.galewings.dto.youtube.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionListRendererContents {

  public ItemSectionRenderer itemSectionRenderer;
}
