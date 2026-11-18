package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;

public class PeriodDurationExample {
    public static void main(String[] args) {
        Period period = Period.of(1, 2, 3);
        Period period2 = Period.ofDays(26);
        Period period3 = Period.ofWeeks(26);
        Period period4 = Period.ofYears(26);
        Period period5 = Period.ofMonths(26);

        Duration duration = Duration.ofHours(2).plusMinutes(30);
        // all take long as arg
        Duration duration2 = Duration.ofSeconds(10000L);
        Duration duration3 = Duration.ofHours(10000);
        Duration duration4 = Duration.ofDays((short)500);
        Duration duration5 = Duration.ofMillis(10000L);
        Duration duration6 = Duration.ofNanos(10000L);
        Duration duration7 = Duration.ofMinutes(10000L);

        System.out.println(period);                  // P1Y2M3D
        System.out.println(period2);                  // P26D
        System.out.println(period3);                  // P182D
        System.out.println(period4);                  // P26Y
        System.out.println(period5);                  // P26M

        System.out.println(duration);                // PT2H30M
        System.out.println(duration2);                // PT2H46M40S
        System.out.println(duration3);                // PT10000H
        System.out.println(duration4);                // PT12000H
        System.out.println(duration5);                // PT10S
        System.out.println(duration6);                // PT0.00001S
        System.out.println(duration7);                // PT166H40M

        System.out.println(Period.ZERO.getUnits());  // [Years, Months, Days]
        System.out.println(Duration.ZERO.getUnits()); // [Seconds, Nanos]

        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 3, 15);
        System.out.println(Period.between(start, end)); // P2M14D
        System.out.println(start.plus(Period.ofDays(2))); // 2025-01-03
        start.plusWeeks(7).plusMonths(7).minusYears(2);

        LocalTime t1 = LocalTime.of(10, 0);
        LocalTime t2 = LocalTime.of(12, 30);
        System.out.println(Duration.between(t1, t2)); // PT2H30M

        try {
            System.out.println(Duration.between(start, end));
        } catch (DateTimeException e) {
            System.out.println("Dates cannot supply seconds: " + e.getClass().getSimpleName());
        }

        // normalization
        System.out.println("normalization");
        System.out.println(Period.of(1, 13, 1));
        // P1Y13M1D

        System.out.println(Period.of(1, 13, 1).normalized());
        // P2Y1M1D

        System.out.println(Period.ofMonths(26).normalized());
        // P2Y2M

        // normalized() converts excess months into years, but does not convert days into months, because calendar months have different lengths.
        System.out.println(Period.ofDays(40).normalized());
        // P40D
    }
}
