# Sealed Classes

[🔙 Back](README.md)

Quick reference for restricting inheritance with `sealed`, `permits`, `final`, `sealed` and `non-sealed`.

## Contents

- [The Basic Idea](#the-basic-idea)
- [Permitted Direct Subclasses](#permitted-direct-subclasses)
- [Omitting `permits`](#omitting-permits)
- [Direct Inheritance](#direct-inheritance)
- [Module and Package Rules](#module-and-package-rules)
- [Sealed Interfaces](#sealed-interfaces)
- [Quick Reference](#quick-reference)

---

## The Basic Idea

A `sealed` class or interface restricts which types may **directly** extend or implement it.

```java
sealed class Animal permits Dog, Cat {
}

final class Dog extends Animal {
}

non-sealed class Cat extends Animal {
}
```

Think of `permits` as the list of types allowed to be **direct children**.

```text
SEALED
→ inheritance is allowed
→ but the direct children are restricted
```

---

## Permitted Direct Subclasses

Every **direct permitted subclass** of a sealed class must choose one of three modifiers:

```text
final
sealed
non-sealed
```

For example:

```java
sealed class Animal permits Dog, Cat, Bird {
}

final class Dog extends Animal {
}

sealed class Cat extends Animal permits Lion {
}

non-sealed class Bird extends Animal {
}

final class Lion extends Cat {
}
```

The three choices mean:

```text
final
→ inheritance stops

sealed
→ inheritance continues
→ but remains restricted

non-sealed
→ sealed restriction ends
→ normal inheritance resumes
```

### Only Direct Children Have This Requirement

Consider:

```java
sealed class Animal permits Bird {
}

non-sealed class Bird extends Animal {
}

class Sparrow extends Bird {
}
```

`Bird` is a direct child of the sealed `Animal`, so it must be:

```text
final
sealed
or
non-sealed
```

`Sparrow` does not have that requirement because its direct parent, `Bird`, is `non-sealed`.

Memory:

> **The sealed parent controls its direct children, not every descendant.**

---

## Omitting `permits`

The `permits` clause can be omitted when all permitted direct subclasses are declared in the **same compilation unit (source file)** as the sealed type.

For example:

```java
sealed class Animal {
}

final class Dog extends Animal {
}

final class Cat extends Animal {
}
```

Java can infer the direct permitted subclasses.

Conceptually:

```java
sealed class Animal permits Dog, Cat {
}
```

### Different Source Files

If the permitted subclasses are in separate source files, specify them explicitly:

```java
// Animal.java
sealed class Animal permits Dog, Cat {
}
```

```java
// Dog.java
final class Dog extends Animal {
}
```

```java
// Cat.java
final class Cat extends Animal {
}
```

Memory:

> **Same source file determines whether `permits` may be omitted.**

This is separate from the module/package rules governing where permitted subclasses are allowed to exist.

---

## Direct Inheritance

A type listed in `permits` must actually **directly inherit** from the sealed type.

This is invalid:

```java
sealed class Animal permits Dog {
}

class Mammal {
}

final class Dog extends Mammal { // DOES NOT COMPILE
}
```

Listing:

```java
permits Dog
```

does not establish the inheritance relationship.

`Dog` must directly extend `Animal`:

```java
sealed class Animal permits Dog {
}

final class Dog extends Animal {
}
```

Think:

> **`permits` is an allowed direct-child list, not an inheritance declaration.**

---

## Module and Package Rules

Where permitted subclasses may be declared depends on whether the sealed type belongs to a **named module**.

### Named Module

If the sealed type belongs to a named module, every permitted direct subclass must belong to the **same named module**.

They do **not** have to be in the same package.

For example:

```text
module zoo

animals.Animal
dogs.Dog
cats.Cat
```

This can be valid:

```java
package animals;

public sealed class Animal
        permits dogs.Dog, cats.Cat {
}
```

provided all three classes belong to the same named module.

Think:

```text
NAMED MODULE
→ permitted children must be in same MODULE
→ different packages are allowed
```

### Unnamed Module

If the sealed type is not in a named module, its permitted direct subclasses must be in the **same package**.

This can be valid:

```text
animals.Animal
animals.Dog
animals.Cat
```

This cannot:

```text
animals.Animal
dogs.Dog
```

if the classes are in the unnamed module.

Think:

```text
UNNAMED MODULE
→ permitted children must be in same PACKAGE
```

### Automatic Modules

An automatic module is still a **named module**.

Therefore the named-module rule applies:

```text
automatic module
→ named module
→ permitted subclasses must be in same module
```

---

## Sealed Interfaces

Interfaces can also be sealed:

```java
sealed interface Shape permits Circle, Rectangle {
}

final class Circle implements Shape {
}

final class Rectangle implements Shape {
}
```

The same basic inheritance restriction applies:

```text
sealed interface
→ restricts its direct permitted subtypes
```

A permitted subinterface must also choose an appropriate modifier:

```java
sealed interface Shape permits Polygon {
}

non-sealed interface Polygon extends Shape {
}
```

For interfaces, the relevant choices are:

```text
sealed
non-sealed
```

An interface cannot be `final`.

A permitted class implementing a sealed interface still chooses from:

```text
final
sealed
non-sealed
```

as appropriate for a class.

---

# Quick Reference

## Sealed Hierarchy

```text
SEALED PARENT
     │
     ├── final
     │     → STOP
     │
     ├── sealed
     │     → RESTRICT AGAIN
     │
     └── non-sealed
           → OPEN AGAIN
```

Only the **direct children** of the sealed type must make this choice.

## Three Independent Rules

### Direct Child

```text
sealed class
     ↓
direct subclass
     ↓
final / sealed / non-sealed
```

For a direct subinterface:

```text
sealed / non-sealed
```

because interfaces cannot be `final`.

### `permits`

```text
same source file
→ permitted direct children can be inferred
→ permits may be omitted

different source files
→ permits must be explicit
```

### Location

```text
NAMED MODULE
→ same module
→ different packages allowed

UNNAMED MODULE
→ same package
```

An automatic module counts as a named module.

## Do Not Mix Up `permits` and Location

These answer different questions:

```text
SAME SOURCE FILE
→ Can Java infer the permits list?

SAME MODULE / PACKAGE
→ Is the permitted inheritance relationship legal?
```

This is the key distinction:

> **Same file determines whether `permits` can be omitted. Same module/package determines whether the sealed inheritance relationship is legal.**

## Final Memory Kicks

> **`sealed` restricts direct children, not every descendant.**

> **Direct subclasses must be `final`, `sealed`, or `non-sealed`.**

> **`final` → stop; `sealed` → restrict again; `non-sealed` → open again.**

> **A type named in `permits` must actually directly extend or implement the sealed type.**

> **`permits` may be omitted when the permitted direct children are declared in the same source file.**

> **Named module → permitted children must be in the same module.**

> **Unnamed module → permitted children must be in the same package.**

> **Automatic module → named module rules.**

> **Interfaces can be `sealed` or `non-sealed`, but not `final`.**

> **FILE controls `permits` inference; MODULE/PACKAGE controls legal location.**