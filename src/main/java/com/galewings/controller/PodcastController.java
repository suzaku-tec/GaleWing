package com.galewings.controller;

import com.galewings.entity.PodcastFeed;
import com.galewings.service.PodcastService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Podcast管理画面用コントローラ
 * <p>
 * https://www.get-rss.com/を利用してRSSを取得する(spotify向け)
 * うまく取れれば、フィードに表示可能
 */
@RequestMapping("/podcast")
@Controller
@Transactional
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @GetMapping(value = "")
    public String index(Model model) {
        model.addAttribute("modalFileList", List.of("addPodcast", "updateMessage"));
        return "/podcast/index";
    }

    @PostMapping("/add")
    @ResponseBody
    public void add(@RequestBody Map<String, String> body) {
        String url = body.getOrDefault("url", StringUtils.EMPTY);
        String title = body.getOrDefault("title", StringUtils.EMPTY);
        podcastService.addPodcast(url, title);
    }

    @PostMapping("/sync")
    @ResponseBody
    public void sync() {
        podcastService.sync();
    }

    @PostMapping("/notReadFeed")
    @ResponseBody
    public List<PodcastFeed> getNotReadFeed() {
        return podcastService.getNotReadFeed();
    }

    @PostMapping("/markRead")
    @ResponseBody
    public int markRead(@RequestBody Map<String, String> body) {
        String url = body.getOrDefault("url", StringUtils.EMPTY);
        return podcastService.markRead(url);
    }

}
