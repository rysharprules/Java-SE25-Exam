package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;

public class YearMonthDayExample {
    public static void main(String[] args) {
        Year year = Year.of(2024);
        System.out.println(year.isLeap()); // true
        System.out.println(year.atDay(60)); // 2024-02-29
        System.out.println(year.atMonth(3)); // 2024-03 (YearMonth)

        YearMonth ym = YearMonth.of(2024, Month.FEBRUARY);
        System.out.println(ym.lengthOfMonth()); // 29
        System.out.println(ym.atDay(29));       // 2024-02-29
        System.out.println(ym.atEndOfMonth());  // 2024-02-29

        System.out.println(MonthDay.now());       // --10-15
        MonthDay md = MonthDay.of(Month.FEBRUARY, 29);
        System.out.println(md);                   // --02-29
        System.out.println(md.isValidYear(2025)); // false
        System.out.println(md.atYear(2025));      // 2025-02-28

        System.out.println(Month.of(3));         // MARCH
        System.out.println(Month.MARCH.getValue()); // 3
        System.out.println(DayOfWeek.of(1));     // MONDAY
        System.out.println(DayOfWeek.of(7));     // SUNDAY

        try {
            YearMonth.of(2025, 2).atDay(29);
        } catch (DateTimeException e) {
            System.out.println("Invalid date: " + e.getClass().getSimpleName());
        }
        // No LocalDate.ofMonthYear() or YearMonth.ofMonthYear() methods exist.
    }
}
