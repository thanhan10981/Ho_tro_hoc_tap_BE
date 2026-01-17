package com.hoctap.learningsupportapi;

import com.hoctap.learningsupportapi.config.TestGeminiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest
@ActiveProfiles("test")
@Import(TestGeminiConfig.class)
class LearningSupportApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
