package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;

public class LocalDateExample {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2025, Month.MARCH, 15);
        System.out.println("Date: " + date);                     // 2025-03-15
        System.out.println("Day of year: " + date.getDayOfYear()); // 74
        System.out.println("Weekday: " + date.getDayOfWeek());     // SATURDAY

        System.out.println(LocalDate.ofYearDay(2025, 74)); // 2025-03-15
        System.out.println(LocalDate.ofYearDay(2024, 60)); // 2024-02-29
        System.out.println(LocalDate.ofEpochDay(0));      // 1970-01-01
        System.out.println(LocalDate.ofEpochDay(-1));     // 1969-12-31

        LocalDate jan31 = LocalDate.of(2025, 1, 31);
        System.out.println(jan31.plusMonths(1)); // 2025-02-28
        System.out.println(jan31);               // unchanged: immutable

        try {
            System.out.println(LocalDate.ofYearDay(2025, 366));
        } catch (DateTimeException e) {
            System.out.println("Invalid day of year: " + e.getClass().getSimpleName());
        }
    }
}
