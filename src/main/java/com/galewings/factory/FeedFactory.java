package com.galewings.factory;

import com.galewings.entity.Feed;
import com.rometools.rome.feed.synd.SyndEntry;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
      f.publishedDate = null;
    }

    f.imageUrl = searchImageUrl(syndEntry);

    return f;
  }

  private static String searchImageUrl(SyndEntry syndEntry) {
    return syndEntry.getContents().stream().map(syndContent -> {
          if ("text/html".equals(syndContent.getType()) || "html".equals(syndContent.getType())) {
            Document doc = Jsoup.parseBodyFragment(syndContent.getValue());
            Element imgEl = doc.select("img").first();
            if (imgEl != null) {
              return imgEl.attr("src");
            }
          }

          return null;
        }).filter(StringUtils::isNotEmpty)
        .findFirst().orElseGet(() -> null);
  }

}
