package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.ModelMock;
import com.galewings.dto.input.ViewSaveDto;
import com.galewings.entity.Site;
import com.galewings.entity.View;
import com.galewings.repository.SiteRepository;
import com.galewings.repository.ViewsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.Mockito.*;

class ViewsControllerTest {
    @Mock
    ViewsRepository viewsRepository;
    @Mock
    SiteRepository siteRepository;
    @InjectMocks
    ViewsController viewsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() throws JsonProcessingException {
        when(viewsRepository.idList()).thenReturn(List.of(new View()));
        when(viewsRepository.selectSiteList(anyString())).thenReturn(List.of(new Site()));
        when(siteRepository.getAllSite()).thenReturn(List.of(new Site()));

        Model model = new ModelMock();

        String result = viewsController.index(model);
        Assertions.assertEquals("views", result);
    }

    @Test
    void testList() {
        List<View> viewList = List.of(new View());
        when(viewsRepository.idList()).thenReturn(viewList);

        List<View> result = viewsController.list();
        Assertions.assertEquals(viewList, result);
    }

    @Test
    void testInfo() {
        View view = new View();
        when(viewsRepository.info(anyString())).thenReturn(view);

        View result = viewsController.info("id");
        Assertions.assertEquals(view, result);
    }

    @Test
    void testSave_insert() {
        doNothing().when(viewsRepository).insertView(any());
        doNothing().when(viewsRepository).insertViewSite(any(), anyString());

        ViewSaveDto dto = new ViewSaveDto();
        dto.siteIdList = List.of("");
        viewsController.save(dto);
        verify(viewsRepository).insertView(any(ViewSaveDto.class));
        verify(viewsRepository).insertViewSite(any(ViewSaveDto.class), anyString());
    }

    @Test
    void testSave_update() {
        doNothing().when(viewsRepository).updateView(any());
        doNothing().when(viewsRepository).updateViewSite(any(), anyString());

        ViewSaveDto dto = new ViewSaveDto();
        dto.viewId = "#";
        dto.siteIdList = List.of("");
        viewsController.save(dto);
        verify(viewsRepository).updateView(any(ViewSaveDto.class));
        verify(viewsRepository).updateViewSite(any(ViewSaveDto.class), anyString());
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme