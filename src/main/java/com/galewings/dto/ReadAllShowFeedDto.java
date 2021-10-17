package com.galewings.dto;

import java.io.Serializable;

/**
 * 全既読処理入力情報
 */
public class ReadAllShowFeedDto implements Serializable {

  /**
   * サイト識別情報
   */
  private String identifier;

  /**
   * サイト識別情報取得
   *
   * @return サイト識別情報
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * サイト識別情報設定
   *
   * @param identifier サイト識別情報
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
