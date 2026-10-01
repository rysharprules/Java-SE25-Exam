# Java 21 → 25 Minor API Additions

A concise review of smaller Java additions and behavioural changes introduced after JDK 21 that may be useful for the Java 25 certification exam.

Major features such as module imports, compact source files, instance `main` methods, flexible constructor bodies, Scoped Values, Stream Gatherers, and unnamed variables/patterns are covered in separate guides.

---

## 1. `Instant.until(Instant)`

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

### Existing `until()` overload

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

Important points:

* The result is directional, so it can be negative.
* Only complete units are counted; the result is a `long`.
* Incomplete units are truncated rather than rounded.

For a difference of 1 hour, 45 minutes and 30 seconds:

```java
start.until(end, ChronoUnit.HOURS); // 1
```

not `1.75`, `1.758...`, or `2`.

### Unsupported units

This compiles:

```java
start.until(end, ChronoUnit.MONTHS);
```

because `MONTHS` is a valid `TemporalUnit`.

It fails at runtime with `UnsupportedTemporalTypeException` because `Instant` does not support calendar-based months.

Mental model:

> `Instant` represents points on the timeline, not calendar arithmetic.

### `Duration` vs `Period`

```text
Duration → time-based amounts: hours/minutes/seconds
Period   → date-based amounts: years/months/days
```

A useful edge case:

```java
Duration.ofDays(1)
```

means exactly 24 hours.

A `Period` of one calendar day is not necessarily 24 elapsed hours around daylight-saving changes.

---

## 2. `Console` Locale Formatting

Java 23 adds locale-aware overloads including:

```java
format(Locale, String, Object...)
printf(Locale, String, Object...)
readLine(Locale, String, Object...)
readPassword(Locale, String, Object...)
```

Example:

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

remains `"Value"`.

### Locale with `readLine()`

```java
String input = console.readLine(
    Locale.GERMANY,
    "Enter value %,.2f: ",
    1234.5
);
```

The locale affects the **formatted prompt**:

```text
Enter value 1.234,50:
```

It does **not** parse or reinterpret what the user subsequently types.

If the user enters:

```text
1234.50
```

`readLine()` returns `"1234.50"`.

If they enter:

```text
1234,50
```

it returns `"1234,50"`.

Mental model:

> `Locale` formats the prompt; it does not parse the input.

### Existing `Console` trap

Remember:

```java
System.console()
```

may return `null` depending on how the application was launched.

Therefore:

```java
System.console().printf("Hello");
```

can result in a `NullPointerException`.

---

## 3. `Reader.of(CharSequence)`

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

Normal `Reader` behaviour still applies: `read()` returns an `int`, and `-1` indicates the end of the input.

### Relation to existing I/O

Think:

```text
InputStreamReader
    bytes → characters

Reader.of(...)
    characters already in memory → Reader
```

`Reader.of()` accepts a `CharSequence`, not specifically a `String`.

Therefore this is valid:

```java
StringBuilder sb = new StringBuilder("Hello");

Reader reader = Reader.of(sb);
```

Other `CharSequence` implementations can also be supplied.

### `mark()` / `reset()`

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

`Reader.of()` does not promise to snapshot/copy the supplied sequence.

For example, mutations to a `StringBuilder` can be observable by subsequent reads. Modifying a mutable sequence while its reader is open should not be relied upon; behaviour can be undefined.

### `null`

This compiles:

```java
Reader.of(null);
```

but throws `NullPointerException` at runtime.

### Not a general `Reader` factory

`Reader.of()` does not create arbitrary `Reader` subclasses.

This does not compile:

```java
BufferedReader br = Reader.of("Hello");
```

because the return type is `Reader`.

Wrapping still works normally:

```java
BufferedReader br =
    new BufferedReader(Reader.of("Hello"));
```

Likewise, `InputStreamReader`, `FileReader`, etc. are still constructed normally.

Mental model:

```text
Reader.of(CharSequence) → characters → Reader
InputStreamReader       → bytes → characters
BufferedReader          → Reader → buffered Reader
```

---

## 4. `CharSequence.getChars()`

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

into an **existing** destination array starting at `dstBegin`.

Example:

```java
CharSequence cs = "ORACLE";

char[] result =
    {'A', 'B', 'C', 'D', 'E', 'F'};

cs.getChars(1, 4, result, 2);
```

Trace the source:

```text
ORACLE
012345

1 → 4 exclusive = R A C
```

Then the destination:

```text
index:   0 1 2 3 4 5
before:  A B C D E F
             ↓ ↓ ↓
source:      R A C

after:   A B R A C F
```

So:

```java
Arrays.toString(result);
```

produces:

```text
[A, B, R, A, C, F]
```

### Important behaviour

`getChars()`:

* overwrites existing destination elements
* does not insert elements
* does not resize the array
* returns `void`

Therefore:

```java
char[] result = cs.getChars(...); // DOES NOT COMPILE
```

### Why the Java 25 change matters

`String` already had a `getChars()` method.

Java 25 adds it to the **`CharSequence` interface** itself as a default method.

Therefore:

```java
CharSequence cs = "Java";

cs.getChars(0, 2, new char[2], 0);
```

now works directly through a `CharSequence` reference.

### Range shortcut

For Java's common half-open ranges:

```text
[start, end)
```

the number of elements is:

```text
end - start
```

Example:

```text
[1, 4) → 4 - 1 = 3 elements
```

Contrast this with an explicitly closed range:

```java
IntStream.rangeClosed(1, 4);
```

which contains:

```text
1, 2, 3, 4
```

and therefore:

```text
end - start + 1
```

elements.

---

## 5. `CharBuffer.getChars()`

`CharBuffer` implements `CharSequence`, so Java 25 provides the new `getChars()` operation.

The important complication is that a `CharBuffer` has:

```text
position
limit
capacity
```

The `getChars()` indexes are relative to the buffer's **current position**.

Example:

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

into the destination beginning at index `1`:

```text
before: A B C D E
          ↓ ↓ ↓
source:   V A 2

after:  A V A 2 E
```

### Important edge case: position

`getChars()` does **not** advance the `CharBuffer` position.

Therefore:

```java
buffer.position(); // still 2
```

Do not confuse this with a relative bulk `get()`:

```java
buffer.get(result, 1, 3);
```

which reads from the current position **and advances the position**.

Mental model:

```text
getChars(...) → indexed copy relative to current position
                position unchanged

get(...)      → consumes characters
                position advances
```

### Exam technique

This is the sort of API where it is safer to write out the indexes than attempt to visualise everything mentally.

---

## 6. `ForkJoinPool` Scheduling and Timeouts

Java 25 makes:

```java
ForkJoinPool
```

implement:

```java
ScheduledExecutorService
```

Therefore the normal **is-a** relationship applies:

```java
ScheduledExecutorService service =
    new ForkJoinPool();
```

This compiles in Java 25.

If already familiar with `ScheduledExecutorService`, there is little new behaviour to learn.

`ForkJoinPool` can now use scheduling operations such as:

```java
schedule(...)
scheduleAtFixedRate(...)
scheduleWithFixedDelay(...)
```

### Delayed scheduling

```java
pool.schedule(
    task,
    10,
    TimeUnit.SECONDS
);
```

does **not** guarantee execution exactly ten seconds later.

Instead:

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

which associates a timeout with the submitted task.

Do not confuse this with:

```java
future.get(2, TimeUnit.SECONDS);
```

The distinction is important.

`Future.get(timeout)`:

> The **caller** waits at most that long for the result.

If the timeout expires, `get()` throws `TimeoutException`. That does not inherently cancel the underlying task.

`submitWithTimeout()`:

> The **task itself** has an associated timeout.

If it does not finish in time, it can be cancelled and the supplied timeout handler invoked.

Also recognise, but do not overlearn:

```text
getDelayedTaskCount()
cancelDelayedTasksOnShutdown()
```

---

## 7. `CompletableFuture` Common-Pool Change

**Low priority / recognition knowledge.**

Java 25 changes an edge case in the default executor behaviour of asynchronous `CompletableFuture` operations.

For relevant async operations with **no explicit executor**:

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

If an executor is supplied:

```java
CompletableFuture.supplyAsync(
    () -> "Java",
    executor
);
```

the explicitly supplied executor is used.

### Quick method recognition

```java
supplyAsync(() -> "java")
```

means approximately:

> Start asynchronous work that eventually supplies a value.

This is conceptually similar to submitting a `Callable` and obtaining a future result.

```java
thenApplyAsync(String::toUpperCase)
```

means approximately:

> When the previous stage completes, asynchronously transform its result.

So:

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

For this guide, the important Java 25 rule is simply:

> **Async operation + no supplied Executor → common ForkJoinPool.**

---

## 8. Virtual Threads and `synchronized`

**Low priority / recognition knowledge.**

Java 24 improves how virtual threads interact with `synchronized`.

### Platform vs virtual threads

Platform threads are traditional Java threads that closely correspond to OS threads and are comparatively expensive resources.

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

This is one reason very large numbers of virtual threads are practical.

### The old pinning problem

Before Java 24:

```java
synchronized (lock) {
    blockingOperation();
}
```

could cause a blocked virtual thread to remain **pinned** to its carrier.

That meant both were effectively waiting:

```text
Virtual A waiting
       +
carrier waiting
```

reducing the scalability advantage of virtual threads.

Java 24 removes this pinning problem for normal `synchronized` usage.

### What did NOT change?

Synchronization semantics remain the same.

If many virtual threads execute:

```java
synchronized (lock) {
    readFromNetwork();
}
```

only one thread can own `lock` at a time.

Think:

```text
lock ownership     → unchanged
mutual exclusion   → unchanged
carrier utilisation → improved
```

This is principally a performance/implementation improvement rather than a change to how correctly written synchronized code behaves.

---

# Quick Revision Table

| Addition                                    | Version | Key thing to remember                                        |
| ------------------------------------------- | ------: | ------------------------------------------------------------ |
| `Instant.until(Instant)`                    |      23 | Returns directed `Duration`                                  |
| `Console` Locale overloads                  |      23 | Locale formats output/prompts, not user input                |
| `Reader.of(CharSequence)`                   |      24 | Creates `Reader` over in-memory characters                   |
| Virtual-thread `synchronized` improvement   |      24 | Blocking no longer pins carriers in normal synchronized code |
| `CharSequence.getChars()`                   |      25 | `[begin,end)` copied into existing `char[]`                  |
| `CharBuffer.getChars()`                     |      25 | Indexes relative to position; **doesn't advance position**   |
| `ForkJoinPool` scheduling                   |      25 | Now a `ScheduledExecutorService`                             |
| `ForkJoinPool.submitWithTimeout()`          |      25 | Timeout belongs to task, unlike `Future.get(timeout)`        |
| `CompletableFuture` default executor change |      25 | Async + no explicit executor → common pool                   |

---