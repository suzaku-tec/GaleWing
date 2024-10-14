package com.galewings.controller;

import com.galewings.dto.newsapi.response.NewsApiResponseDto;
import com.galewings.service.NewsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URISyntaxException;

@RequestMapping("/newsApi")
@Controller
public class NewsApiController {

    private final NewsApiService newsApiService;

    @Autowired
    public NewsApiController(NewsApiService newsApiService) {
        this.newsApiService = newsApiService;
    }

    @GetMapping("/")
    public String index(Model model) throws IOException, URISyntaxException, IllegalAccessException {
        NewsApiResponseDto newsApiResponseDto = newsApiService.topHeadlines();
        model.addAttribute("newsApiResponseDto", newsApiResponseDto);
        return "newsApi";
    }
}
