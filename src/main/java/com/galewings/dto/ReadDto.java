package com.galewings.dto;

import java.io.Serializable;

/**
 * 既読情報
 */
public class ReadDto implements Serializable {

  /**
   * リンク
   */
  private String link;

  /**
   * リンク取得
   *
   * @return
   */
  public String getLink() {
    return link;
  }

  /**
   * リンク設定
   *
   * @param link
   */
  public void setLink(String link) {
    this.link = link;
  }

}
