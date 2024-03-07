package com.galewings.controller;

import com.galewings.repository.NewsSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/news/summary")
public class NewsSummaryController {

    @Autowired
    private NewsSummaryRepository newsSummaryRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newsSummaryList", newsSummaryRepository.selectAll());
        return "newsSummary";
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam("uuid") String uuid) {
        newsSummaryRepository.delete(uuid);
    }

    @PostMapping("/add")
    @ResponseBody
    public void addSummary(Model model, @RequestParam(name = "uuid", required = true) String uuid) {
        if (!newsSummaryRepository.exists(uuid)) {
            newsSummaryRepository.insert(uuid);
        }
    }

}
