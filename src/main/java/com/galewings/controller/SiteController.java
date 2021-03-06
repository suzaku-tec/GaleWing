package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.input.SiteDeleteDto;
import com.galewings.entity.Site;
import com.galewings.repository.SiteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SiteController {

  @Autowired
  SiteRepository siteRepository;

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
}
