package com.galewings.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.Executor;

class AsyncConfigurationTest {
    AsyncConfiguration asyncConfiguration = new AsyncConfiguration();

    @Test
    void testTaskExecutor() {
        Executor result = asyncConfiguration.taskExecutor();
        Assertions.assertNotNull(result);
    }

    @Test
    void testTaskTagAnalysExecutor() {
        TaskExecutor result = asyncConfiguration.taskTagAnalysExecutor();
        Assertions.assertNotNull(result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme