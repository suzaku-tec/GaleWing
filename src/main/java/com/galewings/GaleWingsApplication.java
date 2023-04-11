package com.galewings;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * GaleWingsApplication起動クラス
 */
@SpringBootApplication
@EnableScheduling
public class GaleWingsApplication {

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();
    dotenv.entries()
        .forEach(dotenvEntry -> System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue()));
    SpringApplication.run(GaleWingsApplication.class, args);
  }

}
