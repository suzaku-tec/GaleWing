package com.galewings.service;

import com.galewings.util.OllamaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OllamaService implements TellMeAi<String> {

    private final OllamaClient ollamaClient;

    @Value("${ollama.model}")
    private String model;

    @Autowired
    public OllamaService(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    @Override
    public String tellMe(String text) {
        return this.ollamaClient.generate(text, model);
    }
}
