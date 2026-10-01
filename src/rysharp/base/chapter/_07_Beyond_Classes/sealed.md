# Sealing Classes

## Sealed Classes — Quick Reference

A `sealed` class/interface restricts which types may **directly** extend/implement it.

```java id="b65i2u"
sealed class Animal permits Dog, Cat {}

final class Dog extends Animal {}
non-sealed class Cat extends Animal {}
```

---

### 1. Direct subclasses must choose one of three modifiers

Every **direct** permitted subclass must be:

```java id="mlklbw"
final
sealed
non-sealed
```

Example:

```java id="lvt2n1"
sealed class Animal permits Dog, Cat, Bird {}

final class Dog extends Animal {}

sealed class Cat extends Animal permits Lion {}

non-sealed class Bird extends Animal {}

final class Lion extends Cat {}
```

Meaning:

```text id="cqim94"
final
→ inheritance stops

sealed
→ inheritance continues, but remains restricted

non-sealed
→ restriction ends; normal inheritance resumes
```

Only the **direct** children of a sealed type have this requirement.

For example:

```java id="cfb5bh"
sealed class Animal permits Bird {}

non-sealed class Bird extends Animal {}

class Sparrow extends Bird {} // ✓ normal class
```

`Sparrow` does not need `final`, `sealed`, or `non-sealed` because its direct parent is no longer sealed.

---

# 2. When can `permits` be omitted?

If all direct subclasses are declared in the **same compilation unit (source file)** as the sealed class, Java can infer them.

```java id="whjbf5"
sealed class Animal {}

final class Dog extends Animal {}
final class Cat extends Animal {}
```

This is valid.

Java effectively infers:

```java id="90h85f"
sealed class Animal permits Dog, Cat {}
```

### Different source files?

Then specify `permits`:

```java id="u3vxbw"
// Animal.java
sealed class Animal permits Dog, Cat {}
```

```java id="lnkl89"
// Dog.java
final class Dog extends Animal {}
```

```java id="yrrl4f"
// Cat.java
final class Cat extends Animal {}
```

### Exam rule

> **`permits` may be omitted when the permitted direct subclasses are declared in the same source file.**

---

# 3. Permitted subclasses must directly inherit from the sealed type

This is invalid:

```java id="n0bbmp"
sealed class Animal permits Dog {}

class Mammal {}

final class Dog extends Mammal {} // ✗
```

Listing `Dog` in `permits` does not itself establish inheritance.

`Dog` must actually directly extend `Animal`:

```java id="ez35yv"
sealed class Animal permits Dog {}

final class Dog extends Animal {} // ✓
```

Think of `permits` as an **allowed direct-child list**, not an inheritance declaration.

---

# 4. Named Modules: Same Module

If the sealed type belongs to a **named module**, its permitted direct subclasses must belong to the **same named module**.

They do **not** have to be in the same package.

For example:

```text id="8buw89"
module zoo
│
├── package animals
│      Animal
│
├── package dogs
│      Dog
│
└── package cats
       Cat
```

This can be valid:

```java id="q3hdzb"
// package animals
public sealed class Animal
        permits dogs.Dog, cats.Cat {}
```

provided all those classes belong to the same named module.

Mental model:

> **Named module → permitted subclasses must stay inside the module.**

---

# 5. Unnamed Module: Same Package

If the sealed class is **not in a named module**, its permitted direct subclasses must be in the **same package**.

For example:

```text id="2e2v90"
package animals

Animal
Dog
Cat
```

✓ possible.

But:

```text id="eylcvz"
animals.Animal
dogs.Dog
```

✗ `Dog` cannot be a permitted subclass of `Animal` when they're in the unnamed module and different packages.

Mental model:

```text id="n62jke"
Named module
    → same MODULE
    → different packages allowed

Unnamed module
    → same PACKAGE
```

---

# 6. `permits` and location are separate rules

Don't confuse these two exam questions.

### Can Java infer the permitted subclasses?

This concerns the **source file**:

> Same compilation unit → `permits` can be omitted.

---

# Exam Cheat Sheet

```text id="aizkaf"
SEALED PARENT
     │
     ├── final       → STOP
     │
     ├── sealed      → RESTRICT AGAIN
     │
     └── non-sealed  → OPEN AGAIN
```

And remember these three independent rules:

```text id="isvfrd"
1. DIRECT CHILD
   must be final, sealed or non-sealed

2. PERMITS
   may be omitted when direct children are
   declared in the same source file

3. LOCATION
   named module   → same module
   unnamed module → same package
```

The particularly useful exam distinction is:

> **Same file determines whether `permits` can be omitted. Same module/package determines whether the inheritance relationship is legal.**

This restriction applies specifically to sealed inheritance. Ordinary Java inheritance can cross package boundaries subject to the normal accessibility rules.