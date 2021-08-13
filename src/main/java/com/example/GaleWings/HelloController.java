package com.example.GaleWings;

import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    SqlManager sqlManager;

    @RequestMapping(value = "/")
    @Transactional
    public String index() {
//        Session session = SessionFactory.getSession();
//        SqlManager sqlManager = session.getSqlManager();
//        session.begin();
        List<Map> resultList = sqlManager.getResultList(Map.class, new ClasspathSqlResource("sample.sql"));
        System.out.println(resultList);

//        session.commit();

        return "index";
    }
}
