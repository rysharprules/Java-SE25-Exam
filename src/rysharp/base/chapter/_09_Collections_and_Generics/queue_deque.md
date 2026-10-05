# Queue & Deque

`Queue` represents elements waiting to be processed, while `Deque` allows operations at both ends and can therefore behave as either a FIFO queue or a LIFO stack.

## Contents

- [Interface Hierarchy](#interface-hierarchy)
- [Queue](#queue)
- [Queue Method Pairs](#queue-method-pairs)
- [FIFO Queue Behaviour](#fifo-queue-behaviour)
- [Deque](#deque)
- [Deque Method Pairs](#deque-method-pairs)
- [FIFO with Deque](#fifo-with-deque)
- [LIFO Stack with Deque](#lifo-stack-with-deque)
- [FIFO vs LIFO](#fifo-vs-lifo)
- [ArrayDeque](#arraydeque)
- [LinkedList as a Deque](#linkedlist-as-a-deque)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Interface Hierarchy

The important relationship is:

```text
Collection
    ↑
  Queue
    ↑
  Deque
```

`Deque` extends both `Queue` and `SequencedCollection`.

Therefore a `Deque` has:

- normal `Queue` operations such as `offer()`, `poll()` and `peek()`
- first/last operations
- stack operations such as `push()` and `pop()`

Common implementations are:

```java
Queue<String> queue = new ArrayDeque<>();

Deque<String> deque = new ArrayDeque<>();
Deque<String> linked = new LinkedList<>();
```

The general `SequencedCollection` API is covered in
[Collections](collections.md#sequenced-collections).

---

## Queue

A `Queue` normally processes elements in **FIFO** order:

```text
FIFO = First In, First Out
```

For example:

```java
Queue<String> queue = new ArrayDeque<>();

queue.offer("A");
queue.offer("B");
queue.offer("C");

System.out.println(queue.poll()); // A
System.out.println(queue.poll()); // B
System.out.println(queue.poll()); // C
```

Conceptually:

```text
HEAD                  TAIL
 ↓                      ↓
[A] → [B] → [C]
 ↑
leaves first
```

`A` entered first, so `A` leaves first.

---

## Queue Method Pairs

`Queue` provides two versions of its three main operations.

| Operation | Exception Form | Special-Value Form |
|---|---|---|
| Insert | `add(e)` | `offer(e)` |
| Remove | `remove()` | `poll()` |
| Examine | `element()` | `peek()` |

The difference is what happens when the operation cannot be performed.

### Removing from an Empty Queue

```java
Queue<String> queue = new ArrayDeque<>();

queue.remove(); // NoSuchElementException
```

But:

```java
Queue<String> queue = new ArrayDeque<>();

System.out.println(queue.poll()); // null
```

Therefore:

```text
remove() → element
         → NoSuchElementException if empty

poll()   → element
         → null if empty
```

---

### Examining an Empty Queue

`element()` and `peek()` examine the head without removing it.

```java
Queue<String> queue = new ArrayDeque<>();

queue.element(); // NoSuchElementException
```

But:

```java
System.out.println(queue.peek()); // null
```

Therefore:

```text
element() → HEAD
          → NoSuchElementException if empty

peek()    → HEAD
          → null if empty
```

---

### `add()` vs `offer()`

Both attempt to insert an element.

```java
queue.add("A");
queue.offer("B");
```

If a capacity-restricted queue cannot accept another element:

```text
add()   → exception
offer() → false
```

For an ordinary `ArrayDeque`, both normally succeed because it dynamically
grows as required.

---

### Queue Failure Pattern

The methods form a useful pattern:

```text
EXCEPTION FAMILY

add(e)    → exception if insertion fails
remove()  → NoSuchElementException if empty
element() → NoSuchElementException if empty
```

versus:

```text
SPECIAL-VALUE FAMILY

offer(e) → false if insertion fails
poll()   → null if empty
peek()   → null if empty
```

---

## FIFO Queue Behaviour

The three methods most strongly associated with normal queue behaviour are:

```text
offer → TAIL
poll  → HEAD
peek  → HEAD
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
HEAD                         TAIL
 ↓                             ↓
[A] → [B] → [C] ← offer("C")

 ↑
poll()
peek()
```

`peek()` examines the head.

`poll()` removes the head.

`offer()` adds at the tail.

Memory rule:

```text
QUEUE / FIFO

offer → TAIL
poll  → HEAD
peek  → HEAD
```

---

## Deque

`Deque` means **double-ended queue**.

It extends `Queue`, but allows operations at both ends:

```text
FIRST / HEAD                    LAST / TAIL
     ↓                               ↓
    [A] ↔ [B] ↔ [C] ↔ [D]
     ↑                               ↑
 operations                       operations
 at first                         at last
```

This allows a `Deque` to be used as either:

```text
QUEUE → FIFO
STACK → LIFO
```

Common implementations are:

```java
Deque<String> deque = new ArrayDeque<>();
```

and:

```java
Deque<String> deque = new LinkedList<>();
```

---

## Deque Method Pairs

`Deque` provides explicit operations for both ends.

### Adding

| First / Head | Last / Tail |
|---|---|
| `addFirst(e)` | `addLast(e)` |
| `offerFirst(e)` | `offerLast(e)` |

Failure behaviour:

```text
addFirst / addLast
→ exception if insertion fails

offerFirst / offerLast
→ false if insertion fails
```

---

### Removing

| First / Head | Last / Tail |
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

| First / Head | Last / Tail |
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

---

### Deque Failure Pattern

The same naming pattern appears throughout the API:

```text
add     → exception
offer   → false

remove  → exception
poll    → null

get     → exception
peek    → null
```

This applies to both ends:

```text
addFirst      addLast
offerFirst    offerLast

removeFirst   removeLast
pollFirst     pollLast

getFirst      getLast
peekFirst     peekLast
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
HEAD / FIRST                 TAIL / LAST
     ↓                            ↓
    [A] → [B] → [C]

     ↑                       ← offerLast()
     |
 pollFirst()
```

Therefore FIFO can be expressed explicitly as:

```text
INSERT → TAIL
REMOVE → HEAD
```

The normal Queue methods are equivalent to:

```text
offer(e) → offerLast(e)

poll()   → pollFirst()

peek()   → peekFirst()
```

So:

```java
deque.offer("A");
deque.poll();
deque.peek();
```

uses normal FIFO queue behaviour.

---

## LIFO Stack with Deque

`Deque` also provides methods specifically for using it as a **stack**.

```text
LIFO = Last In, First Out
```

The stack methods are:

```java
push(e);
pop();
peek();
```

`push()` and `pop()` are defined by `Deque`.

All three stack operations use the **head/first end**:

| Stack Method | Equivalent Deque Operation | End Used | Failure Behaviour |
|---|---|---|---|
| `push(e)` | `addFirst(e)` | HEAD | exception if insertion fails |
| `pop()` | `removeFirst()` | HEAD | `NoSuchElementException` if empty |
| `peek()` | `peekFirst()` | HEAD | `null` if empty |

Conceptually:

```text
HEAD / STACK TOP             TAIL
       ↓                       ↓
      [C] → [B] → [A]
       ↑
 push / pop / peek
```

### `push()`

`push(e)` inserts at the **head**.

It is equivalent to:

```java
deque.addFirst(e);
```

For example:

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A"); // [A]
stack.push("B"); // [B, A]
stack.push("C"); // [C, B, A]
```

Each new element becomes the new head:

```text
push("A")

[A]
 ↑
HEAD


push("B")

[B, A]
 ↑
HEAD


push("C")

[C, B, A]
 ↑
HEAD
```

Therefore:

```text
push(e)
→ addFirst(e)
→ add at HEAD
→ exception-style insertion
```

---

### `pop()`

`pop()` removes and returns the element at the **head**.

It is equivalent to:

```java
deque.removeFirst();
```

Example:

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A");
stack.push("B");
stack.push("C");

// [C, B, A]

System.out.println(stack.pop()); // C
System.out.println(stack);       // [B, A]
```

If the deque is empty:

```java
stack.pop(); // NoSuchElementException
```

Therefore:

```text
pop()
→ removeFirst()
→ remove HEAD
→ return removed element
→ NoSuchElementException if empty
```

---

### `peek()`

`peek()` examines the element at the **head without removing it**.

It is equivalent to:

```java
deque.peekFirst();
```

Example:

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A");
stack.push("B");

// [B, A]

System.out.println(stack.peek()); // B
System.out.println(stack);        // [B, A]
```

If the deque is empty:

```java
System.out.println(stack.peek()); // null
```

Therefore:

```text
peek()
→ peekFirst()
→ examine HEAD
→ does not remove
→ null if empty
```

---

### `pop()` vs `poll()`

`pop()` and `poll()` both remove from the **head**.

Their difference is failure behaviour.

```text
pop()
→ removeFirst()
→ HEAD
→ NoSuchElementException if empty

poll()
→ pollFirst()
→ HEAD
→ null if empty
```

Given:

```text
[A, B, C]
 ↑
HEAD
```

both `pop()` and `poll()` would remove `A`.

The difference between queue and stack behaviour therefore comes primarily
from **where elements are inserted**.

---

## FIFO vs LIFO

This is the key distinction.

### Queue / FIFO

```text
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

// [A, B, C]

System.out.println(deque.poll()); // A
```

The first element inserted is the first removed.

---

### Stack / LIFO

```text
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

// [C, B, A]

System.out.println(deque.pop()); // C
```

The last element inserted is the first removed.

So the crucial difference is:

```text
QUEUE

offer → TAIL
poll  → HEAD


STACK

push → HEAD
pop  → HEAD
```

Both remove from the head.

**Insertion at opposite ends creates FIFO vs LIFO behaviour.**

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

- supports operations at both ends
- dynamically resizes
- does **not** allow `null`
- does not provide indexed access

This fails:

```java
Deque<String> deque = new ArrayDeque<>();

deque.add(null); // NullPointerException
```

The prohibition of `null` also means methods such as:

```java
poll();
peek();
```

can safely use `null` to represent an empty deque.

### No Indexed Access

Unlike a `List`, a deque does not provide indexed access:

```java
Deque<String> deque = new ArrayDeque<>();

deque.get(0); // DOES NOT COMPILE
```

Deque operations work with the **first/head** and **last/tail** ends rather
than indexes.

---

## LinkedList as a Deque

`LinkedList<E>` implements both:

```text
List<E>
Deque<E>
```

Therefore it can be referenced as either:

```java
List<String> list = new LinkedList<>();

Deque<String> deque = new LinkedList<>();
```

The reference type determines which interface methods are directly available.

For example:

```java
List<String> list = new LinkedList<>();

list.add("A");
list.get(0);
```

uses the `List` API.

Whereas:

```java
Deque<String> deque = new LinkedList<>();

deque.offerFirst("A");
deque.offerLast("B");
```

uses the `Deque` API.

List-specific `LinkedList` behaviour is covered in [List](list.md).

### `null`

Unlike `ArrayDeque`, `LinkedList` permits `null`.

```java
Deque<String> deque = new LinkedList<>();

deque.offer(null); // allowed
```

This creates an ambiguity with special-value methods such as `poll()` and
`peek()`:

```java
String value = deque.poll();
```

A returned `null` could represent:

```text
the deque was empty

OR

the deque contained null
```

This is one reason `null` elements are discouraged when using `Queue` and
`Deque` APIs whose special-value methods use `null` to represent emptiness.

---

## Quick Reference

### Interface Hierarchy

```text
Collection
    ↑
  Queue
    ↑
  Deque
```

`Deque` also extends `SequencedCollection`.

---

### Queue Methods

| Operation | Exception Form | Special-Value Form |
|---|---|---|
| Insert | `add(e)` | `offer(e)` → `false` |
| Remove | `remove()` | `poll()` → `null` |
| Examine | `element()` | `peek()` → `null` |

---

### Deque Methods

| Operation | First — Exception | First — Special | Last — Exception | Last — Special |
|---|---|---|---|---|
| Insert | `addFirst` | `offerFirst` | `addLast` | `offerLast` |
| Remove | `removeFirst` | `pollFirst` | `removeLast` | `pollLast` |
| Examine | `getFirst` | `peekFirst` | `getLast` | `peekLast` |

---

### Queue Equivalents

```text
offer(e) → offerLast(e)
poll()   → pollFirst()
peek()   → peekFirst()
```

---

### Stack Equivalents

```text
push(e) → addFirst(e)
pop()   → removeFirst()
peek()  → peekFirst()
```

All stack operations use the:

```text
HEAD / FIRST
```

---

### FIFO

```text
offer → TAIL
poll  → HEAD
peek  → HEAD
```

---

### LIFO

```text
push → HEAD
pop  → HEAD
peek → HEAD
```

---

### Implementations

```text
ArrayDeque
→ Deque
→ no null
→ no indexed access

LinkedList
→ List + Deque
→ null allowed
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


WHY FIFO vs LIFO?

Queue inserts at TAIL and removes from HEAD.

Stack inserts at HEAD and removes from HEAD.


STACK ALIASES:

push → addFirst
pop  → removeFirst
peek → peekFirst


EXCEPTION FAMILY:

add
remove
element

addFirst / addLast
removeFirst / removeLast
getFirst / getLast

push → addFirst
pop  → removeFirst


SPECIAL-VALUE FAMILY:

offer → false
poll  → null
peek  → null

offerFirst / offerLast
pollFirst / pollLast
peekFirst / peekLast


POP vs POLL:

both remove HEAD

pop  → exception if empty
poll → null if empty


ARRAYDEQUE:

Deque
NO null
NO indexed access


LINKEDLIST:

List + Deque
null allowed
```