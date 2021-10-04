package com.galewings;

import com.galewings.entity.Stack;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    stack.stackDate = sdformat.parse("1987/09/14 00:00:00");

    return stack;
  }
}
