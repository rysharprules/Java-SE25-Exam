# Scoped Values (JEP 506)

Java 25 makes **Scoped Values** a permanent Java feature.

A `ScopedValue` lets you make a value available to code further down the call chain **without passing it through every method parameter**.

### Code Example
You can find two code examples demonstrating Scoped Values:

1. A basic example in `src\rysharp\jdk25\addendum\Scoped_Values\code\ScopedValueExample.java`.
2. A more real-life example (Request Context) in `src\rysharp\jdk25\addendum\Scoped_Values\code\RealLifeScopedValueExample.java`.

The key idea is:

> **A ScopedValue is a value temporarily bound to a scope of execution.**

---

## 1. Why Scoped Values?

Imagine a server handling a request:

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

Only `databaseCall()` may actually need the request ID, but every method has to carry it as a parameter.

A `ScopedValue` provides another option:

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

`databaseCall()` can access the request ID without it being explicitly passed through `authenticate()` and `loadAccount()`.

This is why a `ScopedValue` can be thought of as behaving somewhat like an **implicit method parameter**.

---

# 2. Creating a Scoped Value

Usually a scoped value is declared as a `static final` field:

```java
static final ScopedValue<String> NAME =
    ScopedValue.newInstance();
```

`NAME` is effectively a **key**.

It does not initially have a value.

The value is supplied when a scope is established:

```java
ScopedValue.where(NAME, "Alice")
           .run(() -> doSomething());
```

Inside `doSomething()`:

```java
NAME.get()
```

returns:

```text
Alice
```

---

# 3. The Scope

The value only exists within the scope established by `where(...).run(...)` or `where(...).call(...)`.

Example:

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

The binding exists while `run()` is executing.

Once `run()` finishes, the value is no longer bound.

Mental model:

```text
outside scope
    NAME = unbound

where(NAME, "Alice")
    |
    +-- run(...)
        |
        +-- methodA()
            |
            +-- NAME.get() -> "Alice"

scope ends
    |
    NAME = unbound
```

This temporary nature is the central idea behind **scoped** values.

---

# 4. Values Flow Down the Call Chain

A Scoped Value is available to methods called from within its scope.

```java
static void main() {
    ScopedValue.where(NAME, "Alice")
               .run(() -> methodA());
}

static void methodA() {
    methodB();
}

static void methodB() {
    methodC();
}

static void methodC() {
    System.out.println(NAME.get());
}
```

`methodA()` and `methodB()` don't need a `NAME` parameter.

The value flows down the call chain:

```text
main()
  |
  v
methodA()
  |
  v
methodB()
  |
  v
methodC()
  |
  v
NAME.get() -> "Alice"
```

This is one of the most important things to remember for the exam.

---

# 5. `get()`

`get()` retrieves the currently bound value.

```java
NAME.get()
```

If the value is currently bound:

```java
NAME.get();   // returns the value
```

If it is **not** bound, `get()` throws:

```text
NoSuchElementException
```

Example:

```java
static void methodA() {
    System.out.println(NAME.get());
}
```

This only works when `methodA()` is executing inside an appropriate scope.

---

# 6. `isBound()`

Use `isBound()` to check whether a value is currently bound:

```java
if (NAME.isBound()) {
    System.out.println(NAME.get());
}
```

It returns:

```text
true
```

when the Scoped Value is bound in the current scope, otherwise:

```text
false
```

---

# 7. `orElse()`

`orElse()` provides a fallback if the value isn't bound:

```java
String name = NAME.orElse("Guest");
```

If `NAME` is bound:

```text
Alice
```

is returned.

If it isn't:

```text
Guest
```

is returned.

Useful methods to recognise:

| Method             | Behaviour                                                 |
| ------------------ | --------------------------------------------------------- |
| `get()`            | Returns value; throws `NoSuchElementException` if unbound |
| `isBound()`        | Returns whether a value is currently bound                |
| `orElse(value)`    | Returns value or the supplied fallback                    |
| `orElseThrow(...)` | Returns value or throws the supplied exception            |

---

# 8. `run()` vs `call()`

There are two important ways to execute code within a Scoped Value binding.

## `run()`

Use when you don't need a return value:

```java
ScopedValue.where(NAME, "Alice")
           .run(() -> process());
```

Conceptually:

```text
run() -> perform an operation
```

## `call()`

Use when the operation returns a value:

```java
String result =
    ScopedValue.where(NAME, "Alice")
               .call(() -> process());
```

Conceptually:

```text
call() -> perform an operation and return a result
```

`call()` also supports operations that can throw checked exceptions.

For exam purposes, remember:

> **`run()` = no result**
>
> **`call()` = result**

---

# 9. Nested Scopes and Rebinding

A Scoped Value can be rebound inside a nested scope.

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

Why?

The outer scope establishes:

```text
NAME = Alice
```

The inner scope temporarily establishes:

```text
NAME = Bob
```

When the inner scope finishes, the previous binding is restored:

```text
NAME = Alice
```

Mental model:

```text
outer scope
NAME = Alice

    inner scope
    NAME = Bob

    inner scope ends

NAME = Alice again
```

This is a particularly useful exam pattern.

---

# 10. `where()` and `Carrier`

`where()` establishes a binding and returns a `ScopedValue.Carrier`.

This allows multiple bindings to be combined:

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

For the exam, recognise that `where()` can be chained to establish multiple Scoped Value bindings before executing the operation.

---

# 11. Scoped Values vs ThreadLocal

`ThreadLocal` is useful background for understanding Scoped Values, but it is **not the main subject of JEP 506**.

Very simply:

> **ThreadLocal = a value associated with a particular thread.**

Different threads can have different values:

```text
Thread A -> user = Alice
Thread B -> user = Bob
```

A `ThreadLocal` value can remain associated with the thread until it is removed.

A Scoped Value instead provides a value for a **bounded scope of execution**:

```text
ScopedValue
    |
    +-- scope starts -> value available
    |
    +-- called methods can access it
    |
    +-- scope ends -> binding disappears
```

The important distinction for this exam is:

> **ThreadLocal is thread-oriented; ScopedValue is scope-oriented.**

Scoped Values are designed particularly for one-way sharing of context without requiring every method to receive it as a parameter.

You do not need to learn the `ThreadLocal` API in depth for JEP 506.

---

# 12. Scoped Values Are Intended for One-Way Sharing

A Scoped Value is designed for a value that is established by an enclosing operation and read by code further down the call chain.

For example:

```java
ScopedValue.where(USER, "Alice")
           .run(() -> process());
```

Code inside `process()` can read:

```java
USER.get()
```

A useful mental model is:

```text
caller
  |
  | establishes value
  v
callee
  |
  | reads value
  v
deeper callee
  |
  | reads value
  v
scope ends
```

This is different from treating the Scoped Value as an ordinary mutable variable shared between methods.

---

# 13. Immutability

Scoped Values are designed for sharing **immutable data**.

For example:

```java
static final ScopedValue<String> USER =
    ScopedValue.newInstance();
```

`String` is immutable, so this is a natural use.

Important distinction:

> A `ScopedValue` does not magically make the object stored in it immutable.

For example, a mutable `List` stored in a Scoped Value is still a mutable `List`.

The design is intended to encourage safe, one-way sharing of immutable context.

---

# 14. Virtual Threads — Exam Context

Scoped Values were designed to work well with Java's modern concurrency model, including **virtual threads**.

The important relationship is:

```text
ScopedValue
    |
    +-- designed for efficient context sharing
        |
        +-- works well with virtual threads
```

You do **not** need to think of Scoped Values as being "for virtual threads".

They are useful independently of virtual threads.

For the Java 25 exam, the important thing is simply:

> **Scoped Values integrate well with Java's modern concurrency model, including virtual threads.**

Do not confuse the two concepts.

---

# 15. Structured Concurrency — Exam Context

Structured Concurrency is related to Scoped Values because Scoped Value bindings can be inherited by child threads when used with `StructuredTaskScope`.

However, **Structured Concurrency is still a preview feature in Java 25**.

Therefore, for an exam-focused JEP 506 lesson, you only need the relationship:

```text
ScopedValue
    |
    +-- can work with child threads
        |
        +-- StructuredTaskScope
```

You do not need to learn the Structured Concurrency API in depth as part of understanding Scoped Values.

The important permanent Java 25 feature here is:

> **Scoped Values (JEP 506).**

---

# 16. Real-World Example: Request Context

A common use case is a server handling an HTTP request.

Suppose a request has:

```text
Request ID: abc-123
```

Without a Scoped Value:

```java
handleRequest(requestId);
authenticate(requestId);
loadAccount(requestId);
databaseCall(requestId);
```

Every method has to carry the value, even when some methods don't directly use it.

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
    System.out.println("Request: " + REQUEST_ID.get());
}
```

Now `databaseCall()` can obtain the request ID without it being explicitly passed through every method.

This is the practical problem Scoped Values are designed to solve.

---

# 17. Exam Mental Model

Think of a Scoped Value as:

> **A temporary, read-oriented context value that flows down the call chain.**

The basic pattern is:

```java
static final ScopedValue<T> VALUE =
    ScopedValue.newInstance();

ScopedValue.where(VALUE, someValue)
           .run(() -> {
               // VALUE.get() is available here
           });
```

And remember:

```text
newInstance()
    ↓
creates the ScopedValue/key

where(...)
    ↓
establishes a binding

run(...) / call(...)
    ↓
executes code within the binding

get()
    ↓
reads the current value

scope ends
    ↓
binding disappears
```

---

# Exam Checklist

Know these:

* `ScopedValue.newInstance()`
* `ScopedValue.where(...)`
* `run(...)`
* `call(...)`
* `get()`
* `isBound()`
* `orElse(...)`
* `orElseThrow(...)`
* values are available to called methods within the scope
* nested scopes can rebind a value
* the outer value is restored after the nested scope ends
* a value is unbound after its scope ends
* `get()` on an unbound value throws `NoSuchElementException`
* `where()` can be chained for multiple bindings
* Scoped Values are intended for one-way sharing of context
* the relationship with virtual threads
* Structured Concurrency is only contextual background for Java 25 because it remains a preview feature

---

# Common Exam Traps

### Trap 1

```java
ScopedValue.where(NAME, "Alice")
           .run(() -> methodA());

System.out.println(NAME.get());
```

Does `get()` still return `"Alice"`?

**No.**

The scope has ended, so `NAME` is unbound.

---

### Trap 2

```java
ScopedValue.where(NAME, "Alice").run(() -> {
    ScopedValue.where(NAME, "Bob").run(() -> {
        System.out.println(NAME.get());
    });

    System.out.println(NAME.get());
});
```

Output:

```text
Bob
Alice
```

The inner binding does not permanently replace the outer binding.

---

### Trap 3

```java
NAME.get();
```

Does this return `null` if there is no binding?

**No.**

It throws `NoSuchElementException`.

---

### Trap 4

A Scoped Value is not simply a `ThreadLocal` with a different name.

The important distinction is:

```text
ThreadLocal
    -> associated with a thread

ScopedValue
    -> associated with a bounded scope of execution
```

---

# One-Line Memory Aid

> **ScopedValue = establish a value for a scope, let callees read it, then automatically lose the binding when the scope ends.**
