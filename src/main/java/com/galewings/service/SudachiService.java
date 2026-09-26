package com.galewings.service;

import com.worksap.nlp.sudachi.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SudachiService {

    private final Tokenizer tokenizer;

    public SudachiService() throws IOException {
        ClassPathResource resource =
                new ClassPathResource("sudachi/system_core.dic");

        String baseDir = System.getProperty("app.data.dir");
        if (baseDir == null || baseDir.isBlank()) {
            // デフォルト：ユーザーホーム配下にアプリ専用ディレクトリ
            baseDir = System.getProperty("user.home") + "/.galewings/sudachi";
        }
        Path dicDir = Paths.get(baseDir, "dic");
        Files.createDirectories(dicDir);
        Path dictionaryPath = dicDir.resolve("system_core.dic");

        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, dictionaryPath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        try (Dictionary dictionary = new DictionaryFactory()
                .create(Config.defaultConfig()
                        .systemDictionary(dictionaryPath))) {

            this.tokenizer = dictionary.create();
        }
    }

    public List<String> tokenize(String text) {
        return tokenizer.tokenize(Tokenizer.SplitMode.C, text)
                .stream()
                .map(Morpheme::surface)
                .toList();
    }
}
