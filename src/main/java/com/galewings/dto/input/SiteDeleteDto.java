package com.galewings.dto.input;

/**
 * サイト削除情報
 */
public class SiteDeleteDto {

  /**
   * UUID
   */
  private String uuid;

  /**
   * UUID取得
   *
   * @return UUID
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * UUID設定
   *
   * @param uuid UUID
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
