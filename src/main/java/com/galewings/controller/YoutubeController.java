package com.galewings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class YoutubeController {

  @GetMapping("/youtube")
  public String index() {
    return "/youtube/index";
  }
}
