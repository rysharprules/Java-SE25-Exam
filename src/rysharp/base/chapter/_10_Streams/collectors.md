# Collectors

Collectors perform mutable reductions on streams, accumulating elements into collections, maps, strings, statistics, groups, partitions, or other result types.

## Contents

- [collect and Collectors](#collect-and-collectors)
- [Basic Collectors](#basic-collectors)
- [Joining](#joining)
- [Numeric Collectors](#numeric-collectors)
- [Min and Max](#min-and-max)
- [toMap](#tomap)
- [groupingBy](#groupingby)
- [partitioningBy](#partitioningby)
- [Downstream Collectors](#downstream-collectors)
- [mapping](#mapping)
- [filtering](#filtering)
- [teeing](#teeing)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## collect and Collectors

`collect()` is a terminal stream operation used to accumulate stream elements
into a result.

A common form accepts a `Collector`:

```java
<R, A> R collect(
        Collector<? super T, A, R> collector)
```

The `Collectors` utility class provides many predefined collectors.

For example:

```java
List<String> result =
        Stream.of("A", "B", "C")
                .collect(Collectors.toList());

System.out.println(result);
// [A, B, C]
```

Think of the relationship as:

```text
Stream
   ↓
collect(...)
   ↓
Collector
   ↓
RESULT
```

`collect()` is the **terminal operation**.

`Collectors.toList()` is a **factory method that creates a Collector**.

These are therefore different:

```java
stream.collect(...)
```

versus:

```java
Collectors.toList()
```

---

## Basic Collectors

### `toList()`

Collects elements into a `List`.

```java
List<String> result =
        Stream.of("A", "B", "C")
                .collect(Collectors.toList());
```

Result:

```text
[A, B, C]
```

Do not rely on a particular concrete `List` implementation.

---

### `toSet()`

Collects elements into a `Set`.

```java
Set<String> result =
        Stream.of("A", "B", "A", "C")
                .collect(Collectors.toSet());

System.out.println(result);
```

The duplicate `"A"` is removed.

Do not rely on:

- a particular concrete `Set` implementation
- a particular encounter order from the resulting set

---

### `toCollection()`

Use `toCollection()` when a particular collection implementation is required.

It accepts a `Supplier`.

```java
TreeSet<String> result =
        Stream.of("C", "A", "B")
                .collect(
                        Collectors.toCollection(
                                TreeSet::new
                        )
                );

System.out.println(result);
// [A, B, C]
```

Here:

```text
TreeSet::new
```

is the collection factory.

Another example:

```java
ArrayList<String> result =
        Stream.of("A", "B", "C")
                .collect(
                        Collectors.toCollection(
                                ArrayList::new
                        )
                );
```

Memory:

```text
toList()
→ some List

toSet()
→ some Set

toCollection(Supplier)
→ YOU choose collection type
```

---

### `counting()`

Counts the elements.

```java
long count =
        Stream.of("A", "B", "C")
                .collect(
                        Collectors.counting()
                );

System.out.println(count); // 3
```

The result type is the wrapper:

```text
Long
```

when considered as the Collector result type.

Compare this with:

```java
stream.count()
```

which returns primitive:

```text
long
```

Conceptually both count elements, but one is a direct stream terminal
operation and one is a Collector.

---

## Joining

`joining()` combines character sequences into a single `String`.

### No Delimiter

```java
String result =
        Stream.of("A", "B", "C")
                .collect(
                        Collectors.joining()
                );

System.out.println(result);
// ABC
```

---

### With Delimiter

```java
String result =
        Stream.of("A", "B", "C")
                .collect(
                        Collectors.joining(", ")
                );

System.out.println(result);
// A, B, C
```

---

### Delimiter, Prefix and Suffix

Another overload accepts:

```java
joining(
        delimiter,
        prefix,
        suffix
)
```

For example:

```java
String result =
        Stream.of("A", "B", "C")
                .collect(
                        Collectors.joining(
                                ", ",
                                "[",
                                "]"
                        )
                );

System.out.println(result);
// [A, B, C]
```

Memory:

```text
joining()
→ ABC

joining(",")
→ A,B,C

joining(",", "[", "]")
→ [A,B,C]
```

---

## Numeric Collectors

Collectors provide operations for averaging, summing and summarising numeric
values.

These operate on object streams using primitive functional interfaces.

For example:

```java
List<String> words =
        List.of("cat", "lion", "elephant");
```

We can average their lengths:

```java
double average =
        words.stream()
                .collect(
                        Collectors.averagingInt(
                                String::length
                        )
                );

System.out.println(average); // 5.0
```

The stream contains:

```text
String
```

but:

```text
String::length
```

maps each String to an `int`.

---

### Averaging

```text
averagingInt(ToIntFunction)
averagingLong(ToLongFunction)
averagingDouble(ToDoubleFunction)
```

All return:

```text
Double
```

as the collector result.

Memory:

```text
AVERAGING
→ result is Double
```

---

### Summing

```text
summingInt(ToIntFunction)
summingLong(ToLongFunction)
summingDouble(ToDoubleFunction)
```

Result types:

```text
summingInt
→ Integer

summingLong
→ Long

summingDouble
→ Double
```

Example:

```java
int total =
        Stream.of("cat", "lion")
                .collect(
                        Collectors.summingInt(
                                String::length
                        )
                );

System.out.println(total); // 7
```

---

### Summarizing

```text
summarizingInt(ToIntFunction)
summarizingLong(ToLongFunction)
summarizingDouble(ToDoubleFunction)
```

Return:

```text
IntSummaryStatistics
LongSummaryStatistics
DoubleSummaryStatistics
```

Example:

```java
IntSummaryStatistics stats =
        Stream.of("cat", "lion", "elephant")
                .collect(
                        Collectors.summarizingInt(
                                String::length
                        )
                );

System.out.println(stats.getCount());   // 3
System.out.println(stats.getSum());     // 15
System.out.println(stats.getMin());     // 3
System.out.println(stats.getMax());     // 8
System.out.println(stats.getAverage()); // 5.0
```

---

### Numeric Collector Pattern

```text
averagingX
→ Double

summingInt
→ Integer

summingLong
→ Long

summingDouble
→ Double

summarizingX
→ XSummaryStatistics
```

---

## Min and Max

Collectors provides:

```text
minBy(Comparator)
maxBy(Comparator)
```

They return:

```text
Optional<T>
```

Example:

```java
Optional<String> longest =
        Stream.of("cat", "lion", "elephant")
                .collect(
                        Collectors.maxBy(
                                Comparator.comparingInt(
                                        String::length
                                )
                        )
                );

System.out.println(longest.get());
// elephant
```

Why `Optional`?

The stream might be empty.

Compare:

```text
stream.max(comparator)

Collectors.maxBy(comparator)
```

Both can produce:

```text
Optional<T>
```

but one is a direct terminal operation and the other is a Collector.

---

## toMap

`toMap()` collects stream elements into a `Map`.

This is one of the more important Collector families because duplicate keys
require special handling.

---

### Key Mapper and Value Mapper

The simplest overload is conceptually:

```java
toMap(
        keyMapper,
        valueMapper
)
```

Both arguments are functions.

Example:

```java
Map<String, Integer> result =
        Stream.of("cat", "lion", "elephant")
                .collect(
                        Collectors.toMap(
                                word -> word,
                                String::length
                        )
                );

System.out.println(result);
```

Conceptually:

```text
"cat"
→ key   = "cat"
→ value = 3

"lion"
→ key   = "lion"
→ value = 4

"elephant"
→ key   = "elephant"
→ value = 8
```

Result:

```text
{
    cat=3,
    lion=4,
    elephant=8
}
```

Do not rely on iteration order from the default resulting map.

---

### Duplicate Keys

Consider:

```java
Stream.of("cat", "dog", "lion")
        .collect(
                Collectors.toMap(
                        String::length,
                        word -> word
                )
        );
```

The generated keys are:

```text
cat  → 3
dog  → 3
lion → 4
```

Both `"cat"` and `"dog"` produce key `3`.

The simple two-argument `toMap()` does not know how to resolve this duplicate
key.

It throws:

```text
IllegalStateException
```

Memory:

```text
toMap(keyMapper, valueMapper)

DUPLICATE KEY
→ IllegalStateException
```

---

### Merge Function

A third argument specifies how duplicate values should be combined:

```java
toMap(
        keyMapper,
        valueMapper,
        mergeFunction
)
```

The merge function is a:

```text
BinaryOperator<U>
```

For example:

```java
Map<Integer, String> result =
        Stream.of("cat", "dog", "lion")
                .collect(
                        Collectors.toMap(
                                String::length,
                                word -> word,
                                (first, second) ->
                                        first + "/" + second
                        )
                );

System.out.println(result);
```

Conceptually:

```text
key 3:

"cat"
+
"dog"

→ "cat/dog"
```

Result contains:

```text
3=cat/dog
4=lion
```

The merge function combines **values**, not keys.

Memory:

```text
DUPLICATE KEY

old value + new value
        ↓
  merge function
        ↓
   stored value
```

---

### Choosing the Map Implementation

A fourth argument can supply the map implementation:

```java
toMap(
        keyMapper,
        valueMapper,
        mergeFunction,
        mapSupplier
)
```

For example:

```java
TreeMap<Integer, String> result =
        Stream.of("cat", "dog", "lion")
                .collect(
                        Collectors.toMap(
                                String::length,
                                word -> word,
                                (first, second) ->
                                        first + "/" + second,
                                TreeMap::new
                        )
                );
```

The final argument is a:

```text
Supplier<Map>
```

Conceptually:

```text
2 args
→ keys + values

3 args
→ keys + values + duplicate handling

4 args
→ keys + values + duplicate handling
  + map implementation
```

---

## groupingBy

`groupingBy()` groups elements according to a classification function.

The simplest form is:

```java
groupingBy(classifier)
```

For example:

```java
Map<Integer, List<String>> result =
        Stream.of(
                "cat",
                "dog",
                "lion",
                "bear",
                "elephant"
        )
        .collect(
                Collectors.groupingBy(
                        String::length
                )
        );
```

Conceptually:

```text
length 3
→ cat
→ dog

length 4
→ lion
→ bear

length 8
→ elephant
```

Result:

```text
3 → [cat, dog]
4 → [lion, bear]
8 → [elephant]
```

Default shape:

```text
Map<K, List<T>>
```

where the classifier determines `K`.

---

### Classification Function

The classifier is a:

```text
Function<T, K>
```

For:

```java
Collectors.groupingBy(
        String::length
)
```

the function is:

```text
String → Integer
```

Therefore the map key is:

```text
Integer
```

---

### `groupingBy()` with a Downstream Collector

Instead of collecting each group into a `List`, a second collector can process
each group.

For example:

```java
Map<Integer, Long> result =
        Stream.of(
                "cat",
                "dog",
                "lion",
                "bear",
                "elephant"
        )
        .collect(
                Collectors.groupingBy(
                        String::length,
                        Collectors.counting()
                )
        );
```

Result:

```text
3 → 2
4 → 2
8 → 1
```

The result changed from:

```text
Map<Integer, List<String>>
```

to:

```text
Map<Integer, Long>
```

because the downstream collector is:

```java
Collectors.counting()
```

---

### Choosing the Map Type

A three-argument form can specify the map implementation:

```java
groupingBy(
        classifier,
        mapFactory,
        downstreamCollector
)
```

Example:

```java
TreeMap<Integer, Long> result =
        Stream.of(
                "cat",
                "dog",
                "lion"
        )
        .collect(
                Collectors.groupingBy(
                        String::length,
                        TreeMap::new,
                        Collectors.counting()
                )
        );
```

Memory:

```text
groupingBy(classifier)
→ Map<K, List<T>>

groupingBy(classifier, downstream)
→ Map<K, D>

groupingBy(classifier, mapFactory, downstream)
→ chosen Map<K, D>
```

---

## partitioningBy

`partitioningBy()` separates elements based on a `Predicate`.

Since a Predicate produces only:

```text
true
false
```

the result is:

```text
Map<Boolean, ...>
```

Example:

```java
Map<Boolean, List<Integer>> result =
        Stream.of(1, 2, 3, 4, 5)
                .collect(
                        Collectors.partitioningBy(
                                n -> n % 2 == 0
                        )
                );
```

Conceptually:

```text
true
→ [2, 4]

false
→ [1, 3, 5]
```

Result type:

```text
Map<Boolean, List<Integer>>
```

---

### `partitioningBy()` with Downstream Collector

A downstream collector can process each partition.

```java
Map<Boolean, Long> result =
        Stream.of(1, 2, 3, 4, 5)
                .collect(
                        Collectors.partitioningBy(
                                n -> n % 2 == 0,
                                Collectors.counting()
                        )
                );
```

Result:

```text
true  → 2
false → 3
```

---

### `groupingBy()` vs `partitioningBy()`

This distinction is important.

```text
groupingBy(Function)

Function can produce MANY keys
```

For example:

```text
length:

3
4
5
6
7
...
```

Whereas:

```text
partitioningBy(Predicate)

Predicate produces exactly:
true / false
```

Memory:

```text
GROUP
→ classify
→ potentially MANY groups

PARTITION
→ Predicate
→ TRUE / FALSE
```

---

## Downstream Collectors

A downstream collector processes the elements inside another collector's
groups.

For example:

```java
Collectors.groupingBy(
        String::length,
        Collectors.counting()
)
```

Think:

```text
FIRST:

group by length

3 → cat, dog
4 → lion, bear


THEN:

apply counting() INSIDE each group

3 → 2
4 → 2
```

The downstream collector changes the map's **values**.

Without downstream collector:

```text
Map<Integer, List<String>>
```

With:

```java
counting()
```

the result becomes:

```text
Map<Integer, Long>
```

Many collectors can be used downstream:

```text
counting()
mapping(...)
filtering(...)
toList()
toSet()
joining(...)
maxBy(...)
minBy(...)
```

This ability to nest collectors is one reason collector expressions can appear
complicated.

Read them from the outside inward:

```java
groupingBy(
    classifier,
    downstream
)
```

Ask:

```text
1. What creates the groups?
2. What happens INSIDE each group?
```

---

## mapping

`Collectors.mapping()` transforms elements before passing them to a downstream
collector.

Shape:

```java
mapping(
        mapper,
        downstreamCollector
)
```

For example:

```java
Map<Integer, Set<Character>> result =
        Stream.of(
                "cat",
                "dog",
                "lion",
                "bear"
        )
        .collect(
                Collectors.groupingBy(
                        String::length,
                        Collectors.mapping(
                                word -> word.charAt(0),
                                Collectors.toSet()
                        )
                )
        );
```

Conceptually:

```text
FIRST:
group by length

3 → cat, dog
4 → lion, bear


INSIDE each group:
map word → first character

3 → c, d
4 → l, b


THEN:
collect into Set
```

Result:

```text
3 → [c, d]
4 → [l, b]
```

Think:

```text
mapping
→ TRANSFORM
→ then downstream collector
```

---

## filtering

`Collectors.filtering()` filters elements before passing them to a downstream
collector.

Shape:

```java
filtering(
        predicate,
        downstreamCollector
)
```

For example:

```java
Map<Integer, List<String>> result =
        Stream.of(
                "cat",
                "dog",
                "lion",
                "bear"
        )
        .collect(
                Collectors.groupingBy(
                        String::length,
                        Collectors.filtering(
                                word ->
                                        word.startsWith("b"),
                                Collectors.toList()
                        )
                )
        );
```

Conceptually:

```text
FIRST:
group by length

3 → cat, dog
4 → lion, bear


INSIDE each group:
keep words beginning with b

3 → []
4 → [bear]
```

The important idea is that the filtering occurs **downstream of the grouping**.

This is not necessarily equivalent to filtering the stream before grouping.

For example:

```java
stream
        .filter(word -> word.startsWith("b"))
        .collect(
                Collectors.groupingBy(
                        String::length
                )
        );
```

may omit a group entirely if no elements survive.

With downstream:

```java
groupingBy(
        String::length,
        filtering(
                predicate,
                toList()
        )
)
```

a group created by the upstream elements can remain with an empty downstream
result:

```text
3 → []
```

Memory:

```text
stream.filter(...)
→ filter BEFORE groups exist

Collectors.filtering(...)
→ groups FIRST
→ filter INSIDE each group
```

---

## teeing

`teeing()` sends every stream element to **two collectors** and then combines
their two results.

Shape:

```java
teeing(
        collector1,
        collector2,
        merger
)
```

The merger is a:

```text
BiFunction
```

Conceptually:

```text
                 ┌→ COLLECTOR 1 → result A ─┐
STREAM ELEMENTS ─┤                          ├→ merger → FINAL RESULT
                 └→ COLLECTOR 2 → result B ─┘
```

---

### Example

Suppose we want both:

```text
number of elements
+
sum of elements
```

We can use:

```java
String result =
        Stream.of(10, 20, 30)
                .collect(
                        Collectors.teeing(
                                Collectors.counting(),
                                Collectors.summingInt(
                                        Integer::intValue
                                ),
                                (count, sum) ->
                                        count + ":" + sum
                        )
                );

System.out.println(result);
// 3:60
```

Collector 1:

```java
Collectors.counting()
```

produces:

```text
3
```

Collector 2:

```java
Collectors.summingInt(
        Integer::intValue
)
```

produces:

```text
60
```

The merger receives:

```text
3 and 60
```

and produces:

```text
"3:60"
```

Memory:

```text
teeing

SAME INPUT
   ↓
 ┌─┴─┐
 C1  C2
 ↓    ↓
 R1  R2
 └─┬──┘
 MERGE
   ↓
 FINAL RESULT
```

The name makes more sense if you picture the stream splitting like a letter
`T` into two collectors.

---

## Quick Reference

### Basic Collectors

| Collector | Result |
|---|---|
| `toList()` | `List<T>` |
| `toSet()` | `Set<T>` |
| `toCollection(Supplier)` | chosen collection |
| `counting()` | `Long` |
| `joining()` | `String` |
| `minBy()` | `Optional<T>` |
| `maxBy()` | `Optional<T>` |

---

### Numeric Collectors

```text
averagingInt
averagingLong
averagingDouble

→ Double
```

```text
summingInt
→ Integer

summingLong
→ Long

summingDouble
→ Double
```

```text
summarizingInt
→ IntSummaryStatistics

summarizingLong
→ LongSummaryStatistics

summarizingDouble
→ DoubleSummaryStatistics
```

---

### `toMap()`

```text
toMap(keyMapper, valueMapper)

→ duplicate key = IllegalStateException
```

```text
toMap(
    keyMapper,
    valueMapper,
    mergeFunction
)

→ merge handles duplicate values
```

```text
toMap(
    keyMapper,
    valueMapper,
    mergeFunction,
    mapSupplier
)

→ choose Map implementation
```

---

### `groupingBy()`

```text
groupingBy(classifier)

→ Map<K, List<T>>
```

```text
groupingBy(
    classifier,
    downstream
)

→ Map<K, D>
```

```text
groupingBy(
    classifier,
    mapFactory,
    downstream
)

→ chosen Map<K, D>
```

---

### `partitioningBy()`

```text
partitioningBy(Predicate)

→ Map<Boolean, List<T>>
```

```text
partitioningBy(
    Predicate,
    downstream
)

→ Map<Boolean, D>
```

---

### Group vs Partition

```text
groupingBy
→ Function
→ MANY possible keys
```

```text
partitioningBy
→ Predicate
→ TRUE / FALSE
```

---

### Downstream Collectors

```text
OUTER collector
→ creates groups/partitions

DOWNSTREAM collector
→ processes elements INSIDE them
```

```text
mapping
→ transform inside downstream processing

filtering
→ filter inside downstream processing
```

---

### `teeing()`

```text
teeing(
    collector1,
    collector2,
    merger
)

INPUT
→ BOTH collectors

two results
→ BiFunction

BiFunction result
→ final result
```

---

## Final Memory Kicks

```text
COLLECT:

stream.collect(Collector)

Collectors.xxx()
→ creates Collector
```

```text
TO COLLECTION:

toList
→ List

toSet
→ Set

toCollection(Supplier)
→ choose implementation
```

```text
JOIN:

joining()
→ ABC

joining(",")
→ A,B,C

joining(",", "[", "]")
→ [A,B,C]
```

```text
NUMERIC:

averagingX
→ Double

summingX
→ wrapper matching X

summarizingX
→ XSummaryStatistics
```

```text
TO MAP:

2 args
→ key + value

duplicate key
→ IllegalStateException


3 args
→ key + value + MERGE


4 args
→ key + value + MERGE + MAP TYPE
```

```text
GROUPING:

Function<T,K>

→ potentially MANY keys

default:
Map<K, List<T>>
```

```text
PARTITIONING:

Predicate<T>

→ exactly BOOLEAN keys

Map<Boolean, ...>
```

```text
DOWNSTREAM:

group first
→ THEN process each group
```

```text
MAPPING:

transform
→ downstream
```

```text
FILTERING:

group first
→ filter INSIDE group
```

```text
TEEING:

         ┌→ collector 1 ─┐
INPUT ───┤               ├→ MERGE → RESULT
         └→ collector 2 ─┘
```

```text
WHEN A COLLECTOR EXPRESSION LOOKS HORRIBLE:

READ OUTSIDE → INSIDE

1. What is the outer operation?
2. What creates the keys/groups?
3. What happens downstream?
4. What is the final value type?
```