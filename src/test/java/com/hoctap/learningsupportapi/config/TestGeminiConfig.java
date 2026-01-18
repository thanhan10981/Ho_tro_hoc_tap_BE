package com.hoctap.learningsupportapi.config;

import com.google.genai.Client;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestGeminiConfig {

    @Bean
    public Client geminiClient() {
        return Mockito.mock(Client.class);
    }
}