# Unnamed Variables and Patterns (_)

Quick reference for Java unnamed variables and patterns using `_`.

## Contents

- [The Basic Idea](#the-basic-idea)
- [Unnamed Variables](#unnamed-variables)
- [Where `_` Can Be Used](#where--can-be-used)
- [Unnamed Patterns](#unnamed-patterns)
- [`_` Cannot Be Referenced](#_-cannot-be-referenced)
- [`_` Is Not a Universal Wildcard](#_-is-not-a-universal-wildcard)
- [Quick Reference](#quick-reference)

---

## The Basic Idea

Java 22 made **unnamed variables and patterns** permanent.

The underscore:

```java
_
```

means:

> **A value exists here, but I deliberately do not need to name or use it.**

For example:

```java
try {
    doSomething();
} catch (Exception _) {
    System.out.println("Failed");
}
```

The exception is caught, but the exception object does not receive a usable variable name.

The core mental model is:

> **Declare it, ignore it, never refer to it.**

---

## Unnamed Variables

An unnamed variable allows Java to receive or initialise a value without giving that value a usable name.

For example:

```java
int _ = calculateSomething();
```

The calculation still happens:

```java
calculateSomething();
```

but its resulting value cannot subsequently be accessed through `_`.

This can be useful when the declaration is required but the value itself is irrelevant.

### Multiple `_` Declarations

Because `_` does not introduce a normal variable name, multiple unnamed variables can appear where the syntax permits them.

Think:

```text
_
≠ normal identifier
```

There is no ordinary variable named `_` competing with another variable named `_`.

---

## Where `_` Can Be Used

Java permits `_` in specific unnamed-variable and unnamed-pattern contexts.

### Local Variable

```java
int _ = calculateSomething();
```

The initializer executes, but the resulting value is deliberately unnamed.

### Enhanced `for`

```java
for (String _ : names) {
    doSomething();
}
```

Each element is obtained as part of the loop, but the loop body does not need to refer to it.

### Try-With-Resources

```java
try (var _ = openResource()) {
    doSomething();
}
```

The resource still participates normally in try-with-resources and is automatically closed.

Its reference simply has no usable name.

### `catch`

```java
try {
    something();
} catch (Exception _) {
    handleFailure();
}
```

The exception type matters, but the exception object itself is ignored.

### Lambda Parameters

```java
BiFunction<Integer, Integer, Integer> function =
        (x, _) -> x * 2;
```

The lambda accepts two arguments but deliberately ignores the second.

Another example:

```java
list.forEach(_ -> doSomething());
```

The lambda is invoked for each element, but the element itself is irrelevant to the body.

### Traditional `for` Loop

An underscore can represent an unused variable, but it cannot then be used to control the loop.

This therefore does **not** compile:

```java
for (int _ = 0; _ < 10; _++) {
    doSomething();
}
```

The problem is not the declaration:

```java
int _ = 0;
```

The problem is the later attempts to reference `_`:

```java
_ < 10
_++
```

Memory:

> **Declaring `_` can be legal; reading or updating it through `_` is not.**

---

## Unnamed Patterns

`_` can also appear as an **unnamed pattern**.

For example:

```java
if (obj instanceof String _) {
    System.out.println("It's a String");
}
```

Here the code cares that:

```text
obj is a String
```

but does not need a variable referring to that `String`.

Compare:

```java
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

with:

```java
if (obj instanceof String _) {
    System.out.println("It's a String");
}
```

Think:

```text
String s
→ match String
→ keep reference as s

String _
→ match String
→ ignore reference
```

Unnamed patterns are also useful within pattern structures such as record patterns when part of the matched data is irrelevant.

Memory:

> **The pattern still performs the match; `_` discards the value that would otherwise receive a name.**

---

## `_` Cannot Be Referenced

This is the most important rule.

An underscore used as an unnamed variable or pattern does **not** introduce a variable that can subsequently be referenced.

This does not compile:

```java
catch (Exception _) {
    System.out.println(_); // DOES NOT COMPILE
}
```

Neither does:

```java
int _ = 10;

System.out.println(_); // DOES NOT COMPILE
```

Think:

```text
int _ = 10;
    ↑
value deliberately has no usable name
```

not:

```text
create a normal variable whose name happens to be _
```

### Assignment and Modification

The same rule means you cannot subsequently modify it:

```java
int _ = 10;

_++;      // DOES NOT COMPILE
_ = 20;   // DOES NOT COMPILE
```

There is no variable named `_` to update.

### Fields

`_` is not a general-purpose identifier that can be used for fields.

This does not compile:

```java
class Example {

    int _ = 10;       // DOES NOT COMPILE

    static String _;  // DOES NOT COMPILE
}
```

Java permits `_` only in the supported unnamed-variable and pattern contexts.

Memory:

> **`_` means unnamed — it is not a special spelling of a normal variable name.**

---

## `_` Is Not a Universal Wildcard

Do not interpret `_` as:

```text
match absolutely anything
```

It is not a standalone wildcard syntax that can be inserted anywhere a pattern might appear.

For example:

```java
if (obj instanceof _) {
}
```

does **not** compile.

Nor can you simply write:

```java
case _ -> ...
```

as a universal top-level match-anything case.

Instead, `_` is used where Java has a supported variable or pattern component whose value can deliberately be ignored.

Think:

```text
String _
       ↑
ignore the value produced
by this String pattern
```

rather than:

```text
_
↑
universal wildcard
```

---

# Quick Reference

## Core Meaning

```java
_
```

means:

```text
a value exists here
        ↓
I deliberately don't need its name
        ↓
I cannot refer to it later
```

Memory:

> **Declare it, ignore it, never refer to it.**

## Common Uses

| Context | Example |
|---|---|
| Local variable | `int _ = calculate();` |
| Enhanced `for` | `for (String _ : names)` |
| Try-with-resources | `try (var _ = open())` |
| `catch` | `catch (Exception _)` |
| Lambda | `(x, _) -> x * 2` |
| Pattern | `obj instanceof String _` |

## Declaration vs Use

This can be legal:

```java
int _ = calculate();
```

This is not:

```java
System.out.println(_);
```

Likewise:

```java
for (String _ : names) {
    doSomething();
}
```

can be valid because the element is ignored.

But:

```java
for (int _ = 0; _ < 10; _++) {
}
```

is invalid because `_` is subsequently referenced.

## Pattern Matching

```java
obj instanceof String s
```

means:

```text
match String
+
bind matched value to s
```

Whereas:

```java
obj instanceof String _
```

means:

```text
match String
+
ignore matched value
```

## Not Allowed

```text
reference _ later                 ✗
assign to _ later                 ✗
increment _                       ✗
field named _                     ✗
standalone instanceof _           ✗
universal case _ wildcard         ✗
```

## Reliable Check

When you encounter `_`:

```text
1. Is this a supported unnamed
   variable or pattern context?

2. If YES:
   the value deliberately receives
   no usable name

3. Does later code try to use _?

   YES
   → DOES NOT COMPILE

4. Is _ being treated as an ordinary
   field or general identifier?

   YES
   → DOES NOT COMPILE

5. Is _ being used as a standalone
   universal wildcard?

   YES
   → DOES NOT COMPILE
```

## Final Memory Kicks

> **`_` means: “I deliberately don't need this value.”**

> **An unnamed variable can receive a value, but `_` cannot subsequently be used to access that value.**

> **`int _ = calculate();` can be valid; `System.out.println(_);` is not.**

> **Useful contexts include locals, enhanced `for`, try-with-resources, `catch`, lambda parameters and patterns.**

> **`String _` can test a pattern without retaining a usable reference to the matched value.**

> **`_` is not a normal variable name.**

> **Fields cannot simply be named `_`.**

> **`_` is not a universal match-anything wildcard.**

> **Declare it → ignore it → never refer to it.**