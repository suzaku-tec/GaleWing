package com.galewings.controller;

import com.galewings.entity.SiteFeedCount;
import com.galewings.repository.SiteRepository;
import com.miragesql.miragesql.SqlManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GaleWingsController {

  @Autowired
  SqlManager sqlManager;

  @Autowired
  SiteRepository siteRepository;

  @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
  @Transactional
  public String index(Model model, @RequestParam(name = "uuid", required = false) String uuid) {
    List<SiteFeedCount> resultList = siteRepository.getSiteFeedCount();
    model.addAttribute("sitelist", resultList);
    model.addAttribute("identifier", uuid);
    return "index";
  }

}
