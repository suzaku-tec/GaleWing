package com.galewings.factory;

import com.galewings.entity.Site;
import com.rometools.rome.feed.synd.SyndFeed;
import java.util.UUID;

public class SiteFactory {

  public static Site create(RssSyndFeed rssSyndFeed) {
    Site site = new Site();
    site.uuid = UUID.randomUUID().toString();
    site.title = rssSyndFeed.getSyndFeed().getTitle();
    site.htmlUrl = rssSyndFeed.getSyndFeed().getLink();
    site.xmlUrl = rssSyndFeed.getRssUrl();

    return site;
  }

  public interface RssSyndFeed {

    String getRssUrl();

    SyndFeed getSyndFeed();
  }
}
