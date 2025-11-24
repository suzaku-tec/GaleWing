package com.galewings.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class WuDiffLinesTest {

    @Test
    void testDiff() {
        List<WuDiffLines.Diff> result = WuDiffLines.diff(new String[]{"a"}, new String[]{"b"});
        assertEquals(2, result.size());
    }
    
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme