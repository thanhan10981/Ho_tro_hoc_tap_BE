package com.hoctap.learningsupportapi.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dbjqkhq2u");
        config.put("api_key", "828546537827878");
        config.put("api_secret", "NSr1gzbbOWEU_rMIcNyifQX2uu4");
        return new Cloudinary(config);
    }
}
