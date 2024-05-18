package com.galewings.repository;

import com.galewings.dto.input.ViewSaveDto;
import com.galewings.entity.Site;
import com.galewings.entity.View;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.SqlResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ViewsRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    ViewsRepository viewsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIdList() {
        List<Object> viewList = List.of(new View());
        when(sqlManager.getResultList(any(), any())).thenReturn(viewList);

        List<View> result = viewsRepository.idList();
        Assertions.assertEquals(viewList, result);
    }

    @Test
    void testInfo() {
        Object view = new View();
        when(sqlManager.getSingleResult(any(), any(), any(Map.class))).thenReturn(view);

        View result = viewsRepository.info("id");
        Assertions.assertEquals(view, result);
    }

    @Test
    void testInsert() {
        when(sqlManager.insertEntity(any())).thenReturn(0);

        int result = viewsRepository.insert(new View());
        Assertions.assertEquals(0, result);
    }

    @Test
    void testInsertView() {
        when(sqlManager.executeUpdate(any(SqlResource.class), any())).thenReturn(0);

        viewsRepository.insertView(new ViewSaveDto());
    }

    @Test
    void testInsertViewSite() {
        when(sqlManager.executeUpdate(any(SqlResource.class), any())).thenReturn(0);

        viewsRepository.insertViewSite(new ViewSaveDto(), "siteUuid");
    }

    @Test
    void testUpdateView() {
        when(sqlManager.executeUpdate(any(SqlResource.class), any(Object.class))).thenReturn(0);

        viewsRepository.updateView(new ViewSaveDto());
    }

    @Test
    void testUpdateViewSite() {
        when(sqlManager.executeUpdate(any(SqlResource.class), any(Object.class))).thenReturn(0);

        viewsRepository.updateViewSite(new ViewSaveDto(), "siteUuid");
    }

    @Test
    void testSelectSiteList() {
        List<Object> siteList = List.of(new Site());
        when(sqlManager.getResultList(any(), any(), any())).thenReturn(siteList);

        List<Site> result = viewsRepository.selectSiteList("viewId");
        Assertions.assertEquals(siteList, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme