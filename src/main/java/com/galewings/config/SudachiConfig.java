package com.galewings.config;

import com.worksap.nlp.sudachi.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class SudachiConfig {

    @Bean(destroyMethod = "close")
    public Dictionary sudachiDictionary() throws IOException {
        Path directory = new ClassPathResource("sudachi/")
                .getFile()
                .toPath()
                .toAbsolutePath();

        Path settingPath = directory.resolve("sudachi.json");
        String settingJson = Files.readString(settingPath);
        PathAnchor anchor = PathAnchor.classpath();
        anchor = PathAnchor.filesystem(Paths.get(directory.toString())).andThen(anchor);
        Config config = Config.fromJsonString(settingJson, anchor);

        return new DictionaryFactory().create(config);
    }

    @Bean
    public Tokenizer sudachiTokenizer(
            Dictionary sudachiDictionary) {
        return sudachiDictionary.create();
    }
}
