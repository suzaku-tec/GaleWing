package com.galewings.controller;

import com.galewings.entity.SiteFeedCount;
import com.galewings.repository.SiteRepository;
import com.rometools.utils.Strings;
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
    public String cardLayout(Model model, @RequestParam(name = "uuid", required = false) String uuid,
                             @RequestParam(name = "index", required = false) String index) {
        List<SiteFeedCount> resultList = siteRepository.getSiteFeedCount();
        model.addAttribute("sitelist", resultList);
        model.addAttribute("identifier", uuid);

        int pageIndex = Strings.isBlank(index) ? 1 : Integer.parseInt(index);
        model.addAttribute("pageIndex", pageIndex);

        int startIndex = 1;
        int endIndex = resultList.size();
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("endIndex", endIndex);

        int previousIndex = pageIndex == 1 ? 1 : pageIndex - 1;
        int nextIndex = pageIndex == endIndex ? endIndex : pageIndex + 1;
        model.addAttribute("previousIndex", previousIndex);
        model.addAttribute("nextIndex", nextIndex);

        boolean startSpace = 1 < previousIndex;
        boolean endSpace = nextIndex < endIndex;
        model.addAttribute("startSpace", startSpace);
        model.addAttribute("endSpace", endSpace);

        return "card";
    }

}
