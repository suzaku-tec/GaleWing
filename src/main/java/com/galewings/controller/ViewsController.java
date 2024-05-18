package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.input.ViewSaveDto;
import com.galewings.entity.Site;
import com.galewings.entity.View;
import com.galewings.repository.SiteRepository;
import com.galewings.repository.ViewsRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/views")
@Controller
public class ViewsController {

    @Autowired
    private ViewsRepository viewsRepository;

    @Autowired
    private SiteRepository siteRepository;

    @GetMapping("/")
    public String index(Model model) throws JsonProcessingException {
        List<Site> siteList = siteRepository.getAllSite();
        model.addAttribute("siteList", siteList);

        List<View> viewList = viewsRepository.idList();
        ObjectMapper om = new ObjectMapper();
        for (View view : viewList) {
            List<Site> viewSiteList = viewsRepository.selectSiteList(view.id);
            List<Map<String, String>> minifyViewSiteList = viewSiteList.stream().map(viewSite -> {
                Map<String, String> minify = new HashMap<>();
                minify.put("uuid", viewSite.uuid);
                minify.put("title", viewSite.title);
                return minify;
            }).toList();
            view.siteJson = om.writeValueAsString(minifyViewSiteList);
        }
        model.addAttribute("viewList", viewList);
        return "views";
    }

    @PostMapping("/list")
    @ResponseBody
    public List<View> list() {
        return viewsRepository.idList();
    }

    @PostMapping("/info")
    @ResponseBody
    public View info(String id) {
        return viewsRepository.info(id);
    }

    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestBody ViewSaveDto viewSaveDto) {
        if (Strings.isNullOrEmpty(viewSaveDto.viewId)) {
            // View情報を登録
            viewSaveDto.viewId = UUID.randomUUID().toString();
            viewsRepository.insertView(viewSaveDto);

            viewSaveDto.siteIdList.forEach(siteUuid -> {
                viewsRepository.insertViewSite(viewSaveDto, siteUuid);
            });
        } else {
            // View情報を更新
            viewsRepository.updateView(viewSaveDto);
            viewSaveDto.siteIdList.forEach(siteUuid -> {
                viewsRepository.updateViewSite(viewSaveDto, siteUuid);
            });
        }
    }
}

