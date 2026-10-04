# Scoped Values (JEP 506)

Quick reference for Java 25 Scoped Values, bindings, scope, rebinding and context sharing.

## Contents

- [The Basic Idea](#the-basic-idea)
- [Creating and Binding a Scoped Value](#creating-and-binding-a-scoped-value)
- [Reading a Scoped Value](#reading-a-scoped-value)
- [`run()` vs `call()`](#run-vs-call)
- [Nested Scopes and Rebinding](#nested-scopes-and-rebinding)
- [Multiple Bindings](#multiple-bindings)
- [Scoped Values vs `ThreadLocal`](#scoped-values-vs-threadlocal)
- [Intended Use](#intended-use)
- [Concurrency Context](#concurrency-context)
- [Quick Reference](#quick-reference)

---

## The Basic Idea

Java 25 makes **Scoped Values** a permanent Java feature.

A `ScopedValue` lets code further down a call chain access a value without passing it through every intermediate method parameter.

Without a Scoped Value:

```java
void handleRequest(String requestId) {
    authenticate(requestId);
}

void authenticate(String requestId) {
    loadAccount(requestId);
}

void loadAccount(String requestId) {
    databaseCall(requestId);
}
```

Even if only `databaseCall()` needs the request ID, every method has to carry it.

With a Scoped Value:

```java
static final ScopedValue<String> REQUEST_ID =
        ScopedValue.newInstance();

void handleRequest() {
    ScopedValue.where(REQUEST_ID, "abc-123")
               .run(() -> authenticate());
}

void authenticate() {
    loadAccount();
}

void loadAccount() {
    databaseCall();
}

void databaseCall() {
    System.out.println(REQUEST_ID.get());
}
```

The value flows down the call chain:

```text
handleRequest()
      ↓
authenticate()
      ↓
loadAccount()
      ↓
databaseCall()
      ↓
REQUEST_ID.get()
```

The intermediate methods do not need a `requestId` parameter.

Memory:

> **A Scoped Value is a temporary context value that flows down the call chain.**

It can be thought of somewhat like an **implicit method parameter**.

---

## Creating and Binding a Scoped Value

A Scoped Value is commonly declared as a `static final` field:

```java
static final ScopedValue<String> NAME =
        ScopedValue.newInstance();
```

`NAME` acts like a **key**.

Creating it does not itself establish a value:

```java
ScopedValue.newInstance()
```

creates the Scoped Value.

A value is supplied by establishing a binding:

```java
ScopedValue.where(NAME, "Alice")
           .run(() -> doSomething());
```

Think:

```text
newInstance()
     ↓
create key

where(NAME, "Alice")
     ↓
bind key to value

run(...)
     ↓
execute within binding
```

### Scope

The binding exists only while the operation is executing.

```java
static final ScopedValue<String> NAME =
        ScopedValue.newInstance();

static void main() {

    ScopedValue.where(NAME, "Alice")
               .run(() -> methodA());

    System.out.println(NAME.isBound());
}

static void methodA() {
    System.out.println(NAME.get());
}
```

Output:

```text
Alice
false
```

Think:

```text
outside scope
NAME = unbound

       ↓

where(NAME, "Alice")

       ↓

run(...)
NAME = "Alice"

       ↓

called methods
can read NAME

       ↓

run() finishes

       ↓

NAME = unbound
```

Memory:

> **The binding disappears automatically when the scope ends.**

It does not remain `"Alice"` after `run()` finishes.

---

## Reading a Scoped Value

Several methods determine what happens when you read a Scoped Value.

### `get()`

```java
NAME.get()
```

returns the currently bound value.

If bound:

```java
ScopedValue.where(NAME, "Alice")
           .run(() -> System.out.println(NAME.get()));
```

prints:

```text
Alice
```

If the Scoped Value is unbound:

```java
NAME.get();
```

throws:

```text
NoSuchElementException
```

It does **not** return `null`.

Memory:

```text
get()

bound
→ value

unbound
→ NoSuchElementException
```

### `isBound()`

Use:

```java
NAME.isBound()
```

to determine whether a binding currently exists.

```java
if (NAME.isBound()) {
    System.out.println(NAME.get());
}
```

Result:

```text
true  → currently bound
false → currently unbound
```

### `orElse()`

`orElse()` supplies a fallback:

```java
String name = NAME.orElse("Guest");
```

Think:

```text
bound
→ bound value

unbound
→ fallback value
```

### `orElseThrow()`

`orElseThrow(...)` returns the bound value or throws the supplied exception when unbound.

Method recognition:

| Method | If Bound | If Unbound |
|---|---|---|
| `get()` | value | `NoSuchElementException` |
| `isBound()` | `true` | `false` |
| `orElse(x)` | value | `x` |
| `orElseThrow(...)` | value | supplied exception |

---

## `run()` vs `call()`

There are two important ways to execute code within a binding.

### `run()`

Use `run()` when no result is required:

```java
ScopedValue.where(NAME, "Alice")
           .run(() -> process());
```

Think:

```text
run()
→ perform operation
→ no result
```

### `call()`

Use `call()` when the operation returns a value:

```java
String result =
        ScopedValue.where(NAME, "Alice")
                   .call(() -> process());
```

Think:

```text
call()
→ perform operation
→ return result
```

`call()` can also support operations that throw checked exceptions.

Memory:

```text
run()  → no result
call() → result
```

---

## Nested Scopes and Rebinding

A Scoped Value can be rebound within a nested scope.

```java
ScopedValue.where(NAME, "Alice").run(() -> {

    System.out.println(NAME.get());

    ScopedValue.where(NAME, "Bob").run(() -> {
        System.out.println(NAME.get());
    });

    System.out.println(NAME.get());
});
```

Output:

```text
Alice
Bob
Alice
```

The outer scope establishes:

```text
NAME = Alice
```

The inner scope temporarily establishes:

```text
NAME = Bob
```

When the inner scope ends:

```text
NAME = Alice
```

is restored.

Think:

```text
OUTER SCOPE
NAME = Alice
     │
     ├── INNER SCOPE
     │   NAME = Bob
     │
     └── inner ends
         ↓
NAME = Alice again
```

The inner binding does **not** permanently replace the outer binding.

Memory:

> **Nested binding temporarily shadows the outer binding; when the nested scope ends, the outer binding is restored.**

---

## Multiple Bindings

`where()` returns a `ScopedValue.Carrier`.

Bindings can therefore be chained before executing the operation.

```java
static final ScopedValue<String> USER =
        ScopedValue.newInstance();

static final ScopedValue<String> REQUEST_ID =
        ScopedValue.newInstance();

ScopedValue.where(USER, "Alice")
           .where(REQUEST_ID, "abc-123")
           .run(() -> process());
```

Inside `process()`:

```java
USER.get();        // Alice
REQUEST_ID.get();  // abc-123
```

Think:

```text
where(USER, "Alice")
      ↓
where(REQUEST_ID, "abc-123")
      ↓
run(...)
      ↓
both bindings available
```

For exam purposes, recognise:

> **`where()` can be chained to establish multiple bindings.**

---

## Scoped Values vs `ThreadLocal`

`ThreadLocal` is useful background for understanding Scoped Values.

Very broadly:

```text
ThreadLocal
→ value associated with a thread

ScopedValue
→ value associated with a bounded scope of execution
```

A `ThreadLocal` value can remain associated with a thread until it is removed.

A Scoped Value instead follows a defined execution scope:

```text
scope starts
     ↓
binding available
     ↓
called methods can read it
     ↓
scope ends
     ↓
binding disappears
```

Memory:

> **`ThreadLocal` is thread-oriented; `ScopedValue` is scope-oriented.**

A Scoped Value is not simply a renamed `ThreadLocal`.

---

## Intended Use

Scoped Values are designed primarily for **one-way sharing of context**.

Think:

```text
caller
  │
  │ establishes value
  ↓
callee
  │
  │ reads value
  ↓
deeper callee
  │
  │ reads value
  ↓
scope ends
```

Good examples include contextual information such as:

```text
request ID
user context
configuration/context data
```

The value is established by an enclosing operation and read further down the call chain.

### Read-Oriented Context

Do not think of a Scoped Value as an ordinary mutable variable shared between methods.

The intended model is:

```text
bind
 ↓
read
 ↓
read
 ↓
scope ends
```

rather than:

```text
bind
 ↓
mutate repeatedly
 ↓
share mutable state
```

### Immutability

Scoped Values are designed to work well with immutable context.

For example:

```java
static final ScopedValue<String> USER =
        ScopedValue.newInstance();
```

is a natural use because `String` is immutable.

But:

> **A `ScopedValue` does not make the object stored inside it immutable.**

If a mutable `List` is stored in a Scoped Value, that `List` remains mutable.

Memory:

```text
ScopedValue
→ encourages immutable context

ScopedValue
≠ makes object immutable
```

---

## Concurrency Context

Scoped Values work well with Java's modern concurrency model, including virtual threads.

But do not think:

```text
ScopedValue
= virtual-thread feature only
```

Scoped Values are useful independently of virtual threads.

The useful relationship is simply:

```text
ScopedValue
     ↓
efficient context sharing
     ↓
works well with virtual threads
```

### Structured Concurrency

Scoped Values are also related to Structured Concurrency because bindings can be inherited by child threads when used with `StructuredTaskScope`.

For Java 25:

```text
Scoped Values
→ permanent feature

Structured Concurrency
→ preview feature
```

So Structured Concurrency is useful context here, but its API does not need to be learned as part of the core Scoped Value rules.

---

# Quick Reference

## Basic Lifecycle

```java
static final ScopedValue<String> NAME =
        ScopedValue.newInstance();

ScopedValue.where(NAME, "Alice")
           .run(() -> {
               System.out.println(NAME.get());
           });
```

Think:

```text
newInstance()
     ↓
create key

where(...)
     ↓
establish binding

run() / call()
     ↓
execute inside scope

get()
     ↓
read binding

scope ends
     ↓
binding disappears
```

## Core Methods

| Method | Purpose |
|---|---|
| `newInstance()` | Creates the Scoped Value/key |
| `where(...)` | Establishes a binding |
| `run(...)` | Executes operation with no result |
| `call(...)` | Executes operation and returns result |
| `get()` | Returns binding; throws if unbound |
| `isBound()` | Checks whether currently bound |
| `orElse(...)` | Value or fallback |
| `orElseThrow(...)` | Value or supplied exception |

## Scope

```text
BEFORE
NAME = unbound

       ↓

where(NAME, "Alice").run(...)

       ↓

INSIDE
NAME = Alice

       ↓

scope ends

       ↓

AFTER
NAME = unbound
```

## Nested Binding

```text
NAME = Alice

    NAME = Bob

NAME = Alice
```

Memory:

```text
inner binding
→ temporarily shadows outer

inner scope ends
→ outer binding restored
```

## `get()` Trap

```java
NAME.get();
```

when unbound:

```text
NoSuchElementException
```

not:

```text
null
```

## `run()` vs `call()`

```text
run()
→ no result

call()
→ result
```

## Multiple Bindings

```java
ScopedValue.where(USER, "Alice")
           .where(REQUEST_ID, "abc-123")
           .run(...);
```

→ both bindings available within the scope.

## `ThreadLocal` Comparison

```text
ThreadLocal
→ thread-oriented

ScopedValue
→ scope-oriented
```

## Reliable Check

When tracing Scoped Value code:

```text
1. Find where(...) bindings

2. Determine which run()/call()
   establishes the active scope

3. Follow execution DOWN the call chain

4. For get(), determine the nearest
   active binding

5. For nested where(), use the inner
   binding while its scope is active

6. When inner scope ends, restore
   the outer binding

7. When the outer scope ends,
   the value becomes unbound

8. get() while unbound
   → NoSuchElementException
```

## Final Memory Kicks

> **`ScopedValue.newInstance()` creates the key; it does not bind a value.**

> **`where(...)` establishes a temporary binding for a scope of execution.**

> **The binding flows down the call chain without needing to be passed through every method parameter.**

> **`get()` on an unbound Scoped Value throws `NoSuchElementException`; it does not return `null`.**

> **`run()` performs an operation without a result; `call()` returns a result.**

> **Nested scopes can rebind a Scoped Value; when the inner scope ends, the outer binding is restored.**

> **Multiple bindings can be chained with `where()`.**

> **Scoped Values are designed for one-way, read-oriented context sharing.**

> **`ThreadLocal` is thread-oriented; `ScopedValue` is scope-oriented.**

> **Scoped Values work well with virtual threads but are not exclusively a virtual-thread feature.**

> **ScopedValue = bind for a scope → callees read it → scope ends → binding disappears.**