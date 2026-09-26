package com.galewings.config;

import com.worksap.nlp.sudachi.Dictionary;
import com.worksap.nlp.sudachi.DictionaryFactory;
import com.worksap.nlp.sudachi.Tokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class SudachiConfig {

    @Bean(destroyMethod = "close")
    public Dictionary sudachiDictionary() throws IOException {
        Path directory = new ClassPathResource("sudachi/")
                .getFile()
                .toPath()
                .toAbsolutePath();

        Path settingPath = directory.resolve("sudachi.json");
        Path dictionaryPath = directory.resolve("system_core.dic");

        System.out.println("Sudachi directory = " + directory);
        System.out.println("sudachi.json = " + settingPath);
        System.out.println("sudachi.json exists = " + Files.exists(settingPath));
        System.out.println("system_core.dic = " + dictionaryPath);
        System.out.println("system_core.dic exists = " + Files.exists(dictionaryPath));
        System.out.println("system_core.dic size = " + Files.size(dictionaryPath));

        String settingJson = Files.readString(settingPath);

        System.out.println("Sudachi settings = " + settingJson);

        return new DictionaryFactory().create(
                directory.toString(),
                settingJson
        );
    }

    @Bean
    public Tokenizer sudachiTokenizer(
            Dictionary sudachiDictionary) {
        return sudachiDictionary.create();
    }
}
