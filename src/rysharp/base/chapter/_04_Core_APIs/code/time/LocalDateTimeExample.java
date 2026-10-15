package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;

public class LocalDateTimeExample {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2025, 3, 15);
        LocalDate date2 = LocalDate.of(2025, Month.MARCH, 15);
        LocalTime time = LocalTime.of(14, 30);

        LocalDateTime a = LocalDateTime.of(2025, 3, 15, 14, 30);
        LocalDateTime a2 = LocalDateTime.of(2025, Month.FEBRUARY, 15, 14, 30);
        LocalDateTime b = LocalDateTime.of(date, time);
        LocalDateTime c = date.atTime(time);
        LocalDateTime d = time.atDate(date);
        System.out.println(a); // 2025-03-15T14:30
        System.out.println(a.equals(b) && b.equals(c) && c.equals(d)); // true
        System.out.println(a.toLocalDate()); // 2025-03-15
        System.out.println(a.toLocalTime()); // 14:30
        System.out.println(date.atStartOfDay()); // 2025-03-15T00:00
        System.out.println(a.plusDays(1).plusHours(2)); // 2025-03-16T16:30
        // A LocalDateTime has no time zone or UTC offset.
    }
}
