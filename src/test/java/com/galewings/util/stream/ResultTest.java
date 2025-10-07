package com.galewings.util.stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void runCatching_Success() {
        Result<String> result = Result.runCatching(() -> "success");
        assertTrue(result.isSuccess());
        assertEquals("success", result.getOrNull());
    }

    @Test
    void runCatching_Failure() {
        Result<String> result = Result.runCatching(() -> {
            throw new RuntimeException("error");
        });
        assertTrue(result.isFailure());
        assertEquals("error", result.getException().getMessage());
    }

    @Test
    void runCatchingVoid_Success() {
        Result<Void> result = Result.runCatching(() -> {
        });
        assertTrue(result.isSuccess());
        assertNull(result.getOrNull());
    }

    @Test
    void runCatchingVoid_Failure() {
        Result<Void> result = Result.runCatching((ThrowingRunnable) () -> {
            throw new Exception("fail");
        });
        assertTrue(result.isFailure());
        assertEquals("fail", result.getException().getMessage());
    }

    @Test
    void testOnScucess() {
        Result.runCatching(() -> "success")
                .onSuccess(result -> assertEquals("success", result))
                .onFailure(ex -> fail());
    }

    @Test
    void testOnFailure() {
        Result.runCatching(() -> {
                    throw new RuntimeException("error");
                })
                .onSuccess(result -> fail())
                .onFailure(ex -> assertEquals("error", ex.getMessage()));
    }

}