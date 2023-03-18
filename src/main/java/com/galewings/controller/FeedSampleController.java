package com.galewings.controller;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.URL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/feedSample")
@Controller
public class FeedSampleController {

  @GetMapping("")
  @ResponseBody
  public SyndFeed index(@RequestParam(required = false) String url) throws FeedException {
    try {
      SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
      return syndFeed;
    } catch (IOException e) {
      return null;
    }
  }

}
