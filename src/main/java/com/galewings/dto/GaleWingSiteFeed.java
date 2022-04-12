package com.galewings.dto;

import com.galewings.entity.Site;
import com.rometools.rome.feed.synd.SyndFeed;
import java.util.Optional;

public interface GaleWingSiteFeed {

  /**
   * サイト情報
   *
   * @return サイト情報
   */
  Site getSite();

  /**
   * フィード情報
   *
   * @return フィード情報
   */
  Optional<SyndFeed> getOptionalSyndFeed();
}
