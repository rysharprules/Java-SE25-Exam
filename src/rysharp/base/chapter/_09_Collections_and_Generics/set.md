# Set

[🔙 Back](README.md)

A `Set` is a collection that contains no duplicate elements. Different implementations determine whether elements have encounter order, insertion order, or sorted order.

## Contents

- [Set Characteristics](#set-characteristics)
- [Adding Duplicate Elements](#adding-duplicate-elements)
- [HashSet](#hashset)
- [LinkedHashSet](#linkedhashset)
- [TreeSet](#treeset)
- [SortedSet](#sortedset)
- [NavigableSet](#navigableset)
- [Range Views](#range-views)
- [TreeSet and Comparison](#treeset-and-comparison)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Set Characteristics

`Set<E>` extends `Collection<E>`.

A `Set`:

- contains no duplicate elements
- does not provide indexed access
- uses implementation-specific ordering
- may allow `null`, depending on the implementation

```java
Set<String> set = new HashSet<>();

set.add("A");
set.add("B");
set.add("A");

System.out.println(set.size()); // 2
```

Set creation and mutability using methods such as `Set.of()`,
`Collections.singleton()` and `Collections.unmodifiableSet()` are covered in
[Collections](collections.md).

---

## Adding Duplicate Elements

`add()` returns a `boolean` indicating whether the set changed.

```java
Set<String> set = new HashSet<>();

System.out.println(set.add("A")); // true
System.out.println(set.add("B")); // true
System.out.println(set.add("A")); // false
```

The second attempt to add `"A"` does not modify the set because `"A"` is
already present.

```text
add(new element)      → true
add(duplicate element) → false
```

This differs from a `List`, where duplicate elements are allowed.

---

## HashSet

`HashSet<E>` is a general-purpose implementation of `Set`.

```java
Set<String> set = new HashSet<>();

set.add("B");
set.add("A");
set.add("C");
```

Characteristics:

- no duplicates
- no guaranteed encounter order
- allows `null`
- uses hashing to store and locate elements

Do not rely on the order produced when iterating over a `HashSet`.

```java
for (String value : set) {
    System.out.println(value);
}
```

The iteration order is not guaranteed to match insertion order or sorted
order.

---

## LinkedHashSet

`LinkedHashSet<E>` maintains elements in insertion order.

```java
Set<String> set = new LinkedHashSet<>();

set.add("B");
set.add("A");
set.add("C");

System.out.println(set); // [B, A, C]
```

Characteristics:

- no duplicates
- maintains insertion order
- allows `null`
- implements `SequencedSet`

Compare:

```text
HashSet
→ no guaranteed encounter order

LinkedHashSet
→ insertion order

TreeSet
→ sorted order
```

Because `LinkedHashSet` is sequenced, it also supports first/last and reversed
view operations inherited through `SequencedSet`.

The general sequenced-collection API is covered in
[Collections](collections.md#sequenced-collections).

---

## TreeSet

`TreeSet<E>` stores its elements in sorted order.

```java
Set<Integer> set = new TreeSet<>();

set.add(30);
set.add(10);
set.add(20);

System.out.println(set); // [10, 20, 30]
```

Characteristics:

- no duplicates
- sorted
- does not allow `null`
- implements `NavigableSet`
- uses natural ordering or a supplied `Comparator`
- does not provide indexed access

A `TreeSet` can be created with a custom comparator:

```java
Set<String> set =
        new TreeSet<>(Comparator.reverseOrder());

set.add("A");
set.add("C");
set.add("B");

System.out.println(set); // [C, B, A]
```

`Comparator`, natural ordering and custom ordering are covered in
[Collections](collections.md#comparator).

---

## SortedSet

`SortedSet<E>` represents a set whose elements are maintained in sorted order.

`NavigableSet<E>` extends `SortedSet<E>`, and `TreeSet<E>` implements
`NavigableSet<E>`.

Conceptually:

```text
Set
│
└── SequencedSet
      ↑
   SortedSet
      ↑
 NavigableSet
      ↑
   TreeSet
```

`SortedSet` provides methods including:

```java
first()
last()

headSet(toElement)
tailSet(fromElement)
subSet(fromElement, toElement)
```

Example:

```java
SortedSet<Integer> set =
        new TreeSet<>(List.of(10, 20, 30, 40, 50));

System.out.println(set.first()); // 10
System.out.println(set.last());  // 50
```

---

## NavigableSet

`NavigableSet` adds methods for finding the closest elements relative to a
given value.

The four important methods are:

| Method | Meaning |
|---|---|
| `lower(e)` | greatest element `< e` |
| `floor(e)` | greatest element `<= e` |
| `ceiling(e)` | smallest element `>= e` |
| `higher(e)` | smallest element `> e` |

Given:

```java
NavigableSet<Integer> set =
        new TreeSet<>(List.of(10, 20, 30, 40, 50));
```

For the value `30`:

```text
lower(30)    → 20    <
floor(30)    → 30    <=

ceiling(30)  → 30    >=
higher(30)   → 40    >
```

If the exact value is not present:

```java
System.out.println(set.lower(25));   // 20
System.out.println(set.floor(25));   // 20
System.out.println(set.ceiling(25)); // 30
System.out.println(set.higher(25));  // 30
```

If no suitable element exists, these methods return `null`:

```java
System.out.println(set.lower(10));  // null
System.out.println(set.higher(50)); // null
```

### Memory Pattern

```text
lower    → <
floor    → <=

ceiling  → >=
higher   → >
```

Think of:

```text
        20         30         40
         ↑          ↑          ↑
       lower    floor/ceiling higher
                  for 30
```

---

## Range Views

`SortedSet` and `NavigableSet` provide views over portions of the sorted set.

These are **backed views**, not independent copies.

### `headSet()`

The one-argument version:

```java
headSet(toElement)
```

means:

```text
elements < toElement
```

The boundary is **exclusive**.

```java
SortedSet<Integer> set =
        new TreeSet<>(List.of(10, 20, 30, 40, 50));

SortedSet<Integer> head = set.headSet(30);

System.out.println(head); // [10, 20]
```

`30` itself is excluded.

```text
headSet(30)

10   20   |30|   40   50
───────────┘
   included
```

---

### `tailSet()`

The one-argument version:

```java
tailSet(fromElement)
```

means:

```text
elements >= fromElement
```

The boundary is **inclusive**.

```java
SortedSet<Integer> tail = set.tailSet(30);

System.out.println(tail); // [30, 40, 50]
```

So:

```text
headSet(x) → < x
tailSet(x) → >= x
```

They divide the set cleanly:

```text
            boundary
               ↓
10   20   |   30   40   50
──────────    ─────────────
 headSet        tailSet
   < 30          >= 30
```
Nothing is duplicated and nothing is missing.

Memory rule:
> headSet(x) → BEFORE x → <
> tailSet(x) → FROM x   → >=

---

### `subSet()`

The two-argument `SortedSet` version:

```java
subSet(fromElement, toElement)
```

uses the familiar half-open range:

```text
[fromElement, toElement)
```

Therefore:

```java
SortedSet<Integer> set =
        new TreeSet<>(List.of(10, 20, 30, 40, 50));

SortedSet<Integer> middle =
        set.subSet(20, 40);

System.out.println(middle); // [20, 30]
```

`20` is included and `40` is excluded.

Memory rule:

```text
headSet(to)       → < to

tailSet(from)     → >= from

subSet(from, to)  → [from, to)
                   → >= from && < to
```

---

### NavigableSet Boundary Control

`NavigableSet` provides overloaded versions where the inclusivity can be
specified explicitly.

```java
NavigableSet<Integer> set =
        new TreeSet<>(List.of(10, 20, 30, 40, 50));
```

For `headSet`:

```java
set.headSet(30, false); // [10, 20]
set.headSet(30, true);  // [10, 20, 30]
```

For `tailSet`:

```java
set.tailSet(30, true);  // [30, 40, 50]
set.tailSet(30, false); // [40, 50]
```

For `subSet`:

```java
set.subSet(20, true, 40, false); // [20, 30]
set.subSet(20, true, 40, true);  // [20, 30, 40]
```

The four-argument form is:

```java
subSet(
    fromElement,
    fromInclusive,
    toElement,
    toInclusive
)
```

The booleans directly control whether each boundary is included.

---

### Range Methods Return Views

Like `List.subList()`, these methods return views backed by the original
collection.

```java
NavigableSet<Integer> set =
        new TreeSet<>(List.of(10, 20, 30, 40, 50));

NavigableSet<Integer> head =
        set.headSet(30, true);

head.remove(20);

System.out.println(head); // [10, 30]
System.out.println(set);  // [10, 30, 40, 50]
```

Changes through the view affect the original set.

The view also remains constrained to its range.

For example:

```java
head.add(25); // valid
```

but:

```java
head.add(40); // IllegalArgumentException
```

`40` lies outside the range represented by `head`.

Memory rule:

```text
headSet / tailSet / subSet
→ BACKED RANGE VIEWS
→ modifications affect the original
→ elements must remain inside the view's range
```

---

## TreeSet and Comparison

A `TreeSet` determines both **ordering and uniqueness** using comparison.

This is an important distinction from `HashSet`.

Consider:

```java
record Person(String name, int age) {}
```

and:

```java
Comparator<Person> byAge =
        Comparator.comparingInt(Person::age);

Set<Person> people = new TreeSet<>(byAge);

people.add(new Person("Alice", 30));
people.add(new Person("Bob", 30));
```

The comparator considers both objects equal for ordering because:

```text
compare(Alice, Bob) == 0
```

Therefore the `TreeSet` treats the second object as a duplicate.

```java
System.out.println(people.size()); // 1
```

Even though:

```java
new Person("Alice", 30).equals(
        new Person("Bob", 30)
)
```

is `false`.

For a `TreeSet`:

```text
comparison result == 0
→ same position in ordering
→ duplicate as far as the TreeSet is concerned
```

This is why a comparator used with sorted sets should normally be consistent
with `equals`.

---

## Quick Reference

### Implementations

| Implementation | Ordering | `null` |
|---|---|---|
| `HashSet` | No guaranteed encounter order | Allowed |
| `LinkedHashSet` | Insertion order | Allowed |
| `TreeSet` | Sorted order | Generally not usable with natural ordering |

### Navigable Methods

```text
lower(e)   → <
floor(e)   → <=
ceiling(e) → >=
higher(e)  → >
```

No matching element:

```text
→ null
```

### SortedSet Ranges

```text
headSet(x)       → < x

tailSet(x)       → >= x

subSet(a, b)     → [a, b)
                  → >= a && < b
```

### NavigableSet Ranges

```text
headSet(x, inclusive)

tailSet(x, inclusive)

subSet(
    from,
    fromInclusive,
    to,
    toInclusive
)
```

These return:

```text
BACKED RANGE VIEWS
```

### Set Ordering

```text
HashSet
→ no guaranteed order

LinkedHashSet
→ insertion order

TreeSet
→ sorted order
```

---

## Final Memory Kicks

```text
SET
→ NO DUPLICATES

HashSet
→ HASH
→ no guaranteed order

LinkedHashSet
→ LINKED
→ insertion order

TreeSet
→ TREE
→ sorted order

add(new)
→ true

add(duplicate)
→ false

NAVIGABLE:

lower   → <
floor   → <=

ceiling → >=
higher  → >

SORTED RANGES:

headSet(x)
→ < x
→ EXCLUDES x

tailSet(x)
→ >= x
→ INCLUDES x

subSet(a, b)
→ [a, b)
→ INCLUDES a
→ EXCLUDES b

RANGE METHODS
→ BACKED VIEWS

TreeSet uniqueness
→ compare() == 0 means duplicate
```