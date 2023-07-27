package com.galewings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * OPML設定
 */
@Configuration
public class OpmlConfig {

    /**
     * OPMLテンプレートエンジン
     *
     * @return OPMLテンプレートエンジン
     */
    @Bean
    public SpringTemplateEngine opmlTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        SpringTemplateEngine engine = new SpringTemplateEngine();
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setPrefix("templates/");
        resolver.setSuffix(".opml");
        engine.setTemplateResolver(resolver);
        return engine;
    }

}
