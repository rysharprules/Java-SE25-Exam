# Concurrent Collections

[🔙 Back](README.md)

A focused reference for Java's concurrent collection classes, their behaviour, iteration guarantees and important API differences.

## Contents

- [Why Concurrent Collections?](<#why-concurrent-collections>)
- [ConcurrentHashMap](<#concurrenthashmap>)
- [ConcurrentSkipListMap](<#concurrentskiplistmap>)
- [ConcurrentSkipListSet](<#concurrentskiplistset>)
- [CopyOnWriteArrayList](<#copyonwritearraylist>)
- [CopyOnWriteArraySet](<#copyonwritearrayset>)
- [Concurrent Queues and Deques](<#concurrent-queues-and-deques>)
- [Blocking Queues](<#blocking-queues>)
- [Iterator Behaviour](<#iterator-behaviour>)
- [Class Comparison](<#class-comparison>)
- [Quick Reference](<#quick-reference>)
- [Final Memory Kicks](<#final-memory-kicks>)

---

## Why Concurrent Collections?

Ordinary collections such as `ArrayList` and `HashMap` are not generally thread-safe.

Java provides concurrent collections in `java.util.concurrent` for sharing data safely between threads.

``` java
Map<String, Integer> map = new ConcurrentHashMap<>();
```

Concurrent collections offer different guarantees depending on their implementation.

**Thread-safe does not mean every multi-step operation is atomic.**

For example:

``` java
if (!map.containsKey("A")) {
    map.put("A", 1);
}
```

Another thread could modify the map between the two calls.

Prefer an atomic operation where appropriate:

``` java
map.putIfAbsent("A", 1);
```

---

## ConcurrentHashMap

A thread-safe map supporting concurrent reads and updates.

``` java
ConcurrentHashMap<String, Integer> map =
        new ConcurrentHashMap<>();

map.put("A", 10);
map.put("B", 20);

System.out.println(map.get("A")); // 10
```

### Key Properties

- No guaranteed iteration order.
- Does not allow `null` keys or values.
- Supports concurrent access without locking the entire map for ordinary operations.
- Iterators are **weakly consistent**, not fail-fast.
- Provides atomic operations such as `putIfAbsent()`, `compute()` and `merge()`.

### Constructors

The important overloads are:

``` java
new ConcurrentHashMap<>();

new ConcurrentHashMap<>(16);

new ConcurrentHashMap<>(16, 0.75f);

new ConcurrentHashMap<>(16, 0.75f, 4);

new ConcurrentHashMap<>(existingMap);
```

| Constructor | Parameters |
| --- | --- |
| `()` | Defaults |
| `(int)` | Initial capacity |
| `(int, float)` | Initial capacity, load factor |
| `(int, float, int)` | Initial capacity, load factor, concurrency level |
| `(Map)` | Initial mappings |

The third parameter in the three-argument constructor is the `concurrencyLevel`, a sizing hint retained for compatibility, not a fixed number of locks or threads.

### Primitive Widening

Consider:

``` java
new ConcurrentHashMap<>(10, 2);
```

This **compiles**.

Java widens the second `int` to `float`, selecting:

``` java
ConcurrentHashMap(int initialCapacity, float loadFactor)
```

It does not select the three-argument constructor.

Compare:

``` java
new ConcurrentHashMap<>(10, 2, 4);
```

This also compiles:

``` text
10 → initialCapacity
 2 → loadFactor (int widened to float)
 4 → concurrencyLevel
```

Remember:

``` text
2 arguments
→ CAPACITY + LOAD FACTOR

3 arguments
→ CAPACITY + LOAD FACTOR + CONCURRENCY LEVEL
```

### Atomic Map Operations

``` java
map.putIfAbsent("A", 10);
```

Adds a mapping only when absent.

``` java
map.computeIfAbsent("B", key -> 20);
```

Computes a value if absent.

``` java
map.merge("A", 5, Integer::sum);
```

If `"A"` currently maps to `10`, the result is:

``` text
A → 15
```

These operations support atomic updates for the specified mapping.

### ConcurrentHashMap vs HashMap

| Feature | `HashMap` | `ConcurrentHashMap` |
| --- | --- | --- |
| Thread-safe | No | Yes |
| Null key | One allowed | Not allowed |
| Null values | Allowed | Not allowed |
| Iterators | Fail-fast on best-effort basis | Weakly consistent |
| Ordering | No guarantee | No guarantee |

---

## ConcurrentSkipListMap

A thread-safe, **sorted** map.

``` java
ConcurrentSkipListMap<Integer, String> map =
        new ConcurrentSkipListMap<>();

map.put(30, "C");
map.put(10, "A");
map.put(20, "B");

System.out.println(map.keySet());
```

Output:

``` text
[10, 20, 30]
```

### Key Properties

- Sorted by natural ordering or a supplied comparator.
- Implements `ConcurrentNavigableMap`.
- Does not allow null keys or values.
- Supports navigation methods.
- Iterators are weakly consistent.

### Navigational Methods

``` java
map.lowerKey(20);    // 10
map.floorKey(20);    // 20
map.ceilingKey(20);  // 20
map.higherKey(20);   // 30
```

Same boundary meanings as `TreeMap`.

See the Chapter 9 **Map** guide for the complete navigation and range-method rules.

### Compared With ConcurrentHashMap

``` text
ConcurrentHashMap
→ CONCURRENT + UNORDERED

ConcurrentSkipListMap
→ CONCURRENT + SORTED
```

---

## ConcurrentSkipListSet

A thread-safe, **sorted** set.

``` java
ConcurrentSkipListSet<Integer> set =
        new ConcurrentSkipListSet<>();

set.add(30);
set.add(10);
set.add(20);

System.out.println(set);
```

Output:

``` text
[10, 20, 30]
```

### Key Properties

- Sorted elements.
- No duplicates.
- Does not allow null.
- Implements `NavigableSet`.
- Weakly consistent iterators.

Navigation:

``` java
set.lower(20);    // 10
set.floor(20);    // 20
set.ceiling(20);  // 20
set.higher(20);   // 30
```

See the Chapter 9 **Set** guide for range views and boundary rules.

Memory:

``` text
SkipListMap
→ SORTED MAP

SkipListSet
→ SORTED SET
```

---

## CopyOnWriteArrayList

A thread-safe list designed for situations with **many reads and relatively few writes**.

``` java
CopyOnWriteArrayList<String> list =
        new CopyOnWriteArrayList<>();

list.add("A");
list.add("B");
list.add("C");
```

### How It Works

Mutating operations create a new backing array.

This makes writes comparatively expensive but allows readers to work with stable snapshots.

### Snapshot Iterators

``` java
CopyOnWriteArrayList<String> list =
        new CopyOnWriteArrayList<>();

list.add("A");
list.add("B");

Iterator<String> iterator = list.iterator();

list.add("C");

while (iterator.hasNext()) {
    System.out.println(iterator.next());
}
```

Output:

``` text
A
B
```

The iterator sees the snapshot from when it was created.

The current list contains:

``` text
[A, B, C]
```

### Iterator Removal

``` java
Iterator<String> iterator = list.iterator();

iterator.next();
iterator.remove(); // UnsupportedOperationException
```

Copy-on-write iterators do not support `remove()`, `set()` or `add()` mutation operations.

### When Useful?

``` text
MANY READS
+
FEW WRITES
→ CopyOnWriteArrayList
```

---

## CopyOnWriteArraySet

A thread-safe set using copy-on-write behaviour.

``` java
CopyOnWriteArraySet<String> set =
        new CopyOnWriteArraySet<>();

set.add("A");
set.add("B");
set.add("A");

System.out.println(set);
```

Output:

``` text
[A, B]
```

### Key Properties

- No duplicates.
- Allows one null element.
- Preserves insertion order during normal iteration.
- Snapshot iterators.
- Good for small sets with frequent iteration and infrequent modification.

### Compared With CopyOnWriteArrayList

| Feature | List | Set |
| --- | --- | --- |
| Duplicates | Allowed | Not allowed |
| Indexed access | Yes | No |
| Snapshot iterator | Yes | Yes |
| Writes copy backing array | Yes | Yes |

Memory:

``` text
CopyOnWrite
→ COPY when modifying
→ iterator sees SNAPSHOT
```

---

## Concurrent Queues and Deques

These collections support concurrent insertion and removal without requiring callers to synchronise ordinary operations.

### ConcurrentLinkedQueue

A thread-safe, non-blocking FIFO queue.

``` java
Queue<String> queue =
        new ConcurrentLinkedQueue<>();

queue.offer("A");
queue.offer("B");

System.out.println(queue.poll()); // A
System.out.println(queue.poll()); // B
```

Properties:

- FIFO.
- Unbounded.
- No null elements.
- Weakly consistent iterators.
- `poll()` returns `null` when empty.

### ConcurrentLinkedDeque

A thread-safe, non-blocking double-ended queue.

``` java
Deque<String> deque =
        new ConcurrentLinkedDeque<>();

deque.offerFirst("A");
deque.offerLast("B");

System.out.println(deque.pollLast());  // B
System.out.println(deque.pollFirst()); // A
```

Properties:

- Insertion/removal at both ends.
- Unbounded.
- No null elements.
- Weakly consistent iterators.

The FIFO/LIFO method relationships are covered in Chapter 9 **Queue \& Deque**.

---

## Blocking Queues

A `BlockingQueue` supports operations that can wait for space or an available element.

This is useful in producer-consumer designs.

### ArrayBlockingQueue

A bounded, array-backed blocking queue.

``` java
BlockingQueue<String> queue =
        new ArrayBlockingQueue<>(2);

queue.offer("A");
queue.offer("B");

System.out.println(queue.offer("C"));
```

Output:

``` text
false
```

The queue is full.

### LinkedBlockingQueue

A linked-node blocking queue.

``` java
BlockingQueue<String> queue =
        new LinkedBlockingQueue<>(2);
```

Can be bounded using a capacity or effectively unbounded using the no-argument constructor.

### BlockingQueue Methods

| Operation | Throws on failure | Special value | Blocks | Timed |
| --- | --- | --- | --- | --- |
| Insert | `add(e)` | `offer(e)` | `put(e)` | `offer(e, time, unit)` |
| Remove | `remove()` | `poll()` | `take()` | `poll(time, unit)` |
| Examine | `element()` | `peek()` | — | — |

Important:

``` java
queue.put("A");
```

Waits for space if necessary.

``` java
String item = queue.take();
```

Waits for an element if necessary.

Both can throw `InterruptedException`.

### Blocking vs Non-Blocking

``` text
ConcurrentLinkedQueue
→ NON-BLOCKING queue operations

ArrayBlockingQueue
→ BLOCKING operations available

LinkedBlockingQueue
→ BLOCKING operations available
```

**Do not confuse** **`poll()`** **with** **`take()`:**

``` text
poll()
→ returns null if empty

take()
→ waits if empty
```

### Other Blocking Queues

| Class | Key Characteristic |
| --- | --- |
| `PriorityBlockingQueue` | Priority-ordered, unbounded |
| `DelayQueue` | Elements become available after their delay expires |
| `SynchronousQueue` | No internal storage; direct handoff |
| `LinkedBlockingDeque` | Blocking operations at both ends |

A `PriorityBlockingQueue` does not guarantee FIFO order; elements are retrieved according to priority.

---

## Iterator Behaviour

One of the most important distinctions between concurrent collections.

### Fail-Fast

Ordinary collections such as `ArrayList` and `HashMap` commonly use fail-fast iterators.

``` java
List<String> list =
        new ArrayList<>(List.of("A", "B"));

for (String item : list) {
    list.add("C");
}
```

Typically throws:

``` text
ConcurrentModificationException
```

Fail-fast behaviour is best-effort, not a thread-safety guarantee.

### Weakly Consistent

Used by collections such as:

- `ConcurrentHashMap`
- `ConcurrentSkipListMap`
- `ConcurrentSkipListSet`
- `ConcurrentLinkedQueue`
- `ConcurrentLinkedDeque`

An iterator:

- Does not throw `ConcurrentModificationException` due to concurrent updates.
- May reflect some modifications made after iterator creation.
- Does not guarantee a fixed snapshot.

### Snapshot

Used by:

- `CopyOnWriteArrayList`
- `CopyOnWriteArraySet`

An iterator:

- Sees the collection as it existed when the iterator was created.
- Does not reflect later modifications.
- Does not support iterator mutation.

### Comparison

| Behaviour | Meaning |
| --- | --- |
| Fail-fast | May throw `ConcurrentModificationException` |
| Weakly consistent | Can tolerate concurrent updates; may see changes |
| Snapshot | Fixed view from iterator creation |

---

## Class Comparison

| Class | Structure | Ordering | Nulls | Iterator |
| --- | --- | --- | --- | --- |
| `ConcurrentHashMap` | Map | Unspecified | No keys/values | Weakly consistent |
| `ConcurrentSkipListMap` | Sorted map | Sorted | No keys/values | Weakly consistent |
| `ConcurrentSkipListSet` | Sorted set | Sorted | No | Weakly consistent |
| `CopyOnWriteArrayList` | List | Index/insertion | Allowed | Snapshot |
| `CopyOnWriteArraySet` | Set | Insertion | Allowed | Snapshot |
| `ConcurrentLinkedQueue` | Queue | FIFO | No | Weakly consistent |
| `ConcurrentLinkedDeque` | Deque | Double-ended | No | Weakly consistent |
| `ArrayBlockingQueue` | Bounded queue | FIFO | No | Weakly consistent |
| `LinkedBlockingQueue` | Blocking queue | FIFO | No | Weakly consistent |
| `PriorityBlockingQueue` | Priority queue | Priority | No | Weakly consistent |

---

## Quick Reference

### ConcurrentHashMap Constructors

``` java
new ConcurrentHashMap<>();
new ConcurrentHashMap<>(16);
new ConcurrentHashMap<>(16, 0.75f);
new ConcurrentHashMap<>(16, 0.75f, 4);
new ConcurrentHashMap<>(existingMap);
```

``` text
(int, float)
→ capacity, load factor

(int, float, int)
→ capacity, load factor, concurrency level
```

### Sorted Concurrent Collections

``` text
ConcurrentSkipListMap
→ SORTED MAP

ConcurrentSkipListSet
→ SORTED SET
```

### Copy-On-Write

``` text
CopyOnWriteArrayList
→ duplicates allowed

CopyOnWriteArraySet
→ duplicates rejected

BOTH
→ snapshot iterators
```

### Queue Families

``` text
ConcurrentLinkedQueue
→ non-blocking FIFO

ConcurrentLinkedDeque
→ non-blocking double-ended

ArrayBlockingQueue
→ bounded blocking queue

LinkedBlockingQueue
→ blocking queue, optional capacity
```

### BlockingQueue Methods

``` text
offer()
→ returns false if full

poll()
→ returns null if empty

put()
→ waits for space

take()
→ waits for element
```

### Iterators

``` text
ORDINARY COLLECTIONS
→ commonly FAIL-FAST

CONCURRENT COLLECTIONS
→ commonly WEAKLY CONSISTENT

COPY-ON-WRITE
→ SNAPSHOT
```

---

## Final Memory Kicks

``` text
ConcurrentHashMap
→ THREAD-SAFE MAP
→ NO NULLS
→ UNORDERED
```

``` text
CONSTRUCTOR:

(int, float)
→ CAPACITY + LOAD FACTOR

(int, float, int)
→ CAPACITY + LOAD FACTOR + CONCURRENCY LEVEL

int can widen to float
```

``` text
SkipList
→ SORTED
```

``` text
CopyOnWrite
→ READ-HEAVY
→ WRITES COPY ARRAY
→ SNAPSHOT ITERATOR
```

``` text
ConcurrentLinked
→ NON-BLOCKING
```

``` text
BlockingQueue:

offer → FALSE if full
poll  → NULL if empty
put   → WAIT if full
take  → WAIT if empty
```

``` text
ITERATORS:

Fail-fast
→ may throw CME

Weakly consistent
→ may observe concurrent changes

Snapshot
→ fixed at iterator creation
```