# Switch

[🔙 Back](README.md)

Quick reference for Java `switch` statements, expressions, patterns and common compile-time traps.

## Contents

- [Supported Selector Types](#supported-selector-types)
- [Switch Statements vs Expressions](#switch-statements-vs-expressions)
- [Arrow vs Colon Labels](#arrow-vs-colon-labels)
- [Switch Expressions and yield](#switch-expressions-and-yield)
- [Pattern Matching](#pattern-matching)
- [Guarded Patterns](#guarded-patterns)
- [Pattern Dominance](#pattern-dominance)
- [Null Handling](#null-handling)
- [Exhaustiveness](#exhaustiveness)
- [Quick Reference](#quick-reference)

---

## Supported Selector Types

A `switch` selector can use:

```text
Primitive:
byte
short
char
int

Wrapper:
Byte
Short
Character
Integer

Reference:
String
enum
other reference types with pattern matching
```

These primitive types are not supported as normal switch selectors:

```text
boolean
long
float
double
```

### `var`

`var` is not itself a selector type. Its inferred type determines whether the switch is valid.

```java
var value = 10;   // int

switch (value) {
    case 10 -> System.out.println("Ten");
    default -> System.out.println("Other");
}
```

This works because `value` is inferred as `int`.

---

## Switch Statements vs Expressions

A switch can be either a **statement** or an **expression**.

### Statement

Performs an action:

```java
switch (value) {
    case 1 -> System.out.println("One");
    case 2 -> System.out.println("Two");
    default -> System.out.println("Other");
}
```

No value is produced.

### Expression

Produces a value:

```java
int result = switch (value) {
    case 1 -> 10;
    case 2 -> 20;
    default -> 0;
};
```

A switch expression must be exhaustive.

Memory:

```text
switch statement  → performs action
switch expression → produces value
```

The distinction is **not** determined by `:` or `->`.

Both statements and expressions can use either form.

---

## Arrow vs Colon Labels

### Arrow `->`

```java
switch (value) {
    case 1 -> System.out.println("One");
    case 2 -> System.out.println("Two");
    default -> System.out.println("Other");
}
```

Arrow rules do **not** fall through.

### Colon `:`

Traditional statement groups can fall through:

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

Without `break`, execution can continue into the following statement group.

Memory:

```text
->  → no fall-through
:   → can fall through
```

Do not confuse this with statement vs expression:

```text
statement  → can use : or ->
expression → can use : or ->
```

---

## Switch Expressions and `yield`

An arrow expression directly supplies a value:

```java
int result = switch (value) {
    case 1 -> 100;
    default -> 0;
};
```

### Block Rules

If an arrow rule needs multiple statements, use a block and `yield`:

```java
int result = switch (value) {
    case 1 -> {
        System.out.println("One");
        yield 100;
    }
    default -> 0;
};
```

`yield` supplies the value of the switch expression.

Memory:

```text
case X -> value

case X -> {
    statements;
    yield value;
}
```

### Colon Syntax in an Expression

A switch expression can also use traditional colon labels:

```java
int result = switch (value) {
    case 1:
        yield 100;
    default:
        yield 0;
};
```

Again:

> **`:` vs `->` does not determine whether the switch is a statement or expression.**

---

## Pattern Matching

Reference-type selectors can use type patterns:

```java
Object obj = "Java";

switch (obj) {
    case String s  -> System.out.println(s.length());
    case Integer i -> System.out.println(i * 2);
    case null      -> System.out.println("null");
    default        -> System.out.println("other");
}
```

The pattern variable has the matched type:

```java
case String s -> System.out.println(s.length());
```

Inside that rule:

```text
s is known to be a String
```

Pattern matching with `instanceof` and flow scoping is covered separately in the **Pattern Matching** notes.

---

## Guarded Patterns

A type pattern can include a `when` guard:

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

The guarded case matches when:

```text
type pattern matches
        AND
guard evaluates to true
```

### Ordering

Put the guarded/specific case before its unguarded equivalent:

```java
case String s when s.length() > 5 -> ...
case String s                     -> ...
```

The unguarded case covers every `String`, so placing it first would make the later guarded case unreachable.

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

Every `String` already matches `Object`.

Correct:

```java
switch (obj) {
    case String s -> System.out.println("String");
    case Object o -> System.out.println("Object");
}
```

Memory:

> **Specific patterns before general patterns.**

Think:

```text
String
   ↓
Object

specific → general   ✓
general → specific   ✗ dominated
```

---

## Null Handling

Reference selectors require special consideration for `null`.

### `case null`

Handle it explicitly:

```java
switch (obj) {
    case null     -> System.out.println("null");
    case String s -> System.out.println(s);
    default       -> System.out.println("other");
}
```

`case null`:

```text
explicitly handles null
is not itself a type pattern
does not have to be the final label
```

### `default` Does Not Mean `null`

A normal `default` is the fallback when no case matches.

It does not generally mean:

```text
"everything else including null"
```

For example:

```java
switch (obj) {
    case String s -> System.out.println(s);
    default       -> System.out.println("other");
}
```

A `null` selector is not automatically handled by `default` and can result in `NullPointerException`.

Memory:

```text
default   → fallback
case null → explicitly handles null
```

### `case null, default`

They can be combined:

```java
switch (obj) {
    case String s      -> System.out.println(s);
    case Integer i     -> System.out.println(i);
    case null, default -> System.out.println("other");
}
```

This handles:

```text
null
+
anything otherwise unmatched
```

Unlike standalone `case null`:

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

Memory:

```text
case null
    → explicit null handling
    → needn't be last

case null, default
    → null + fallback
    → must be last
```

---

## Exhaustiveness

### Switch Expressions

A switch expression must always be exhaustive:

```java
int result = switch (value) {
    case 1 -> 10;
    case 2 -> 20;
    default -> 0;
};
```

Every possible selector value must result in a value.

### Ordinary Switch Statements

An ordinary switch statement does not normally need to be exhaustive:

```java
switch (value) {
    case 1 -> System.out.println("One");
    case 2 -> System.out.println("Two");
}
```

### Enhanced Switch Statements

A switch statement that uses:

```text
a case pattern
OR
case null
```

is an **enhanced switch statement**.

Enhanced switch statements must be exhaustive.

Think:

```text
                    SWITCH
                       │
              ┌────────┴────────┐
              │                 │
          STATEMENT         EXPRESSION
              │                 │
        performs action     produces value
              │                 │
              │            exhaustive
        ┌─────┴─────┐
        │           │
    ordinary     enhanced
        │           │
   needn't be    exhaustive
   exhaustive
```

### Achieving Exhaustiveness

`default` is the obvious approach:

```java
switch (obj) {
    case String s -> System.out.println(s);
    default       -> System.out.println("other");
}
```

But `default` is not always required if the compiler can determine that all possibilities are covered.

For example:

```java
enum Size {
    SMALL, LARGE
}

int result = switch (size) {
    case SMALL -> 1;
    case LARGE -> 2;
};
```

All enum constants are covered.

Sealed hierarchies can similarly allow exhaustive pattern matching.

### Redundant `default`

An already exhaustive switch can still contain a legal `default` in situations where it is redundant.

Do not reason:

```text
exhaustive
    ↓
default must be unreachable
    ↓
compile error
```

Exhaustiveness and pattern dominance are separate concepts.

---

# Quick Reference

## Selector Types

```text
byte / Byte          ✓
short / Short        ✓
char / Character     ✓
int / Integer        ✓

String               ✓
enum                 ✓
reference patterns   ✓

boolean              ✗
long                 ✗
float                ✗
double               ✗
```

## Statement vs Expression

```text
STATEMENT
→ performs action
→ ordinary statement needn't be exhaustive

EXPRESSION
→ produces value
→ must be exhaustive

ENHANCED STATEMENT
→ uses pattern and/or case null
→ must be exhaustive
```

## Syntax

```text
:   → can fall through
->  → no fall-through
```

Independent of:

```text
statement vs expression
```

## Switch Expression

```java
case X -> value;
```

or:

```java
case X -> {
    statements;
    yield value;
}
```

## Patterns

```text
specific → general                 ✓
general → specific                 ✗ dominated

guarded pattern → unguarded        ✓
unguarded → equivalent guarded     ✗ dominated
```

## Null

```text
case null
→ explicitly handles null
→ not itself a type pattern
→ needn't be last

default
→ fallback when no case matches
→ does not generally handle null

case null, default
→ null + otherwise unmatched
→ must be last
```

## Exhaustiveness

```text
switch expression
→ exhaustive

ordinary switch statement
→ needn't be exhaustive

enhanced switch statement
→ exhaustive
```

## Final Memory Kicks

> **Statement = action; expression = value.**

> **`:` vs `->` controls fall-through style, not statement vs expression.**

> **Switch expressions and enhanced switch statements must be exhaustive.**

> **Specific patterns go before general patterns.**

> **Guarded pattern goes before its unguarded equivalent.**

> **`default` is fallback; `case null` handles null explicitly.**

> **`case null, default` must be last.**

> **Arrow block in a switch expression → use `yield` to produce the value.**