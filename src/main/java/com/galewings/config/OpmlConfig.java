package com.galewings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class OpmlConfig {

  @Bean
  public TemplateEngine opmlTemplateEngine() {
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    TemplateEngine engine = new TemplateEngine();
    resolver.setTemplateMode(TemplateMode.TEXT);
    resolver.setPrefix("templates/");
    resolver.setSuffix(".opml");
    engine.setTemplateResolver(resolver);
    return engine;
  }

}
