package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.input.SiteDeleteDto;
import com.galewings.dto.input.SiteUpdateIconDto;
import com.galewings.entity.Site;
import com.galewings.exception.GaleWingsRuntimeException;
import com.galewings.repository.SiteRepository;
import com.galewings.service.URLService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
public class SiteController {

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    private URLService urlService;

    /**
     * サイトリスト取得
     *
     * @return Siteテーブルの全データのJSON
     * @throws JsonProcessingException
     */
    @GetMapping("/sitelist")
    @ResponseBody
    public String getSiteList() throws JsonProcessingException {
        List<Site> resultList = siteRepository.getAllSite();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(resultList);
    }

    /**
     * サイト管理画面初期表示
     *
     * @param model
     * @return 遷移先情報
     */
    @GetMapping(value = "/site/management")
    public String index(Model model) {
        List<Site> resultList = siteRepository.getAllSite();
        model.addAttribute("sitelist", resultList);

        return "/site/management";
    }

    /**
     * サイト削除
     *
     * @param dto 削除対象サイト情報
     * @return 処理結果(boolean true : 成功 false : 失敗)
     */
    @PostMapping(value = "/site/delete")
    @ResponseBody
    public String delete(@RequestBody SiteDeleteDto dto) {
        boolean result = siteRepository.delete(dto.getUuid());
        return Boolean.toString(result);
    }

    @GetMapping("/site/lastUpdateDate")
    public String fixLastUpdate(Model model) {

        siteRepository.selectSiteLastFeed().stream().forEach(siteLastFeedDto -> {
            siteRepository.updateSiteLastUpdateDate(siteLastFeedDto.uuid, siteLastFeedDto.lastUpdateDate);
        });

        return index(model);
    }

    @PostMapping(value = "/site/updateIcon")
    @ResponseBody
    public void updateIcon(@RequestBody SiteUpdateIconDto siteUpdateIconDto) throws IOException {
        Site site = siteRepository.getSite(siteUpdateIconDto.getUuid());

        Document doc = Jsoup.connect(site.htmlUrl).get();
        Elements els = doc.select("link[href*='favi']");

        if (els.isEmpty()) {
            els = doc.select("link[rel='icon']");
        }

        els.stream().findFirst().ifPresent(element -> {
            String imageUrl = element.absUrl("href");

            try {
                byte[] imageBytes = urlService.getUrlResourceAllByte(imageUrl);
                String base64 = Base64.getEncoder().encodeToString(imageBytes);
                siteRepository.updateSiteIcon(siteUpdateIconDto.getUuid(), base64);
            } catch (IOException e) {
                throw new GaleWingsRuntimeException(e);
            }
        });
    }

}
