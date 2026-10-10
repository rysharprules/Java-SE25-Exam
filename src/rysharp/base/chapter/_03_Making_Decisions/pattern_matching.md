# Pattern Matching

[🔙 Back](README.md)

Quick reference for `instanceof` pattern matching and flow scoping.

## Contents

- [instanceof Pattern Matching](#instanceof-pattern-matching)
- [Null](#null)
- [Flow Scoping](#flow-scoping)
- [Boolean Expressions](#boolean-expressions)
- [Negation and Early Exit](#negation-and-early-exit)
- [Pattern Variable Behaviour](#pattern-variable-behaviour)
- [Compile-Time Type Compatibility](#compile-time-type-compatibility)
- [Generics and instanceof](#generics-and-instanceof)
- [Quick Reference](#quick-reference)

---

## `instanceof` Pattern Matching

Traditional `instanceof` requires an explicit cast:

```java
Object obj = "Hello";

if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}
```

Pattern matching combines the type test and declaration:

```java
Object obj = "Hello";

if (obj instanceof String s) {
    System.out.println(s.length());
}
```

Syntax:

```java
reference instanceof Type variable
```

For:

```java
obj instanceof String s
```

Java asks:

```text
Is obj a String?
       ↓ yes
Introduce s as a String
```

There is no explicit cast.

---

## Null

`null` does not match a type pattern.

```java
Object obj = null;

if (obj instanceof String s) {
    System.out.println(s);
}
```

The condition is:

```text
false
```

Therefore:

```java
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

is automatically null-safe.

You don't need:

```java
obj != null && obj instanceof String s
```

Memory:

> **`null` never matches an `instanceof` type pattern.**

---

## Flow Scoping

A pattern variable is in scope only where the compiler can determine that the pattern **must have matched**.

This is called **flow scoping**.

Do not think:

> "`s` is available inside the nearest braces."

Instead ask:

> **Could execution reach this use of `s` if the pattern failed?**

If yes:

```text
s is not available
```

If no:

```text
s can be available
```

### Basic `if`

```java
Object obj = "Java";

if (obj instanceof String s) {
    System.out.println(s.length());   // OK
}

System.out.println(s);                // DOES NOT COMPILE
```

Inside the `if`, the pattern must have matched.

After the `if`, execution could have arrived there when the pattern was false.

### `else`

```java
if (obj instanceof String s) {
    System.out.println(s);       // OK
} else {
    System.out.println(s);       // DOES NOT COMPILE
}
```

The `else` branch executes when the pattern is false, so `s` is not available there.

Memory:

> **Pattern-variable scope follows control flow, not simply `{ }` block structure.**

---

## Boolean Expressions

Short-circuit operators are important because flow scoping follows the paths through the expression.

### `&&`

This is valid:

```java
if (obj instanceof String s && s.length() > 3) {
    System.out.println(s);
}
```

`&&` evaluates left-to-right and short-circuits.

The right side is reached only when:

```java
obj instanceof String s
```

is true.

Therefore `s` is available:

```text
pattern matches
      ↓
      s exists
      ↓
evaluate s.length()
```

### Reversed `&&`

This does not compile:

```java
if (s.length() > 3 && obj instanceof String s) {
    // ...
}
```

At:

```java
s.length()
```

the pattern has not been evaluated yet.

### `||`

This does not compile:

```java
if (obj instanceof String s || s.length() > 3) {
    // ...
}
```

The right side of `||` is evaluated when the left side is **false**.

That means:

```java
obj instanceof String s
```

failed precisely when Java might need to evaluate:

```java
s.length()
```

So `s` cannot be used there.

Memory:

```text
pattern && use-pattern-variable   ✓

pattern || use-pattern-variable   ✗
```

Or:

> **`&&` can carry a successful match forward. `||` cannot assume the match succeeded.**

### Chained `&&`

```java
if (obj instanceof String s
        && !s.isEmpty()
        && s.length() > 5) {

    System.out.println(s.toUpperCase());
}
```

This is valid.

Each later operand is reached only if every earlier operand was true.

### Parentheses

Parentheses affect grouping, but do not override flow-scoping rules.

Valid:

```java
if ((obj instanceof String s) && s.length() > 2) {
    // OK
}
```

Invalid:

```java
if ((obj instanceof String s) || s.length() > 2) {
    // DOES NOT COMPILE
}
```

Still ask:

> **Could this use of `s` be reached when the pattern failed?**

---

## Negation and Early Exit

Negation makes flow scoping particularly interesting.

### Negated Pattern

```java
if (!(obj instanceof String s)) {
    // pattern has not successfully matched here
}
```

Inside the block, you cannot treat `s` as a successfully matched `String`.

### Early `return`

```java
static void printLength(Object obj) {
    if (!(obj instanceof String s)) {
        return;
    }

    System.out.println(s.length());
}
```

This compiles.

Consider the two paths:

```text
NOT a String
     ↓
enter if
     ↓
return
     ↓
cannot reach later code
```

or:

```text
IS a String
     ↓
s successfully matched
     ↓
condition is false
     ↓
continue after if
```

Therefore, reaching:

```java
System.out.println(s.length());
```

proves the pattern succeeded.

### Early `throw`

The same applies if the failed path throws:

```java
static void printLength(Object obj) {
    if (!(obj instanceof String s)) {
        throw new IllegalArgumentException();
    }

    System.out.println(s.length());
}
```

The failed-pattern path cannot continue.

### Normal Completion

Compare:

```java
if (!(obj instanceof String s)) {
    System.out.println("Not a String");
}

System.out.println(s.length());   // DOES NOT COMPILE
```

The `if` body can finish normally.

Execution can therefore reach the final line when the pattern failed.

Memory:

```text
failed pattern → return/throw → later use may be valid

failed pattern → continues    → later use invalid
```

---

## Pattern Variable Behaviour

### Local Variable

Once successfully matched and in scope, a pattern variable behaves much like a local variable:

```java
if (obj instanceof String s) {
    System.out.println(s);
}
```

What makes it unusual is that its scope is determined by control flow.

### Same Object

Pattern matching does not create another object:

```java
if (obj instanceof String s) {
    // obj and s refer to the same object
}
```

Conceptually:

```text
obj ───┐
       ├──► String object
s   ───┘
```

`s` simply gives you a reference with the more specific type.

---

## Compile-Time Type Compatibility

`instanceof` tests must make sense based on the compile-time types involved.

### Reference Types

Normal `instanceof` operates on references.

```java
int x = 10;

if (x instanceof Integer i) { }   // DOES NOT COMPILE
```

Automatic boxing does not make this a valid normal `instanceof` expression.

### Possible Type Match

This is valid:

```java
Object obj = "Java";

if (obj instanceof Integer i) {
    // ...
}
```

It evaluates to `false` for this particular object, but the test itself is legal.

Why?

An `Object` reference could potentially refer to an `Integer`.

### Impossible Type Match

This is not valid:

```java
String s = "Java";

if (s instanceof Integer i) { }   // DOES NOT COMPILE
```

A `String` cannot possibly be an `Integer`.

Memory:

```text
Object → Integer?
possible                 ✓ compile

String → Integer?
impossible               ✗ compile
```

The compiler considers what is possible from the **declared types**, not merely the object's current runtime value.

---

## Generics and `instanceof`

Runtime type erasure limits which generic types can be tested.

### Parameterized Type

Invalid:

```java
Object obj = new ArrayList<String>();

if (obj instanceof ArrayList<String> list) {
    // DOES NOT COMPILE
}
```

Java cannot test the `<String>` type argument at runtime.

### Unbounded Wildcard

This is valid:

```java
if (obj instanceof ArrayList<?> list) {
    System.out.println(list.size());
}
```

The wildcard does not require Java to determine a specific erased type argument.

Memory:

```text
ArrayList<String>   ✗
ArrayList<?>        ✓

List<String>        ✗
List<?>             ✓
```

---

# Quick Reference

## Basic Pattern

```java
if (obj instanceof String s) {
    use(s);   // ✓
}
```

Equivalent idea:

```text
type test + cast + variable declaration
```

### Null

```text
null instanceof Type
→ false
```

## Flow Scoping

| Code | Compiles? | Reason |
|---|---:|---|
| `obj instanceof String s` | ✓ | Valid type pattern |
| `obj instanceof String s && s.isEmpty()` | ✓ | RHS reached after successful match |
| `obj instanceof String s \|\| s.isEmpty()` | ✗ | RHS can execute when pattern failed |
| `if (obj instanceof String s) { use(s); }` | ✓ | Successful branch |
| `if (obj instanceof String s) {} else { use(s); }` | ✗ | Pattern failed in `else` |
| `if (!(obj instanceof String s)) return; use(s);` | ✓ | Failed path cannot reach `use(s)` |
| `if (!(obj instanceof String s)) {} use(s);` | ✗ | Failed path can reach `use(s)` |

## Type Compatibility

```text
Object obj = ...
obj instanceof String s
→ ✓ possible

String str = ...
str instanceof Integer i
→ ✗ impossible
```

## Generics

```text
obj instanceof List<String> list
→ ✗

obj instanceof List<?> list
→ ✓
```

## Flow-Scoping Method

For every use of a pattern variable:

```text
1. Find the pattern
       ↓
2. Determine when it succeeds
       ↓
3. Ask:
   Could the program reach this use
   when the pattern was false?
```

If:

```text
YES → variable unavailable
NO  → variable can be available
```

## Final Memory Kicks

> **`instanceof` pattern matching combines the type test and variable declaration.**

> **`null` never matches a type pattern.**

> **Flow scoping follows control flow, not simply braces.**

> **`&&` can carry a successful pattern forward; `||` cannot assume success.**

> **Early `return` or `throw` can make the pattern variable available afterward.**

> **Pattern matching does not create a new object.**

> **The tested types must have a possible relationship at compile time.**

> **`List<String>` cannot normally be tested with `instanceof`; `List<?>` can.**

> **When unsure: could this line be reached if the pattern failed?**