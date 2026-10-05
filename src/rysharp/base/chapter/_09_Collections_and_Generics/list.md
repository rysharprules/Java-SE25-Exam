# List

A `List` is an ordered collection that supports positional access and allows duplicate elements.

## Contents

- [List Characteristics](#list-characteristics)
- [Common List Methods](#common-list-methods)
- [Adding Elements](#adding-elements)
- [Getting and Replacing Elements](#getting-and-replacing-elements)
- [Removing Elements](#removing-elements)
- [Finding Elements](#finding-elements)
- [replaceAll()](#replaceall)
- [subList()](#sublist)
- [ArrayList](#arraylist)
- [LinkedList](#linkedlist)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## List Characteristics

`List<E>` extends `SequencedCollection<E>`.

A `List`:

- maintains encounter order
- allows duplicate elements
- provides zero-based indexed access
- can generally contain `null`, depending on the implementation
- provides first/last operations through `SequencedCollection`

```java
List<String> list = new ArrayList<>();

list.add("A");
list.add("B");
list.add("A");

System.out.println(list);        // [A, B, A]
System.out.println(list.get(1)); // B
```

List creation and mutability are covered elsewhere:

- `List.of()`, `List.copyOf()`, `Collections.singletonList()` and
  `Collections.unmodifiableList()` → [Collections](collections.md#collection-factory-methods)
- `Arrays.asList()` → [Array Operations](../_04_Core_APIs/array_operations.md#arraysaslist)

---

## Common List Methods

Important `List` operations include:

| Method | Purpose |
|---|---|
| `add(E e)` | Add at the end |
| `add(int index, E e)` | Insert at an index |
| `get(int index)` | Retrieve an element |
| `set(int index, E e)` | Replace an existing element |
| `remove(int index)` | Remove by index |
| `remove(Object o)` | Remove first matching element |
| `indexOf(Object o)` | Find first matching index |
| `lastIndexOf(Object o)` | Find last matching index |
| `replaceAll(UnaryOperator<E>)` | Replace each element |
| `subList(int from, int to)` | Create a backed range view |

Remember that many methods such as `contains()`, `size()`, `isEmpty()` and
`clear()` come from `Collection`.

---

## Adding Elements

### `add(E)`

Adds an element to the end of the list.

```java
List<String> list = new ArrayList<>();

list.add("A");
list.add("B");

System.out.println(list); // [A, B]
```

`add(E)` returns a `boolean`:

```java
boolean result = list.add("C");

System.out.println(result); // true
```

For ordinary mutable lists such as `ArrayList`, adding an element succeeds and
returns `true`.

---

### `add(int, E)`

Inserts an element at a specific index.

```java
List<String> list =
        new ArrayList<>(List.of("A", "C"));

list.add(1, "B");

System.out.println(list); // [A, B, C]
```

Existing elements from that position onwards are shifted to the right.

The valid insertion range is:

```text
0 <= index <= size()
```

This means this is valid:

```java
list.add(list.size(), "D");
```

It appends `"D"` to the end.

But:

```java
list.add(list.size() + 1, "E");
```

throws `IndexOutOfBoundsException`.

---

## Getting and Replacing Elements

### `get()`

Retrieves an element without modifying the list.

```java
List<String> list =
        new ArrayList<>(List.of("A", "B", "C"));

System.out.println(list.get(1)); // B
```

Valid indexes are:

```text
0 <= index < size()
```

---

### `set()`

Replaces an element already at an index.

```java
List<String> list =
        new ArrayList<>(List.of("A", "B", "C"));

String old = list.set(1, "X");

System.out.println(old);  // B
System.out.println(list); // [A, X, C]
```

`set()` returns the element that was replaced.

It does **not** insert an element or change the size of the list.

Compare:

```java
list.set(1, "X"); // replace index 1
list.add(1, "X"); // insert at index 1
```

If the list has size `3`:

```java
list.set(3, "X"); // IndexOutOfBoundsException
list.add(3, "X"); // valid: appends
```

Therefore:

```text
get/set:  index < size()
add:      index <= size()
```

---

## Removing Elements

`List` has an important overloaded `remove()` method.

```java
E remove(int index)
boolean remove(Object o)
```

### Remove by index

```java
List<String> list =
        new ArrayList<>(List.of("A", "B", "C"));

String removed = list.remove(1);

System.out.println(removed); // B
System.out.println(list);    // [A, C]
```

---

### Remove by object

```java
List<String> list =
        new ArrayList<>(List.of("A", "B", "A"));

boolean removed = list.remove("A");

System.out.println(removed); // true
System.out.println(list);    // [B, A]
```

Only the **first matching element** is removed.

---

### The `Integer` Problem

This distinction becomes particularly important with `List<Integer>`.

```java
List<Integer> numbers =
        new ArrayList<>(List.of(10, 20, 30));

numbers.remove(1);
```

The argument is primitive `int`, so Java selects:

```java
remove(int index)
```

Therefore it removes the value at index `1`:

```text
[10, 30]
```

To remove the actual value `1`:

```java
numbers.remove(Integer.valueOf(1));
```

Now Java selects:

```java
remove(Object)
```

Another example:

```java
List<Integer> numbers =
        new ArrayList<>(List.of(1, 2, 3));

numbers.remove(1);

System.out.println(numbers); // [1, 3]
```

It removes the element at **index 1**, which is `2`, rather than removing the
value `1`.

Memory rule:

```text
remove(int)     → INDEX
remove(Object)  → VALUE
```

---

## Finding Elements

### `indexOf()`

Returns the index of the first matching element.

```java
List<String> list =
        List.of("A", "B", "A", "C");

System.out.println(list.indexOf("A")); // 0
System.out.println(list.indexOf("C")); // 3
System.out.println(list.indexOf("X")); // -1
```

---

### `lastIndexOf()`

Returns the index of the last matching element.

```java
System.out.println(list.lastIndexOf("A")); // 2
```

If the element does not exist:

```java
System.out.println(list.lastIndexOf("X")); // -1
```

Therefore:

```text
indexOf()     → first occurrence
lastIndexOf() → last occurrence
not found     → -1
```

---

## `replaceAll()`

`replaceAll()` applies a `UnaryOperator` to every element and stores the
result back into the list.

```java
List<String> list =
        new ArrayList<>(List.of("a", "b", "c"));

list.replaceAll(String::toUpperCase);

System.out.println(list); // [A, B, C]
```

Equivalent lambda:

```java
list.replaceAll(s -> s.toUpperCase());
```

The important functional-interface shape is:

```text
UnaryOperator<T>

T → T
```

Each existing element is replaced with the value returned by the operator.

---

## `subList()`

```java
List<E> subList(int fromIndex, int toIndex)
```

The range is:

```text
[fromIndex, toIndex)
```

The start is **inclusive** and the end is **exclusive**.

```java
List<String> list =
        new ArrayList<>(List.of("A", "B", "C", "D", "E"));

List<String> sub = list.subList(1, 4);

System.out.println(sub); // [B, C, D]
```

The indexes are:

```text
          0    1    2    3    4
list =   [A,   B,   C,   D,   E]
               └─────────┘
                [1, 4)
```

### `subList()` Is a View

`subList()` does **not** create an independent copy.

The returned list is backed by the original list.

```java
List<String> list =
        new ArrayList<>(List.of("A", "B", "C", "D"));

List<String> sub = list.subList(1, 3);

sub.set(0, "X");

System.out.println(sub);  // [X, C]
System.out.println(list); // [A, X, C, D]
```

Structural changes through the sublist also affect the original:

```java
sub.remove(1);

System.out.println(sub);  // [X]
System.out.println(list); // [A, X, D]
```

Think:

```text
subList()
→ VIEW, NOT COPY
```

This is similar to the idea behind `reversed()` — both provide views backed by
the underlying list.

### Structural Changes Outside the View

Be careful about structurally modifying the original list while a sublist
view is being used.

```java
List<String> list =
        new ArrayList<>(List.of("A", "B", "C", "D"));

List<String> sub = list.subList(1, 3);

list.add("E");

// Further use of sub is unsafe and may throw ConcurrentModificationException
```

A structural modification changes the size of the list, such as `add()`,
`remove()` or `clear()`.
For `ArrayList`, subsequent use of the sublist after such a modification is
typically detected and results in `ConcurrentModificationException`.

---

## ArrayList

`ArrayList<E>` is a resizable-array implementation of `List`.

```java
List<String> list = new ArrayList<>();
```

Characteristics:

- maintains insertion order
- allows duplicates
- allows `null`
- fast indexed access
- dynamically grows as elements are added

Conceptually:

```text
index:   0    1    2    3
        ┌────┬────┬────┬────┐
        │ A  │ B  │ C  │ D  │
        └────┴────┴────┴────┘
```

Getting an element by index is efficient:

```java
list.get(2);
```

Inserting or removing near the beginning/middle may require later elements to
be shifted.

```java
list.add(1, "X");
```

Conceptually:

```text
Before:

[A, B, C, D]

After:

[A, X, B, C, D]
       └──────→ shifted
```

---

## LinkedList

`LinkedList<E>` implements both:

```text
List<E>
Deque<E>
```

Therefore it can be used as a list:

```java
List<String> list = new LinkedList<>();
```

or as a deque:

```java
Deque<String> deque = new LinkedList<>();
```

As a `List`, it:

- maintains encounter order
- allows duplicates
- allows `null`
- supports indexed operations
- does not provide the same efficient random indexed access as `ArrayList`

Conceptually, its elements are linked:

```text
[A] ↔ [B] ↔ [C] ↔ [D]
```

Finding an element by index may require traversing the links.

```java
list.get(3);
```

The `Deque` behaviour of `LinkedList` is covered in [Queue & Deque](queue_deque.md) rather
than duplicated here.

---

## ArrayList vs LinkedList

| Feature | `ArrayList` | `LinkedList` |
|---|---|---|
| Implements `List` | Yes | Yes |
| Implements `Deque` | No | Yes |
| Allows duplicates | Yes | Yes |
| Allows `null` | Yes | Yes |
| Indexed access | Fast | Requires traversal |
| Internal concept | Resizable array | Doubly-linked nodes |

For most general-purpose list usage, `ArrayList` is the usual implementation.

The main certification distinction is structural:

```text
ArrayList  → List
LinkedList → List + Deque
```

---

## Quick Reference

| Operation | Behaviour |
|---|---|
| `add(e)` | Append |
| `add(i, e)` | Insert at index |
| `get(i)` | Retrieve |
| `set(i, e)` | Replace and return old value |
| `remove(i)` | Remove by index |
| `remove(obj)` | Remove first matching value |
| `indexOf(obj)` | First matching index |
| `lastIndexOf(obj)` | Last matching index |
| `replaceAll(op)` | Replace every element |
| `subList(a, b)` | Backed view of `[a, b)` |
| `reversed()` | Backed reverse-order view |

Index rules:

```text
get/set/remove: 0 <= index < size()
add:            0 <= index <= size()
```

Removal:

```text
remove(int)    → INDEX
remove(Object) → VALUE
```

Views:

```text
subList()  → VIEW, NOT COPY
reversed() → VIEW, NOT COPY
```

Implementations:

```text
ArrayList  → List
LinkedList → List + Deque
```

---

## Final Memory Kicks

```text
LIST
→ ordered
→ indexed
→ duplicates allowed

add(index, value)
→ INSERT
→ size increases

set(index, value)
→ REPLACE
→ size unchanged
→ returns old value

remove(int)
→ INDEX

remove(Object)
→ VALUE

indexOf()
→ FIRST match

lastIndexOf()
→ LAST match

subList(from, to)
→ [from, to)
→ BACKED VIEW

replaceAll()
→ UnaryOperator
→ T → T

ArrayList
→ resizable array
→ fast indexed access

LinkedList
→ List + Deque
→ linked nodes
```