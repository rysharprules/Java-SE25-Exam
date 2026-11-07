
package rysharp.exam.actionplan.enthuware;

import java.time.*;

public class DateTimeOffsetsDST {

    static final ZoneId NEW_YORK = ZoneId.of("America/New_York");
    static final ZoneId LONDON = ZoneId.of("Europe/London");

    public static void main(String[] args) {
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();
        Q6();
    }

    static void Q1() {
        // ME9 Q28: Same local time, different offsets.
        // June in New York uses -04:00 (DST), not -05:00.

        LocalDateTime ldt = LocalDateTime.of(2026, 6, 2, 6, 0);

        OffsetDateTime fixed = ldt.atOffset(ZoneOffset.ofHours(-5));
        ZonedDateTime zoned = ldt.atZone(NEW_YORK);

        System.out.println("\nQ1 - Mock question");
        System.out.println(fixed);             // 06:00-05:00
        System.out.println(zoned);             // 06:00-04:00[America/New_York]
        System.out.println(fixed.toInstant()); // 11:00 UTC
        System.out.println(zoned.toInstant()); // 10:00 UTC

        System.out.println(Duration.between(fixed, zoned)); // PT-1H
        System.out.println(Duration.between(zoned, fixed)); // PT1H
    }

    static void Q2() {
        // In January, New York uses -05:00.
        // Both representations now describe the same instant.

        LocalDateTime ldt = LocalDateTime.of(2026, 1, 2, 6, 0);

        OffsetDateTime fixed = ldt.atOffset(ZoneOffset.ofHours(-5));
        ZonedDateTime zoned = ldt.atZone(NEW_YORK);

        System.out.println("\nQ2 - Winter");
        System.out.println(fixed.getOffset()); // -05:00
        System.out.println(zoned.getOffset()); // -05:00
        System.out.println(Duration.between(fixed, zoned)); // PT0S

        // Note: Period.between() returns P0D when the two dates are identical.
    }

    static void Q3() {
        // Same instant, different local clock times.
        // withZoneSameInstant() preserves the instant.

        ZonedDateTime ny = ZonedDateTime.of(
                2026, 6, 2, 6, 0, 0, 0, NEW_YORK);

        ZonedDateTime london = ny.withZoneSameInstant(LONDON);

        System.out.println("\nQ3 - Same instant");
        System.out.println(ny);     // 06:00-04:00
        System.out.println(london); // 11:00+01:00
        System.out.println(Duration.between(ny, london)); // PT0S
    }

    static void Q4() {
        // UK clocks go forward on 29 March 2026.
        // Local times between 01:00 and 01:59 do not exist.

        LocalDateTime missing = LocalDateTime.of(
                2026, 3, 29, 1, 30);

        ZonedDateTime adjusted = missing.atZone(LONDON);

        System.out.println("\nQ4 - Spring DST gap");
        System.out.println(missing);  // 01:30
        System.out.println(adjusted); // 02:30+01:00

        // Java moves a nonexistent local time forward by the gap.
    }

    static void Q5() {
        // UK clocks go back on 25 October 2026.
        // 01:30 occurs TWICE, with two different offsets.

        LocalDateTime repeated = LocalDateTime.of(
                2026, 10, 25, 1, 30);

        ZonedDateTime earlier = repeated.atZone(LONDON);
        ZonedDateTime later = earlier.withLaterOffsetAtOverlap();

        System.out.println("\nQ5 - Autumn DST overlap");
        System.out.println(earlier); // 01:30+01:00
        System.out.println(later);   // 01:30Z

        System.out.println(earlier.toInstant()); // 00:30 UTC
        System.out.println(later.toInstant());   // 01:30 UTC

        System.out.println(Duration.between(earlier, later)); // PT1H
    }

    static void Q6() {
        // Adding a calendar day is not always adding 24 hours.
        // UK clocks go forward between these two local times.

        ZonedDateTime start = ZonedDateTime.of(
                2026, 3, 28, 12, 0, 0, 0, LONDON);

        ZonedDateTime plusDay = start.plusDays(1);
        ZonedDateTime plus24Hours = start.plusHours(24);

        System.out.println("\nQ6 - Calendar day vs 24 hours");
        System.out.println(start);       // 28 Mar 12:00Z
        System.out.println(plusDay);     // 29 Mar 12:00+01:00
        System.out.println(plus24Hours); // 29 Mar 13:00+01:00

        System.out.println(Duration.between(start, plusDay));     // PT23H
        System.out.println(Duration.between(start, plus24Hours)); // PT24H
    }
}
