package com.galewings.controller;

import com.galewings.dto.wordNews.response.WordNewsDto;
import com.galewings.service.WordNewsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping("/worldNews")
@Controller
public class WorldNewsApiController {

    private final WordNewsApiService wordNewsApiService;

    @Autowired
    public WorldNewsApiController(WordNewsApiService wordNewsApiService) {
        this.wordNewsApiService = wordNewsApiService;
    }

    @GetMapping("/")
    public String index(Model model) throws IOException {
        WordNewsDto wordNewsDto = wordNewsApiService.topNews("jp");
        model.addAttribute("wordNewsDto", wordNewsDto);
        return "worldNews";
    }
}
