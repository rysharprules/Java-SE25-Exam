# Date & Time API

Quick reference for the Java date/time APIs and common exam traps.

```java
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
```

## Contents

- [Core API at a Glance](#core-api-at-a-glance)
- [LocalDate](#localdate)
- [LocalTime](#localtime)
- [LocalDateTime](#localdatetime)
- [ZoneId and ZonedDateTime](#zoneid-and-zoneddatetime)
- [Period](#period)
- [Duration](#duration)
- [Period vs Duration](#period-vs-duration)
- [Instant](#instant)
- [ChronoUnit](#chronounit)
- [DateTimeFormatter](#datetimeformatter)
- [SimpleDateFormat](#simpledateformat)
- [Immutability](#immutability)
- [Quick Reference](#quick-reference)

---

## Core API at a Glance

| Type | Represents | Typical Creation |
|---|---|---|
| `LocalDate` | Date, no time/zone | `LocalDate.of(2026, 10, 4)` |
| `LocalTime` | Time, no date/zone | `LocalTime.of(14, 30)` |
| `LocalDateTime` | Date + time, no zone | `LocalDateTime.of(2026, 10, 4, 14, 30)` |
| `ZonedDateTime` | Date + time + zone | `ZonedDateTime.of(date, time, zone)` |
| `ZoneId` | Time-zone identifier | `ZoneId.of("Europe/London")` |
| `Instant` | Point on UTC timeline | `Instant.now()` |
| `Period` | Date-based amount | `Period.ofDays(3)` |
| `Duration` | Time-based amount | `Duration.ofHours(3)` |
| `DateTimeFormatter` | Format/parse date/time | `DateTimeFormatter.ofPattern("dd/MM/yyyy")` |
| `ChronoUnit` | Standard temporal unit | `ChronoUnit.DAYS.between(a, b)` |

Core distinction:

```text
LocalDate      → DATE
LocalTime      → TIME
LocalDateTime  → DATE + TIME
ZonedDateTime  → DATE + TIME + ZONE
Instant        → point on UTC timeline

Period         → DATE/calendar amount
Duration       → TIME/elapsed amount
```

---

## `LocalDate`

Represents a date without a time or zone.

### Creation

```java
var date = LocalDate.of(2026, Month.OCTOBER, 4);
var date2 = LocalDate.of(2026, 10, 4);
var today = LocalDate.now();
```

### Common Operations

```java
date.plusDays(2);
date.plusWeeks(1);
date.plusMonths(1);
date.plusYears(1);

date.minusDays(2);

date.getYear();
date.getMonth();
date.getMonthValue();
date.getDayOfMonth();
date.getDayOfWeek();

date.isBefore(other);
date.isAfter(other);
```

Remember: these operations return a new object.

---

## `LocalTime`

Represents a time without a date or zone.

### Creation

```java
var time = LocalTime.of(14, 30);
var time2 = LocalTime.of(14, 30, 45);
var now = LocalTime.now();
```

### Common Operations

```java
time.plusHours(2);
time.plusMinutes(30);
time.minusSeconds(10);

time.getHour();
time.getMinute();
time.getSecond();
```

### Valid Values

```java
LocalTime.of(23, 59);   // OK
LocalTime.of(24, 0);    // DateTimeException
```

---

## `LocalDateTime`

Represents a date and time, but **no time zone**.

### Creation

```java
var dt = LocalDateTime.of(
        2026, 10, 4,
        14, 30
);
```

Or combine existing values:

```java
LocalDate date = LocalDate.of(2026, 10, 4);
LocalTime time = LocalTime.of(14, 30);

LocalDateTime dt = LocalDateTime.of(date, time);
```

It supports both date and time operations:

```java
dt.plusDays(1);
dt.plusHours(2);
```

Remember:

> **LocalDateTime has NO TIME ZONE.**

---

## `ZoneId` and `ZonedDateTime`

`ZoneId` identifies a time zone:

```java
ZoneId zone = ZoneId.of("Europe/London");
```

`ZonedDateTime` combines date, time and zone:

```java
ZonedDateTime zdt = ZonedDateTime.of(
        LocalDate.of(2026, 10, 4),
        LocalTime.of(14, 30),
        zone
);
```

Or:

```java
ZonedDateTime.now(zone);
```

Think:

```text
LocalDateTime → date + time
ZonedDateTime → date + time + WHERE
```

### Daylight Saving Time

`ZonedDateTime` understands DST transitions.

Date/calendar arithmetic and exact elapsed-time arithmetic can therefore behave differently when clocks change.

Memory:

> **If `ZonedDateTime` crosses a DST transition, don't blindly perform ordinary clock arithmetic.**

Check the zone and whether the operation is date-based or time-based.

---

## `Period`

Represents a **date-based** amount using years, months and days.

### Creation

```java
Period.ofYears(2);
Period.ofMonths(3);
Period.ofWeeks(2);
Period.ofDays(10);

Period.of(1, 2, 3);   // 1 year, 2 months, 3 days
```

### `Period.between()`

Normally used with `LocalDate`:

```java
LocalDate a = LocalDate.of(2025, 1, 1);
LocalDate b = LocalDate.of(2026, 1, 1);

Period p = Period.between(a, b);
```

Result:

```text
P1Y
```

### ISO Representation

```text
P1Y       → 1 year
P2M       → 2 months
P3D       → 3 days
P1Y2M3D   → 1 year, 2 months, 3 days
```

---

## `Duration`

Represents a **time-based / elapsed-time** amount.

### Creation

```java
Duration.ofDays(2);
Duration.ofHours(6);
Duration.ofMinutes(30);
Duration.ofSeconds(10);
```

`Duration.ofDays()` treats a day as exactly 24 hours.

```java
Duration.ofDays(2)
```

is 48 hours and displays as:

```text
PT48H
```

not:

```text
P2D
```

### `Duration.between()`

Works naturally with time-based temporals:

```java
LocalTime a = LocalTime.of(10, 0);
LocalTime b = LocalTime.of(12, 30);

Duration d = Duration.between(a, b);
```

Result:

```text
PT2H30M
```

### ISO Representation

```text
PT6H      → 6 hours
PT30M     → 30 minutes
PT45S     → 45 seconds
PT2H30M   → 2 hours, 30 minutes
```

### Unsupported Temporal Trap

This compiles:

```java
LocalDate a = LocalDate.of(2025, 1, 1);
LocalDate b = LocalDate.of(2025, 1, 2);

Duration d = Duration.between(a, b);
```

`LocalDate` implements `Temporal`, so the method signature is valid.

However, `Duration` requires time-based information such as seconds, which `LocalDate` does not support.

Runtime result:

```text
UnsupportedTemporalTypeException
```

Use:

```java
Period.between(date1, date2);
```

for date-based differences.

Memory:

> **Method signature compatibility does not guarantee that the Temporal supports the required unit.**

### Arithmetic

Useful methods:

```java
duration.plus(...)
duration.minus(...)

duration.multipliedBy(...)
duration.dividedBy(...)

duration.negated()
duration.abs()
```

For example:

```java
Duration d = Duration.ofHours(12);

d.dividedBy(2);   // PT6H
```

All return another `Duration`.

---

## Period vs Duration

| | `Period` | `Duration` |
|---|---|---|
| Based on | Date/calendar | Time/elapsed |
| Years | ✅ | ❌ |
| Months | ✅ | ❌ |
| Days | ✅ | Exact 24-hour units |
| Hours | ❌ | ✅ |
| Minutes | ❌ | ✅ |
| Seconds | ❌ | ✅ |
| Typical `between()` | `LocalDate` | `LocalTime`, `LocalDateTime`, `Instant` |

Memory:

```text
Period   → calendar/date amount
Duration → elapsed/time amount
```

---

## `Instant`

Represents a point on the UTC timeline.

### Creation

```java
Instant now = Instant.now();
```

Typical representation:

```text
2026-10-04T13:00:00Z
```

`Z` means UTC.

### Common Operations

```java
instant.plusSeconds(10);
instant.minusSeconds(10);

instant.isBefore(other);
instant.isAfter(other);
```

`Instant` works naturally with `Duration`:

```java
Duration d = Duration.between(start, end);
```

### Java 25 Addition

The newer:

```java
Duration Instant.until(Instant endExclusive)
```

is covered separately in the [Minor API Additions](../../../addendum/Minor_Api_Additions/README.md).

---

## `ChronoUnit`

Represents standard temporal units.

Common values:

```java
ChronoUnit.DAYS
ChronoUnit.HOURS
ChronoUnit.MINUTES
ChronoUnit.SECONDS
ChronoUnit.MONTHS
ChronoUnit.YEARS
```

### `between()`

```java
long days = ChronoUnit.DAYS.between(date1, date2);
```

Unlike `Period.between()` or `Duration.between()`, this returns a numeric count:

```text
Period / Duration       → amount object
ChronoUnit.X.between()  → long
```

### Adding Units

```java
date.plus(3, ChronoUnit.DAYS);
```

### Unsupported Units

Not every temporal supports every unit.

Valid:

```java
instant.plus(1, ChronoUnit.HOURS);
```

But calendar concepts such as months are not supported by `Instant`.

A call can therefore:

```text
COMPILE
```

but fail at runtime with:

```text
UnsupportedTemporalTypeException
```

Always distinguish:

```text
Does the method call compile?
          ↓
Does this temporal actually support the requested unit?
```

---

## `DateTimeFormatter`

Used to format and parse the modern `java.time` classes.

### Creation

Predefined:

```java
DateTimeFormatter.ISO_LOCAL_DATE
DateTimeFormatter.ISO_LOCAL_TIME
DateTimeFormatter.ISO_LOCAL_DATE_TIME
DateTimeFormatter.ISO_ZONED_DATE_TIME
DateTimeFormatter.ISO_INSTANT
DateTimeFormatter.ISO_WEEK_DATE
```

Custom:

```java
DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
```

### Formatting

Both forms are valid:

```java
String result = date.format(formatter);
```

```java
String result = formatter.format(date);
```

### Parsing

```java
LocalDate date =
        LocalDate.parse("04/10/2026", formatter);
```

### Formatting Symbols

| Symbol | Meaning | Example |
|---|---|---|
| `y` | Year | `2026` |
| `M` | Month | `10` |
| `MM` | 2-digit month | `10` |
| `MMM` | Short month | `Oct` |
| `MMMM` | Full month | `October` |
| `d` | Day | `4` |
| `dd` | 2-digit day | `04` |
| `H` | Hour 0–23 | `14` |
| `h` | Hour 1–12 | `2` |
| `m` | Minute | `30` |
| `s` | Second | `45` |
| `a` | AM/PM | `PM` |

High-value distinctions:

```text
M       → MONTH
m       → MINUTE

MMM     → Oct
MMMM    → October

d       → 4
dd      → 04

H       → 0–23
h       → 1–12
```

### Formatter Must Match the Temporal

A formatter can request information that a temporal doesn't contain:

```java
LocalDate date = LocalDate.now();

DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("HH:mm");

date.format(formatter);
```

This compiles, but `LocalDate` has no time fields.

The operation therefore fails at runtime.

Memory:

> **Compile-time compatibility does not mean the temporal contains the requested fields.**

---

## `SimpleDateFormat`

Legacy date/time formatter from:

```java
java.text
```

The important formatting distinctions remain:

```text
M       month
m       minute

MMM     Oct
MMMM    October

d       4
dd      04
```

Example:

```java
new SimpleDateFormat("MMMM dd");
```

Don't confuse:

```text
DateTimeFormatter  → modern java.time API
SimpleDateFormat   → legacy java.text API
```

---

## Immutability

The main `java.time` classes are immutable:

```text
LocalDate
LocalTime
LocalDateTime
ZonedDateTime
Instant
Period
Duration
```

Therefore:

```java
LocalDate date = LocalDate.of(2026, 10, 4);

date.plusDays(5);

System.out.println(date);   // 2026-10-04
```

The returned object was discarded.

Use:

```java
date = date.plusDays(5);
```

### `plus()` / `minus()` Compatibility

Typical combinations:

```java
date.plus(Period.ofDays(2));
time.plus(Duration.ofHours(2));

dateTime.plus(Period.ofDays(2));
dateTime.plus(Duration.ofHours(2));
```

Think about what information the temporal possesses:

```text
LocalDate → calendar date, no clock
LocalTime → clock time, no date
```

Do not assume every `TemporalAmount` works with every temporal type.

---

# Quick Reference

## Types

```text
LocalDate       → date
LocalTime       → time
LocalDateTime   → date + time
ZonedDateTime   → date + time + zone
Instant         → UTC timeline point

Period          → date/calendar amount
Duration        → time/elapsed amount
```

## Creation

```text
LocalDate.of(y, m, d)
LocalTime.of(h, m)
LocalDateTime.of(y, m, d, h, m)

ZoneId.of("Europe/London")
ZonedDateTime.of(date, time, zone)

Instant.now()

Period.ofYears/Months/Weeks/Days()
Duration.ofDays/Hours/Minutes/Seconds()

DateTimeFormatter.ofPattern(...)
```

## Between

```text
Period.between(date, date)          → Period
Duration.between(time, time)        → Duration
ChronoUnit.X.between(a, b)          → long
```

Watch:

```text
Duration.between(LocalDate, LocalDate)
→ compiles
→ UnsupportedTemporalTypeException at runtime
```

## Duration Operations

```text
plus()
minus()
multipliedBy()
dividedBy()
negated()
abs()
```

## Immutability

```text
date.plusDays(1);          → result discarded
date = date.plusDays(1);   → result retained
```

## Formatting

```text
M       month
m       minute

MMM     Oct
MMMM    October

d       4
dd      04

H       0–23 hour
h       1–12 hour

a       AM/PM
```

## Final Memory Kicks

> **Period = date/calendar; Duration = time/elapsed.**

> **Date/time objects are immutable — capture the result of `plus()`, `minus()`, etc.**

> **A temporal operation may compile but fail at runtime if the required field/unit isn't supported.**

> **`ChronoUnit.between()` returns a `long`; `Period.between()` and `Duration.between()` return amount objects.**

> **`M` = month, `m` = minute; `MMM` = short month, `MMMM` = full month.**

> **With `ZonedDateTime`, watch for DST before doing simple clock arithmetic.**