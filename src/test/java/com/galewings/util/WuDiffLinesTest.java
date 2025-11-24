package com.galewings.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class WuDiffLinesTest {

    @Test
    void testDiffCompletelyDifferent() {
        List<WuDiffLines.Diff> result = WuDiffLines.diff(new String[]{"a"}, new String[]{"b"});

        assertEquals(2, result.size());
        assertEquals(WuDiffLines.EditType.DELETE, result.getFirst().type);
        assertEquals(WuDiffLines.EditType.INSERT, result.get(1).type);
    }

    @Test
    void testDiffException() {
        try {
            WuDiffLines.diff(new String[]{}, new String[]{});
            fail();
        } catch (RuntimeException e) {
            // 正常終了
        }
    }

    @Test
    void testDiffExactMatch() {
        List<WuDiffLines.Diff> result = WuDiffLines.diff(new String[]{"line1", "line2"}, new String[]{"line1", "line2"});
        result.stream().forEach(diff -> {
            if (diff.type != WuDiffLines.EditType.MATCH) {
                fail(diff.toString());
            }
        });
    }

    @Test
    void testDiffInsertionOnly() {
        List<WuDiffLines.Diff> result = WuDiffLines.diff(new String[]{"line1"}, new String[]{"line1", "line2"});
        assertEquals(2, result.size());
        assertEquals(WuDiffLines.EditType.MATCH, result.getFirst().type);
        assertEquals(WuDiffLines.EditType.INSERT, result.get(1).type);

    }

    @Test
    void testDiffDeleteOnly() {
        List<WuDiffLines.Diff> result = WuDiffLines.diff(new String[]{"line1", "line2"}, new String[]{"line1"});
        assertEquals(2, result.size());
        assertEquals(WuDiffLines.EditType.MATCH, result.getFirst().type);
        assertEquals(WuDiffLines.EditType.DELETE, result.get(1).type);

    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme