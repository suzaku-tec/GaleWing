package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.dto.RssBridgeKey;
import com.galewings.dto.input.ContentsListDto;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import com.galewings.service.RssBridgeService;
import com.galewings.service.RssProxyService;
import com.galewings.service.rssbridge.InstagramBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/rssBridge")
@Controller
@Transactional
public class RssBridgeController {

    private final RssBridgeService rssBridgeService;
    private final InstagramBridgeService instagramBridgeService;
    private final RssProxyService rssProxyService;

    @Autowired
    public RssBridgeController(RssBridgeService rssBridgeService, InstagramBridgeService instagramBridgeService, RssProxyService rssProxyService) {
        this.rssBridgeService = rssBridgeService;
        this.instagramBridgeService = instagramBridgeService;
        this.rssProxyService = rssProxyService;
    }

    @RequestMapping("")
    public String index(Model model) {

        List<RssBridgeKey> rssBridgeKeys = rssBridgeService.selectKeyList();
        model.addAttribute("connectSelect", rssBridgeKeys.stream().map(rssBridgeKey -> rssBridgeKey.connect).distinct().toList());
        model.addAttribute("keySelect", rssBridgeKeys);

        return "rssBridge";
    }

    @PostMapping("/instagram/contentsList")
    @ResponseBody
    public List<String> instagramContentsList(@RequestBody ContentsListDto contentsListDto) throws JsonProcessingException {
        RssBridgeResponse rssBridgeResponse = instagramBridgeService.contentsList(contentsListDto.username);
        return rssBridgeResponse.items.stream().map(item -> item.content_html).map(rssProxyService::convertUrlToProxy).toList();
    }

}
