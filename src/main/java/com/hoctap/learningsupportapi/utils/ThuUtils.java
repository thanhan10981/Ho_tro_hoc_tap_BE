package com.hoctap.learningsupportapi.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class ThuUtils {

    public static LocalDate nextDateFromThu(String thu) {
        DayOfWeek day = switch (thu.trim()) {
            case "Thứ 2" -> DayOfWeek.MONDAY;
            case "Thứ 3" -> DayOfWeek.TUESDAY;
            case "Thứ 4" -> DayOfWeek.WEDNESDAY;
            case "Thứ 5" -> DayOfWeek.THURSDAY;
            case "Thứ 6" -> DayOfWeek.FRIDAY;
            case "Thứ 7" -> DayOfWeek.SATURDAY;
            case "Chủ nhật" -> DayOfWeek.SUNDAY;
            default -> throw new RuntimeException("Không nhận diện được thứ: " + thu);
        };

        LocalDate now = LocalDate.now();
        return now.with(day);
    }
}
