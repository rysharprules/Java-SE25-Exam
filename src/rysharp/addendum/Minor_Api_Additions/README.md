# Minor API Additions

[🔙 Back](../../../../README.md)

Quick reference for smaller API additions and behavioural changes introduced after JDK 21 that may be relevant to Java 25.

Major features such as **Module Import Declarations**, **Compact Source Files and Instance Main Methods**, **Flexible Constructor Bodies**, **Scoped Values**, **Stream Gatherers**, and **Unnamed Variables and Patterns** are covered in separate guides.

## Contents

- [`Instant.until(Instant)`](#instantuntilinstant)
- [`Console` Locale Formatting](#console-locale-formatting)
- [`Reader.of(CharSequence)`](#readerofcharsequence)
- [`CharSequence.getChars()`](#charsequencegetchars)
- [`CharBuffer.getChars()`](#charbuffergetchars)
- [`ForkJoinPool` Scheduling and Timeouts](#forkjoinpool-scheduling-and-timeouts)
- [`CompletableFuture` Common-Pool Change](#completablefuture-common-pool-change)
- [Virtual Threads and `synchronized`](#virtual-threads-and-synchronized)
- [Quick Reference](#quick-reference)

---

## `Instant.until(Instant)`

Java 23 adds:

```java
Duration until(Instant endExclusive)
```

Example:

```java
Instant start = Instant.parse("2025-06-01T10:00:00Z");
Instant end   = Instant.parse("2025-06-01T11:45:30Z");

Duration duration = start.until(end);
```

This returns the directed `Duration` from `start` to `end`.

### Existing `until()` Overload

Do not confuse the new overload with the older:

```java
long until(Temporal endExclusive, TemporalUnit unit)
```

For example:

```java
start.until(end, ChronoUnit.HOURS);   // 1
start.until(end, ChronoUnit.MINUTES); // 105
end.until(start, ChronoUnit.MINUTES); // -105
```

The older overload:

```text
returns long
counts complete units
can return a negative value
truncates incomplete units
```

For a difference of 1 hour, 45 minutes and 30 seconds:

```java
start.until(end, ChronoUnit.HOURS); // 1
```

not:

```text
1.75
1.758...
2
```

Memory:

```text
start.until(end)
→ Duration

start.until(end, unit)
→ long
```

### Unsupported Units

This compiles:

```java
start.until(end, ChronoUnit.MONTHS);
```

but fails at runtime with:

```text
UnsupportedTemporalTypeException
```

`MONTHS` is a valid `TemporalUnit`, but `Instant` does not support calendar-based months.

Memory:

> **`Instant` represents points on the timeline, not calendar arithmetic.**

### `Duration` vs `Period`

```text
Duration → time-based amounts
           hours / minutes / seconds

Period   → date-based amounts
           years / months / days
```

For example:

```java
Duration.ofDays(1)
```

means exactly:

```text
24 hours
```

A `Period` of one calendar day is not necessarily 24 elapsed hours around daylight-saving changes.

For the wider date/time API, see **Date & Time API**.

---

## `Console` Locale Formatting

Java 23 adds locale-aware overloads including:

```java
format(Locale, String, Object...)
printf(Locale, String, Object...)
readLine(Locale, String, Object...)
readPassword(Locale, String, Object...)
```

For example:

```java
console.printf(
    Locale.US,
    "Value: %,.2f%n",
    1234.5
);

console.printf(
    Locale.GERMANY,
    "Value: %,.2f%n",
    1234.5
);
```

Conceptually:

```text
US:      Value: 1,234.50
Germany: Value: 1.234,50
```

The locale controls formatting conventions such as decimal and grouping separators.

It does **not** translate literal text:

```text
"Value"
```

remains:

```text
"Value"
```

### Locale with `readLine()`

```java
String input = console.readLine(
    Locale.GERMANY,
    "Enter value %,.2f: ",
    1234.5
);
```

The locale affects the formatted prompt:

```text
Enter value 1.234,50:
```

It does **not** parse or reinterpret the user's input.

If the user enters:

```text
1234.50
```

the result is:

```java
"1234.50"
```

If they enter:

```text
1234,50
```

the result is:

```java
"1234,50"
```

Memory:

> **`Locale` formats the prompt; it does not parse the input.**

### `System.console()` Can Be `null`

Remember:

```java
System.console()
```

may return `null` depending on how the application was launched.

Therefore:

```java
System.console().printf("Hello");
```

can result in:

```text
NullPointerException
```

---

## `Reader.of(CharSequence)`

Java 24 adds:

```java
Reader.of(CharSequence)
```

It creates a `Reader` over characters already held in memory.

```java
Reader reader = Reader.of("ABC");

System.out.println((char) reader.read()); // A
System.out.println((char) reader.read()); // B
System.out.println((char) reader.read()); // C
System.out.println(reader.read());        // -1
```

Normal `Reader` behaviour still applies:

```text
read() → int
-1     → end of input
```

### Relationship to Existing I/O

Think:

```text
Reader.of(...)
→ characters already in memory
→ Reader

InputStreamReader
→ bytes
→ characters
```

`Reader.of()` accepts a `CharSequence`, not specifically a `String`.

Therefore:

```java
StringBuilder sb = new StringBuilder("Hello");

Reader reader = Reader.of(sb);
```

is valid.

### `mark()` and `reset()`

The returned reader supports marking:

```java
Reader r = Reader.of("ABC");

r.read();      // A
r.mark(10);
r.read();      // B
r.reset();

System.out.println((char) r.read()); // B
```

and:

```java
r.markSupported(); // true
```

### Mutable `CharSequence`

`Reader.of()` does not promise to snapshot or copy the supplied sequence.

Mutations to something such as a `StringBuilder` can therefore be observable by subsequent reads.

Do not rely on modifying a mutable sequence while its reader is open.

### `null`

This compiles:

```java
Reader.of(null);
```

but throws:

```text
NullPointerException
```

at runtime.

### Return Type

The return type is `Reader`.

This does not compile:

```java
BufferedReader br = Reader.of("Hello");
```

Wrapping it is fine:

```java
BufferedReader br =
        new BufferedReader(Reader.of("Hello"));
```

Memory:

```text
Reader.of(CharSequence)
→ characters
→ Reader

NOT automatically:
→ BufferedReader
→ FileReader
→ InputStreamReader
```

---

## `CharSequence.getChars()`

Java 25 adds the default method:

```java
void getChars(
    int srcBegin,
    int srcEnd,
    char[] dst,
    int dstBegin
)
```

It copies characters from:

```text
[srcBegin, srcEnd)
```

into an **existing** destination array beginning at `dstBegin`.

### Example

```java
CharSequence cs = "ORACLE";

char[] result =
        {'A', 'B', 'C', 'D', 'E', 'F'};

cs.getChars(1, 4, result, 2);
```

Source:

```text
ORACLE
012345

[1, 4) → R A C
```

Destination:

```text
index:   0 1 2 3 4 5
before:  A B C D E F
             ↓ ↓ ↓
source:      R A C

after:   A B R A C F
```

Therefore:

```java
Arrays.toString(result);
```

produces:

```text
[A, B, R, A, C, F]
```

### Behaviour

`getChars()`:

```text
overwrites existing elements
does not insert
does not resize the array
returns void
```

Therefore:

```java
char[] result = cs.getChars(...); // DOES NOT COMPILE
```

### Why the Java 25 Addition Matters

`String` already had `getChars()`.

Java 25 adds it to the `CharSequence` interface as a default method.

Therefore this now works directly through a `CharSequence` reference:

```java
CharSequence cs = "Java";

cs.getChars(0, 2, new char[2], 0);
```

### Range Shortcut

For a half-open range:

```text
[start, end)
```

the number of elements is:

```text
end - start
```

For example:

```text
[1, 4)
→ 4 - 1
→ 3 elements
```

Compare a closed range:

```text
[start, end]
```

where the count is:

```text
end - start + 1
```

Memory:

> **Half-open `[start,end)` → count = `end - start`.**

---

## `CharBuffer.getChars()`

`CharBuffer` implements `CharSequence`, so it gains the Java 25 `getChars()` operation.

The complication is that a `CharBuffer` has:

```text
position
limit
capacity
```

For `CharBuffer`, the `getChars()` indexes are relative to the buffer's **current position**.

### Relative Indexes

```java
CharBuffer buffer =
        CharBuffer.wrap("JAVA25");

buffer.position(2);
```

Conceptually:

```text
JAVA25
012345
  ↑
position = 2
```

Relative to the current position:

```text
index 0 → V
index 1 → A
index 2 → 2
index 3 → 5
```

Now:

```java
char[] result =
        {'A', 'B', 'C', 'D', 'E'};

buffer.getChars(0, 3, result, 1);
```

copies:

```text
V A 2
```

giving:

```text
before: A B C D E
          ↓ ↓ ↓
source:   V A 2

after:  A V A 2 E
```

### Position Does Not Advance

This is the important edge case:

```java
buffer.getChars(0, 3, result, 1);

buffer.position(); // still 2
```

Do not confuse this with relative bulk `get()`:

```java
buffer.get(result, 1, 3);
```

which reads from the current position **and advances it**.

Memory:

```text
getChars(...)
→ indexes relative to current position
→ position unchanged

get(...)
→ consumes characters
→ position advances
```

For this API, writing out the indexes is safer than trying to visualize them mentally.

---

## `ForkJoinPool` Scheduling and Timeouts

Java 25 makes:

```java
ForkJoinPool
```

implement:

```java
ScheduledExecutorService
```

Therefore:

```java
ScheduledExecutorService service =
        new ForkJoinPool();
```

compiles in Java 25.

If you already understand `ScheduledExecutorService`, there is little new behaviour to learn.

### Scheduling

`ForkJoinPool` can now use operations such as:

```java
schedule(...)
scheduleAtFixedRate(...)
scheduleWithFixedDelay(...)
```

For example:

```java
pool.schedule(
    task,
    10,
    TimeUnit.SECONDS
);
```

This does not guarantee execution **exactly** ten seconds later.

Think:

```text
wait at least 10 seconds
        ↓
task becomes eligible
        ↓
executor runs it when resources permit
```

### `submitWithTimeout()`

Java 25 also adds:

```java
submitWithTimeout(...)
```

Do not confuse this with:

```java
future.get(2, TimeUnit.SECONDS);
```

The distinction is:

```text
Future.get(timeout)
→ timeout belongs to the CALLER'S WAIT

submitWithTimeout(...)
→ timeout belongs to the TASK
```

With `Future.get(timeout)`, expiration causes `get()` to throw `TimeoutException`; that does not inherently cancel the underlying task.

With `submitWithTimeout()`, the task itself has an associated timeout and can be cancelled with the supplied timeout action invoked if it does not finish in time.

Also recognise:

```text
getDelayedTaskCount()
cancelDelayedTasksOnShutdown()
```

but these are lower priority.

---

## `CompletableFuture` Common-Pool Change

**Low priority / recognition knowledge.**

Java 25 changes an edge case in the default executor behaviour of asynchronous `CompletableFuture` operations.

For relevant asynchronous operations with **no explicit executor**:

```java
CompletableFuture.supplyAsync(
    () -> "Java"
);
```

the default is:

```java
ForkJoinPool.commonPool()
```

Java 25 makes this consistent even when the common pool has very low parallelism.

If an executor is explicitly supplied:

```java
CompletableFuture.supplyAsync(
    () -> "Java",
    executor
);
```

that executor is used.

### Method Recognition

```java
supplyAsync(() -> "java")
```

means approximately:

```text
start asynchronous work
that eventually supplies a value
```

And:

```java
thenApplyAsync(String::toUpperCase)
```

means approximately:

```text
when the previous stage completes,
asynchronously transform its result
```

For example:

```java
CompletableFuture
        .supplyAsync(() -> "java")
        .thenApplyAsync(String::toUpperCase);
```

eventually produces:

```text
JAVA
```

Do not confuse:

```java
thenApply(...)
```

with:

```java
thenApplyAsync(...)
```

For this Java 25 change, the important memory rule is:

> **Async operation + no supplied `Executor` → common `ForkJoinPool`.**

---

## Virtual Threads and `synchronized`

**Low priority / recognition knowledge.**

Java 24 improves how virtual threads interact with `synchronized`.

### Platform vs Virtual Threads

Platform threads are traditional Java threads that closely correspond to OS threads.

Virtual threads are lightweight Java threads scheduled by the JVM onto platform threads known as **carrier threads**.

Conceptually:

```text
many virtual threads
       ↓
JVM scheduler
       ↓
fewer carrier/platform threads
```

When a virtual thread blocks, the JVM can normally unmount it from its carrier:

```text
Virtual A blocks
       ↓
Virtual A unmounted
       ↓
carrier runs Virtual B
```

### `synchronized` Pinning Improvement

Previously:

```java
synchronized (lock) {
    blockingOperation();
}
```

could cause a blocked virtual thread to remain **pinned** to its carrier.

Conceptually:

```text
virtual thread waiting
        +
carrier waiting
```

Java 24 removes this pinning problem for normal `synchronized` usage.

### Synchronization Semantics Did Not Change

This does **not** change lock ownership or mutual exclusion.

```java
synchronized (lock) {
    readFromNetwork();
}
```

still allows only one thread to own `lock` at a time.

Think:

```text
lock ownership      → unchanged
mutual exclusion    → unchanged
carrier utilisation → improved
```

This is principally a performance/implementation improvement rather than a change to normal synchronization semantics.

---

# Quick Reference

| Addition | Version | Key Thing to Remember |
|---|---:|---|
| `Instant.until(Instant)` | 23 | Returns directed `Duration` |
| `Console` Locale overloads | 23 | Locale formats output/prompts, not input |
| `Reader.of(CharSequence)` | 24 | Creates `Reader` over in-memory characters |
| Virtual thread + `synchronized` | 24 | Normal blocking no longer pins carrier |
| `CharSequence.getChars()` | 25 | `[begin,end)` copied into existing `char[]` |
| `CharBuffer.getChars()` | 25 | Relative to position; position does **not** advance |
| `ForkJoinPool` scheduling | 25 | Now a `ScheduledExecutorService` |
| `ForkJoinPool.submitWithTimeout()` | 25 | Timeout belongs to task |
| `CompletableFuture` change | 25 | Async + no explicit executor → common pool |

## API Recognition

```text
Instant.until(Instant)
→ Duration

Instant.until(Temporal, TemporalUnit)
→ long


Console + Locale
→ formatting
→ NOT input parsing


Reader.of(CharSequence)
→ in-memory characters
→ Reader


CharSequence.getChars(...)
→ copy [begin,end)
→ existing char[]
→ void


CharBuffer.getChars(...)
→ relative to current position
→ position unchanged


ForkJoinPool
→ ScheduledExecutorService


Future.get(timeout)
→ caller wait timeout

submitWithTimeout(...)
→ task timeout


CompletableFuture async
+ no Executor
→ ForkJoinPool.commonPool()


virtual thread + synchronized
→ mutual exclusion unchanged
→ carrier pinning improved
```

## Range Reminder

```text
[start, end)
→ end - start elements

[start, end]
→ end - start + 1 elements
```

## Priority

```text
KNOW WELL
─────────
Instant.until(Instant)
Console Locale overloads
Reader.of(CharSequence)
CharSequence.getChars()
CharBuffer.getChars()
ForkJoinPool scheduling / timeout


RECOGNITION
───────────
CompletableFuture common-pool change
virtual-thread synchronized improvement
```

## Final Memory Kicks

> **`Instant.until(Instant)` returns a directed `Duration`; the older unit-based overload returns a `long`.**

> **A `Console` locale formats output and prompts — it does not parse what the user types.**

> **`Reader.of(CharSequence)` creates a `Reader` over characters already held in memory.**

> **`CharSequence.getChars()` copies `[begin,end)` into an existing `char[]` and returns `void`.**

> **`CharBuffer.getChars()` uses indexes relative to the current position but does not advance that position.**

> **Java 25 `ForkJoinPool` is a `ScheduledExecutorService`.**

> **`Future.get(timeout)` limits how long the caller waits; `submitWithTimeout()` associates the timeout with the task.**

> **Async `CompletableFuture` operation + no supplied executor → common `ForkJoinPool`.**

> **Java 24 improved virtual-thread behaviour inside `synchronized`; mutual-exclusion semantics did not change.**