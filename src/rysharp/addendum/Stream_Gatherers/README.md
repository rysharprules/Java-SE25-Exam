# Stream Gatherers

Quick reference for Java 25 Stream Gatherers and the built-in `windowFixed()`, `windowSliding()`, `fold()`, `scan()` and `mapConcurrent()` gatherers.

## Contents

- [The Basic Idea](#the-basic-idea)
- [`windowFixed()`](#windowfixed)
- [`windowSliding()`](#windowsliding)
- [`fold()`](#fold)
- [`scan()`](#scan)
- [`mapConcurrent()`](#mapconcurrent)
- [Quick Reference](#quick-reference)

---

## The Basic Idea

Normal stream operations include:

```java
var result = Stream.of(1, 2, 3, 4)
        .map(x -> x * 2)
        .filter(x -> x > 4)
        .toList();
```

Java 25 Stream Gatherers provide another kind of **intermediate stream operation**.

The useful mental model is:

> **A Gatherer is an intermediate stream operation that can look at multiple elements and control how elements are accumulated and emitted.**

A Gatherer is used with:

```java
stream.gather(gatherer)
```

Java provides several built-in gatherers through:

```java
Gatherers
```

The important built-ins are:

| Gatherer | Mental Model |
|---|---|
| `windowFixed(n)` | Non-overlapping chunks |
| `windowSliding(n)` | Overlapping windows |
| `fold(...)` | Final accumulated result |
| `scan(...)` | Running accumulated results |
| `mapConcurrent(...)` | Concurrent mapping |

### `gather()` Is Intermediate

Do not confuse:

```java
.gather(...)
```

with:

```java
.collect(...)
```

`gather()` is an **intermediate operation**.

It produces another `Stream`.

For example:

```java
var result = Stream.of(1, 2, 3, 4)
        .gather(Gatherers.windowFixed(2))
        .toList();
```

Conceptually:

```text
Stream<Integer>
      ↓
   gather()
      ↓
Stream<List<Integer>>
      ↓
   toList()
      ↓
List<List<Integer>>
```

The Gatherer can therefore change the type of elements flowing through the remainder of the stream pipeline.

Memory:

> **`gather()` transforms a stream; it does not terminate the pipeline.**

---

## `windowFixed()`

`windowFixed(n)` groups elements into **non-overlapping windows**.

```java
Stream.of(1, 2, 3, 4, 5, 6, 7)
        .gather(Gatherers.windowFixed(3))
        .toList();
```

Think:

```text
1 2 3 | 4 5 6 | 7
```

Result:

```text
[[1, 2, 3],
 [4, 5, 6],
 [7]]
```

Each element belongs to one window.

### Final Window

The final window can contain fewer than `n` elements.

Therefore:

```java
windowFixed(3)
```

does **not** mean:

```text
only emit windows containing exactly 3 elements
```

Instead think:

```text
group elements into non-overlapping
windows of up to 3 elements
```

Memory:

> **`windowFixed()` → chunks; final chunk may be smaller.**

---

## `windowSliding()`

`windowSliding(n)` creates **overlapping windows**.

```java
Stream.of(1, 2, 3, 4, 5)
        .gather(Gatherers.windowSliding(3))
        .toList();
```

Produces:

```text
[1, 2, 3]
[2, 3, 4]
[3, 4, 5]
```

Each new window:

```text
drops the oldest element
        +
adds the next element
```

Visualise:

```text
1 2 3
  2 3 4
    3 4 5
```

### Fixed vs Sliding

```text
windowFixed(3)

[1 2 3] [4 5 6] [7]


windowSliding(3)

[1 2 3]
  [2 3 4]
    [3 4 5]
      [4 5 6]
```

Think:

```text
FIXED
→ chunks
→ no overlap

SLIDING
→ neighbouring windows
→ overlap
```

### Typical Use

Sliding windows are useful when neighbouring values matter.

For example:

```text
temperatures:

10 12 14 16 18
```

with:

```java
windowSliding(3)
```

gives:

```text
10 12 14
12 14 16
14 16 18
```

These windows could then be mapped to moving averages.

Memory:

> **`windowSliding()` → drop oldest, add newest.**

---

## `fold()`

`fold()` accumulates many input elements and emits the **final accumulated result**.

Consider:

```text
1 2 3 4
```

Accumulation:

```text
1          → 1
1 + 2      → 3
1 + 2 + 3  → 6
1 + 2 + 3 + 4
           → 10
```

`fold()` emits only:

```text
10
```

For example:

```java
var result = Stream.of(1, 2, 3, 4)
        .gather(
            Gatherers.fold(
                () -> 0,
                (sum, n) -> sum + n
            )
        )
        .toList();
```

The resulting stream contains one accumulated value:

```text
[10]
```

Visualise:

```text
1 ─┐
2 ─┤
3 ─┤──→ 10
4 ─┘
```

Memory:

> **`fold()` → give me the final accumulated result.**

### `fold()` vs `reduce()`

Both can accumulate values, but they belong to different stream mechanisms.

```java
stream.reduce(...)
```

is an existing **terminal operation**.

Whereas:

```java
stream.gather(Gatherers.fold(...))
```

uses a Gatherer as an **intermediate operation**.

Think:

```text
reduce()
→ terminal

gather(fold(...))
→ intermediate
→ produces another Stream
```

---

## `scan()`

`scan()` performs a running accumulation and emits **each intermediate result**.

Using:

```text
1 2 3 4
```

the running accumulation is:

```text
1 → 1
2 → 3
3 → 6
4 → 10
```

So the output is:

```text
[1, 3, 6, 10]
```

Visualise:

```text
1 ──→ 1
2 ──→ 3
3 ──→ 6
4 ──→ 10
```

Memory:

> **`scan()` → give me the accumulated result after every input.**

### Initial Value

The initial state is used to begin the accumulation but is not emitted separately.

Conceptually, with an initial value of:

```text
0
```

and input:

```text
1 2 3 4
```

think:

```text
initial state = 0

0 + 1 → 1   ← emit
1 + 2 → 3   ← emit
3 + 3 → 6   ← emit
6 + 4 → 10  ← emit
```

Output:

```text
[1, 3, 6, 10]
```

not:

```text
[0, 1, 3, 6, 10]
```

### `fold()` vs `scan()`

This is the crucial distinction:

| Gatherer | Output |
|---|---|
| `fold()` | Final accumulated result |
| `scan()` | Every intermediate accumulated result |

For:

```text
1 2 3 4
```

think:

```text
fold
────
1 2 3 4
   ↓
  10


scan
────
1 2 3 4
↓ ↓ ↓  ↓
1 3 6 10
```

Or simply:

```text
fold()
→ What is the total?

scan()
→ Show me the running total.
```

---

## `mapConcurrent()`

`mapConcurrent(maxConcurrency, mapper)` applies a mapping operation **concurrently**.

For example:

```java
Stream.of("A", "B", "C")
        .gather(
            Gatherers.mapConcurrent(
                3,
                s -> doSomething(s)
            )
        );
```

Instead of thinking:

```text
A → process → result
B → process → result
C → process → result
```

think:

```text
A ──→ process ──→ result
B ──→ process ──→ result
C ──→ process ──→ result
       ↑
   concurrently
```

The first argument controls the maximum number of mapping operations that may be active concurrently.

### Concurrency Limit

For:

```java
Gatherers.mapConcurrent(
    2,
    x -> slowOperation(x)
)
```

think:

```text
maximum concurrent mappings = 2
```

Conceptually:

```text
1 ────────→ result
2 ────────→ result
     ↑
running concurrently

3 ────────→ result
4 ────────→ result
```

At most two mapping operations are in flight at once.

Memory:

> **`mapConcurrent(n, mapper)` → up to `n` concurrent mapping operations.**

### Encounter Order

Concurrent processing does not mean the resulting stream is randomly reordered.

Suppose the input encounter order is:

```text
A B C
```

but processing completes:

```text
B
C
A
```

The resulting stream still respects the encounter order:

```text
A B C
```

Memory:

> **`mapConcurrent()` performs work concurrently while preserving encounter order.**

### Sequential and Parallel Streams

`mapConcurrent()` can be used with sequential or parallel streams.

For example:

```java
Stream.of(1, 2, 3, 4)
        .parallel()
        .gather(
            Gatherers.mapConcurrent(
                2,
                x -> slowOperation(x)
            )
        );
```

There are now two different concepts:

```text
parallel()
→ parallelism of the stream pipeline

mapConcurrent(2)
→ concurrency limit for the mapping operation
```

Do **not** interpret:

```java
.parallel()
.gather(Gatherers.mapConcurrent(2, ...))
```

as:

```text
exactly 2 threads
```

The stream's parallelism and the Gatherer's concurrency limit are separate concepts.

Think:

> **`mapConcurrent()` introduces and controls concurrency specifically around the mapping operation rather than simply making the whole stream parallel.**

---

# Quick Reference

## Built-In Gatherers

| Gatherer | Think |
|---|---|
| `windowFixed(n)` | Non-overlapping chunks |
| `windowSliding(n)` | Overlapping windows |
| `fold(...)` | Final accumulation |
| `scan(...)` | Running accumulation |
| `mapConcurrent(n, mapper)` | Concurrent mapping |

## `gather()`

```java
stream.gather(gatherer)
```

is:

```text
INTERMEDIATE
```

not:

```text
TERMINAL
```

Therefore:

```java
stream
    .gather(...)
    .map(...)
    .filter(...)
    .toList();
```

can continue processing after the Gatherer.

## Fixed vs Sliding

```text
INPUT
1 2 3 4 5 6 7


windowFixed(3)

[1 2 3] [4 5 6] [7]

→ non-overlapping
→ final window may be smaller


windowSliding(3)

[1 2 3]
  [2 3 4]
    [3 4 5]
      [4 5 6]
        [5 6 7]

→ overlapping
```

## Fold vs Scan

```text
INPUT
1 2 3 4


fold()

1 2 3 4
   ↓
  10


scan()

1 2 3 4
↓ ↓ ↓  ↓
1 3 6 10
```

Memory:

```text
fold → FINAL
scan → RUNNING
```

The initial state used by `scan()` is not itself emitted.

## Fold vs Reduce

```text
reduce(...)
→ terminal operation

gather(Gatherers.fold(...))
→ intermediate operation
```

## `mapConcurrent()`

```text
mapConcurrent(maxConcurrency, mapper)

→ concurrent mapping
→ maxConcurrency limits mappings in flight
→ preserves encounter order
```

Do not equate:

```text
mapConcurrent(n)
```

with:

```text
parallel stream using exactly n threads
```

## Reliable Recognition

When you see:

```java
.gather(Gatherers.???)
```

think:

```text
windowFixed
→ CHUNKS

windowSliding
→ OVERLAPPING CHUNKS

fold
→ FINAL RESULT

scan
→ RUNNING RESULTS

mapConcurrent
→ CONCURRENT MAP
```

Then remember:

```text
gather()
→ intermediate
→ another Stream
```

## Final Memory Kicks

> **`gather()` is an intermediate stream operation, not a terminal operation.**

> **`windowFixed(n)` creates non-overlapping chunks, and its final window may be smaller than `n`.**

> **`windowSliding(n)` creates overlapping windows by dropping the oldest element and adding the next.**

> **`fold()` emits the final accumulated result.**

> **`scan()` emits each intermediate accumulated result; its initial state is not emitted separately.**

> **`reduce()` is terminal; `gather(Gatherers.fold(...))` remains intermediate.**

> **`mapConcurrent(n, mapper)` allows up to `n` mapping operations to run concurrently.**

> **`mapConcurrent()` preserves encounter order even if individual operations complete out of order.**

> **Stream parallelism and `mapConcurrent()` concurrency are separate concepts.**

> **FIXED = chunks → SLIDING = overlap → FOLD = final → SCAN = running → MAP CONCURRENT = concurrent mapping.**