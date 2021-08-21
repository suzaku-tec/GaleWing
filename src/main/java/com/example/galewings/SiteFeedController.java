package com.example.galewings;

import com.example.galewings.entity.Feed;
import com.example.galewings.entity.Site;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SiteFeedController {

    @Autowired
    SqlManager sqlManager;

    @GetMapping("/sitelist")
    @ResponseBody
    @Transactional
    public String getSiteList() throws JsonProcessingException {
        List<Site> resultList = sqlManager.getResultList(Site.class, new ClasspathSqlResource("sql/select_all_site.sql"));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(resultList);
    }

    @GetMapping("/feedlist")
    @ResponseBody
    @Transactional
    public String getFeedList(@RequestParam(value = "uuid", required = false) String uuid) throws JsonProcessingException {

        System.out.println("uuid:" + uuid);
        List<Feed> feeds;
        if (Strings.isNullOrEmpty(uuid)) {
            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_all_feed.sql"));
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("uuid", uuid);
            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_site_for_uuid.sql"), params);
        }

        System.out.println(feeds);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(feeds);
    }
}
