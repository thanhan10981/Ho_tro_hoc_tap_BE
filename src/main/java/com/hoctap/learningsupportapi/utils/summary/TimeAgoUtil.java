package com.hoctap.learningsupportapi.utils.summary;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeAgoUtil {

    public static String format(LocalDateTime time) {
        if (time == null) return "";

        LocalDateTime now = LocalDateTime.now();

        long seconds = ChronoUnit.SECONDS.between(time, now);
        if (seconds < 60) {
            return seconds + " giây trước";
        }

        long minutes = ChronoUnit.MINUTES.between(time, now);
        if (minutes < 60) {
            return minutes + " phút trước";
        }

        long hours = ChronoUnit.HOURS.between(time, now);
        if (hours < 24) {
            return hours + " giờ trước";
        }

        long days = ChronoUnit.DAYS.between(time, now);
        if (days < 7) {
            return days + " ngày trước";
        }

        if (days < 31) {
            return (days / 7) + " tuần trước";
        }

        if (days < 365) {
            return (days / 31) + " tháng trước";
        }

        return (days / 365) + " năm trước";
    }
}
