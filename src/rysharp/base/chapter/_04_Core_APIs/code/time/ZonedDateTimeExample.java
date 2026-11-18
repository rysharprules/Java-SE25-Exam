package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;

public class ZonedDateTimeExample {
    public static void main(String[] args) {
        ZoneId london = ZoneId.of("Europe/London");
        ZoneId paris = ZoneId.of("Europe/Paris");
        ZoneOffset fixed = ZoneOffset.ofHours(18); // range is -18 to 18
        System.out.println("Fixed offset: " + fixed); // +18:00

        LocalDateTime local = LocalDateTime.of(2025, 7, 15, 14, 30);
        ZonedDateTime londonTime = local.atZone(london);
        var londonTime2 = ZonedDateTime.of(local, fixed);
        System.out.println(londonTime2); // 2025-07-15T14:30+18:00
        System.out.println(londonTime); // 2025-07-15T14:30+01:00[Europe/London]
        System.out.println(londonTime.withZoneSameInstant(paris)); // 15:30 in Paris
        System.out.println(londonTime.withZoneSameLocal(paris));   // 14:30 in Paris
        System.out.println(londonTime.toInstant()); // 2025-07-15T13:30:00Z

        // London skips 01:00-01:59 on 30 March 2025.
        LocalDateTime nonexistent = LocalDateTime.of(2025, 3, 30, 1, 30);
        System.out.println(nonexistent.atZone(london)); // 2025-03-30T02:30+01:00[Europe/London]

        OffsetDateTime offsetDateTime = local.atOffset(fixed);
        System.out.println(offsetDateTime); // 2025-07-15T14:30+18:00
    }
}
