package com.galewings.dto;

import java.io.Serializable;

public class ReadAllShowFeedDto implements Serializable {

  private String identifier;

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
