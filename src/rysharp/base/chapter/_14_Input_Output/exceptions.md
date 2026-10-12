# Exceptions — Execution Order and Try-with-Resources

[🔙 Back](../README.md)

## Overview

Exception questions frequently test the **order in which statements execute**, rather than simply whether an exception is caught.

The most important distinction is between:

- An exception thrown inside a `try` block.
- An exception thrown while closing a resource.
- An exception thrown inside a `catch` or `finally` block.
- An exception that becomes **suppressed** rather than propagated.

For try-with-resources, remember this order:

**Resource creation → try body → resource closing → catch → finally**

Resources close in **reverse order of successful initialization**.

## 1. Standard try-catch-finally

```java
try {
    System.out.print("A");
    throw new RuntimeException();
} catch (RuntimeException e) {
    System.out.print("B");
} finally {
    System.out.print("C");
}
```

Output:

```text
ABC
```

Execution:

1. The `try` block prints `A`.
2. The exception interrupts the remaining `try` statements.
3. The matching `catch` prints `B`.
4. The `finally` block prints `C`.

The `finally` block normally executes whether or not an exception occurs, including when a `return` is encountered.

A `finally` block does not require a `catch` block.

## 2. Try-with-resources execution order

A try-with-resources statement automatically closes resources that implement `AutoCloseable`.

```java
class Resource implements AutoCloseable {
    private final String name;

    Resource(String name) {
        this.name = name;
        System.out.print("Open " + name + " ");
    }

    @Override
    public void close() {
        System.out.print("Close " + name + " ");
    }
}
```

Example:

```java
try (Resource a = new Resource("A");
     Resource b = new Resource("B")) {

    System.out.print("Body ");

} catch (Exception e) {
    System.out.print("Catch ");

} finally {
    System.out.print("Finally");
}
```

Output:

```text
Open A Open B Body Close B Close A Finally
```

Notice that:

- Resources open from left to right.
- Resources close from right to left.
- Resources close **before** the `catch` block executes.
- Resources close **before** the `finally` block executes.

### Execution sequence

| Step | Operation |
|---|---|
| 1 | Construct resource A |
| 2 | Construct resource B |
| 3 | Execute try body |
| 4 | Close resource B |
| 5 | Close resource A |
| 6 | Execute matching catch, if needed |
| 7 | Execute finally, if present |

This order is fundamental to certification questions.

## 3. Exception thrown from `close()`

A resource's `close()` method can throw an exception.

```java
class Resource implements AutoCloseable {

    @Override
    public void close() {
        System.out.print("Close ");
        throw new RuntimeException("Close failed");
    }
}
```

Example:

```java
try (Resource r = new Resource()) {
    System.out.print("Try ");
} catch (RuntimeException e) {
    System.out.print("Catch ");
} finally {
    System.out.print("Finally");
}
```

Output:

```text
Try Close Catch Finally
```

**The exception from `close()` is thrown before the `catch` block executes.**

Because the try body completed normally, the exception from `close()` becomes the primary exception and can be caught by the associated `catch`.

## 4. Exception from the try body AND `close()`

This is the most important try-with-resources exception rule.

```java
class Resource implements AutoCloseable {

    @Override
    public void close() {
        System.out.print("Close ");
        throw new RuntimeException("Close exception");
    }
}
```

Example:

```java
try (Resource r = new Resource()) {
    System.out.print("Try ");
    throw new RuntimeException("Body exception");

} catch (RuntimeException e) {
    System.out.print(e.getMessage());
}
```

Output:

```text
Try Close Body exception
```

Both the body and `close()` throw exceptions, but **the exception from the try body remains primary**.

The exception thrown by `close()` is suppressed.

| Exception source | Treatment |
|---|---|
| Try body | Primary exception |
| Resource `close()` | Suppressed exception |

### Inspecting suppressed exceptions

```java
try (Resource r = new Resource()) {
    throw new RuntimeException("Body exception");

} catch (RuntimeException e) {
    System.out.println(e.getMessage());

    for (Throwable suppressed : e.getSuppressed()) {
        System.out.println(suppressed.getMessage());
    }
}
```

Output:

```text
Close Body exception
Close exception
```

The first `Close` comes from the resource's `close()` method.

**Exam rule:** If the try body throws and resource closing also throws, the body exception wins.

## 5. Multiple resources throwing during `close()`

When multiple resources are declared, they close in reverse order, even if one `close()` throws.

```java
class Resource implements AutoCloseable {
    private final String name;

    Resource(String name) {
        this.name = name;
    }

    @Override
    public void close() {
        System.out.print(name + " ");
        throw new RuntimeException(name);
    }
}
```

Example:

```java
try (Resource a = new Resource("A");
     Resource b = new Resource("B")) {

    System.out.print("Try ");

} catch (RuntimeException e) {
    System.out.print("Caught " + e.getMessage());

    for (Throwable suppressed : e.getSuppressed()) {
        System.out.print(" Suppressed " + suppressed.getMessage());
    }
}
```

Output:

```text
Try B A Caught B Suppressed A
```

Why?

1. The body completes normally.
2. Resource B closes first and throws.
3. Resource A still closes and also throws.
4. B's exception becomes primary.
5. A's exception becomes suppressed.

**When the body succeeds, the first exception encountered during reverse-order closing becomes primary.**

If the body had thrown an exception, that body exception would remain primary and both closing exceptions would be suppressed.

## 6. Exception during resource initialization

Resources are initialized left to right.

If a later resource fails to initialize, previously initialized resources must still be closed.

```java
class Resource implements AutoCloseable {
    private final String name;

    Resource(String name) {
        this.name = name;
        System.out.print("Open " + name + " ");

        if (name.equals("B")) {
            throw new RuntimeException("B failed");
        }
    }

    @Override
    public void close() {
        System.out.print("Close " + name + " ");
    }
}
```

Example:

```java
try (Resource a = new Resource("A");
     Resource b = new Resource("B")) {

    System.out.print("Body ");

} catch (RuntimeException e) {
    System.out.print("Catch");
}
```

Output:

```text
Open A Open B Close A Catch
```

Important:

- A is successfully initialized.
- B's constructor throws.
- The try body never executes.
- A is automatically closed.
- B is not closed because its initialization never completed.
- The exception is then handled by `catch`.

If closing A also throws, that closing exception is suppressed under the exception from B's failed initialization.

## 7. `catch` versus `finally` when closing throws

Consider:

```java
try (Resource r = new Resource()) {
    System.out.print("A ");
} catch (RuntimeException e) {
    System.out.print("B ");
} finally {
    System.out.print("C ");
}
```

If `close()` throws, the order is:

```text
A Close B C
```

It is **not**:

```text
A B Close C
```

This is a particularly common exam trap.

The implicit resource-closing operation is part of completing the try-with-resources statement's `try` portion. The associated `catch` and `finally` run afterwards.

## 8. `finally` throwing an exception

A `finally` block can override an exception already being propagated.

```java
try {
    throw new IllegalArgumentException("Try");
} finally {
    throw new IllegalStateException("Finally");
}
```

The propagated exception is:

```text
IllegalStateException: Finally
```

The original `IllegalArgumentException` is replaced; it is **not automatically added as a suppressed exception**.

Compare:

| Scenario | Primary exception |
|---|---|
| Try body throws; `close()` throws | Try body exception |
| Try body succeeds; `close()` throws | Close exception |
| Multiple `close()` calls throw | First closing exception |
| Try body throws; `finally` throws | Finally exception |

A `return` inside `finally` can also suppress a pending exception or replace an earlier return value. This is legal but strongly discouraged.

## 9. `AutoCloseable` versus `Closeable`

| Feature | `AutoCloseable` | `Closeable` |
|---|---|---|
| Package | `java.lang` | `java.io` |
| Method | `close()` | `close()` |
| Declared checked exception | `Exception` | `IOException` |
| Supported by try-with-resources | Yes | Yes |
| Relationship | Parent interface | Extends `AutoCloseable` |

A resource in try-with-resources must implement `AutoCloseable`.

The compiler checks the declared `close()` method's possible checked exceptions.

```java
class Resource implements AutoCloseable {
    @Override
    public void close() throws Exception {
    }
}
```

Because `close()` declares `Exception`, the following requires appropriate exception handling or declaration:

```java
void run() throws Exception {
    try (Resource r = new Resource()) {
        System.out.println("Working");
    }
}
```

## 10. Checked versus unchecked exceptions

| Type | Checked? | Must catch or declare? |
|---|---|---|
| `Exception` | Yes | Yes |
| `IOException` | Yes | Yes |
| `SQLException` | Yes | Yes |
| `RuntimeException` | No | No |
| `NullPointerException` | No | No |
| `IllegalArgumentException` | No | No |
| `Error` | No | No |

The main rule:

**Checked exceptions must be handled or declared; unchecked exceptions do not have this requirement.**

`RuntimeException` and its subclasses are unchecked, as are `Error` and its subclasses.

## 11. Catch ordering and compilation

More specific exception types must appear before broader types.

```java
try {
    // Code
} catch (FileNotFoundException e) {
    // Specific
} catch (IOException e) {
    // General
}
```

Reversing the catch blocks would make `FileNotFoundException` unreachable.

### Multi-catch

```java
catch (IOException | SQLException e) {
    // Handle either exception
}
```

Multi-catch alternatives cannot be related by subclassing.

```java
catch (FileNotFoundException | IOException e) {
    // DOES NOT COMPILE
}
```

The multi-catch parameter is implicitly final.

## 12. Existing resources in try-with-resources

Since Java 9, an existing final or effectively final resource can be used directly.

```java
Resource resource = new Resource();

try (resource) {
    System.out.println("Working");
}
```

The resource must be final or effectively final.

The resource variable remains in scope after the statement, but the resource has already been closed.

## Quick Reference

| Situation | Execution / Outcome |
|---|---|
| Standard `try-catch-finally` | Try → Catch (if applicable) → Finally |
| Try-with-resources | Initialize → Body → Close → Catch → Finally |
| Multiple resources | Initialize left to right; close right to left |
| Body and `close()` both throw | Body exception primary; close exception suppressed |
| Only `close()` throws | Close exception primary |
| Multiple closes throw | First closing exception primary; later ones suppressed |
| Resource initialization fails | Close previously initialized resources |
| `finally` throws | Replaces pending exception |
| `getSuppressed()` | Retrieves exceptions suppressed during resource closing |

## Final Memory Kicks

- **Resources close before `catch` and `finally`.**
- Resources open **left to right** and close **right to left**.
- If `close()` throws, later closing operations still happen.
- **Body exception wins over close exception.**
- A close exception becomes **suppressed** when another exception is already primary.
- If only closing throws, the **first close exception wins**.
- `finally` throwing is different from resource closing throwing: it can **replace** the pending exception.
- Failed resource initialization still closes earlier successfully initialized resources.
- `AutoCloseable.close()` may declare `Exception`.
- Existing resources in try-with-resources must be **final or effectively final**.

**Exam technique:** For every execution-order question, write down the events in this sequence:

**Initialize → Try body → Close (reverse order) → Catch → Finally**

Then determine which exception is primary and which, if any, are suppressed.