package com.galewings;

import com.galewings.entity.SiteFeedCount;
import com.galewings.repository.SiteRepository;
import com.miragesql.miragesql.SqlManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

  @Autowired
  SqlManager sqlManager;

  @Autowired
  SiteRepository siteRepository;

  @RequestMapping(value = "/")
  @Transactional
  public String index(Model model, @RequestParam(name = "uuid", required = false) String uuid) {
    List<SiteFeedCount> resultList = siteRepository.getSiteFeedCount();
    model.addAttribute("sitelist", resultList);
    model.addAttribute("identifier", uuid);
    return "index";
  }

}
