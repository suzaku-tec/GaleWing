package com.galewings.controller;

import com.galewings.dto.output.KeywordPrevDayComparisonDto;
import com.galewings.entity.Feed;
import com.galewings.service.TrendAnalyseService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/trend")
@Controller
@Transactional
public class TrendAnalyseController {

    private final TrendAnalyseService trendAnalyseService;

    public TrendAnalyseController(TrendAnalyseService trendAnalyseService) {
        this.trendAnalyseService = trendAnalyseService;
    }

    @GetMapping("/")
    public String index() {
        return "trend";
    }

    @GetMapping("/search")
    public String searchDate(@RequestParam("targetDate") String targetDate, Model model) {
        List<KeywordPrevDayComparisonDto> keywordPrevDayComparisonDtos = trendAnalyseService.searchDate(targetDate);
        model.addAttribute("keywordPrevDayComparisonList", keywordPrevDayComparisonDtos);
        model.addAttribute("targetDate", targetDate);
        return "trend";
    }

    @GetMapping("/keyword/feeds")
    @ResponseBody
    public List<Feed> getKeywordFeedList(@RequestParam("keyword") String keyword) {
        return trendAnalyseService.selectKeywordFeed(keyword);
    }
}
