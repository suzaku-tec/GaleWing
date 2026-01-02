package com.galewings.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;

class OpmlConfigTest {
    OpmlConfig opmlConfig = new OpmlConfig();

    @Test
    void testOpmlTemplateEngine() {
        TemplateEngine result = opmlConfig.opmlTemplateEngine();
        Assertions.assertNotNull(result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme