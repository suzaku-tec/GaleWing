package com.galewings.controller;

import com.galewings.dto.statistics.ReadRateDto;
import com.galewings.service.StaticsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/statics")
@RestController
@Transactional
public class StatisticsController {

    private final StaticsService staticsService;

    public StatisticsController(StaticsService staticsService) {
        this.staticsService = staticsService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("statistics"); // hello.html を指定
        return mav;
    }

    @PostMapping("/readRate")
    public ReadRateDto getReadRate() {
        return staticsService.selectReadRate();
    }
}
