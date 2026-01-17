package com.hoctap.learningsupportapi.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LichHocTimeFormatter {

    public static String format(LocalDateTime time) {
        if (time == null) return "";

        LocalDate today = LocalDate.now();
        LocalDate date = time.toLocalDate();

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        if (date.isEqual(today)) {
            return "Hôm nay " + time.format(timeFmt);
        }

        if (date.isEqual(today.plusDays(1))) {
            return "Ngày mai " + time.format(timeFmt);
        }

        if (date.isBefore(today.plusDays(7))) {
            String thu = switch (date.getDayOfWeek()) {
                case MONDAY -> "Thứ 2";
                case TUESDAY -> "Thứ 3";
                case WEDNESDAY -> "Thứ 4";
                case THURSDAY -> "Thứ 5";
                case FRIDAY -> "Thứ 6";
                case SATURDAY -> "Thứ 7";
                case SUNDAY -> "Chủ nhật";
            };

            return thu + " " + time.format(timeFmt);
        }

        return time.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }
}
