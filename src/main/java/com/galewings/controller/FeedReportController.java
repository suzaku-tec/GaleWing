package com.galewings.controller;

import com.galewings.dto.input.FeedReportDto;
import com.galewings.service.ai.FeedReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/feed/analyze")
@Controller
public class FeedReportController {

    public final FeedReportService feedReportService;

    @Autowired
    public FeedReportController(FeedReportService feedReportService) {
        this.feedReportService = feedReportService;
    }

    @PostMapping("/summary")
    @ResponseBody
    public void summary(@RequestBody FeedReportDto feedReportDto) {
        feedReportService.summary(feedReportDto.link);
    }

    @PostMapping("/informationGathering")
    @ResponseBody
    public void informationGathering(@RequestBody FeedReportDto feedReportDto) {
        feedReportService.informationGathering(feedReportDto.link);
    }

}
