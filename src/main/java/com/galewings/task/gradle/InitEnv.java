package com.galewings.task.gradle;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InitEnv {

    private static final String ENV_FILE = ".env";

    private static final Set<String> ENV_KEYS = Stream.of(
            "minhon.client-id"
            , "minhon.client-secret"
            , "minhon.name"
            , "google.ai.api-key"
            , "wordNews.api-key"
            , "newsapi.api-key"
            , "newsapi.url"
    ).collect(Collectors.toSet());

    public static void main(String[] args) throws IOException {
        Path envPath = Paths.get(ENV_FILE);

        Properties property = new Properties();
        if (Files.exists(envPath)) {
            property.load(new FileInputStream(envPath.toFile()));
        }
        ENV_KEYS.stream().filter(key -> !property.containsKey(key)).forEach(key -> property.put(key,
                ""));

        System.out.println(envPath.toAbsolutePath());

        property.list(System.out);

        try (var out = Files.newOutputStream(envPath)) {
            property.store(out, "");
        }

    }
}
