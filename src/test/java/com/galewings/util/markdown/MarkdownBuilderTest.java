package com.galewings.util.markdown;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class MarkdownBuilderTest {

    @Test
    void createReturnsNewInstance() {
        MarkdownBuilder builder1 = MarkdownBuilder.create();
        MarkdownBuilder builder2 = MarkdownBuilder.create();
        assertNotSame(builder1, builder2);
    }

    @Test
    void linkGeneratesCorrectMarkdownWithAltTitle() {
        MarkdownBuilder builder = MarkdownBuilder.create();
        String result = builder.link("Alt", "Title", "http://example.com").build();
        assertEquals("[Alt](http://example.com Title)", result);
    }

    @Test
    void linkGeneratesCorrectMarkdownWithoutAltTitle() {
        MarkdownBuilder builder = MarkdownBuilder.create();
        String result = builder.link("Title", "http://example.com").build();
        assertEquals("[Title](http://example.com)", result);
    }

    @Test
    void newlineAppendsSystemLineSeparator() {
        MarkdownBuilder builder = MarkdownBuilder.create();
        String result = builder.newline().build();
        assertEquals(System.lineSeparator(), result);
    }

    @Test
    void listAppendsListMarker() {
        MarkdownBuilder builder = MarkdownBuilder.create();
        String result = builder.list().build();
        assertEquals("- ", result);
    }

    @Test
    void lineBreaksCollectorJoinsStringsWithTwoSpaces() {
        String result = List.of("line1", "line2", "line3").stream()
                .collect(MarkdownBuilder.lineBreaks());
        assertEquals("line1  line2  line3", result);
    }

    @Test
    void paragraphCollectorJoinsStringsWithDoubleLineSeparators() {
        String result = List.of("para1", "para2", "para3").stream()
                .collect(MarkdownBuilder.paragraph());
        assertEquals("para1" + System.lineSeparator() + System.lineSeparator() +
                "para2" + System.lineSeparator() + System.lineSeparator() +
                "para3", result);
    }
}