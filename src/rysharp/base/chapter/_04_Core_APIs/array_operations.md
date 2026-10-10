# Array Operations

[🔙 Back](README.md)

Quick reference for common array operations and the `java.util.Arrays` API.

```java
import java.util.Arrays;
```

## Contents

- [Array Length](#array-length)
- [Arrays.sort()](#arrayssort)
- [Arrays.binarySearch()](#arraysbinarysearch)
- [Arrays.equals()](#arraysequals)
- [Arrays.deepEquals()](#arraysdeepequals)
- [Arrays.compare()](#arrayscompare)
- [Arrays.mismatch()](#arraysmismatch)
- [Arrays.toString() and deepToString()](#arraystostring-and-deeptostring)
- [Arrays.fill()](#arraysfill)
- [Arrays.copyOf()](#arrayscopyof)
- [Arrays.copyOfRange()](#arrayscopyofrange)
- [Arrays.asList()](#arraysaslist)
- [Quick Reference](#quick-reference)

---

## Array Length

Arrays have a **field** called `length`, not a method.

```java
int[] numbers = {10, 20, 30};

System.out.println(numbers.length);   // 3
```

Remember:

```text
array.length
String.length()
List.size()
```

For an array of length `n`:

```text
first index = 0
last index  = n - 1
```

An invalid index normally causes:

```text
ArrayIndexOutOfBoundsException
```

at runtime.

---

## `Arrays.sort()`

Sorts the **existing array** into ascending/natural order.

```java
int[] numbers = {5, 2, 8, 1};

Arrays.sort(numbers);

System.out.println(Arrays.toString(numbers));
// [1, 2, 5, 8]
```

### Mutation

`sort()` changes the original array and returns `void`.

```java
Arrays.sort(numbers);                 // OK
int[] result = Arrays.sort(numbers);  // DOES NOT COMPILE
```

### Partial Sort

```java
Arrays.sort(array, fromIndex, toIndex);
```

The range is:

```text
[fromIndex, toIndex)
```

Example:

```java
int[] numbers = {9, 4, 3, 2, 8};

Arrays.sort(numbers, 1, 4);

// [9, 2, 3, 4, 8]
```

Indexes `1`, `2`, and `3` are sorted.

### String Ordering

Strings use their natural lexicographical ordering.

```java
String[] words = {"dog", "Cat", "apple"};

Arrays.sort(words);
```

Do not assume case-insensitive dictionary ordering.

---

## `Arrays.binarySearch()`

Searches a **sorted array** and returns an `int`.

```java
int[] numbers = {10, 20, 30, 40, 50};

int result = Arrays.binarySearch(numbers, 30);

// 2
```

### Array Must Already Be Sorted

Correct:

```java
int[] numbers = {30, 10, 20};

Arrays.sort(numbers);

int result = Arrays.binarySearch(numbers, 20);
```

`binarySearch()` does **not** sort the array.

If the array is not sorted according to the search ordering, the result is not reliable.

### Element Found

If found:

```text
return its index
```

Given:

```java
int[] numbers = {10, 20, 30, 40};
```

```java
Arrays.binarySearch(numbers, 10);   // 0
Arrays.binarySearch(numbers, 30);   // 2
Arrays.binarySearch(numbers, 40);   // 3
```

### Element Not Found

If not found:

```text
-insertionPoint - 1
```

The **insertion point** is where the value would need to be inserted to keep the array sorted.

Given:

```java
int[] numbers = {10, 20, 30, 40};
```

Searching for `25`:

```text
[10, 20, 30, 40]
         ↑
      insert at
       index 2
```

Therefore:

```text
-2 - 1 = -3
```

```java
Arrays.binarySearch(numbers, 25);   // -3
```

Useful examples:

| Search | Insertion Point | Result |
|---|---:|---:|
| `10` | found | `0` |
| `30` | found | `2` |
| `5` | `0` | `-1` |
| `15` | `1` | `-2` |
| `25` | `2` | `-3` |
| `50` | `4` | `-5` |

Memory:

```text
FOUND     → index
NOT FOUND → -insertionPoint - 1
```

The extra `-1` ensures a missing value whose insertion point is `0` returns `-1`, rather than `0`, which already means **found at index 0**.

### Range Search

```java
Arrays.binarySearch(array, fromIndex, toIndex, key);
```

Again:

```text
[fromIndex, toIndex)
```

The returned index refers to the **original array**, not an index relative to the searched range.

### Duplicate Values

If multiple matching elements exist:

```java
int[] values = {1, 2, 2, 2, 3};
```

`binarySearch()` returns the index of **a matching element**.

Do not assume it returns the first or last duplicate.

---

## `Arrays.equals()`

Compares the contents of two arrays.

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

Arrays.equals(a, b);   // true
```

### Array `equals()` Is Not Content Equality

Arrays do not override `Object.equals()` to compare their contents.

```java
a == b;              // false
a.equals(b);         // false
Arrays.equals(a,b);  // true
```

If:

```java
int[] c = a;
```

then:

```java
a == c;              // true
a.equals(c);         // true
Arrays.equals(a,c);  // true
```

Memory:

```text
==                → same array object?
array.equals()    → effectively identity
Arrays.equals()   → same contents?
```

### Length and Order Matter

```java
Arrays.equals(
    new int[]{1, 2},
    new int[]{1, 2, 3}
); // false
```

and:

```java
Arrays.equals(
    new int[]{1, 2, 3},
    new int[]{3, 2, 1}
); // false
```

---

## `Arrays.deepEquals()`

Use for **nested arrays**.

```java
int[][] a = {
    {1, 2},
    {3, 4}
};

int[][] b = {
    {1, 2},
    {3, 4}
};
```

The outer arrays contain other arrays.

Therefore:

```java
Arrays.equals(a, b);       // false
Arrays.deepEquals(a, b);   // true
```

Why?

Conceptually:

```text
a
├──► int[] {1, 2}
└──► int[] {3, 4}

b
├──► int[] {1, 2}
└──► int[] {3, 4}
```

`Arrays.equals()` compares one level.

`Arrays.deepEquals()` recursively compares nested array contents.

Memory:

```text
Arrays.equals()      → one level
Arrays.deepEquals()  → nested arrays
```

---

## `Arrays.compare()`

Lexicographically compares two arrays.

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 4};

Arrays.compare(a, b);
```

Return meaning:

```text
negative → a comes before b
0        → equal
positive → a comes after b
```

Usually care about the **sign**, not the exact returned number.

### First Difference Decides

```java
int[] a = {1, 9, 100};
int[] b = {1, 10, 0};
```

Comparison:

```text
1 == 1
9 < 10    ← first difference
```

Therefore:

```java
Arrays.compare(a, b) < 0
```

Later elements do not matter.

### Prefix Rule

```java
int[] a = {1, 2};
int[] b = {1, 2, 3};
```

`a` is an equal prefix of `b`, so the shorter array comes first:

```java
Arrays.compare(a, b) < 0
Arrays.compare(b, a) > 0
```

---

## `Arrays.mismatch()`

Returns the index of the **first difference**.

```java
int[] a = {1, 2, 3, 4};
int[] b = {1, 2, 9, 4};

Arrays.mismatch(a, b);   // 2
```

Visual:

```text
index    0  1  2  3
a       [1, 2, 3, 4]
b       [1, 2, 9, 4]
               ↑
          first mismatch
```

### Equal Arrays

If there is no mismatch:

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

Arrays.mismatch(a, b);   // -1
```

Memory:

```text
>= 0 → first mismatch index
-1   → arrays match
```

### Different Lengths

If one array is an equal prefix of the other:

```java
int[] a = {1, 2};
int[] b = {1, 2, 3};

Arrays.mismatch(a, b);   // 2
```

The first mismatch is where the shorter array ends.

### `equals()` vs `compare()` vs `mismatch()`

These answer three different questions:

```java
Arrays.equals(a, b)
```

> **Are they the same?**

```java
Arrays.compare(a, b)
```

> **Which comes first?**

```java
Arrays.mismatch(a, b)
```

> **Where do they first differ?**

Memory:

```text
equals()    → SAME?
compare()   → ORDER?
mismatch()  → WHERE?
```

---

## `Arrays.toString()` and `deepToString()`

Printing an array directly does not normally show its contents:

```java
int[] numbers = {1, 2, 3};

System.out.println(numbers);
```

Use:

```java
Arrays.toString(numbers);
```

Result:

```text
[1, 2, 3]
```

### Nested Arrays

For:

```java
int[][] numbers = {
    {1, 2},
    {3, 4}
};
```

use:

```java
Arrays.deepToString(numbers);
```

Result:

```text
[[1, 2], [3, 4]]
```

Same mental distinction:

```text
Arrays.toString()      → one level
Arrays.deepToString()  → nested arrays

Arrays.equals()        → one level
Arrays.deepEquals()    → nested arrays
```

---

## `Arrays.fill()`

Fills an existing array with a value.

```java
int[] numbers = new int[4];

Arrays.fill(numbers, 7);

// [7, 7, 7, 7]
```

It mutates the original array and returns `void`.

### Partial Fill

```java
int[] numbers = {1, 2, 3, 4, 5};

Arrays.fill(numbers, 1, 4, 9);

// [1, 9, 9, 9, 5]
```

Uses:

```text
[fromIndex, toIndex)
```

---

## `Arrays.copyOf()`

Creates a **new array**.

```java
int[] original = {1, 2, 3};

int[] copy = Arrays.copyOf(original, 3);

Arrays.equals(original, copy);  // true
original == copy;               // false
```

### Different Length

Larger:

```java
Arrays.copyOf(original, 5);
```

produces:

```text
[1, 2, 3, 0, 0]
```

Extra positions receive their default values.

Smaller:

```java
Arrays.copyOf(original, 2);
```

produces:

```text
[1, 2]
```

### Object Arrays Are Shallow Copies

```java
StringBuilder[] original = {
    new StringBuilder("A")
};

StringBuilder[] copy =
        Arrays.copyOf(original, original.length);
```

The arrays are different, but the contained references are copied:

```text
original[0] ──┐
              ├──► StringBuilder("A")
copy[0] ──────┘
```

Therefore:

```java
copy[0].append("B");

System.out.println(original[0]);   // AB
```

---

## `Arrays.copyOfRange()`

Copies part of an array into a new array:

```java
int[] original = {10, 20, 30, 40, 50};

int[] copy =
        Arrays.copyOfRange(original, 1, 4);

// [20, 30, 40]
```

Uses:

```text
[from, to)
```

Indexes `1`, `2`, and `3` are copied.

---

## `Arrays.asList()`

For an object array:

```java
String[] array = {"A", "B", "C"};

List<String> list = Arrays.asList(array);
```

The result is a **fixed-size list backed by the array**.

### Changes Are Shared

```java
list.set(0, "X");

System.out.println(array[0]);   // X
```

Likewise:

```java
array[1] = "Y";

System.out.println(list);       // [X, Y, C]
```

### Size Cannot Change

Allowed:

```java
list.set(0, "X");
```

Not allowed:

```java
list.add("D");       // UnsupportedOperationException
list.remove("A");    // UnsupportedOperationException
```

Compare:

```text
Arrays.asList() → fixed-size, set() allowed
List.of()       → unmodifiable
```

### Primitive Array Trap

```java
int[] numbers = {1, 2, 3};

var list = Arrays.asList(numbers);
```

This produces a list containing **one `int[]` object**:

```text
List<int[]>
└──► [1, 2, 3]
```

Therefore:

```java
list.size();   // 1
```

Compare:

```java
Integer[] numbers = {1, 2, 3};

var list = Arrays.asList(numbers);

list.size();   // 3
```

---

# Quick Reference

## Core Operations

| Operation | Meaning | Result |
|---|---|---|
| `array.length` | Number of elements | `int` |
| `Arrays.sort(a)` | Sort array | `void`, mutates |
| `Arrays.binarySearch(a,x)` | Find value | index / negative insertion result |
| `Arrays.equals(a,b)` | Same contents? | `boolean` |
| `Arrays.deepEquals(a,b)` | Same nested contents? | `boolean` |
| `Arrays.compare(a,b)` | Ordering? | negative / zero / positive |
| `Arrays.mismatch(a,b)` | First difference? | index / `-1` |
| `Arrays.toString(a)` | Display contents | `String` |
| `Arrays.deepToString(a)` | Display nested contents | `String` |
| `Arrays.fill(a,x)` | Fill existing array | `void`, mutates |
| `Arrays.copyOf(a,n)` | Copy array | new array |
| `Arrays.copyOfRange(a,x,y)` | Copy range | new array |
| `Arrays.asList(a)` | Array-backed list | fixed-size `List` |

## Range Rule

Most array range APIs use:

```text
[from, to)
```

Therefore:

```text
from → included
to   → excluded
count = to - from
```

## Search Rule

```text
binarySearch()

found     → index
not found → -insertionPoint - 1
```

## Comparison Rules

```text
Arrays.equals()      → SAME?
Arrays.deepEquals()  → SAME NESTED CONTENTS?
Arrays.compare()     → ORDER?
Arrays.mismatch()    → WHERE FIRST DIFFERENT?
```

## Shallow / Deep

```text
Arrays.equals()       → one level
Arrays.deepEquals()   → recursive nested arrays

Arrays.toString()     → one level
Arrays.deepToString() → nested arrays

Arrays.copyOf()       → new array, shallow element/reference copy
```

## Mutation

```text
MUTATES ORIGINAL
Arrays.sort()
Arrays.fill()

CREATES NEW ARRAY
Arrays.copyOf()
Arrays.copyOfRange()

INSPECTS ONLY
Arrays.binarySearch()
Arrays.equals()
Arrays.deepEquals()
Arrays.compare()
Arrays.mismatch()
```

## Final Memory Kicks

> **Array = `length`; String = `length()`; List = `size()`.**

> **`sort()` mutates and returns `void`.**

> **Sort before `binarySearch()`.**

> **Binary-search miss = `-insertionPoint - 1`.**

> **`equals()` = same, `compare()` = order, `mismatch()` = where.**

> **Nested arrays: think `deepEquals()` and `deepToString()`.**

> **Array ranges are normally `[from, to)`.**

> **`copyOf()` creates a new array but object references are copied shallowly.**

> **`Arrays.asList()` is backed by the array and fixed-size.**