# Queue & Deque

`Queue` represents elements waiting to be processed, while `Deque` extends the idea to allow operations at both ends and can therefore behave as either a queue or a stack.

## Contents

- [Queue](#queue)
- [Queue Method Pairs](#queue-method-pairs)
- [FIFO Queue Behaviour](#fifo-queue-behaviour)
- [Deque](#deque)
- [Deque Method Pairs](#deque-method-pairs)
- [FIFO with Deque](#fifo-with-deque)
- [LIFO Stack with Deque](#lifo-stack-with-deque)
- [ArrayDeque](#arraydeque)
- [LinkedList as a Deque](#linkedlist-as-a-deque)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Queue

`Queue<E>` extends `Collection<E>`.

A queue normally processes elements in **FIFO** order:

```text
FIFO = First In, First Out
```

For example:

```text
add A
add B
add C

[A, B, C]
 ↑
next element removed
```

`A` entered first, so `A` leaves first.

A typical queue can be created using `ArrayDeque`:

```java
Queue<String> queue = new ArrayDeque<>();

queue.offer("A");
queue.offer("B");
queue.offer("C");

System.out.println(queue.poll()); // A
System.out.println(queue.poll()); // B
System.out.println(queue.poll()); // C
```

---

## Queue Method Pairs

`Queue` provides two versions of its main operations.

| Operation | Throws exception on failure | Special value on failure |
|---|---|---|
| Insert | `add(e)` | `offer(e)` |
| Remove | `remove()` | `poll()` |
| Examine | `element()` | `peek()` |

The most important difference appears when an operation cannot be performed.

### Empty Queue

```java
Queue<String> queue = new ArrayDeque<>();
```

Removing:

```java
queue.remove(); // NoSuchElementException

queue.poll();   // null
```

Examining the head:

```java
queue.element(); // NoSuchElementException

queue.peek();    // null
```

Therefore:

```text
remove()  → exception if empty
poll()    → null if empty

element() → exception if empty
peek()    → null if empty
```

### `add()` vs `offer()`

Both attempt to insert an element.

```java
queue.add("A");
queue.offer("B");
```

For a capacity-restricted queue that cannot accept the element:

```text
add()   → exception
offer() → false
```

For an ordinary unbounded `ArrayDeque`, both normally succeed.

Memory pattern:

```text
EXCEPTION methods:
add
remove
element

SPECIAL-VALUE methods:
offer → false
poll  → null
peek  → null
```

---

## FIFO Queue Behaviour

The three methods worth associating immediately with normal queue behaviour are:

```text
offer → add at TAIL
poll  → remove from HEAD
peek  → examine HEAD
```

Example:

```java
Queue<String> queue = new ArrayDeque<>();

queue.offer("A");
queue.offer("B");
queue.offer("C");

System.out.println(queue);        // [A, B, C]
System.out.println(queue.peek()); // A
System.out.println(queue.poll()); // A
System.out.println(queue);        // [B, C]
```

Conceptually:

```text
HEAD                  TAIL
 ↓                      ↓
[A] → [B] → [C] → offer(D)

poll()
  ↓

[A] leaves first
```

So:

```text
QUEUE / FIFO

offer → TAIL
poll  → HEAD
peek  → HEAD
```

---

## Deque

`Deque` means **double-ended queue**.

It extends both:

```text
Queue
+
SequencedCollection
```

A deque allows operations at both ends:

```text
             Deque

FIRST                       LAST
  ↓                           ↓
[A] ↔ [B] ↔ [C] ↔ [D]
 ↑                           ↑
operations                 operations
at first                   at last
```

This means a `Deque` can naturally be used as:

```text
QUEUE → FIFO

or

STACK → LIFO
```

Common implementations include:

```java
Deque<String> deque = new ArrayDeque<>();
```

and:

```java
Deque<String> deque = new LinkedList<>();
```

---

## Deque Method Pairs

`Deque` expands the Queue API with explicit operations for both ends.

### Adding

| First | Last |
|---|---|
| `addFirst(e)` | `addLast(e)` |
| `offerFirst(e)` | `offerLast(e)` |

The `add` versions throw if insertion cannot be performed.

The `offer` versions return `false`.

---

### Removing

| First | Last |
|---|---|
| `removeFirst()` | `removeLast()` |
| `pollFirst()` | `pollLast()` |

On an empty deque:

```text
removeFirst / removeLast
→ NoSuchElementException

pollFirst / pollLast
→ null
```

---

### Examining

| First | Last |
|---|---|
| `getFirst()` | `getLast()` |
| `peekFirst()` | `peekLast()` |

On an empty deque:

```text
getFirst / getLast
→ NoSuchElementException

peekFirst / peekLast
→ null
```

The naming pattern is therefore:

```text
add     → exception
offer   → false

remove  → exception
poll    → null

get     → exception
peek    → null
```

---

## FIFO with Deque

A `Deque` can be used as a normal FIFO queue.

```java
Deque<String> deque = new ArrayDeque<>();

deque.offerLast("A");
deque.offerLast("B");
deque.offerLast("C");

System.out.println(deque.pollFirst()); // A
System.out.println(deque.pollFirst()); // B
System.out.println(deque.pollFirst()); // C
```

Conceptually:

```text
FIRST                     LAST
  ↓                         ↓
[A] → [B] → [C]

 ↑                 ← offerLast()
 |
pollFirst()
```

So FIFO can be expressed explicitly as:

```text
INSERT → LAST
REMOVE → FIRST
```

The inherited Queue methods correspond naturally to this:

```text
offer(e) → offerLast(e)

poll()   → pollFirst()

peek()   → peekFirst()
```

Therefore:

```java
deque.offer("A");
deque.poll();
deque.peek();
```

is normal FIFO queue behaviour.

---

## LIFO Stack with Deque

A deque can also behave as a **stack**.

```text
LIFO = Last In, First Out
```

The stack-specific methods are:

```text
push(e)
pop()
peek()
```

Example:

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A");
stack.push("B");
stack.push("C");

System.out.println(stack.pop()); // C
System.out.println(stack.pop()); // B
System.out.println(stack.pop()); // A
```

Why?

`push()` operates at the **first/head** end.

Starting empty:

```text
push A → [A]

push B → [B, A]

push C → [C, B, A]
          ↑
         TOP
```

Then:

```text
pop() → C
pop() → B
pop() → A
```

The stack methods correspond to deque methods:

```text
push(e) → addFirst(e)

pop()   → removeFirst()

peek()  → peekFirst()
```

Notice that `pop()` uses the exception-style removal operation.

Therefore:

```java
Deque<String> stack = new ArrayDeque<>();

stack.pop(); // NoSuchElementException
```

when empty.

---

## FIFO vs LIFO

This is the key distinction to make automatic.

### Queue

```text
FIFO

offer → TAIL
poll  → HEAD
peek  → HEAD
```

Example:

```java
Deque<String> deque = new ArrayDeque<>();

deque.offer("A");
deque.offer("B");
deque.offer("C");

System.out.println(deque.poll()); // A
```

---

### Stack

```text
LIFO

push → HEAD
pop  → HEAD
peek → HEAD
```

Example:

```java
Deque<String> deque = new ArrayDeque<>();

deque.push("A");
deque.push("B");
deque.push("C");

System.out.println(deque.pop()); // C
```

The important difference is therefore where insertion occurs:

```text
QUEUE:
offer → TAIL
poll  → HEAD

STACK:
push  → HEAD
pop   → HEAD
```

Both remove from the head.

They differ in where new elements are inserted.

---

## ArrayDeque

`ArrayDeque<E>` implements `Deque<E>`.

```java
Deque<String> deque = new ArrayDeque<>();
```

It can therefore be used as either:

```text
FIFO queue
or
LIFO stack
```

Characteristics:

- maintains encounter order
- supports efficient operations at both ends
- dynamically resizes
- **does not allow `null`**
- does not provide indexed access

This fails:

```java
Deque<String> deque = new ArrayDeque<>();

deque.add(null); // NullPointerException
```

The lack of `null` support is particularly useful because methods such as:

```java
poll()
peek()
```

use `null` to indicate that the deque is empty.

### No Indexed Access

Unlike `List`, a deque does not provide:

```java
deque.get(0); // DOES NOT COMPILE
```

Operations are based around the **first and last ends**, not indexes.

---

## LinkedList as a Deque

`LinkedList<E>` implements both:

```text
List<E>
Deque<E>
```

Therefore:

```java
LinkedList<String> linked = new LinkedList<>();
```

can use both List and Deque operations.

It can also be referenced through either interface:

```java
List<String> list = new LinkedList<>();

Deque<String> deque = new LinkedList<>();
```

The reference type determines which interface methods are directly available.

For example:

```java
List<String> list = new LinkedList<>();

list.get(0);       // List operation
```

while:

```java
Deque<String> deque = new LinkedList<>();

deque.offerFirst("A"); // Deque operation
```

### `null`

Unlike `ArrayDeque`, `LinkedList` permits `null`.

```java
Deque<String> deque = new LinkedList<>();

deque.offer(null); // allowed
```

This creates an important ambiguity with methods such as:

```java
deque.poll();
```

because `null` could mean:

```text
the deque was empty

OR

the deque actually contained null
```

This is one reason `ArrayDeque`'s prohibition of `null` works naturally with
the special-value Queue/Deque methods.

List-specific `LinkedList` behaviour is covered in [List](list.md).

---

## Quick Reference

### Queue Methods

| Operation | Exception form | Special-value form |
|---|---|---|
| Insert | `add(e)` | `offer(e)` → `false` |
| Remove | `remove()` | `poll()` → `null` |
| Examine | `element()` | `peek()` → `null` |

### Deque Methods

| Operation | First — exception | First — special | Last — exception | Last — special |
|---|---|---|---|---|
| Insert | `addFirst` | `offerFirst` | `addLast` | `offerLast` |
| Remove | `removeFirst` | `pollFirst` | `removeLast` | `pollLast` |
| Examine | `getFirst` | `peekFirst` | `getLast` | `peekLast` |

### Queue Aliases

```text
offer(e) → offerLast(e)
poll()   → pollFirst()
peek()   → peekFirst()
```

### Stack Aliases

```text
push(e) → addFirst(e)
pop()   → removeFirst()
peek()  → peekFirst()
```

### FIFO

```text
offer → TAIL
poll  → HEAD
peek  → HEAD
```

### LIFO

```text
push → HEAD
pop  → HEAD
peek → HEAD
```

### Implementations

```text
ArrayDeque
→ Deque
→ no null
→ no indexed access

LinkedList
→ List + Deque
→ allows null
```

---

## Final Memory Kicks

```text
QUEUE = FIFO

offer → TAIL
poll  → HEAD
peek  → HEAD


STACK = LIFO

push → HEAD
pop  → HEAD
peek → HEAD


EXCEPTION FAMILY:

add
remove
element

addFirst / addLast
removeFirst / removeLast
getFirst / getLast


SPECIAL-VALUE FAMILY:

offer → false
poll  → null
peek  → null

offerFirst / offerLast
pollFirst / pollLast
peekFirst / peekLast


ARRAYDEQUE
→ Deque
→ NO null

LINKEDLIST
→ List + Deque
→ null allowed
```