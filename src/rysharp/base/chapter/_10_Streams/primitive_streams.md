# Primitive Streams

[🔙 Back](README.md)

Primitive streams provide specialised stream types for `int`, `long` and `double`, avoiding the need to box every primitive value into a wrapper object.

## Contents

- [Primitive Stream Types](#primitive-stream-types)
- [Why Primitive Streams Exist](#why-primitive-streams-exist)
- [Creating Primitive Streams](#creating-primitive-streams)
- [range and rangeClosed](#range-and-rangeclosed)
- [Primitive Stream Operations](#primitive-stream-operations)
- [Average](#average)
- [Summary Statistics](#summary-statistics)
- [Mapping Between Stream Types](#mapping-between-stream-types)
- [Mapping Functional Interfaces](#mapping-functional-interfaces)
- [Boxing Primitive Streams](#boxing-primitive-streams)
- [Primitive Optional Types](#primitive-optional-types)
- [Converting Optional Types](#converting-optional-types)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Primitive Stream Types

Java provides three specialised primitive stream interfaces:

```text
IntStream
LongStream
DoubleStream
```

They correspond to:

```text
int
long
double
```

rather than:

```text
Stream<Integer>
Stream<Long>
Stream<Double>
```

For example:

```java
IntStream numbers =
        IntStream.of(10, 20, 30);
```

versus:

```java
Stream<Integer> numbers =
        Stream.of(10, 20, 30);
```

These are different stream types:

```text
IntStream
≠
Stream<Integer>
```

---

## Why Primitive Streams Exist

A normal generic `Stream<T>` cannot use a primitive as its type argument:

```java
Stream<int> stream; // DOES NOT COMPILE
```

It would require:

```java
Stream<Integer>
```

which involves wrapper objects.

Primitive streams allow numeric values to be processed directly:

```java
IntStream stream =
        IntStream.of(1, 2, 3);
```

They also provide useful numeric operations such as:

```text
sum()
average()
summaryStatistics()
```

which do not exist directly on a general `Stream<T>`.

---

## Creating Primitive Streams

Each primitive stream provides `empty()` and `of()`.

### `IntStream`

```java
IntStream empty =
        IntStream.empty();

IntStream numbers =
        IntStream.of(1, 2, 3);
```

---

### `LongStream`

```java
LongStream numbers =
        LongStream.of(10L, 20L, 30L);
```

---

### `DoubleStream`

```java
DoubleStream numbers =
        DoubleStream.of(1.5, 2.5, 3.5);
```

---

### `generate()`

Primitive streams also support `generate()`.

```java
IntStream.generate(() -> 5)
        .limit(3)
        .forEach(System.out::println);
```

Output:

```text
5
5
5
```

The primitive-specific supplier is used:

```text
IntStream.generate    → IntSupplier
LongStream.generate   → LongSupplier
DoubleStream.generate → DoubleSupplier
```

---

### `iterate()`

Primitive streams also support `iterate()`.

```java
IntStream.iterate(
        1,
        n -> n + 1
)
.limit(5)
.forEach(System.out::println);
```

Output:

```text
1
2
3
4
5
```

The two-argument form is infinite unless another operation limits it.

---

## range and rangeClosed

`IntStream` and `LongStream` provide convenient methods for numeric ranges.

### `range()`

The start is inclusive and the end is exclusive:

```java
IntStream.range(1, 5)
        .forEach(System.out::println);
```

Output:

```text
1
2
3
4
```

Think:

```text
[1, 5)
```

Therefore:

```text
range(a, b)

a <= value < b
```

---

### `rangeClosed()`

Both boundaries are inclusive:

```java
IntStream.rangeClosed(1, 5)
        .forEach(System.out::println);
```

Output:

```text
1
2
3
4
5
```

Think:

```text
[1, 5]
```

Therefore:

```text
rangeClosed(a, b)

a <= value <= b
```

---

### Which Primitive Streams Have Ranges?

The range methods exist on:

```text
IntStream
LongStream
```

For example:

```java
IntStream.range(1, 10);

LongStream.range(1L, 10L);
```

There is no:

```java
DoubleStream.range(1.0, 10.0); // DOES NOT COMPILE
```

Memory:

```text
INT  → range
LONG → range
DOUBLE → NO range
```

---

## Primitive Stream Operations

Primitive streams provide numeric terminal operations.

| Method | `IntStream` | `LongStream` | `DoubleStream` |
|---|---|---|---|
| `sum()` | `int` | `long` | `double` |
| `average()` | `OptionalDouble` | `OptionalDouble` | `OptionalDouble` |
| `min()` | `OptionalInt` | `OptionalLong` | `OptionalDouble` |
| `max()` | `OptionalInt` | `OptionalLong` | `OptionalDouble` |
| `count()` | `long` | `long` | `long` |
| `summaryStatistics()` | `IntSummaryStatistics` | `LongSummaryStatistics` | `DoubleSummaryStatistics` |

---

### `sum()`

```java
int result =
        IntStream.of(10, 20, 30)
                .sum();

System.out.println(result); // 60
```

Return type depends on the primitive stream:

```text
IntStream.sum()    → int
LongStream.sum()   → long
DoubleStream.sum() → double
```

For an empty stream, the sum is the additive identity:

```java
System.out.println(
        IntStream.empty().sum()
); // 0
```

---

### `min()`

```java
OptionalInt result =
        IntStream.of(30, 10, 20)
                .min();

System.out.println(result.getAsInt()); // 10
```

Notice that unlike:

```java
Stream<T>.min(...)
```

no `Comparator` is required.

Primitive values already have their natural numeric ordering.

---

### `max()`

```java
OptionalInt result =
        IntStream.of(30, 10, 20)
                .max();

System.out.println(result.getAsInt()); // 30
```

Return types:

```text
IntStream.max()
→ OptionalInt

LongStream.max()
→ OptionalLong

DoubleStream.max()
→ OptionalDouble
```

The same applies to `min()`.

---

## Average

All three primitive stream types return:

```text
OptionalDouble
```

from `average()`.

For example:

```java
OptionalDouble result =
        IntStream.of(10, 20, 30)
                .average();

System.out.println(
        result.getAsDouble()
); // 20.0
```

Even though the source is:

```text
IntStream
```

the result is:

```text
OptionalDouble
```

because an average may not be an integer.

Likewise:

```java
LongStream.of(1L, 2L)
        .average();
```

returns:

```text
OptionalDouble
```

Memory:

```text
AVERAGE IS ALWAYS OptionalDouble

IntStream    ─┐
LongStream   ├→ OptionalDouble
DoubleStream ─┘
```

---

## Summary Statistics

Each primitive stream has a corresponding statistics class.

```text
IntStream
→ IntSummaryStatistics

LongStream
→ LongSummaryStatistics

DoubleStream
→ DoubleSummaryStatistics
```

Example:

```java
IntSummaryStatistics stats =
        IntStream.of(10, 20, 30, 40)
                .summaryStatistics();

System.out.println(stats.getCount());   // 4
System.out.println(stats.getSum());     // 100
System.out.println(stats.getMin());     // 10
System.out.println(stats.getMax());     // 40
System.out.println(stats.getAverage()); // 25.0
```

This provides several statistics from one terminal operation:

```text
count
sum
min
max
average
```

---

## Mapping Between Stream Types

The method used for mapping depends on:

1. the source stream type
2. the desired result stream type

This table is worth understanding rather than trying to memorise as unrelated
methods.

| Source | To `Stream<R>` | To `DoubleStream` | To `IntStream` | To `LongStream` |
|---|---|---|---|---|
| `Stream<T>` | `map()` | `mapToDouble()` | `mapToInt()` | `mapToLong()` |
| `DoubleStream` | `mapToObj()` | `map()` | `mapToInt()` | `mapToLong()` |
| `IntStream` | `mapToObj()` | `mapToDouble()` | `map()` | `mapToLong()` |
| `LongStream` | `mapToObj()` | `mapToDouble()` | `mapToInt()` | `map()` |

There is a useful pattern.

### Staying in the Same Stream Family

Use:

```text
map()
```

Examples:

```java
Stream<String>
        → map()
        → Stream<Integer>
```

or:

```java
IntStream
        → map()
        → IntStream
```

or:

```java
DoubleStream
        → map()
        → DoubleStream
```

---

### Object Stream to Primitive Stream

Use:

```text
mapToInt()
mapToLong()
mapToDouble()
```

For example:

```java
IntStream lengths =
        Stream.of("cat", "lion", "elephant")
                .mapToInt(String::length);
```

The transformation is:

```text
"cat"      → 3
"lion"     → 4
"elephant" → 8
```

and the resulting type is:

```text
IntStream
```

---

### Primitive Stream to Object Stream

Use:

```text
mapToObj()
```

For example:

```java
Stream<String> words =
        IntStream.of(1, 2, 3)
                .mapToObj(
                        n -> "Number " + n
                );
```

Result:

```text
Stream<String>
```

---

### Primitive Stream to Different Primitive Stream

Use the destination in the method name:

```java
IntStream.of(1, 2, 3)
        .mapToDouble(
                n -> n / 2.0
        );
```

Result:

```text
DoubleStream
```

Memory:

```text
OBJECT → primitive
→ mapToInt
→ mapToLong
→ mapToDouble


primitive → OBJECT
→ mapToObj


primitive → DIFFERENT primitive
→ mapToDestination


stay in SAME family
→ map
```

---

## Mapping Functional Interfaces

The functional interface also depends on the source and destination types.

### From `Stream<T>`

| Destination | Method | Functional Interface |
|---|---|---|
| `Stream<R>` | `map()` | `Function<T,R>` |
| `DoubleStream` | `mapToDouble()` | `ToDoubleFunction<T>` |
| `IntStream` | `mapToInt()` | `ToIntFunction<T>` |
| `LongStream` | `mapToLong()` | `ToLongFunction<T>` |

Example:

```java
Stream<String> words =
        Stream.of("cat", "lion");

IntStream lengths =
        words.mapToInt(String::length);
```

Conceptually:

```text
String → int

ToIntFunction<String>
```

---

### From `IntStream`

| Destination | Method | Functional Interface |
|---|---|---|
| `Stream<R>` | `mapToObj()` | `IntFunction<R>` |
| `DoubleStream` | `mapToDouble()` | `IntToDoubleFunction` |
| `IntStream` | `map()` | `IntUnaryOperator` |
| `LongStream` | `mapToLong()` | `IntToLongFunction` |

Examples:

```java
IntStream.of(1, 2, 3)
        .map(n -> n * 2);
```

uses:

```text
IntUnaryOperator

int → int
```

while:

```java
IntStream.of(1, 2, 3)
        .mapToDouble(n -> n / 2.0);
```

uses:

```text
IntToDoubleFunction

int → double
```

---

### From `LongStream`

| Destination | Method | Functional Interface |
|---|---|---|
| `Stream<R>` | `mapToObj()` | `LongFunction<R>` |
| `DoubleStream` | `mapToDouble()` | `LongToDoubleFunction` |
| `IntStream` | `mapToInt()` | `LongToIntFunction` |
| `LongStream` | `map()` | `LongUnaryOperator` |

---

### From `DoubleStream`

| Destination | Method | Functional Interface |
|---|---|---|
| `Stream<R>` | `mapToObj()` | `DoubleFunction<R>` |
| `DoubleStream` | `map()` | `DoubleUnaryOperator` |
| `IntStream` | `mapToInt()` | `DoubleToIntFunction` |
| `LongStream` | `mapToLong()` | `DoubleToLongFunction` |

---

### Naming Pattern

The names largely describe their input and output.

```text
ToIntFunction<T>

T → int
```

```text
IntFunction<R>

int → R
```

```text
IntToDoubleFunction

int → double
```

```text
IntUnaryOperator

int → int
```

So rather than memorising every name independently, read the interface name
as a description of the conversion.

---

## Boxing Primitive Streams

A primitive stream can be converted to a wrapper-object stream using
`boxed()`.

```java
Stream<Integer> numbers =
        IntStream.of(1, 2, 3)
                .boxed();
```

Conversions:

```text
IntStream
→ Stream<Integer>

LongStream
→ Stream<Long>

DoubleStream
→ Stream<Double>
```

Example:

```java
List<Integer> numbers =
        IntStream.rangeClosed(1, 5)
                .boxed()
                .toList();

System.out.println(numbers);
// [1, 2, 3, 4, 5]
```

Memory:

```text
boxed()
→ primitive stream
→ wrapper Stream
```

---

## Primitive Optional Types

Primitive streams use specialised Optional classes:

```text
OptionalInt
OptionalLong
OptionalDouble
```

These avoid wrapping primitive results in:

```text
Optional<Integer>
Optional<Long>
Optional<Double>
```

---

### Getting the Primitive Value

Each type has its own getter:

```text
OptionalInt
→ getAsInt()

OptionalLong
→ getAsLong()

OptionalDouble
→ getAsDouble()
```

Example:

```java
OptionalInt result =
        IntStream.of(10, 20, 30)
                .max();

System.out.println(
        result.getAsInt()
); // 30
```

If empty, these getter methods throw:

```text
NoSuchElementException
```

just like `Optional.get()`.

---

### `orElse()`

Primitive Optionals provide primitive versions of `orElse()`.

```java
OptionalInt optional =
        OptionalInt.empty();

int value =
        optional.orElse(99);

System.out.println(value); // 99
```

---

### `orElseGet()`

The Supplier type matches the primitive:

| Optional | Getter | `orElseGet()` Supplier |
|---|---|---|
| `OptionalInt` | `getAsInt()` | `IntSupplier` |
| `OptionalLong` | `getAsLong()` | `LongSupplier` |
| `OptionalDouble` | `getAsDouble()` | `DoubleSupplier` |

For example:

```java
OptionalInt optional =
        OptionalInt.empty();

int value =
        optional.orElseGet(
                () -> 42
        );
```

The lambda acts as an:

```text
IntSupplier

() → int
```

---

### Return Types from Primitive Streams

| Operation | `IntStream` | `LongStream` | `DoubleStream` |
|---|---|---|---|
| `min()` | `OptionalInt` | `OptionalLong` | `OptionalDouble` |
| `max()` | `OptionalInt` | `OptionalLong` | `OptionalDouble` |
| `average()` | `OptionalDouble` | `OptionalDouble` | `OptionalDouble` |
| `sum()` | `int` | `long` | `double` |

The odd-looking one is:

```text
average()

ALWAYS → OptionalDouble
```

---

## Converting Optional Types

Do not confuse:

```text
Optional<Integer>
```

with:

```text
OptionalInt
```

They are different classes.

For example:

```java
Optional<Integer> objectOptional =
        Stream.of(1, 2, 3)
                .max(Integer::compare);
```

but:

```java
OptionalInt primitiveOptional =
        IntStream.of(1, 2, 3)
                .max();
```

The getters are also different:

```text
Optional<Integer>
→ get()

OptionalInt
→ getAsInt()
```

Likewise:

```text
OptionalLong
→ getAsLong()

OptionalDouble
→ getAsDouble()
```

Memory:

```text
Stream<Integer>.max(...)
→ Optional<Integer>

IntStream.max()
→ OptionalInt
```

---

## Quick Reference

### Primitive Streams

```text
int
→ IntStream

long
→ LongStream

double
→ DoubleStream
```

---

### Ranges

```text
IntStream.range(a, b)
LongStream.range(a, b)

→ [a, b)
→ end EXCLUSIVE
```

```text
IntStream.rangeClosed(a, b)
LongStream.rangeClosed(a, b)

→ [a, b]
→ end INCLUSIVE
```

```text
DoubleStream
→ NO range / rangeClosed
```

---

### Numeric Operations

```text
SUM:

IntStream    → int
LongStream   → long
DoubleStream → double
```

```text
MIN / MAX:

IntStream    → OptionalInt
LongStream   → OptionalLong
DoubleStream → OptionalDouble
```

```text
AVERAGE:

IntStream    ─┐
LongStream   ├→ OptionalDouble
DoubleStream ─┘
```

---

### Summary Statistics

```text
IntStream
→ IntSummaryStatistics

LongStream
→ LongSummaryStatistics

DoubleStream
→ DoubleSummaryStatistics
```

Provides:

```text
count
sum
min
max
average
```

---

### Mapping

```text
SAME STREAM FAMILY
→ map()
```

```text
Stream<T> → primitive

mapToInt()
mapToLong()
mapToDouble()
```

```text
primitive → Stream<R>

mapToObj()
```

```text
primitive → different primitive

mapToInt()
mapToLong()
mapToDouble()
```

---

### Boxing

```text
IntStream.boxed()
→ Stream<Integer>

LongStream.boxed()
→ Stream<Long>

DoubleStream.boxed()
→ Stream<Double>
```

---

### Primitive Optionals

```text
OptionalInt
→ getAsInt()
→ IntSupplier for orElseGet()

OptionalLong
→ getAsLong()
→ LongSupplier for orElseGet()

OptionalDouble
→ getAsDouble()
→ DoubleSupplier for orElseGet()
```

---

### Mapping Functional Interfaces

```text
T → int
→ ToIntFunction<T>

T → long
→ ToLongFunction<T>

T → double
→ ToDoubleFunction<T>
```

```text
int → R
→ IntFunction<R>

long → R
→ LongFunction<R>

double → R
→ DoubleFunction<R>
```

```text
int → int
→ IntUnaryOperator

long → long
→ LongUnaryOperator

double → double
→ DoubleUnaryOperator
```

```text
int → double
→ IntToDoubleFunction

double → int
→ DoubleToIntFunction
```

The same naming pattern applies to the other primitive conversions.

---

## Final Memory Kicks

```text
PRIMITIVE STREAMS:

IntStream
LongStream
DoubleStream


NOT THE SAME AS:

Stream<Integer>
Stream<Long>
Stream<Double>


RANGE:

range(a, b)
→ [a, b)

rangeClosed(a, b)
→ [a, b]


RANGE EXISTS ON:

IntStream
LongStream

NOT DoubleStream


SUM:

IntStream    → int
LongStream   → long
DoubleStream → double


MIN / MAX:

IntStream    → OptionalInt
LongStream   → OptionalLong
DoubleStream → OptionalDouble


AVERAGE:

ALL THREE
→ OptionalDouble


BOXED:

IntStream
→ Stream<Integer>

LongStream
→ Stream<Long>

DoubleStream
→ Stream<Double>


MAPPING:

same family
→ map

object → primitive
→ mapToX

primitive → object
→ mapToObj

primitive → other primitive
→ mapToX


READ THE FUNCTION NAME:

ToIntFunction<T>
→ T → int

IntFunction<R>
→ int → R

IntToDoubleFunction
→ int → double

IntUnaryOperator
→ int → int


PRIMITIVE OPTIONAL:

OptionalInt
→ getAsInt

OptionalLong
→ getAsLong

OptionalDouble
→ getAsDouble


BIG ONE:

average()
→ ALWAYS OptionalDouble
```