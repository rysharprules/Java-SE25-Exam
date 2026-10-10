# Streams

[🔙 Back](README.md)

A stream represents a sequence of elements that can be processed through a pipeline of operations without modifying the original data source.

## Contents

- [Stream Pipeline](#stream-pipeline)
- [Creating Streams](#creating-streams)
- [Infinite Streams](#infinite-streams)
- [Intermediate and Terminal Operations](#intermediate-and-terminal-operations)
- [Laziness](#laziness)
- [Intermediate Operations](#intermediate-operations)
- [Short-Circuiting](#short-circuiting)
- [Terminal Operations](#terminal-operations)
- [Matching](#matching)
- [Finding Elements](#finding-elements)
- [Reduction](#reduction)
- [Optional](#optional)
- [Infinite Streams and Terminal Operations](#infinite-streams-and-terminal-operations)
- [Stream Reuse](#stream-reuse)
- [Spliterator](#spliterator)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Stream Pipeline

A stream pipeline consists of:

```text
SOURCE
   ↓
ZERO OR MORE INTERMEDIATE OPERATIONS
   ↓
TERMINAL OPERATION
```

For example:

```java
List<String> names =
        List.of("Bob", "Alice", "Ben", "Charlie");

long count = names.stream()              // source
        .filter(s -> s.startsWith("B"))  // intermediate
        .count();                        // terminal

System.out.println(count); // 2
```

The source is:

```java
names.stream()
```

The intermediate operation is:

```java
filter(...)
```

The terminal operation is:

```java
count()
```

Streams do not normally modify their source.

```java
List<String> names =
        new ArrayList<>(
                List.of("Bob", "Alice", "Charlie"));

names.stream()
        .sorted()
        .forEach(System.out::println);

System.out.println(names);
```

Output:

```text
Alice
Bob
Charlie
[Bob, Alice, Charlie]
```

`sorted()` sorted the elements flowing through the stream.

It did not sort the original `List`.

---

## Creating Streams

### `Stream.empty()`

Creates an empty finite stream.

```java
Stream<String> stream =
        Stream.empty();

System.out.println(stream.count()); // 0
```

---

### `Stream.of()`

Creates a finite stream containing the supplied elements.

```java
Stream<String> stream =
        Stream.of("A", "B", "C");

stream.forEach(System.out::println);
```

Output:

```text
A
B
C
```

`Stream.of()` accepts varargs:

```java
Stream.of("A", "B", "C");
```

---

### `Collection.stream()`

Creates a sequential stream from a collection.

```java
List<String> list =
        List.of("A", "B", "C");

Stream<String> stream =
        list.stream();
```

---

### `Collection.parallelStream()`

Creates a stream that may execute operations in parallel.

```java
Stream<String> stream =
        list.parallelStream();
```

Do not assume that a parallel stream processes elements in a predictable
thread or execution order.

Parallel stream behaviour is considered further with concurrency.

---

### Stream Creation Summary

| Method | Finite / Infinite | Description |
|---|---|---|
| `Stream.empty()` | Finite | Empty stream |
| `Stream.of(...)` | Finite | Stream of supplied elements |
| `collection.stream()` | Finite | Sequential stream from collection |
| `collection.parallelStream()` | Finite | Parallel stream from collection |
| `Stream.generate(...)` | Infinite | Repeatedly calls a `Supplier` |
| `Stream.iterate(seed, op)` | Infinite | Repeatedly applies a `UnaryOperator` |
| `Stream.iterate(seed, predicate, op)` | Finite or infinite | Continues while predicate is true |

---

## Infinite Streams

Streams do not have to contain a predetermined finite number of elements.

### `generate()`

`Stream.generate()` accepts a `Supplier`.

```java
Stream<Double> random =
        Stream.generate(Math::random);
```

Conceptually:

```text
Supplier
   ↓
value
   ↓
value
   ↓
value
   ↓
...
```

The stream is infinite unless something limits its processing.

For example:

```java
Stream.generate(() -> "Hello")
        .limit(3)
        .forEach(System.out::println);
```

Output:

```text
Hello
Hello
Hello
```

---

### Two-Argument `iterate()`

```java
Stream.iterate(seed, unaryOperator)
```

creates an infinite stream.

```java
Stream<Integer> numbers =
        Stream.iterate(
                1,
                n -> n + 1
        );
```

Conceptually:

```text
seed = 1

1
↓ +1
2
↓ +1
3
↓ +1
4
...
```

For example:

```java
Stream.iterate(1, n -> n + 1)
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

---

### Three-Argument `iterate()`

The three-argument overload adds a predicate:

```java
Stream.iterate(
        seed,
        predicate,
        unaryOperator
)
```

For example:

```java
Stream.iterate(
        1,
        n -> n <= 5,
        n -> n + 1
).forEach(System.out::println);
```

Output:

```text
1
2
3
4
5
```

Conceptually:

```text
START with seed

↓
does Predicate accept current value?

YES → include it
      ↓
      apply UnaryOperator
      ↓
      test next value

NO  → stop
```

The three-argument form can therefore produce a finite stream.

---

## Intermediate and Terminal Operations

Stream operations fall primarily into two categories.

### Intermediate Operations

Intermediate operations return another stream.

Examples:

```text
filter()
map()
flatMap()
distinct()
sorted()
limit()
skip()
peek()
```

This allows operations to be chained:

```java
stream
        .filter(...)
        .map(...)
        .sorted()
        .limit(5);
```

Intermediate operations are generally **lazy**.

---

### Terminal Operations

Terminal operations produce a final result or side effect.

Examples:

```text
count()
min()
max()
findFirst()
findAny()
allMatch()
anyMatch()
noneMatch()
forEach()
reduce()
collect()
```

A terminal operation causes the stream pipeline to begin processing.

After a terminal operation, the stream is consumed.

---

## Laziness

Streams use **lazy evaluation**.

Intermediate operations do not normally process elements immediately.

Consider:

```java
Stream<String> stream =
        Stream.of("A", "B", "C")
                .filter(s -> {
                    System.out.println("Filtering " + s);
                    return true;
                });
```

Nothing is printed yet.

The stream pipeline has been constructed, but there is no terminal operation.

Now add:

```java
long count = stream.count();
```

The terminal operation causes processing to occur.

Conceptually:

```text
INTERMEDIATE OPERATIONS
→ describe WHAT should happen

TERMINAL OPERATION
→ causes the pipeline to RUN
```

This laziness allows Java to avoid processing elements that are never needed.

---

### Processing Happens Element by Element

It is tempting to imagine:

```text
filter ALL elements
then
map ALL elements
then
limit
```

Streams can instead process elements through the pipeline as needed.

For example:

```java
Stream.of("ant", "bear", "cat", "dog")
        .filter(s -> {
            System.out.println("filter: " + s);
            return s.length() == 3;
        })
        .map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        })
        .limit(2)
        .forEach(System.out::println);
```

Conceptually, an element travels through the pipeline:

```text
element
  ↓
filter
  ↓
map
  ↓
limit
  ↓
terminal operation
```

Once `limit(2)` has received enough elements, additional source elements may
not need to be processed.

---

## Intermediate Operations

## `filter()`

Keeps elements that satisfy a `Predicate`.

```java
Stream.of("ant", "bear", "cat")
        .filter(s -> s.length() == 3)
        .forEach(System.out::println);
```

Output:

```text
ant
cat
```

Shape:

```text
Predicate<T>

T → boolean
```

---

## `distinct()`

Removes duplicate elements according to equality.

```java
Stream.of("A", "B", "A", "C", "B")
        .distinct()
        .forEach(System.out::println);
```

Output:

```text
A
B
C
```

---

## `limit()`

Restricts the stream to at most a specified number of elements.

```java
Stream.iterate(1, n -> n + 1)
        .limit(3)
        .forEach(System.out::println);
```

Output:

```text
1
2
3
```

`limit()` is especially important with infinite streams.

---

## `skip()`

Discards a specified number of elements from the beginning.

```java
Stream.of("A", "B", "C", "D")
        .skip(2)
        .forEach(System.out::println);
```

Output:

```text
C
D
```

`skip()` and `limit()` can be combined:

```java
Stream.iterate(1, n -> n + 1)
        .skip(5)
        .limit(3)
        .forEach(System.out::println);
```

Output:

```text
6
7
8
```

---

## `map()`

Transforms each element.

```java
Stream<String> names =
        Stream.of("Alice", "Bob");

Stream<Integer> lengths =
        names.map(String::length);
```

The transformation is:

```text
"Alice" → 5
"Bob"   → 3
```

`map()` accepts a `Function`:

```text
Function<T, R>

T → R
```

The output type can therefore differ from the input type.

---

## `flatMap()`

`flatMap()` is used when each element maps to another stream and those streams
should be flattened into one stream.

Consider:

```java
List<String> first =
        List.of("A", "B");

List<String> second =
        List.of("C", "D");
```

Without flattening, mapping each list to a stream conceptually gives:

```text
Stream<Stream<String>>
```

Using:

```java
Stream.of(first, second)
        .flatMap(Collection::stream)
        .forEach(System.out::println);
```

produces:

```text
A
B
C
D
```

Think:

```text
map()

[A, B] → Stream<A, B>
[C, D] → Stream<C, D>

result conceptually remains nested
```

versus:

```text
flatMap()

[A, B]
[C, D]

↓ flatten

A
B
C
D
```

---

## `sorted()`

Natural ordering:

```java
Stream.of(3, 1, 2)
        .sorted()
        .forEach(System.out::println);
```

Output:

```text
1
2
3
```

A comparator can also be supplied:

```java
Stream.of("A", "BBB", "CC")
        .sorted(Comparator.comparingInt(String::length))
        .forEach(System.out::println);
```

Output:

```text
A
CC
BBB
```

The original source collection is not automatically sorted.

---

## `peek()`

`peek()` performs an action as elements pass through the pipeline.

```java
long count =
        Stream.of("A", "B", "C")
                .peek(System.out::println)
                .count();
```

`peek()` is an intermediate operation:

```text
peek()
→ returns Stream
→ lazy
```

It is commonly useful for observing a pipeline.

Do not confuse it with the `Queue`/`Deque` `peek()` method.

```text
Stream.peek()
→ intermediate stream operation

Queue.peek()
→ examine head element
```

---

## Short-Circuiting

A short-circuiting operation can finish without processing the entire stream.

For example:

```java
boolean result =
        Stream.of(2, 4, 7, 8, 10)
                .anyMatch(n -> n % 2 != 0);
```

Once `7` is found:

```text
2  → false
4  → false
7  → true

STOP
```

There is no need to test `8` or `10`.

This becomes particularly important with infinite streams.

An infinite stream can terminate if the pipeline contains operations that
allow a result to be determined after a finite amount of processing.

For example:

```java
boolean result =
        Stream.iterate(1, n -> n + 1)
                .anyMatch(n -> n == 5);

System.out.println(result); // true
```

The source is infinite, but processing can stop when `5` is found.

---

## Terminal Operations

## `count()`

Returns the number of elements as a `long`.

```java
long count =
        Stream.of("A", "B", "C")
                .count();

System.out.println(count); // 3
```

Return type:

```text
long
```

An unrestricted infinite stream cannot finish counting.

---

## `min()` and `max()`

Both accept a `Comparator`.

```java
Optional<String> min =
        Stream.of("Bob", "Alice", "Charlie")
                .min(Comparator.naturalOrder());

System.out.println(min.get()); // Alice
```

Return type:

```text
Optional<T>
```

Why `Optional`?

Because the stream might be empty:

```java
Stream<String> stream =
        Stream.empty();

Optional<String> result =
        stream.min(Comparator.naturalOrder());

System.out.println(result.isPresent()); // false
```

---

## `forEach()`

Performs an action for each element.

```java
Stream.of("A", "B", "C")
        .forEach(System.out::println);
```

Return type:

```text
void
```

`forEach()` must process all elements presented to it, so an unrestricted
infinite stream will not terminate.

---

## Matching

There are three matching terminal operations:

```text
anyMatch()
allMatch()
noneMatch()
```

All accept a `Predicate` and return:

```text
boolean
```

---

### `anyMatch()`

Returns `true` when **at least one** element matches.

```java
boolean result =
        Stream.of(2, 4, 7, 8)
                .anyMatch(n -> n % 2 != 0);

System.out.println(result); // true
```

It can stop as soon as a match is found.

```text
anyMatch
→ ONE match is enough for true
```

---

### `allMatch()`

Returns `true` only if **every** element matches.

```java
boolean result =
        Stream.of(2, 4, 6)
                .allMatch(n -> n % 2 == 0);

System.out.println(result); // true
```

It can stop as soon as a failure is found.

```text
allMatch
→ ONE failure is enough for false
```

---

### `noneMatch()`

Returns `true` only if **no** elements match.

```java
boolean result =
        Stream.of(2, 4, 6)
                .noneMatch(n -> n < 0);

System.out.println(result); // true
```

It can stop as soon as a match is found.

```text
noneMatch
→ ONE match is enough for false
```

---

## Finding Elements

### `findFirst()`

Returns the first element, if one exists.

```java
Optional<String> result =
        Stream.of("A", "B", "C")
                .findFirst();

System.out.println(result.get()); // A
```

---

### `findAny()`

Returns an element from the stream.

```java
Optional<String> result =
        Stream.of("A", "B", "C")
                .findAny();
```

Do not rely on `findAny()` selecting a particular element, especially with
parallel streams.

Both return:

```text
Optional<T>
```

Both are short-circuiting terminal operations.

An infinite stream can therefore terminate:

```java
Optional<Integer> result =
        Stream.iterate(1, n -> n + 1)
                .findFirst();

System.out.println(result.get()); // 1
```

---

## Reduction

A reduction combines stream elements into a single result.

Examples include:

```text
count()
min()
max()
reduce()
```

The most flexible general-purpose reduction operation is `reduce()`.

There are three important overloads.

---

### `reduce(BinaryOperator)`

```java
Optional<T> reduce(
        BinaryOperator<T> accumulator)
```

Example:

```java
Optional<Integer> result =
        Stream.of(1, 2, 3, 4)
                .reduce((a, b) -> a + b);

System.out.println(result.get()); // 10
```

Processing conceptually:

```text
1 + 2 = 3
3 + 3 = 6
6 + 4 = 10
```

There is no identity value.

Therefore an empty stream has no result:

```java
Optional<Integer> result =
        Stream.<Integer>empty()
                .reduce((a, b) -> a + b);

System.out.println(result.isEmpty()); // true
```

This is why the return type is:

```text
Optional<T>
```

Memory:

```text
reduce(accumulator)
→ NO identity
→ Optional<T>
```

---

### `reduce(identity, BinaryOperator)`

```java
T reduce(
        T identity,
        BinaryOperator<T> accumulator)
```

Example:

```java
int result =
        Stream.of(1, 2, 3, 4)
                .reduce(
                        0,
                        (a, b) -> a + b
                );

System.out.println(result); // 10
```

Processing:

```text
identity = 0

0 + 1 = 1
1 + 2 = 3
3 + 3 = 6
6 + 4 = 10
```

For an empty stream:

```java
int result =
        Stream.<Integer>empty()
                .reduce(
                        0,
                        Integer::sum
                );

System.out.println(result); // 0
```

The identity itself is the result.

Therefore this overload returns:

```text
T
```

rather than `Optional<T>`.

Memory:

```text
reduce(identity, accumulator)
→ identity exists
→ T
```

---

### Three-Argument `reduce()`

```java
<U> U reduce(
        U identity,
        BiFunction<U, ? super T, U> accumulator,
        BinaryOperator<U> combiner)
```

This overload allows the result type to differ from the stream element type
and supports combining partial results.

For example:

```java
int totalLength =
        Stream.of("cat", "lion", "elephant")
                .reduce(
                        0,
                        (total, word) ->
                                total + word.length(),
                        Integer::sum
                );

System.out.println(totalLength); // 15
```

Here:

```text
Stream element type = String

result type = Integer
```

The arguments are:

```text
0
→ identity

(total, word) -> total + word.length()
→ accumulator

Integer::sum
→ combiner
```

Conceptually:

```text
ACCUMULATOR
combines stream elements into partial result

Integer + String
→ Integer


COMBINER
combines partial results

Integer + Integer
→ Integer
```

The combiner becomes particularly important when work can be split, such as
with parallel processing.

---

### `reduce()` Overload Memory

```text
1 ARGUMENT

reduce(accumulator)

→ Optional<T>
→ no identity
```

```text
2 ARGUMENTS

reduce(identity, accumulator)

→ T
→ identity supplied
```

```text
3 ARGUMENTS

reduce(identity, accumulator, combiner)

→ U
→ result type may differ
→ accumulator combines result + element
→ combiner combines result + result
```

---

### Identity Values

An identity should not change the result when combined with a value.

For addition:

```text
0

0 + x = x
```

For multiplication:

```text
1

1 × x = x
```

For string concatenation:

```text
""

"" + x = x
```

Choosing an inappropriate identity can change the result.

---

## Optional

Several stream terminal operations may have no value to return.

For example:

```text
min()
max()
findFirst()
findAny()
reduce(accumulator)
```

They therefore return:

```java
Optional<T>
```

An `Optional<T>` represents:

```text
VALUE PRESENT

or

NO VALUE
```

---

### Creating Optional Values

```java
Optional<String> present =
        Optional.of("Hello");
```

`Optional.of()` requires a non-null value.

```java
Optional.of(null); // NullPointerException
```

An empty Optional:

```java
Optional<String> empty =
        Optional.empty();
```

When the value may be null:

```java
Optional<String> optional =
        Optional.ofNullable(value);
```

Conceptually:

```text
of(value)
→ value MUST be non-null

ofNullable(value)
→ null becomes Optional.empty()

empty()
→ explicitly empty
```

---

### Optional Methods

| Method | Empty Optional | Value Present |
|---|---|---|
| `get()` | throws `NoSuchElementException` | returns value |
| `isPresent()` | `false` | `true` |
| `isEmpty()` | `true` | `false` |
| `ifPresent(Consumer)` | does nothing | calls Consumer |
| `orElse(T)` | returns supplied value | returns contained value |
| `orElseGet(Supplier)` | calls Supplier | returns contained value |
| `orElseThrow()` | throws `NoSuchElementException` | returns contained value |
| `orElseThrow(Supplier)` | throws supplied exception | returns contained value |

---

### `get()`

```java
Optional<String> optional =
        Optional.of("Hello");

System.out.println(optional.get()); // Hello
```

But:

```java
Optional<String> optional =
        Optional.empty();

optional.get(); // NoSuchElementException
```

---

### `ifPresent()`

Accepts a `Consumer`.

```java
Optional<String> optional =
        Optional.of("Hello");

optional.ifPresent(System.out::println);
```

Output:

```text
Hello
```

For an empty Optional, the Consumer is not called.

---

### `orElse()`

```java
Optional<String> optional =
        Optional.empty();

String value =
        optional.orElse("Default");

System.out.println(value); // Default
```

---

### `orElseGet()`

Accepts a `Supplier`.

```java
Optional<String> optional =
        Optional.empty();

String value =
        optional.orElseGet(
                () -> "Generated"
        );

System.out.println(value); // Generated
```

Memory:

```text
orElse(value)

orElseGet(Supplier)
```

---

### `orElse()` vs `orElseGet()`

There is an important evaluation difference.

The argument to `orElse()` is evaluated before the method is called:

```java
optional.orElse(createDefault());
```

Therefore `createDefault()` is evaluated even if the Optional already contains
a value.

With:

```java
optional.orElseGet(() -> createDefault());
```

the Supplier is only invoked when the Optional is empty.

Conceptually:

```text
orElse(...)
→ argument evaluated normally

orElseGet(...)
→ Supplier called only when needed
```

---

### `orElseThrow()`

Without an argument:

```java
String value =
        optional.orElseThrow();
```

an empty Optional causes:

```text
NoSuchElementException
```

A Supplier can provide another exception:

```java
String value =
        optional.orElseThrow(
                () -> new IllegalStateException(
                        "Missing value"
                )
        );
```

The Supplier is only called if the Optional is empty.

---

## Infinite Streams and Terminal Operations

An infinite source does **not automatically mean the program runs forever**.

The important question is:

> Can the pipeline determine its result after processing finitely many
> elements?

### Normally Does Not Terminate

```text
count()
min()
max()
forEach()
reduce()
collect()
```

on an unrestricted infinite stream generally require all elements and
therefore do not terminate.

---

### Can Terminate

```text
findFirst()
findAny()
```

can terminate after finding an element.

---

### May Terminate

Matching operations depend on the data:

```text
anyMatch()
allMatch()
noneMatch()
```

For example:

```java
Stream.iterate(1, n -> n + 1)
        .anyMatch(n -> n == 5);
```

terminates with:

```text
true
```

But:

```java
Stream.iterate(1, n -> n + 1)
        .anyMatch(n -> n < 0);
```

never finds a match and therefore does not terminate.

Similarly:

```text
allMatch()
→ terminates early if it finds FALSE

noneMatch()
→ terminates early if it finds TRUE
```

---

### Limiting an Infinite Stream

An intermediate operation can make the relevant portion finite:

```java
long count =
        Stream.iterate(1, n -> n + 1)
                .limit(10)
                .count();

System.out.println(count); // 10
```

So always consider the **entire pipeline**, not merely whether the source is
infinite.

---

## Stream Reuse

A stream cannot be reused after a terminal operation.

```java
Stream<String> stream =
        Stream.of("A", "B", "C");

System.out.println(stream.count()); // 3

stream.forEach(System.out::println);
```

The second terminal operation causes:

```text
IllegalStateException
```

Memory:

```text
STREAM
→ single-use pipeline

TERMINAL OPERATION
→ consumes stream
```

If another stream is required, obtain/create another one:

```java
List<String> list =
        List.of("A", "B", "C");

System.out.println(
        list.stream().count()
);

list.stream()
        .forEach(System.out::println);
```

The collection is reusable.

Each call to `stream()` creates a new stream.

---

## Spliterator

A `Spliterator<T>` provides traversal similar to an iterator while also
supporting splitting of elements into portions.

A stream can provide one:

```java
Stream<String> stream =
        Stream.of("A", "B", "C");

Spliterator<String> spliterator =
        stream.spliterator();
```

Important methods are:

```text
tryAdvance()
forEachRemaining()
trySplit()
```

---

### `tryAdvance()`

Attempts to process one element.

```java
Spliterator<String> spliterator =
        Stream.of("A", "B", "C")
                .spliterator();

boolean result =
        spliterator.tryAdvance(
                System.out::println
        );

System.out.println(result);
```

Output:

```text
A
true
```

Another call processes the next element:

```java
spliterator.tryAdvance(
        System.out::println
);
```

Output:

```text
B
```

Once no elements remain:

```text
tryAdvance()
→ false
```

Memory:

```text
tryAdvance
→ TRY ONE
→ boolean says whether one was processed
```

---

### `forEachRemaining()`

Processes all remaining elements.

```java
Spliterator<String> spliterator =
        Stream.of("A", "B", "C")
                .spliterator();

spliterator.tryAdvance(
        System.out::println
); // A

spliterator.forEachRemaining(
        System.out::println
);
```

Output:

```text
A
B
C
```

Memory:

```text
tryAdvance
→ ONE

forEachRemaining
→ REST
```

---

### `trySplit()`

Attempts to split the remaining elements.

```java
Spliterator<Integer> original =
        List.of(1, 2, 3, 4, 5, 6)
                .spliterator();

Spliterator<Integer> split =
        original.trySplit();
```

If splitting succeeds:

```text
split
→ contains some portion of the elements

original
→ retains the remaining portion
```

`trySplit()` may return:

```text
null
```

when the spliterator cannot or should not be split further.

Do not rely on every implementation splitting into exactly equal halves.

Memory:

```text
trySplit()
→ divide work if possible
→ returns another Spliterator
→ null if no useful split is available
```

This ability to divide work is important for parallel processing.

---

## Quick Reference

### Pipeline

```text
SOURCE
  ↓
INTERMEDIATE OPERATIONS
  ↓
TERMINAL OPERATION
```

---

### Creation

```text
Stream.empty()
→ finite

Stream.of(...)
→ finite

collection.stream()
→ finite

collection.parallelStream()
→ finite

Stream.generate(Supplier)
→ infinite

Stream.iterate(seed, UnaryOperator)
→ infinite

Stream.iterate(seed, Predicate, UnaryOperator)
→ finite OR infinite
```

---

### Intermediate Operations

```text
filter     → Predicate
map        → Function
flatMap    → flatten nested streams
distinct   → remove duplicates
sorted     → order elements
limit      → take at most n
skip       → discard first n
peek       → observe elements
```

Intermediate operations:

```text
return another stream
+
are generally lazy
```

---

### Terminal Operations

| Operation | Return Type | Infinite Stream |
|---|---|---|
| `count()` | `long` | Does not terminate |
| `min()` / `max()` | `Optional<T>` | Does not terminate |
| `findFirst()` / `findAny()` | `Optional<T>` | Can terminate |
| `anyMatch()` | `boolean` | May terminate |
| `allMatch()` | `boolean` | May terminate |
| `noneMatch()` | `boolean` | May terminate |
| `forEach()` | `void` | Does not terminate |
| `reduce()` | varies | Does not terminate |
| `collect()` | varies | Does not terminate |

These infinite-stream descriptions assume an unrestricted infinite pipeline.
Operations such as `limit()` can change the result.

---

### Matching

```text
anyMatch
→ one TRUE proves result TRUE

allMatch
→ one FALSE proves result FALSE

noneMatch
→ one TRUE proves result FALSE
```

Empty stream:

```text
anyMatch  → false
allMatch  → true
noneMatch → true
```

---

### Reduction

```text
reduce(accumulator)

→ Optional<T>
```

```text
reduce(identity, accumulator)

→ T
```

```text
reduce(identity, accumulator, combiner)

→ U

accumulator:
U + T → U

combiner:
U + U → U
```

---

### Optional

```text
get()
→ value OR exception

isPresent()
→ boolean

isEmpty()
→ boolean

ifPresent(Consumer)
→ action if present

orElse(value)
→ fallback value

orElseGet(Supplier)
→ lazy fallback

orElseThrow()
→ NoSuchElementException if empty

orElseThrow(Supplier)
→ supplied exception if empty
```

---

### Spliterator

```text
tryAdvance()
→ process ONE
→ boolean

forEachRemaining()
→ process REST

trySplit()
→ attempt to divide elements
→ another Spliterator or null
```

---

## Final Memory Kicks

```text
STREAM PIPELINE:

SOURCE
→ INTERMEDIATE
→ INTERMEDIATE
→ TERMINAL


INTERMEDIATE
→ returns Stream
→ LAZY

TERMINAL
→ produces result/action
→ starts processing
→ consumes stream


LAZINESS:

no terminal operation
→ generally no processing


INFINITE CREATION:

generate(Supplier)

iterate(seed, UnaryOperator)


FINITE / POSSIBLY FINITE ITERATE:

iterate(seed, Predicate, UnaryOperator)


SHORT-CIRCUIT:

findFirst / findAny
→ can stop

anyMatch
→ TRUE can stop

allMatch
→ FALSE can stop

noneMatch
→ TRUE can stop


REDUCE:

reduce(accumulator)
→ Optional<T>

reduce(identity, accumulator)
→ T

reduce(identity, accumulator, combiner)
→ U


OPTIONAL:

get
→ exception if empty

orElse
→ fallback VALUE

orElseGet
→ fallback SUPPLIER

orElseThrow
→ exception if empty


EMPTY MATCH:

anyMatch  → false
allMatch  → true
noneMatch → true


STREAMS ARE SINGLE USE:

terminal operation
→ stream consumed


SPLITERATOR:

tryAdvance
→ ONE

forEachRemaining
→ REST

trySplit
→ DIVIDE


INFINITE STREAM:

don't just ask:
"Is the source infinite?"

ask:
"CAN THIS ENTIRE PIPELINE SHORT-CIRCUIT OR BECOME FINITE?"
```