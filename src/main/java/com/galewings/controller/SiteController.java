package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.input.SiteDeleteDto;
import com.galewings.entity.Site;
import com.galewings.repository.SiteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
  @Transactional
  public String getSiteList() throws JsonProcessingException {
    List<Site> resultList = siteRepository.getAllSite();
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(resultList);
  }

  @GetMapping(value = "/site/management")
  @Transactional
  public String index(Model model) {
    List<Site> resultList = siteRepository.getAllSite();
    model.addAttribute("sitelist", resultList);

    return "/site/management";
  }

  @PostMapping(value = "/site/delete")
  @ResponseBody
  @Transactional
  public String delete(@RequestBody SiteDeleteDto dto) {
    boolean result = siteRepository.delete(dto.getUuid());
    return Boolean.toString(result);
  }

}
