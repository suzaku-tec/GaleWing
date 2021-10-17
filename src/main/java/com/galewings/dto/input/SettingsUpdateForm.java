package com.galewings.dto.input;

import java.util.Map;

/**
 * 設定画面フォーム
 */
public class SettingsUpdateForm {

  /**
   * 設定情報
   */
  private Map<String, String> settings;

  /**
   * 設定情報を取得する
   *
   * @return 設定情報
   */
  public Map<String, String> getSettings() {
    return settings;
  }

  /**
   * 設定情報を設定する
   *
   * @param settings 設定情報
   */
  public void setSettings(Map<String, String> settings) {
    this.settings = settings;
  }
}
