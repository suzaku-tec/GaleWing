package com.galewings.factory;

import com.galewings.entity.Feed;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FeedFactoryTest {

  @Test
  void testCreate() {
    Feed result = FeedFactory.create(new SyndEntryImpl(), "uuid");
    Feed expected = new Feed();
    expected.uuid = "uuid";
    expected.author = "";

    Assertions.assertEquals(expected.title, result.title);
    Assertions.assertEquals(expected.uuid, result.uuid);
    Assertions.assertEquals(expected.link, result.link);
    Assertions.assertEquals(expected.uri, result.uri);
    Assertions.assertEquals(expected.author, result.author);
    Assertions.assertEquals(expected.comments, result.comments);
    Assertions.assertEquals(expected.publishedDate, result.publishedDate);
    Assertions.assertEquals(expected.opened, result.opened);
    Assertions.assertEquals(expected.readed, result.readed);
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme