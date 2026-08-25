package com.galewings.controller;

import com.galewings.annotation.FunctionCtrlAspect;
import com.galewings.entity.Feed;
import com.galewings.service.FeedCategoryService;
import com.galewings.service.filter.ClassificationCategory;
import com.galewings.service.filter.GwRuleBasedNewsClassifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/feedCategory")
@Controller
@FunctionCtrlAspect("feedCategory")
@Transactional
public class FeedCategoryController {

    private final FeedCategoryService feedCategoryService;

    private final GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier;

    public FeedCategoryController(FeedCategoryService feedCategoryService, GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier) {
        this.feedCategoryService = feedCategoryService;
        this.gwRuleBasedNewsClassifier = gwRuleBasedNewsClassifier;
    }

    @GetMapping("/")
    public String index(Model model) {

        List<String> list = gwRuleBasedNewsClassifier.getCategories().stream().map(ClassificationCategory::getId).toList();
        model.addAttribute("categoryList", list);

        return "feedCategory/index";
    }

    @PostMapping("/select/{categoryId}")
    @ResponseBody
    public List<Feed> getFeedCategories(@PathVariable("categoryId") String categoryId) {
        // Implementation for fetching feed categories
        return feedCategoryService.getFeedCategories(categoryId);
    }

}
