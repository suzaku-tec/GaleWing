package com.galewings.dto.input;

/**
 * 積読追加情報
 */
public class StackAddDto {

  /**
   * UUID
   */
  private String uuid;

  /**
   * サイト情報
   */
  private String link;

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

  /**
   * リンク情報取得
   *
   * @return リンク
   */
  public String getLink() {
    return link;
  }

  /**
   * リンク情報設定
   *
   * @param link リンク
   */
  public void setLink(String link) {
    this.link = link;
  }
}
