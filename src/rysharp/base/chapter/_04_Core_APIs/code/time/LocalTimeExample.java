package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;

public class LocalTimeExample {
    public static void main(String[] args) {
        LocalTime time = LocalTime.of(14, 30, 45);
        System.out.println(time);                         // 14:30:45
        System.out.println(time.getHour());                // 14
        System.out.println(time.getMinute());              // 30
        System.out.println(LocalTime.MIDNIGHT);            // 00:00
        System.out.println(LocalTime.NOON);                // 12:00
        System.out.println(LocalTime.ofSecondOfDay(3600)); // 01:00
        System.out.println(time.plusHours(12));            // 02:30:45 (wraps)

        try {
            LocalTime.of(24, 0);
        } catch (DateTimeException e) {
            System.out.println("Invalid hour: " + e.getClass().getSimpleName());
        }
    }
}
