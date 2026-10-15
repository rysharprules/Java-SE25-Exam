package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeFormatterExample {
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 15, 16, 45);
        DateTimeFormatter correct = DateTimeFormatter.ofPattern("dd MMM uuuu HH:mm", Locale.UK);
        DateTimeFormatter wrong = DateTimeFormatter.ofPattern("dd MMM uuuu HH:MM", Locale.UK);
        System.out.println(dateTime.format(correct)); // 15 Mar 2025 16:45
        System.out.println(dateTime.format(wrong));   // 15 Mar 2025 16:03

        LocalDate date = LocalDate.of(2025, 3, 15);
        System.out.println(date.format(DateTimeFormatter.ofPattern("D")));  // 74: day of year
        System.out.println(date.format(DateTimeFormatter.ofPattern("dd"))); // 15: day of month
        System.out.println(date.format(DateTimeFormatter.ofPattern("EEE", Locale.FRENCH))); // sam.
        System.out.println(date.format(DateTimeFormatter.ofPattern("EEE", Locale.UK))); // Sat

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        System.out.println(LocalDate.parse("15/03/2025", parser)); // 2025-03-15
        System.out.println(date.format(DateTimeFormatter.ISO_DATE)); // 2025-03-15

        try {
            System.out.println(date.format(DateTimeFormatter.ofPattern("HH:mm")));
        } catch (DateTimeException e) {
            System.out.println("No time fields on LocalDate: " + e.getClass().getSimpleName());
        }
    }
}
/*
Essential pattern letters
Symbol	Meaning	Example
u	Proleptic year	2025
y	Year of era	2025
M	Month number	3
MM	Two-digit month	03
MMM	Abbreviated month	Mar
MMMM	Full month	March
d	Day of month	5
dd	Two-digit day	05
D	Day of year	74
E	Day-of-week text	Sat
H	Hour, 0–23	16
h	Hour, 1–12	4
m	Minute	30
s	Second	45
a	AM/PM	PM
S	Fraction of second	1
X	ISO offset	+01 or Z
z	Time-zone name	BST
 */