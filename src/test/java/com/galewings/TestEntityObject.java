package com.galewings;

import com.galewings.entity.Settings;
import com.galewings.entity.Stack;
import java.text.ParseException;

public final class TestEntityObject {

  public static Stack stack() throws ParseException {
    Stack stack = new Stack();
    stack.title = "test-title";
    stack.uuid = "test-uuid";
    stack.link = "test-link";
    stack.uri = "test-uri";
    stack.author = "test-author";
    stack.comments = "test-comments";
    stack.publishedDate = "test-publishedDate";
    stack.stackDate = "1987/09/14 00:00:00";

    return stack;
  }

  public static Settings settings() {
    Settings settings = new Settings();
    settings.id = "test";
    settings.setting = "test value";
    settings.description = "test description";
    settings.overview = "test overview";
    return settings;
  }
}
