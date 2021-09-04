package com.example.galewings.config;

import com.miragesql.miragesql.SqlManagerImpl;
import com.miragesql.miragesql.dialect.SQLiteDialect;
import com.miragesql.miragesql.integration.spring.SpringConnectionProvider;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@SuppressWarnings("unused")
@Configuration
public class AppConfig {

  @Bean(destroyMethod = "close")
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.sqlite.JDBC");
    dataSource.setUrl("jdbc:sqlite:library.db");
    dataSource.setUsername("");
    dataSource.setPassword("");
    return dataSource;
  }

  @Bean
  public DataSourceTransactionManager transactionManager() {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
    dataSourceTransactionManager.setDataSource(dataSource());
    return dataSourceTransactionManager;
  }

  @Bean
  public SpringConnectionProvider connectionProvider() {
    SpringConnectionProvider springConnectionProvider = new SpringConnectionProvider();
    springConnectionProvider.setTransactionManager(transactionManager());
    return springConnectionProvider;
  }

  @Bean
  public SQLiteDialect dialect() {
    return new SQLiteDialect();
  }

  @Bean
  public SqlManagerImpl sqlManager() {
    SqlManagerImpl sqlManager = new SqlManagerImpl();
    sqlManager.setConnectionProvider(connectionProvider());
    sqlManager.setDialect(dialect());
    return sqlManager;
  }

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
