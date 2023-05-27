package com.galewings.controller;

import com.galewings.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/podcast")
@Controller
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @GetMapping(value = "")
    public String index() {
        return "/podcast/index";
    }

    @ResponseBody
    public void add() {

    }

    @PostMapping("/sync")
    @ResponseBody
    public String sync() {
        podcastService.sync();
        return "";
    }

}
