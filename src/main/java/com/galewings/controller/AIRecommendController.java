package com.galewings.controller;

import com.galewings.dto.AIRecommendDto;
import com.galewings.entity.AIRecommendFeed;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.service.AIRecommendService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ai/recommend")
@Controller
public class AIRecommendController {

    private final AIRecommendService aiRecommendService;

    private final FeedRepository feedRepository;

    public AIRecommendController(AIRecommendService aiRecommendService, FeedRepository feedRepository) {
        this.aiRecommendService = aiRecommendService;
        this.feedRepository = feedRepository;
    }

    @GetMapping("/")
    public String index(Model model) {

        List<AIRecommendDto> recommends = aiRecommendService.selectAIRecommndFeed().stream().map(feed -> {
            List<Feed> originFeeds = aiRecommendService.selectAIRecommendOrigin(feed.id);
            AIRecommendDto dto = new AIRecommendDto();
            dto.id = feed.id;
            dto.title = feed.title;
            dto.feedList = originFeeds;
            return dto;
        }).toList();

        model.addAttribute("recommends", recommends);

        return "ai_recommend/index";
    }

    @GetMapping("/feed")
    @ResponseBody
    public List<AIRecommendFeed> selectFeed() {
        return aiRecommendService.selectAIRecommndFeed();
    }

    @GetMapping("/origin/{id}")
    @ResponseBody
    public List<Feed> selectFeedOrigin(@PathVariable("id") String id) {
        return aiRecommendService.selectAIRecommendOrigin(id);
    }

    @PostMapping("/read/{id}")
    @ResponseBody
    public void readAiRecommendFeed(@PathVariable("id") String id) {
        List<Feed> feeds = aiRecommendService.selectAIRecommendOrigin(id);
        for (Feed feed : feeds) {
            feedRepository.updateReadFeedNoOpen(feed.link);
        }

        aiRecommendService.deleteAIRecommend(id);
    }
}
