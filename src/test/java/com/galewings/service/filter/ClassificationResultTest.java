package com.galewings.service.filter;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class ClassificationResultTest {

    @Test
    void from_score_null() {
        ClassificationResult result = ClassificationResult.from(null);

        assertEquals("other", result.primaryCategory);
        assertEquals(Set.of("other"), result.categories);
        assertTrue(result.scores.isEmpty());
        assertTrue(result.matchedRules.isEmpty());
        assertEquals(0.0, result.confidence, 0.0001);
    }

    @Test
    void from_score_empty() {
        ClassificationResult result = ClassificationResult.from(Collections.EMPTY_MAP);

        assertEquals("other", result.primaryCategory);
        assertEquals(Set.of("other"), result.categories);
        assertTrue(result.scores.isEmpty());
        assertTrue(result.matchedRules.isEmpty());
        assertEquals(0.0, result.confidence, 0.0001);
    }

    @Test
    void from_score_single() {
        ClassificationResult result = ClassificationResult.from(Collections.singletonMap("A", 1));

        assertEquals("A", result.primaryCategory);
        assertEquals(Set.of("other"), result.categories);
        assertEquals(1, result.scores.get("A").intValue());
        assertTrue(result.matchedRules.isEmpty());
        assertEquals(0.1, result.confidence, 0.0001);
    }

}
