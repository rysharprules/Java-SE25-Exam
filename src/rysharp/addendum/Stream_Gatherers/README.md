## Stream Gatherers

You've already seen normal stream operations:

```java
var result = Stream.of(1, 2, 3, 4)
    .map(x -> x * 2)
    .filter(x -> x > 4)
    .toList();
```

The useful mental model is:

> **A Gatherer is a custom/intermediate stream operation that can look at multiple elements and control how elements are accumulated and emitted.**

You use one with:

```java
stream.gather(gatherer)
```

Java provides several built-in ones through:

```java
Gatherers
```

### The built-in Gatherers you should know

For the Java 25 exam, I'd know these:

| Gatherer             | Simple mental model                      | Example                                |
|----------------------|------------------------------------------|----------------------------------------|
| `windowFixed(n)`     | Groups elements into fixed-size chunks   | `[1,2,3] [4,5,6]`                      |
| `windowSliding(n)`   | Groups elements into overlapping windows | `[1,2] [2,3] [3,4]`                    |
| `fold(...)`          | Many elements → one result               | `[1,2,3] → 6`                          |
| `scan(...)`          | Running accumulation → output each step  | `[1,2,3] → [1,3,6]`                    |
| `mapConcurrent(...)` | Map elements concurrently                | process multiple elements concurrently |

---

## `windowFixed()`

Suppose:

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

The final window **can be smaller** than the requested size. Oracle explicitly specifies this behaviour. ([Oracle Docs][2])

So:

```java
windowFixed(3)
```

doesn't mean:

> "Only produce groups containing exactly 3."

It means:

> "Gather elements into groups of up to 3, in order."

---

## `windowSliding()`

This one is slightly more interesting.

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

Each new window **drops the oldest element and adds the next one**.

So visually:

```text
1 2 3
  2 3 4
    3 4 5
```

That's the big distinction:

```text
windowFixed(3):

[1 2 3] [4 5 6] [7]


windowSliding(3):

[1 2 3]
  [2 3 4]
    [3 4 5]
      [4 5 6]
```

### Why would you use this?

Sliding windows are useful when you care about **neighbouring elements**.

For example, calculating a moving average:

```text
temperatures:
10, 12, 14, 16, 18

windows of 3:
10,12,14
12,14,16
14,16,18
```

You could then calculate an average for each window.

---

## One exam trap

Don't confuse:

```java
.gather(...)
```

with:

```java
.collect(...)
```

`gather()` is an **intermediate operation** — it produces another `Stream`.

So this:

```java
var result = Stream.of(1, 2, 3, 4)
    .gather(Gatherers.windowFixed(2))
    .toList();
```

works because:

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

The output type has changed from a stream of `Integer` to a stream of `List<Integer>`.

## `fold()` vs `scan()`

You've got the window gatherers. Now let's look at the other two that are particularly worth recognising for the exam.

The easiest way to remember them is:

> **`fold()` → give me the final accumulated result.**
> **`scan()` → give me every intermediate accumulated result.**

### `fold()`

Imagine we have:

```java
Stream.of(1, 2, 3, 4)
```

and want to add everything together.

Conceptually:

```text
1 → 1
2 → 1 + 2 = 3
3 → 1 + 2 + 3 = 6
4 → 1 + 2 + 3 + 4 = 10
```

`fold()` gives us **only the final result**:

```text
10
```

A simplified example:

```java
var result = Stream.of(1, 2, 3, 4)
    .gather(Gatherers.fold(() -> 0, (sum, n) -> sum + n))
    .toList();
```

The important bit isn't memorising the lambda syntax yet. The resulting stream contains the **single final accumulated value**:

```text
[10]
```

So think:

```text
fold:

1 ─┐
2 ─┤
3 ─┤──→ 10
4 ─┘
```

---

### `scan()`

`scan()` performs the same kind of accumulation, **but emits each intermediate result**.

Using the same numbers:

```text
1 → 1
2 → 3
3 → 6
4 → 10
```

So conceptually:

```java
Stream.of(1, 2, 3, 4)
    .gather(...)
```

produces:

```text
[1, 3, 6, 10]
```

Visualise it as:

```text
scan:

1 ──→ 1
2 ──→ 3
3 ──→ 6
4 ──→ 10
```

### The crucial difference

| Gatherer | Output                                   |
| -------- | ---------------------------------------- |
| `fold()` | **final** accumulated result             |
| `scan()` | **each intermediate** accumulated result |

This is similar to the distinction between:

> **"What is the total?"**

and

> **"Show me the running total after every item."**

For example:

```text
Values:       1   2   3   4

Running total:
              1   3   6  10
```

`scan()` gives you that running-total sequence.

---

### One exam wrinkle

Don't confuse `fold()` with `reduce()`.

Both can accumulate values, but they're not the same API.

`reduce()` is an existing **terminal stream operation**:

```java
stream.reduce(...)
```

whereas:

```java
stream.gather(Gatherers.fold(...))
```

uses a **Gatherer as an intermediate operation**.

That's a useful Java 25 distinction.

## `mapConcurrent()`

Suppose:

```java
Stream.of("A", "B", "C")
    .gather(Gatherers.mapConcurrent(3, s -> doSomething(s)))
```

The idea is that the mapping function can be applied to multiple elements **concurrently**, with the first argument controlling the maximum number of concurrent operations.

So mentally:

```text
normal map:

A → process → result
B → process → result
C → process → result


mapConcurrent(3):

A ──→ process ──→ result
B ──→ process ──→ result
C ──→ process ──→ result
       ↑
   concurrently
```

It's particularly useful when the mapping operation is relatively expensive and can be performed independently.

**It can be used with both sequential and parallel streams**, but its behaviour isn't simply "parallel stream + more parallelism."

### The key idea

`mapConcurrent(maxConcurrency, mapper)` says:

> **Run up to `maxConcurrency` mapping operations concurrently.**

For example:

```java
var result = Stream.of(1, 2, 3, 4)
    .gather(Gatherers.mapConcurrent(2, x -> slowOperation(x)))
    .toList();
```

Conceptually:

```text
Concurrency limit = 2

1 ────────→ result
2 ────────→ result
             ↑
        running together

3 ────────→ result
4 ────────→ result
```

At most **2 mapping operations** are in flight at once.

---

## What happens with a parallel stream?

You could have:

```java
Stream.of(1, 2, 3, 4)
    .parallel()
    .gather(Gatherers.mapConcurrent(2, x -> slowOperation(x)))
```

Now there are **two different concepts of concurrency** in play:

### 1. The stream itself is parallel

```java
.parallel()
```

allows the stream pipeline to be processed using multiple threads.

### 2. `mapConcurrent(2)` has its own concurrency limit

It controls how many mapping operations that Gatherer allows to be active concurrently.

So don't think:

> `parallel()` + `mapConcurrent(2)` = exactly 2 threads.

That's **not** what it means.

The stream's parallelism and the gatherer's concurrency limit are separate concepts.

> `mapConcurrent()` **lets you introduce/control concurrency specifically around a mapping operation, rather than making the entire stream parallel.**

---

### An important exam point

`mapConcurrent()` is designed to preserve the **encounter order** of the stream.

Imagine:

```text
Input:
A B C
```

Suppose processing takes:

```text
A = 3 seconds
B = 1 second
C = 2 seconds
```

They could finish:

```text
B
C
A
```

But the resulting stream still respects the encounter order:

```text
A B C
```

So concurrency doesn't necessarily mean your output gets randomly reordered.

## Java 25 Gatherer cheat sheet

If you see:

```java
.gather(Gatherers.???)
```

think:

* **`windowFixed`** → chunks
* **`windowSliding`** → overlapping chunks
* **`fold`** → final accumulation
* **`scan`** → running accumulation
* **`mapConcurrent`** → concurrent mapping

---
