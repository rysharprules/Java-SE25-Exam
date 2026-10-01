# Java 25 `switch` — Quick Reference

## Supported Selector Types

A `switch` selector can use:

```text
Primitive:
- byte
- short
- char
- int

Wrapper:
- Byte
- Short
- Character
- Integer

Reference types:
- String
- enum types
- other reference types with pattern matching
```

The following primitive types are **not supported** as `switch` selectors:

```text
boolean
long
float
double
```

### `var`

`var` is not itself a switch-supported type.

It performs local variable type inference:

```java
var value = 10; // value is int

switch (value) {
    case 10 -> System.out.println("Ten");
    default -> System.out.println("Other");
}
```

The switch is valid because `value` has inferred type `int`, not because `var` is a selector type.

---

## `case null`

`case null` allows a switch to explicitly handle a `null` selector:

```java
Object obj = null;

switch (obj) {
    case null     -> System.out.println("null");
    case String s -> System.out.println(s);
    default       -> System.out.println("other");
}
```

`case null` is **not itself a pattern**.

It does **not** have to be the last case.

A switch statement containing either:

* a case pattern, or
* `case null`

is an **enhanced switch statement**.

Enhanced switch statements must be exhaustive.

---

## `case null, default`

`null` and `default` can be combined:

```java
switch (obj) {
    case String s      -> System.out.println(s);
    case Integer i     -> System.out.println(i);
    case null, default -> System.out.println("other");
}
```

This handles `null` and anything not matched by another case.

Unlike `case null` on its own:

> **`case null, default` must be the final switch label.**

Valid:

```java
switch (obj) {
    case String s      -> System.out.println(s);
    case null, default -> System.out.println("other");
}
```

Invalid:

```java
switch (obj) {
    case null, default -> System.out.println("other");
    case String s      -> System.out.println(s); // DOES NOT COMPILE
}
```

Memory rule:

```text
case null
    → may appear anywhere appropriate

case null, default
    → must be last
```

---

## Pattern Matching

A reference-type selector can use type patterns:

```java
Object obj = "Java";

switch (obj) {
    case String s  -> System.out.println(s.length());
    case Integer i -> System.out.println(i * 2);
    case null      -> System.out.println("null");
    default        -> System.out.println("other");
}
```

The pattern variable is available only where the pattern matches:

```java
case String s -> System.out.println(s.length());
```

Here `s` is known to be a `String`.

---

## Pattern Dominance

A broader pattern cannot appear before a narrower pattern that it dominates.

Invalid:

```java
switch (obj) {
    case Object o -> System.out.println("Object");
    case String s -> System.out.println("String"); // DOES NOT COMPILE
}
```

Every `String` is already matched by `Object`, so the `String` case can never be reached.

Reverse the order:

```java
switch (obj) {
    case String s -> System.out.println("String");
    case Object o -> System.out.println("Object");
}
```

Mental model:

> **Specific patterns before general patterns.**

---

## Guarded Patterns — `when`

A pattern can have a guard:

```java
switch (obj) {
    case String s when s.length() > 5 ->
        System.out.println("Long String");

    case String s ->
        System.out.println("Other String");

    default ->
        System.out.println("Other");
}
```

The case matches only when:

1. the pattern matches, and
2. the `when` condition is `true`.

Order matters.

Put the guarded/specific case first:

```java
case String s when s.length() > 5 -> ...
case String s                     -> ...
```

Not:

```java
case String s                     -> ...
case String s when s.length() > 5 -> ... // dominated
```

---

## Exhaustiveness

A `switch` **expression** must always be exhaustive:

```java
int result = switch (value) {
    case 1 -> 10;
    case 2 -> 20;
    default -> 0;
};
```

An **enhanced switch statement** must also be exhaustive.

A `default` is one way to achieve this:

```java
switch (obj) {
    case String s -> System.out.println(s);
    default       -> System.out.println("other");
}
```

But `default` is not always necessary when the compiler can prove all possible values are covered.

For example, an enum can cover every constant:

```java
enum Size { SMALL, LARGE }

Size size = Size.SMALL;

int value = switch (size) {
    case SMALL -> 1;
    case LARGE -> 2;
};
```

Sealed hierarchies can similarly allow exhaustive pattern switches.

---

## `null` and `default` Are Different

A normal `default` does **not** generally mean "including null."

For a reference selector:

```java
switch (obj) {
    case String s -> System.out.println(s);
    default       -> System.out.println("other");
}
```

a `null` selector can still result in `NullPointerException`.

To explicitly handle `null`:

```java
case null -> ...
```

or:

```java
case null, default -> ...
```

This distinction is easy to miss:

> **`default` handles unmatched non-null values; `case null` explicitly handles null.**

---

## Arrow Cases vs Colon Cases

Modern arrow syntax:

```java
switch (value) {
    case 1 -> System.out.println("One");
    case 2 -> System.out.println("Two");
    default -> System.out.println("Other");
}
```

does not fall through.

Traditional colon syntax still exists:

```java
switch (value) {
    case 1:
        System.out.println("One");
        break;
    case 2:
        System.out.println("Two");
        break;
}
```

Without `break`, traditional statement groups can fall through.

---

## `yield`

For a switch expression, an arrow expression supplies its value directly:

```java
int result = switch (value) {
    case 1 -> 100;
    default -> 0;
};
```

If an arrow case needs a block, use `yield` to produce the value:

```java
int result = switch (value) {
    case 1 -> {
        System.out.println("One");
        yield 100;
    }
    default -> 0;
};
```

Mental model:

```text
-> expression
    → expression supplies value

-> { block }
    → yield supplies value
```

---

# Memory Summary

```text
SELECTOR TYPES

byte / Byte         ✓
short / Short       ✓
char / Character    ✓
int / Integer       ✓
String              ✓
enum                ✓
reference patterns  ✓

boolean             ✗
long                ✗
float               ✗
double              ✗
```

```text
NULL

case null
    → explicitly handles null
    → not itself a pattern
    → does not have to be last

case null, default
    → handles null + otherwise unmatched values
    → must be last

default alone
    "Fallback when no case matches."
    → does not generally handle null
    → allowed even when apparently redundant.
```

```text
PATTERNS

specific → general       ✓

general → specific       ✗ dominated

guarded pattern → unguarded pattern   ✓
```

```text
EXHAUSTIVENESS

switch expression and enhanced switch statement
    → must be exhaustive
```

```text
SWITCH EXPRESSION

case X -> value

case X -> {
    statements;
    yield value;
}
```
