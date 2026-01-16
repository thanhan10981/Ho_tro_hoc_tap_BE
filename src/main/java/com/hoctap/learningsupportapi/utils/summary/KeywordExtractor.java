package com.hoctap.learningsupportapi.utils.summary;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KeywordExtractor {

    private static final Set<String> STOP_WORDS = Set.of(
            "là", "và", "của", "cho", "trong", "một", "những", "các",
            "được", "với", "khi", "này", "đó", "từ", "theo"
    );

    public List<String> extract(String text, int limit) {
        if (text == null || text.isBlank()) return List.of();

        List<String> words = Arrays.stream(
                        text.toLowerCase()
                                .replaceAll("[^a-zà-ỹ\\s]", "")
                                .split("\\s+")
                )
                .filter(w -> w.length() > 2)
                .filter(w -> !STOP_WORDS.contains(w))
                .toList();

        Map<String, Integer> freq = new HashMap<>();

        for (int i = 0; i < words.size() - 1; i++) {
            String bi = words.get(i) + " " + words.get(i + 1);
            freq.merge(bi, 1, Integer::sum);

            if (i < words.size() - 2) {
                String tri = bi + " " + words.get(i + 2);
                freq.merge(tri, 1, Integer::sum);
            }
        }

        return freq.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }
}
