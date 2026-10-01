package rysharp.jdk25.addendum.Minor_Api_Additions.code;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class InstantUntilExample {

    public static void main(String[] args) {

        Instant start = Instant.parse("2025-06-01T10:00:00Z");
        Instant end = Instant.parse("2025-06-01T11:45:30Z");

        /*
         * Java 23 added Instant.until(Instant), which returns a Duration
         * representing the directed amount of time between the two Instants.
         */
        Duration duration = start.until(end);

        System.out.println(duration); // PT1H45M30S

        /*
         * Do not confuse the new overload with the older until() method
         * that accepts a TemporalUnit.
         *
         * It returns a long containing the number of COMPLETE units.
         * The extra 45 minutes and 30 seconds are therefore discarded
         * when HOURS is requested.
         */
        System.out.println(start.until(end, ChronoUnit.HOURS));   // 1
        System.out.println(start.until(end, ChronoUnit.MINUTES)); // 105

        /*
         * Direction matters.
         *
         * end -> start travels backwards along the timeline, so the
         * result is negative.
         */
        System.out.println(end.until(start, ChronoUnit.MINUTES)); // -105

        /*
         * This would compile because MONTHS is a TemporalUnit:
         *
         * start.until(end, ChronoUnit.MONTHS);
         *
         * However, it throws UnsupportedTemporalTypeException at runtime
         * because Instant does not support calendar-based months.
         */
    }
}