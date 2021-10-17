package com.galewings.factory;

import com.galewings.entity.FeedContents;
import com.rometools.rome.feed.synd.SyndEntry;

/**
 * FeedContentsFactory
 */
public class FeedContentsFactory {

  /**
   * FeedContents生成
   *
   * @param entry SyndEntry
   * @return FeedContents
   */
  public static FeedContents create(SyndEntry entry) {
    FeedContents contents = new FeedContents();
    contents.uri = entry.getUri();
    contents.type = entry.getContents().get(0).getType();
    contents.value = entry.getContents().get(0).getValue();
    return contents;
  }
}
