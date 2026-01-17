package com.hoctap.learningsupportapi.service;

import org.springframework.stereotype.Service;
@Service
public class IntentService {

    public String detectIntent(String message) {
        message = message.toLowerCase();

        if (containsAll(message, "tuần", "thi", "kiểm tra"))
            return "EXAM_WEEK";

        if (containsAll(message, "tuần", "deadline"))
            return "DEADLINE_WEEK";

        if (containsAll(message, "tuần", "học"))
            return "CLASS_WEEK";

        if (message.contains("sự kiện") || message.contains("lịch"))
            return "UPCOMING_EVENTS";

        return "UNKNOWN";
    }

    private boolean containsAll(String msg, String... keys) {
        for (String k : keys) {
            if (!msg.contains(k)) return false;
        }
        return true;
    }
}
