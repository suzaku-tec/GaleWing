package com.galewings.service;

import com.galewings.entity.Feed;
import com.galewings.entity.FunctionCtrl;
import com.galewings.repository.FunctionCtrlRepository;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.mockito.Mockito.when;

class FeedFactoryServiceTest {

    @InjectMocks
    FeedFactoryService feedFactoryService;

    @Mock
    FunctionCtrlRepository functionCtrlRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        FunctionCtrl functionCtrl = createFunctionCtrlMock("feed-img", "1");
        when(functionCtrlRepository.get("feed-img")).thenReturn(functionCtrl);

        Feed result = feedFactoryService.create(new SyndEntryImpl(), "uuid");
        Feed expected = new Feed();
        expected.uuid = "uuid";
        expected.author = "";

        Assertions.assertEquals(expected.title, result.title);
        Assertions.assertEquals(expected.uuid, result.uuid);
        Assertions.assertEquals(expected.link, result.link);
        Assertions.assertEquals(expected.uri, result.uri);
        Assertions.assertEquals(expected.author, result.author);
        Assertions.assertEquals(expected.comments, result.comments);
        Assertions.assertEquals(expected.publishedDate, result.publishedDate);
        Assertions.assertEquals(expected.opened, result.opened);
        Assertions.assertEquals(expected.readed, result.readed);
    }

    @Test
    void testCreate2() throws ParseException {
        SyndEntryImpl syndEntry = new SyndEntryImpl();

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        syndEntry.setPublishedDate(sdFormat.parse("2000-01-01 00:00:00"));

        List<SyndContent> listContent = List.of(createSyndContentTestData("text/html", "<img src='a'>"),
                createSyndContentTestData("html", "<img src='a'>"), createSyndContentTestData("", ""));
        syndEntry.setContents(listContent);

        FunctionCtrl functionCtrl = createFunctionCtrlMock("feed-img", "1");
        when(functionCtrlRepository.get("feed-img")).thenReturn(functionCtrl);

        Feed result = feedFactoryService.create(syndEntry, "uuid");
        Feed expected = new Feed();
        expected.uuid = "uuid";
        expected.author = "";
        expected.publishedDate = "2000-01-01 12:00:00";

        Assertions.assertEquals(expected.title, result.title);
        Assertions.assertEquals(expected.uuid, result.uuid);
        Assertions.assertEquals(expected.link, result.link);
        Assertions.assertEquals(expected.uri, result.uri);
        Assertions.assertEquals(expected.author, result.author);
        Assertions.assertEquals(expected.comments, result.comments);
        Assertions.assertEquals(expected.publishedDate, result.publishedDate);
        Assertions.assertEquals(expected.opened, result.opened);
        Assertions.assertEquals(expected.readed, result.readed);
    }

    @Test
    void testCreate3() throws ParseException {
        SyndEntryImpl syndEntry = new SyndEntryImpl();

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        syndEntry.setPublishedDate(sdFormat.parse("2000-01-01 00:00:00"));

        List<SyndContent> listContent = List.of(
                createSyndContentTestData("", ""),
                createSyndContentTestData("html", "<img src='a'>"),
                createSyndContentTestData("text/html", "<img src='a'>")
        );
        syndEntry.setContents(listContent);

        FunctionCtrl functionCtrl = createFunctionCtrlMock("feed-img", "1");
        when(functionCtrlRepository.get("feed-img")).thenReturn(functionCtrl);

        Feed result = feedFactoryService.create(syndEntry, "uuid");
        Feed expected = new Feed();
        expected.uuid = "uuid";
        expected.author = "";
        expected.publishedDate = "2000-01-01 12:00:00";

        Assertions.assertEquals(expected.title, result.title);
        Assertions.assertEquals(expected.uuid, result.uuid);
        Assertions.assertEquals(expected.link, result.link);
        Assertions.assertEquals(expected.uri, result.uri);
        Assertions.assertEquals(expected.author, result.author);
        Assertions.assertEquals(expected.comments, result.comments);
        Assertions.assertEquals(expected.publishedDate, result.publishedDate);
        Assertions.assertEquals(expected.opened, result.opened);
        Assertions.assertEquals(expected.readed, result.readed);
    }

    private SyndContent createSyndContentTestData(String type, String value) {
        SyndContent content = new SyndContentImpl();
        content.setType(type);
        content.setValue(value);
        return content;
    }

    private FunctionCtrl createFunctionCtrlMock(String id, String flg) {
        FunctionCtrl functionCtrl = new FunctionCtrl();
        functionCtrl.id = id;
        functionCtrl.flg = flg;

        return functionCtrl;
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme