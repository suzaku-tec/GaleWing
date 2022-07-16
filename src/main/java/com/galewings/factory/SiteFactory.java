package com.galewings.factory;

import com.galewings.entity.Site;
import com.galewings.service.FaviconService;
import com.rometools.rome.feed.synd.SyndFeed;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;

/**
 * SiteFactory
 */
public class SiteFactory {

  public static Site create(RssSyndFeed rssSyndFeed) {
    Site site = new Site();
    site.uuid = UUID.randomUUID().toString();
    site.title = rssSyndFeed.getSyndFeed().getTitle();
    site.htmlUrl = rssSyndFeed.getSyndFeed().getLink();
    site.xmlUrl = rssSyndFeed.getRssUrl();

    return site;
  }

  /**
   * サイトエンティティの生成
   *
   * @param rssUrl RSSのURL
   * @param feed   フィード情報
   * @return Siteエンティティ
   */
  public static Site create(String rssUrl, SyndFeed feed) {
    FaviconService faviconUtil = new FaviconService();
    String favicon = faviconUtil.getBase64Favicon(feed.getLink()).orElse(Strings.EMPTY);
    return create(rssUrl, feed, favicon);
  }

  /**
   * Siteエンティティの生成
   *
   * @param rssUrl         RSSのURL
   * @param feed           フィード情報
   * @param faviconBase64s Base64文字列のFavicon
   * @return Siteエンティティ
   */
  public static Site create(String rssUrl, SyndFeed feed, String faviconBase64s) {
    Site site = new Site();
    site.uuid = UUID.randomUUID().toString();
    site.title = feed.getTitle();
    site.htmlUrl = feed.getLink();
    site.xmlUrl = rssUrl;
    site.faviconBase64 = faviconBase64s;

    return site;
  }

  /**
   * RssSyndFeed
   */
  public interface RssSyndFeed {

    /**
     * RSSのURL
     *
     * @return URL
     */
    String getRssUrl();

    /**
     * SyndFeed
     *
     * @return SyndFeed
     */
    SyndFeed getSyndFeed();
  }
}
