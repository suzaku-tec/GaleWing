package com.example.galewings;

import com.example.galewings.entity.Site;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    SqlManager sqlManager;

    @RequestMapping(value = "/")
    @Transactional
    public String index(Model model, @RequestParam(name = "siteUrl", required = false) String siteUrl) throws JsonProcessingException {
        List<Site> resultList = sqlManager.getResultList(Site.class, new ClasspathSqlResource("sql/select_all_site.sql"));
        model.addAttribute("sitelist", resultList);
        return "index";
    }

}
