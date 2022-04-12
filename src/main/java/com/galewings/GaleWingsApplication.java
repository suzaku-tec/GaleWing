package com.galewings;

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
    SpringApplication.run(GaleWingsApplication.class, args);
  }

}
