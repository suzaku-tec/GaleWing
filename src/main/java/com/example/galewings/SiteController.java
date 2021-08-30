package com.example.galewings;

import com.example.galewings.entity.Site;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class SiteController {

  @Autowired
  SqlManager sqlManager;

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
    List<Site> resultList = sqlManager.getResultList(Site.class,
        new ClasspathSqlResource("sql/site/select_all_site.sql"));
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(resultList);
  }

}
