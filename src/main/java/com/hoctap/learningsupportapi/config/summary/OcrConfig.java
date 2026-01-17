package com.hoctap.learningsupportapi.config.summary;



import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OcrConfig {

    @Value("${tesseract.datapath}")
    private String datapath;

    @Value("${tesseract.language}")
    private String language;

    @Bean
    public Tesseract tesseract() {
        Tesseract t = new Tesseract();
        t.setDatapath(datapath);
        t.setLanguage(language);
        return t;
    }
}
