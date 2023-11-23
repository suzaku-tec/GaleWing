package com.galewings.controller;

import com.galewings.entity.SiteFeedCount;
import com.galewings.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * GaleWingsController
 */
@Controller
public class GaleWingsController {

    /**
     * SiteRepository
     */
    @Autowired
    private SiteRepository siteRepository;

    /**
     * 初期ページ
     *
     * @param model
     * @param uuid  FeedのUUID. 全Feed表示時はnull
     * @return 利用するHTMLテンプレート名
     */
    @GetMapping(value = "/")
    @Transactional
    public String index(Model model, @RequestParam(name = "uuid", required = false) String uuid) {
        List<SiteFeedCount> resultList = siteRepository.getSiteFeedCount();
        model.addAttribute("sitelist", resultList);
        model.addAttribute("identifier", uuid);
        return "index";
    }

    @GetMapping(value = "/card/")
    public String cardLayout(Model model, @RequestParam(name = "uuid", required = false) String uuid) {
        List<SiteFeedCount> resultList = siteRepository.getSiteFeedCount();
        model.addAttribute("sitelist", resultList);
        model.addAttribute("identifier", uuid);
        return "card";
    }

}
