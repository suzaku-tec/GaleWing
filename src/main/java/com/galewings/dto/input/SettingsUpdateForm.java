package com.galewings.dto.input;

import java.util.Map;

public class SettingsUpdateForm {

  private Map<String, String> settings;

  public Map<String, String> getSettings() {
    return settings;
  }

  public void setSettings(Map<String, String> settings) {
    this.settings = settings;
  }
}
