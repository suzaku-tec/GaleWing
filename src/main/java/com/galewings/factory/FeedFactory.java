package com.galewings.factory;

import com.galewings.entity.Feed;
import com.rometools.rome.feed.synd.SyndEntry;
import java.text.SimpleDateFormat;

/**
 * FeedFactory
 */
public class FeedFactory {

  /**
   * Feed生成
   *
   * @param syndEntry SyndEntry
   * @param uuid      UUID
   * @return Feed
   */
  public static Feed create(SyndEntry syndEntry, String uuid) {
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Feed f = new Feed();
    f.uuid = uuid;
    f.title = syndEntry.getTitle();
    f.link = syndEntry.getLink();
    f.uri = syndEntry.getUri();
    f.author = syndEntry.getAuthor();
    f.comments = syndEntry.getComments();

    try {
      f.publishedDate = sdFormat.format(syndEntry.getPublishedDate());
    } catch (NullPointerException e) {
      e.printStackTrace();
      f.publishedDate = null;
    }

    return f;
  }
}
