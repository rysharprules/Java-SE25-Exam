# Map

[🔙 Back](README.md)

A `Map` stores key-value pairs. Each key is unique and maps to at most one value.

## Contents

- [Map Characteristics](#map-characteristics)
- [Adding and Replacing Entries](#adding-and-replacing-entries)
- [Getting Values](#getting-values)
- [Removing Entries](#removing-entries)
- [Checking Keys and Values](#checking-keys-and-values)
- [Map Views](#map-views)
- [Convenience Methods](#convenience-methods)
- [HashMap](#hashmap)
- [LinkedHashMap](#linkedhashmap)
- [TreeMap](#treemap)
- [SortedMap](#sortedmap)
- [NavigableMap](#navigablemap)
- [Range Views](#range-views)
- [TreeMap and Comparison](#treemap-and-comparison)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Map Characteristics

`Map<K, V>` is separate from the `Collection` hierarchy.

```text
Collection
├── List
├── Set
└── Queue

Map
└── separate hierarchy
```

A `Map`:

- stores key-value pairs
- requires unique keys
- may contain duplicate values
- provides lookup by key
- does not extend `Collection`

```java
Map<String, Integer> map = new HashMap<>();

map.put("Alice", 10);
map.put("Bob", 20);
map.put("Charlie", 10);

System.out.println(map.get("Alice")); // 10
System.out.println(map.get("Charlie")); // 10
```

`Alice` and `Charlie` can both map to `10` because **values do not have to be
unique**.

Map creation and mutability using `Map.of()`, `Map.ofEntries()`,
`Map.copyOf()`, `Collections.singletonMap()` and
`Collections.unmodifiableMap()` are covered in [Collections](collections.md).

---

## Adding and Replacing Entries

### `put()`

```java
V put(K key, V value)
```

`put()` adds a new key-value pair:

```java
Map<String, Integer> map = new HashMap<>();

Integer old = map.put("A", 10);

System.out.println(old); // null
System.out.println(map); // {A=10}
```

If the key already exists, its value is **replaced**:

```java
Integer old = map.put("A", 20);

System.out.println(old); // 10
System.out.println(map); // {A=20}
```

`put()` returns the **previous value associated with the key**, or `null` if
there was no previous mapping.

Memory rule:

```text
put(new key, value)
→ adds entry
→ returns null

put(existing key, value)
→ replaces value
→ returns old value
```

There is a `null` caveat: if a map implementation allows `null` values, a
`null` return from `put()` can also mean that the key was previously mapped
to `null`.

---

### `putIfAbsent()`

`putIfAbsent()` only associates the value when the key is not already
associated with a non-null value.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

map.putIfAbsent("A", 20);
map.putIfAbsent("B", 30);

System.out.println(map); // {A=10, B=30}
```

`"A"` keeps its existing value.

`"B"` is added.

A mapping to `null` is treated as absent by `putIfAbsent()`.

---

## Getting Values

### `get()`

```java
V get(Object key)
```

Returns the value associated with a key.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);
map.put("B", 20);

System.out.println(map.get("A")); // 10
System.out.println(map.get("X")); // null
```

A return value of `null` can mean:

```text
key does not exist

OR

key exists and maps to null
```

for map implementations that permit `null` values.

Use `containsKey()` when that distinction matters.

---

### `getOrDefault()`

```java
getOrDefault(key, defaultValue)
```

returns the mapped value when the key exists, otherwise the supplied default.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

System.out.println(map.getOrDefault("A", 99)); // 10
System.out.println(map.getOrDefault("B", 99)); // 99
```

The default value is **returned**, not inserted into the map.

```java
map.getOrDefault("B", 99);

System.out.println(map.containsKey("B")); // false
```

---

## Removing Entries

### `remove(key)`

```java
V remove(Object key)
```

removes the mapping and returns its previous value.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);
map.put("B", 20);

Integer removed = map.remove("A");

System.out.println(removed); // 10
System.out.println(map);     // {B=20}
```

If the key is absent:

```java
System.out.println(map.remove("X")); // null
```

---

### `remove(key, value)`

The two-argument version only removes the entry when **both key and value
match**.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

System.out.println(map.remove("A", 20)); // false
System.out.println(map);                 // {A=10}

System.out.println(map.remove("A", 10)); // true
System.out.println(map);                 // {}
```

Memory rule:

```text
remove(key)
→ remove by key
→ returns old value

remove(key, value)
→ remove only if BOTH match
→ returns boolean
```

---

## Checking Keys and Values

### `containsKey()`

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

System.out.println(map.containsKey("A")); // true
System.out.println(map.containsKey("B")); // false
```

### `containsValue()`

```java
System.out.println(map.containsValue(10)); // true
System.out.println(map.containsValue(20)); // false
```

Remember:

```text
containsKey()
containsValue()
```

There is no:

```java
map.contains("A"); // DOES NOT COMPILE
```

---

## Map Views

A `Map` provides three important collection views:

```java
keySet()
values()
entrySet()
```

Given:

```java
Map<String, Integer> map = new LinkedHashMap<>();

map.put("A", 10);
map.put("B", 20);
map.put("C", 30);
```

### `keySet()`

Returns a `Set` containing the keys:

```java
Set<String> keys = map.keySet();

System.out.println(keys); // [A, B, C]
```

The result is a `Set` because map keys are unique.

---

### `values()`

Returns a `Collection` containing the values:

```java
Collection<Integer> values = map.values();

System.out.println(values); // [10, 20, 30]
```

It is **not a Set**, because duplicate values are allowed.

```text
keySet() → Set<K>

values() → Collection<V>
```

---

### `entrySet()`

Returns a `Set` of map entries:

```java
Set<Map.Entry<String, Integer>> entries =
        map.entrySet();
```

Each entry contains a key and its associated value:

```java
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(
            entry.getKey() + " = " + entry.getValue()
    );
}
```

Output:

```text
A = 10
B = 20
C = 30
```

A `Map.Entry` provides:

```text
getKey()
getValue()
setValue()
```

where mutation is supported.

---

### These Are Views

`keySet()`, `values()` and `entrySet()` are backed by the map.

For example:

```java
Map<String, Integer> map = new LinkedHashMap<>();

map.put("A", 10);
map.put("B", 20);
map.put("C", 30);

Set<String> keys = map.keySet();

keys.remove("B");

System.out.println(keys); // [A, C]
System.out.println(map);  // {A=10, C=30}
```

Removing through the view modifies the backing map.

These are **views, not independent copies**.

---

## Convenience Methods

### `replace()`

```java
replace(key, value)
```

only replaces a value when the key already exists.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

map.replace("A", 20);
map.replace("B", 30);

System.out.println(map); // {A=20}
```

Unlike `put()`:

```text
put()
→ can create a new mapping

replace()
→ existing key required
```

---

### `replace(key, oldValue, newValue)`

The three-argument version only replaces the value when both the key and
current value match.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

System.out.println(map.replace("A", 20, 30)); // false
System.out.println(map.replace("A", 10, 30)); // true

System.out.println(map); // {A=30}
```

---

### `replaceAll()`

Applies a `BiFunction` to every entry.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);
map.put("B", 20);

map.replaceAll((key, value) -> value * 2);

System.out.println(map); // {A=20, B=40}
```

The function receives:

```text
(key, value) → new value
```

---

### `forEach()`

`Map.forEach()` accepts a `BiConsumer`.

```java
Map<String, Integer> map = new LinkedHashMap<>();

map.put("A", 10);
map.put("B", 20);

map.forEach((key, value) ->
        System.out.println(key + " = " + value));
```

Shape:

```text
BiConsumer<K, V>

(K, V) → void
```

---

### `computeIfAbsent()`

Computes a value only when the key is not currently associated with a
non-null value.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

map.computeIfAbsent("A", key -> key.length());
map.computeIfAbsent("BBBB", key -> key.length());

System.out.println(map); // {A=10, BBBB=4}
```

The mapping function receives the **key**:

```text
K → V
```

Conceptually:

```text
key has non-null value
→ keep existing value

key absent / mapped to null
→ run function
→ use computed value if non-null
```

---

### `computeIfPresent()`

Computes a new value when the key currently has a **non-null value**.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

map.computeIfPresent("A",
        (key, value) -> value + 5);

map.computeIfPresent("B",
        (key, value) -> value + 5);

System.out.println(map); // {A=15}
```

The remapping function receives:

```text
(K, V) → V
```

If the remapping function returns `null`, the mapping is removed.

---

### `compute()`

`compute()` runs the remapping function regardless of whether the key
currently exists.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

map.compute("A",
        (key, value) -> value + 5);

System.out.println(map); // {A=15}
```

For an absent key, the old value supplied to the function is `null`:

```java
map.compute("B",
        (key, value) -> value == null ? 1 : value + 1);

System.out.println(map); // {A=15, B=1}
```

If the function returns `null`, the mapping is removed or remains absent.

---

### `merge()`

`merge()` is useful when combining a supplied value with an existing value.

```java
Map<String, Integer> map = new HashMap<>();

map.put("A", 10);

map.merge("A", 5, Integer::sum);
map.merge("B", 5, Integer::sum);

System.out.println(map); // {A=15, B=5}
```

For `"A"`:

```text
existing value = 10
supplied value = 5

Integer::sum
→ 15
```

For `"B"`:

```text
no existing non-null value
→ store supplied value 5
→ remapping function is not called
```

If the remapping function is called and returns `null`, the mapping is
removed.

A useful distinction is:

```text
compute()
→ function receives KEY + old value

merge()
→ function receives old value + supplied value
```

---

## HashMap

`HashMap<K, V>` is a general-purpose implementation of `Map`.

```java
Map<String, Integer> map = new HashMap<>();
```

Characteristics:

- unique keys
- no guaranteed encounter order
- allows one `null` key
- allows `null` values
- uses hashing for keys

Example:

```java
Map<String, Integer> map = new HashMap<>();

map.put(null, 10);
map.put("A", null);
```

Do not rely on the iteration order of a `HashMap`.

---

## LinkedHashMap

`LinkedHashMap<K, V>` maintains a defined encounter order.

With the normal constructors, this is insertion order:

```java
Map<String, Integer> map = new LinkedHashMap<>();

map.put("B", 2);
map.put("A", 1);
map.put("C", 3);

System.out.println(map); // {B=2, A=1, C=3}
```

Characteristics:

- unique keys
- maintains encounter order
- allows one `null` key
- allows `null` values
- implements `SequencedMap`

The general `SequencedMap` API is covered in
[Collections](collections.md#sequenced-collections).

---

## TreeMap

`TreeMap<K, V>` maintains its **keys** in sorted order.

```java
Map<Integer, String> map = new TreeMap<>();

map.put(30, "C");
map.put(10, "A");
map.put(20, "B");

System.out.println(map); // {10=A, 20=B, 30=C}
```

Characteristics:

- keys are sorted
- values are not sorted
- implements `NavigableMap`
- uses natural key ordering or a supplied `Comparator`
- `null` keys are not supported with natural ordering
- `null` values are allowed

A custom comparator can determine the key ordering:

```java
Map<Integer, String> map =
        new TreeMap<>(Comparator.reverseOrder());

map.put(10, "A");
map.put(30, "C");
map.put(20, "B");

System.out.println(map); // {30=C, 20=B, 10=A}
```

Comparison concepts are covered in
[Collections](collections.md#comparator).

---

## SortedMap

`SortedMap<K, V>` represents a map whose **keys** are sorted.

`NavigableMap<K, V>` extends `SortedMap<K, V>`, and `TreeMap<K, V>` implements
`NavigableMap<K, V>`.

Important `SortedMap` methods include:

```text
firstKey()
lastKey()

headMap(toKey)
tailMap(fromKey)
subMap(fromKey, toKey)
```

Example:

```java
SortedMap<Integer, String> map =
        new TreeMap<>();

map.put(10, "A");
map.put(20, "B");
map.put(30, "C");

System.out.println(map.firstKey()); // 10
System.out.println(map.lastKey());  // 30
```

---

## NavigableMap

`NavigableMap` adds methods for finding entries relative to a key.

These closely mirror the `NavigableSet` methods.

Given:

```java
NavigableMap<Integer, String> map =
        new TreeMap<>();

map.put(10, "A");
map.put(20, "B");
map.put(30, "C");
map.put(40, "D");
map.put(50, "E");
```

Key-returning methods include:

| Method | Meaning |
|---|---|
| `lowerKey(k)` | greatest key `< k` |
| `floorKey(k)` | greatest key `<= k` |
| `ceilingKey(k)` | smallest key `>= k` |
| `higherKey(k)` | smallest key `> k` |

For example:

```java
System.out.println(map.lowerKey(30));   // 20
System.out.println(map.floorKey(30));   // 30
System.out.println(map.ceilingKey(30)); // 30
System.out.println(map.higherKey(30));  // 40
```

The same memory pattern as `NavigableSet` applies:

```text
lower   → <
floor   → <=

ceiling → >=
higher  → >
```

If no matching key exists, these methods return `null`.

---

### Entry Versions

`NavigableMap` also provides versions that return complete `Map.Entry`
objects:

```text
lowerEntry()
floorEntry()
ceilingEntry()
higherEntry()
```

For example:

```java
Map.Entry<Integer, String> entry =
        map.floorEntry(30);

System.out.println(entry); // 30=C
```

So:

```text
floorKey(30)
→ 30

floorEntry(30)
→ 30=C
```

---

### First and Last Entries

`NavigableMap` also provides:

```java
firstEntry()
lastEntry()
```

and destructive versions:

```java
pollFirstEntry()
pollLastEntry()
```

Example:

```java
NavigableMap<Integer, String> map =
        new TreeMap<>();

map.put(10, "A");
map.put(20, "B");
map.put(30, "C");

System.out.println(map.pollFirstEntry()); // 10=A
System.out.println(map);                  // {20=B, 30=C}
```

`pollFirstEntry()` and `pollLastEntry()` **remove** the returned entry.

---

## Range Views

The range methods on `SortedMap` follow the same default boundary rules as
`SortedSet`.

Given:

```java
SortedMap<Integer, String> map =
        new TreeMap<>();

map.put(10, "A");
map.put(20, "B");
map.put(30, "C");
map.put(40, "D");
map.put(50, "E");
```

### `headMap()`

```java
SortedMap<Integer, String> head =
        map.headMap(30);

System.out.println(head); // {10=A, 20=B}
```

The boundary is exclusive:

```text
headMap(30)
→ keys < 30
```

---

### `tailMap()`

```java
SortedMap<Integer, String> tail =
        map.tailMap(30);

System.out.println(tail); // {30=C, 40=D, 50=E}
```

The boundary is inclusive:

```text
tailMap(30)
→ keys >= 30
```

---

### `subMap()`

```java
SortedMap<Integer, String> middle =
        map.subMap(20, 40);

System.out.println(middle); // {20=B, 30=C}
```

The range is half-open:

```text
subMap(20, 40)
→ [20, 40)
→ >= 20 && < 40
```

This is exactly the same default boundary pattern as `SortedSet`:

```text
head... → < boundary

tail... → >= boundary

sub...  → [from, to)
```

---

### NavigableMap Boundary Control

`NavigableMap` provides boolean overloads:

```java
NavigableMap<Integer, String> map =
        new TreeMap<>();

map.put(10, "A");
map.put(20, "B");
map.put(30, "C");
map.put(40, "D");
map.put(50, "E");

System.out.println(
        map.headMap(30, true)
); // {10=A, 20=B, 30=C}

System.out.println(
        map.tailMap(30, false)
); // {40=D, 50=E}

System.out.println(
        map.subMap(20, true, 40, true)
); // {20=B, 30=C, 40=D}
```

The booleans explicitly control boundary inclusion.

---

### Range Methods Return Views

These are backed range views rather than independent maps.

```java
NavigableMap<Integer, String> map =
        new TreeMap<>();

map.put(10, "A");
map.put(20, "B");
map.put(30, "C");
map.put(40, "D");

NavigableMap<Integer, String> head =
        map.headMap(30, true);

head.remove(20);

System.out.println(head); // {10=A, 30=C}
System.out.println(map);  // {10=A, 30=C, 40=D}
```

Adding an entry through the view requires its key to be within the view's
range:

```java
head.put(25, "X"); // valid

head.put(40, "Y"); // IllegalArgumentException
```

Memory rule:

```text
headMap / tailMap / subMap
→ BACKED RANGE VIEWS
→ range applies to KEYS
```

---

## TreeMap and Comparison

`TreeMap` determines key ordering and key uniqueness using comparison.

Consider:

```java
record Person(String name, int age) {}
```

and:

```java
Comparator<Person> byAge =
        Comparator.comparingInt(Person::age);

Map<Person, String> map =
        new TreeMap<>(byAge);

map.put(new Person("Alice", 30), "First");
map.put(new Person("Bob", 30), "Second");
```

The comparator considers the two keys equal for ordering:

```text
compare(Alice, Bob) == 0
```

Therefore `TreeMap` treats them as the **same key**.

The second `put()` replaces the value associated with that comparison-equivalent
key.

```java
System.out.println(map.size()); // 1
```

For a `TreeMap`:

```text
comparison result == 0
→ keys occupy the same ordering position
→ treated as the same key
```

This mirrors `TreeSet`, where comparison result `0` means the element is
treated as a duplicate.

A comparator used for a sorted map should normally be consistent with
`equals`.

---

## Quick Reference

### Implementations

| Implementation | Key Ordering | `null` Key | `null` Value |
|---|---|---|---|
| `HashMap` | No guaranteed order | Yes | Yes |
| `LinkedHashMap` | Encounter/insertion order | Yes | Yes |
| `TreeMap` | Sorted | No with natural ordering | Yes |

---

### Core Operations

```text
put(key, value)
→ add OR replace
→ returns old value

get(key)
→ value
→ null if no mapping

remove(key)
→ removes mapping
→ returns old value

remove(key, value)
→ both must match
→ returns boolean
```

---

### Views

```text
keySet()
→ Set<K>

values()
→ Collection<V>

entrySet()
→ Set<Map.Entry<K,V>>
```

All are backed views.

---

### Compute Methods

```text
computeIfAbsent
→ key absent / mapped to null
→ function gets KEY

computeIfPresent
→ key has non-null value
→ function gets KEY + VALUE

compute
→ always compute
→ function gets KEY + old VALUE

merge
→ combine old VALUE + supplied VALUE
```

---

### NavigableMap

```text
lowerKey(k)   → <
floorKey(k)   → <=
ceilingKey(k) → >=
higherKey(k)  → >
```

Entry equivalents:

```text
lowerEntry
floorEntry
ceilingEntry
higherEntry
```

---

### SortedMap Ranges

```text
headMap(k)
→ < k

tailMap(k)
→ >= k

subMap(a, b)
→ [a, b)
```

These ranges apply to **keys**.

---

## Final Memory Kicks

```text
MAP
→ KEY + VALUE
→ keys UNIQUE
→ values may duplicate
→ NOT a Collection


put()
→ add or replace
→ returns OLD value


get()
→ lookup by KEY


remove(key)
→ returns old VALUE

remove(key, value)
→ BOTH must match
→ boolean


MAP VIEWS:

keySet()
→ Set

values()
→ Collection

entrySet()
→ Set<Map.Entry>


HASHMAP
→ no guaranteed order
→ null key/value allowed


LINKEDHASHMAP
→ insertion/encounter order


TREEMAP
→ KEYS sorted
→ comparison determines key uniqueness


NAVIGABLE:

lower   → <
floor   → <=

ceiling → >=
higher  → >


SORTED RANGES:

headMap(k)
→ < k

tailMap(k)
→ >= k

subMap(a, b)
→ [a, b)


RANGE METHODS
→ BACKED VIEWS
→ RANGE IS BASED ON KEYS


COMPUTE:

computeIfAbsent
→ absent → calculate

computeIfPresent
→ present non-null → calculate

compute
→ always calculate

merge
→ combine VALUES
```