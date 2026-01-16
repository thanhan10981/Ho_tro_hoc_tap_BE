package com.hoctap.learningsupportapi.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class ThuUtils {

    public static LocalDate nextDateFromThu(String thuRaw) {

        if (thuRaw == null || thuRaw.isBlank()) {
            throw new RuntimeException("Thứ không được rỗng");
        }

        // Chuẩn hóa chuỗi
        String thu = thuRaw
                .trim()
                .toLowerCase()
                .replace("thứ", "thứ ")   // đảm bảo có khoảng trắng
                .replaceAll("\\s+", " ") // gom nhiều space thành 1
                .trim();

        DayOfWeek day = switch (thu) {

            case "thứ 2", "t2", "2" -> DayOfWeek.MONDAY;
            case "thứ 3", "t3", "3" -> DayOfWeek.TUESDAY;
            case "thứ 4", "t4", "4" -> DayOfWeek.WEDNESDAY;
            case "thứ 5", "t5", "5" -> DayOfWeek.THURSDAY;
            case "thứ 6", "t6", "6" -> DayOfWeek.FRIDAY;
            case "thứ 7", "t7", "7" -> DayOfWeek.SATURDAY;
            case "chủ nhật", "cn", "chunhat" -> DayOfWeek.SUNDAY;

            default -> throw new RuntimeException("Không nhận diện được thứ: " + thuRaw);
        };

        LocalDate now = LocalDate.now();

        return now.with(day);
    }
}
