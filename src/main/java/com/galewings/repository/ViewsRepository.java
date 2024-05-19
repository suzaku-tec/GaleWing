package com.galewings.repository;

import com.galewings.dto.input.ViewSaveDto;
import com.galewings.entity.Site;
import com.galewings.entity.View;
import com.galewings.entity.ViewFeedCount;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Transactional
public class ViewsRepository {

    @Autowired
    private SqlManager sqlManager;

    public List<View> idList() {
        return sqlManager.getResultList(View.class, new ClasspathSqlResource("sql/views/select_all.sql"));
    }

    public View info(String id) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        return sqlManager.getSingleResult(View.class, new ClasspathSqlResource("sql/views/select_one_row.sql"), param);
    }

    public int insert(View view) {
        return sqlManager.insertEntity(view);
    }

    public void insertView(ViewSaveDto viewSaveDto) {
        Map<String, String> param = new HashMap<>();
        param.put("id", viewSaveDto.viewId);
        param.put("viewName", viewSaveDto.viewName);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/views/insert_views.sql"), param);
    }

    public void insertViewSite(ViewSaveDto viewSaveDto, String siteUuid) {
        Map<String, String> param = new HashMap<>();
        param.put("viewsId", viewSaveDto.viewId);
        param.put("siteUuid", siteUuid);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/views/insert_views_site.sql"), param);
    }

    public void updateView(ViewSaveDto viewSaveDto) {
        Map<String, String> param = new HashMap<>();
        param.put("id", UUID.randomUUID().toString());
        param.put("viewName", viewSaveDto.viewName);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/views/update_views.sql"), param);
    }

    public void updateViewSite(ViewSaveDto viewSaveDto, String siteUuid) {
        Map<String, String> param = new HashMap<>();
        param.put("viewsId", viewSaveDto.viewId);
        param.put("siteUuid", siteUuid);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/views/update_views_site.sql"), param);
    }

    public List<Site> selectSiteList(String viewId) {
        Map<String, String> param = new HashMap<>();
        param.put("viewsId", viewId);
        return sqlManager.getResultList(Site.class, new ClasspathSqlResource("sql/views/select_site_list.sql"), param);
    }

    public List<ViewFeedCount> getViewFeedCount() {
        return sqlManager.getResultList(ViewFeedCount.class
                , new ClasspathSqlResource("sql/views/select_all_views_count_feed.sql"));
    }
}
