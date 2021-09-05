package com.galewings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.entity.Settings;
import com.galewings.entity.SettingsClass;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @Autowired
    SqlManager sqlManager;

    @GetMapping("/settings")
    @Transactional
    public String index(Model model) throws JsonProcessingException {
        List<Settings> settingsList = sqlManager.getResultList(Settings.class,
            new ClasspathSqlResource("sql/select_setting.sql"));
        model.addAttribute("settings", settingsList);

        return "settings";
    }

    /**
     * @param id
     * @return
     */
    public List<SettingsClass> getChildSetting(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        List<SettingsClass> childList = sqlManager.getResultList(SettingsClass.class,
            new ClasspathSqlResource("sql/select_setting_child.sql"), params);
        childList.forEach(settings -> {
            settings.settings = getChildSetting(settings.id);
        });
        return childList;
    }
}
