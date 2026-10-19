
package rysharp.base.chapter._04_Core_APIs.code.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTimeFormatterExample {

    public static void main(String[] args) {
        example1_patternLetters();
        example2_datePatterns();
        example3_timePatterns();
        example4_isoFormatters();
        example5_offsetsAndZones();
        example6_localizedFormats();
        example7_parsing();
        example8_examTraps();
    }

    // 1. Pattern letters: case matters!
    private static void example1_patternLetters() {
        System.out.println("\n1. Pattern letters");

        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 15, 16, 45);

        DateTimeFormatter correct =
                DateTimeFormatter.ofPattern("dd MMM uuuu HH:mm", Locale.UK);

        DateTimeFormatter wrong =
                DateTimeFormatter.ofPattern("dd MMM uuuu HH:MM", Locale.UK);

        System.out.println(dateTime.format(correct)); // 15 Mar 2025 16:45
        System.out.println(dateTime.format(wrong));   // 15 Mar 2025 16:03

        // m = minute
        // M = month
        // H = hour (0-23)
        // h = hour (1-12)
        // D = day of year
        // d = day of month
        // u = proleptic year
        // y = year of era
        // Y = week-based year (NOT calendar year)
    }

    // 2. Common date patterns
    private static void example2_datePatterns() {
        System.out.println("\n2. Date patterns");

        LocalDate date = LocalDate.of(2025, 3, 15);

        System.out.println(date.format(DateTimeFormatter.ofPattern("d")));
        // 15

        System.out.println(date.format(DateTimeFormatter.ofPattern("dd")));
        // 15

        System.out.println(date.format(DateTimeFormatter.ofPattern("M")));
        // 3

        System.out.println(date.format(DateTimeFormatter.ofPattern("MM")));
        // 03

        System.out.println(date.format(DateTimeFormatter.ofPattern("MMM", Locale.UK)));
        // Mar

        System.out.println(date.format(DateTimeFormatter.ofPattern("MMMM", Locale.UK)));
        // March

        System.out.println(date.format(DateTimeFormatter.ofPattern("MMMM")));
        // March

        System.out.println(date.format(DateTimeFormatter.ofPattern("MMMM", Locale.FRANCE)));
        // mars

        System.out.println(date.format(DateTimeFormatter.ofPattern("D")));
        // 74 (day of year)

        System.out.println(date.format(DateTimeFormatter.ofPattern("EEE", Locale.UK))); // E, EE, EEE are all Sat
        // Sat

        System.out.println(date.format(DateTimeFormatter.ofPattern("EEEE", Locale.UK)));
        // Saturday

        System.out.println(date.format(DateTimeFormatter.ofPattern("EEE", Locale.FRENCH)));
        // sam.

        System.out.println(date.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
        // 15/03/2025

        System.out.println(date.format(DateTimeFormatter.ofPattern("uuuu-MM-dd")));
        // 2025-03-15

        // Literal text must be enclosed in single quotes.
        System.out.println(date.format(
                DateTimeFormatter.ofPattern("'Date:' dd MMM uuuu", Locale.UK)));
        // Date: 15 Mar 2025
    }

    // 3. Common time patterns
    private static void example3_timePatterns() {
        System.out.println("\n3. Time patterns");

        LocalTime time = LocalTime.of(16, 45, 9, 123_456_789);

        System.out.println(time.format(DateTimeFormatter.ofPattern("HH:mm")));
        // 16:45

        System.out.println(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        // 16:45:09

        System.out.println(time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.UK)));
        // 04:45 pm (AM/PM text depends on locale data)

        System.out.println(time.format(DateTimeFormatter.ofPattern("H")));
        // 16

        System.out.println(time.format(DateTimeFormatter.ofPattern("h")));
        // 4

        System.out.println(time.format(DateTimeFormatter.ofPattern("SSS")));
        // 123 (milliseconds represented as fraction of second)

        System.out.println(time.format(DateTimeFormatter.ofPattern("SSSSSS")));
        // 123456

        System.out.println(time.format(DateTimeFormatter.ofPattern("n")));
        // 123456789 (nano-of-second)

        // S = fraction of second, with the number of S letters
        // controlling the number of digits displayed.
    }

    // 4. Predefined ISO formatters - important for the exam
    private static void example4_isoFormatters() {
        System.out.println("\n4. ISO formatters");

        LocalDate date = LocalDate.of(2025, 3, 15);
        LocalTime time = LocalTime.of(16, 45, 30);
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        OffsetDateTime offsetDateTime = OffsetDateTime.of(
                dateTime, ZoneOffset.ofHours(1));

        ZonedDateTime zonedDateTime = ZonedDateTime.of(
                dateTime, ZoneId.of("Europe/London"));

        System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        // 2025-03-15

        System.out.println(time.format(DateTimeFormatter.ISO_LOCAL_TIME));
        // 16:45:30

        System.out.println(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // 2025-03-15T16:45:30

        System.out.println(date.format(DateTimeFormatter.ISO_DATE));
        // 2025-03-15

        System.out.println(offsetDateTime.format(DateTimeFormatter.ISO_DATE));
        // 2025-03-15+01:00

        System.out.println(offsetDateTime.format(DateTimeFormatter.ISO_TIME));
        // 16:45:30+01:00

        System.out.println(offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
        // 2025-03-15T16:45:30+01:00

        System.out.println(offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE));
        // 2025-03-15+01:00

        System.out.println(offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_TIME));
        // 16:45:30+01:00

        System.out.println(offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        // 2025-03-15T16:45:30+01:00

        System.out.println(zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        // 2025-03-15T16:45:30Z[Europe/London]
        // London is on GMT in March before the DST change.

        System.out.println(date.format(DateTimeFormatter.ISO_ORDINAL_DATE));
        // 2025-074

        System.out.println(date.format(DateTimeFormatter.ISO_WEEK_DATE));
        // 2025-W11-6

        Instant instant = Instant.parse("2025-03-15T16:45:30Z");

        System.out.println(DateTimeFormatter.ISO_INSTANT.format(instant));
        // 2025-03-15T16:45:30Z

        System.out.println(DateTimeFormatter.BASIC_ISO_DATE.format(date));
        // 20250315

        // ISO_LOCAL_*  -> no offset or zone
        // ISO_OFFSET_* -> offset required
        // ISO_ZONED_DATE_TIME -> zone-aware format
        // ISO_DATE/TIME/DATE_TIME -> offset is optional
        // ISO_INSTANT -> UTC instant representation
        // ISO_ORDINAL_DATE -> year and day-of-year
        // ISO_WEEK_DATE -> week-based year, week and day
        // BASIC_ISO_DATE -> compact yyyyMMdd-style output
    }

    // 5. Offsets versus time zones
    private static void example5_offsetsAndZones() {
        System.out.println("\n5. Offsets and zones");

        OffsetDateTime offset = OffsetDateTime.of(
                2025, 7, 15, 16, 45, 0, 0, ZoneOffset.ofHours(1));

        ZonedDateTime zoned = ZonedDateTime.of(
                2025, 7, 15, 16, 45, 0, 0, ZoneId.of("Europe/London"));

        System.out.println(offset.format(DateTimeFormatter.ofPattern("XXX")));
        // +01:00

        System.out.println(offset.format(DateTimeFormatter.ofPattern("XX")));
        // +0100

        System.out.println(offset.format(DateTimeFormatter.ofPattern("X")));
        // +01

        System.out.println(offset.format(DateTimeFormatter.ofPattern("Z")));
        // +0100

        System.out.println(zoned.format(DateTimeFormatter.ofPattern("VV")));
        // Europe/London

        System.out.println(zoned.format(DateTimeFormatter.ofPattern("z", Locale.UK)));
        // BST (time-zone name depends on locale data)

        System.out.println(zoned.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        // 2025-07-15T16:45:00+01:00[Europe/London]
        // Note: ISO formatter normally omits zero seconds:
        // 2025-07-15T16:45:00+01:00[Europe/London]
        // Here the formatter's actual output is:
        // 2025-07-15T16:45:00+01:00[Europe/London]
        // if seconds are explicitly retained by the formatter;
        // ISO_LOCAL_TIME generally omits zero seconds.

        // Offset = fixed difference from UTC (+01:00).
        // Zone = named region with daylight-saving rules.
    }

    // 6. Localized formatting
    private static void example6_localizedFormats() {
        System.out.println("\n6. Localized formats");

        LocalDate date = LocalDate.of(2025, 3, 15);
        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 15, 16, 45);

        System.out.println(date.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        .withLocale(Locale.UK)));
        // 15/03/2025 (locale-data dependent)

        System.out.println(date.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                        .withLocale(Locale.UK)));
        // 15 March 2025 (locale-data dependent)

        System.out.println(dateTime.format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        .withLocale(Locale.US)));
        // Output depends on locale data.

        // FormatStyle: FULL, LONG, MEDIUM, SHORT.
        // Locale affects text and localized formatting conventions.
        // Locale does NOT change the underlying date/time value.
    }

    // 7. Parsing with built-in and custom formatters
    private static void example7_parsing() {
        System.out.println("\n7. Parsing");

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");

        System.out.println(LocalDate.parse("15/03/2025", parser));
        // 2025-03-15

        System.out.println(LocalDate.parse("2025-03-15"));
        // 2025-03-15 (default ISO_LOCAL_DATE)

        System.out.println(LocalDateTime.parse("2025-03-15T16:45:30"));
        // 2025-03-15T16:45:30

        System.out.println(OffsetDateTime.parse("2025-03-15T16:45:30+01:00"));
        // 2025-03-15T16:45:30+01:00

        System.out.println(Instant.parse("2025-03-15T16:45:30Z"));
        // 2025-03-15T16:45:30Z

        try {
            LocalDate.parse("15/03/2025");
        } catch (DateTimeParseException e) {
            System.out.println("Default parser expects ISO date format");
        }

        // Formatting converts temporal object -> String.
        // Parsing converts String -> temporal object.
    }

    // 8. Certification traps
    private static void example8_examTraps() {
        System.out.println("\n8. Exam traps");

        LocalDate date = LocalDate.of(2025, 3, 15);

        try {
            System.out.println(date.format(DateTimeFormatter.ofPattern("HH:mm")));
        } catch (DateTimeException e) {
            System.out.println("No time fields on LocalDate: "
                    + e.getClass().getSimpleName());
        }

        // Formatting a LocalDate with time fields fails at runtime.
        // Formatting a LocalTime with date fields also fails.

        LocalDate endOfYear = LocalDate.of(2021, 12, 31);

        System.out.println(endOfYear.format(
                DateTimeFormatter.ofPattern("uuuu-MM-dd")));
        // 2021-12-31

        System.out.println(endOfYear.format(
                DateTimeFormatter.ofPattern("YYYY-MM-dd", Locale.US)));
        // 2022-12-31
        // Y = week-based year, not calendar year!
        // Week rules are locale-sensitive.

        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 15, 16, 45);

        try {
            System.out.println(dateTime.format(
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        } catch (DateTimeException e) {
            System.out.println("LocalDateTime has no offset");
        }

        // LocalDateTime has no ZoneOffset or ZoneId.
        // ISO_OFFSET_DATE_TIME requires an offset.

        // Case-sensitive pattern traps:
        // MM = month; mm = minute.
        // DD = padded day-of-year; dd = day-of-month.
        // HH = 24-hour; hh = 12-hour.
        // YYYY = week-based year; uuuu = proleptic year.
    }
}

/*
ESSENTIAL PATTERN LETTERS

Symbol   Meaning                  Example
--------------------------------------------------
u        Proleptic year           2025
y        Year of era              2025
Y        Week-based year          2025/2026
M        Month number/text        3 / 03 / Mar / March
d        Day of month             15
D        Day of year              74
E        Day-of-week text         Sat / Saturday
e        Localized day-of-week    Locale-dependent
H        Hour (0-23)              16
h        Hour (1-12)              4
m        Minute                   45
s        Second                   09
a        AM/PM                    pm
S        Fraction of second       123
n        Nano-of-second           123456789
X        ISO offset               +01 / +0100 / +01:00 / Z
x        Offset (zero not Z)      +00 / +0000 / +00:00
Z        RFC-style offset         +0100
z        Time-zone name           BST
V        Zone ID (use VV)         Europe/London

PREDEFINED FORMATTERS

ISO_LOCAL_DATE          2025-03-15
ISO_LOCAL_TIME          16:45:30
ISO_LOCAL_DATE_TIME     2025-03-15T16:45:30
ISO_DATE                2025-03-15[+01:00]
ISO_TIME                16:45:30[+01:00]
ISO_DATE_TIME           2025-03-15T16:45:30[+01:00]
ISO_OFFSET_DATE         2025-03-15+01:00
ISO_OFFSET_TIME         16:45:30+01:00
ISO_OFFSET_DATE_TIME    2025-03-15T16:45:30+01:00
ISO_ZONED_DATE_TIME     2025-03-15T16:45:30+01:00[Europe/London]
ISO_INSTANT             2025-03-15T16:45:30Z
ISO_ORDINAL_DATE        2025-074
ISO_WEEK_DATE           2025-W11-6
BASIC_ISO_DATE          20250315

Square brackets in this reference indicate optional components,
not literal brackets in the output (except zone IDs).
*/
