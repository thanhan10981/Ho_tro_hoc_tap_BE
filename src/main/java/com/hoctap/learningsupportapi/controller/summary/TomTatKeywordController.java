package com.hoctap.learningsupportapi.controller.summary;

import com.hoctap.learningsupportapi.service.summary.TomTatKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tom-tat")
@RequiredArgsConstructor
public class TomTatKeywordController {

    private final TomTatKeywordService keywordService;

    @GetMapping("/{maTomTat}/keywords")
    public List<String> getKeywords(@PathVariable Integer maTomTat) {
        return keywordService.getKeywords(maTomTat);
    }
}
