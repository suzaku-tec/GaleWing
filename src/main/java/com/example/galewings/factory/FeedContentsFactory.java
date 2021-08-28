package com.example.galewings.factory;

import com.example.galewings.entity.FeedContents;
import com.rometools.rome.feed.synd.SyndEntry;

public class FeedContentsFactory {

  public static FeedContents create(SyndEntry entry) {
    FeedContents contents = new FeedContents();
    contents.uri = entry.getUri();
    contents.type = entry.getContents().get(0).getType();
    contents.value = entry.getContents().get(0).getValue();
    return contents;
  }
}
