package com.hoctap.learningsupportapi;

import com.hoctap.learningsupportapi.config.TestGeminiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestGeminiConfig.class)
@SpringBootTest
class LearningSupportApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
