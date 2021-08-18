package com.example.galewings;

import com.example.galewings.entity.Feed;
import com.example.galewings.entity.Site;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    SqlManager sqlManager;

    @RequestMapping(value = "/")
    @Transactional
    public String index(Model model, @RequestParam(name = "siteUrl", required = false) String siteUrl) throws JsonProcessingException {
        List<Site> resultList = sqlManager.getResultList(Site.class, new ClasspathSqlResource("sql/select_all_site.sql"));
        model.addAttribute("sitelist", resultList);

        List<Feed> feeds;
        if (siteUrl == null) {
            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_all_feed.sql"));
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("htmlUrl", siteUrl);
            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_site_for_htmlurl.sql"));
        }

        model.addAttribute("feedList", feeds);
        System.out.println(feeds);

        return "index";
    }

    /**
     * テストデータの配列を返却する。
     */
    @RequestMapping(value = "getTestData", method = RequestMethod.GET)
    @ResponseBody
    public String[] getTestData() {
        return new String[]{"test1", "test2", "test3"};
    }
}
