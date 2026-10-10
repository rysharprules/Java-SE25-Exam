# Compact Source Files and Instance Main Methods (JEP 512)

[🔙 Back](../../../../README.md)

Quick reference for compact source files, instance `main` methods and the Java 25 launch protocol.

## Contents

- [The Two Features](#the-two-features)
- [Compact Source Files](#compact-source-files)
- [Instance Main Methods](#instance-main-methods)
- [Main Signatures](#main-signatures)
- [Launcher Selection](#launcher-selection)
- [Compact Source File Restrictions](#compact-source-file-restrictions)
- [Quick Reference](#quick-reference)

---

## The Two Features

Java 25 allows small Java programs to omit much of the traditional boilerplate.

JEP 512 has two closely related features:

```text
COMPACT SOURCE FILE
→ class declaration can be omitted

INSTANCE main
→ main does not have to be static
```

Traditional Java:

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}
```

A compact Java 25 program:

```java
void main() {
    System.out.println("Hello");
}
```

This removes:

```text
explicit class declaration
public
static
String[] args
```

when they aren't needed.

But the key rule is:

> **Java is still class-based. The boilerplate is hidden, not the class model.**

---

## Compact Source Files

A compact source file omits the explicit class declaration.

```java
void main() {
    System.out.println("Hello");
}
```

Java provides an **implicitly declared class** around the declarations in the source file.

For example:

```java
String greeting = "Hello";

void main() {
    System.out.println(greeting);
}
```

A useful mental model is:

```java
class SomeImplicitClass {

    String greeting = "Hello";

    void main() {
        System.out.println(greeting);
    }
}
```

This is only a mental model — you do not actually declare or name that class.

### Top-Level Declarations Are Members

Declarations in a compact source file become members of the implicit class.

For example, fields:

```java
String name = "Bob";
int count = 10;
```

methods:

```java
void greet() {
    System.out.println("Hello");
}
```

and member classes/interfaces can be declared.

Think:

```text
compact source file
        ↓
implicitly declared class
        ↓
top-level declarations become members
```

### Not a Scripting Language

A compact source file does **not** allow arbitrary executable statements at the top level.

This is invalid:

```java
System.out.println("Hello");   // DOES NOT COMPILE
```

The statement must appear in an executable context such as a method:

```java
void main() {
    System.out.println("Hello");
}
```

Memory:

> **Hidden class, not script.**

---

## Instance Main Methods

Java 25 allows `main` to be an instance method.

Instead of:

```java
public static void main(String[] args)
```

you can write:

```java
void main() {
    System.out.println("Hello");
}
```

Because this `main()` is an instance method, it can directly access instance members:

```java
String message = "Hello";

void main() {
    System.out.println(message);
}
```

It can also use:

```java
this
```

like an ordinary instance method.

### How Instance `main` Is Invoked

Conceptually, the launcher does something like:

```java
SomeClass object = new SomeClass();
object.main();
```

So:

```text
instance main
      ↓
object must exist
      ↓
launcher creates object
      ↓
launcher invokes main on object
```

This explains why an instance `main` has access to instance state.

### Constructor Requirement

For an explicitly declared normal class with an instance `main`, the launcher must be able to instantiate the class using a suitable no-argument constructor.

This works:

```java
class Test {

    Test() {
    }

    void main() {
        System.out.println("Hello");
    }
}
```

But:

```java
class Test {

    Test(String name) {
    }

    void main() {
        System.out.println("Hello");
    }
}
```

does not provide the no-argument construction required for launching that instance `main`.

A compact source file has an implicitly declared default constructor, which is why:

```java
void main() {
    System.out.println("Hello");
}
```

works naturally.

Memory:

> **Instance `main` → launcher needs an instance first.**

---

## Main Signatures

A candidate `main` can be either **static or instance**.

It can also be either **parameterized or no-argument**.

Valid forms include:

```java
static void main(String[] args)
```

```java
void main(String[] args)
```

```java
static void main()
```

```java
void main()
```

`String... args` is equivalent to `String[] args` for this purpose:

```java
void main(String... args)
```

### Access

`main` does not have to be `public`.

A candidate `main` can use:

```text
public
protected
package-private
```

The important change is:

```text
public → no longer mandatory
static → no longer mandatory
String[] → no longer mandatory
```

### Static vs Instance Is Not Part of the Signature

You cannot declare both:

```java
static void main() { }

void main() { }
```

in the same class.

These have the same method signature.

Whether a method is `static` or instance does **not** distinguish overloads.

Memory:

> **`static` is not part of a method signature.**

---

## Launcher Selection

When multiple candidate `main` methods are available, the launcher prefers the version with a `String[]` parameter.

For example:

```java
void main(String[] args) {
    System.out.println("with args");
}

void main() {
    System.out.println("without args");
}
```

The launcher selects:

```java
main(String[])
```

Think:

```text
main(String[])
      ↓ preferred

main()
      ↓ fallback
```

Once the candidate is selected:

```text
static main
→ invoke directly

instance main
→ create object
→ invoke main
```

Memory:

> **Parameterized `main(String[])` takes priority over `main()`.**

---

## Compact Source File Restrictions

The implicit class is not simply an ordinary class declaration with its name removed.

It has specific restrictions.

### No Package Declaration

A compact source file cannot contain a package declaration.

Do not write:

```java
package com.example;   // NOT ALLOWED
```

in a compact source file.

If you need a package, use an explicitly declared class and a normal source file.

### No Constructors

You cannot explicitly declare a constructor for the implicit class.

For example:

```java
Hello() {
}
```

is not valid as a constructor declaration for a compact source file.

The implicit class receives its own implicitly declared default constructor.

### No Initializer Blocks

You cannot declare instance initializer blocks:

```java
{
    System.out.println("Hello");
}
```

or static initializer blocks for the implicit class.

Executable initialization should instead occur through appropriate field initializers or methods.

### Cannot Name the Implicit Class

You cannot write:

```java
SomeImplicitClass object =
        new SomeImplicitClass();
```

because the implicitly declared class has no source-level name for you to use this way.

Think:

```text
implicit class exists
        ≠
implicit class has a usable source name
```

### Top-Level Statements Still Invalid

This remains invalid:

```java
System.out.println("Hello");
```

Use:

```java
void main() {
    System.out.println("Hello");
}
```

The restrictions reinforce the main mental model:

> **Compact source file = restricted implicit class, not a script and not simply an ordinary class body with the class declaration deleted.**

---

# Quick Reference

## Traditional vs Java 25

| Feature | Traditional Form | Java 25 |
|---|---|---|
| Explicit class | Normally present | Can be omitted |
| `main` static | Yes | Not required |
| `String[]` parameter | Yes | Not required |
| `public` main | Yes | Not required |
| Instance `main` | ✗ | ✓ |
| Arbitrary top-level statements | ✗ | ✗ |
| Class-based | ✓ | ✓ |
| Implicit class | ✗ | ✓ for compact source files |

## Compact Source File

```text
no explicit class declaration
        ↓
implicitly declared class
        ↓
top-level declarations are members
```

Allowed kinds of declarations include:

```text
fields
methods
member classes
member interfaces
```

But not:

```text
package declaration
constructors
instance initializer blocks
static initializer blocks
arbitrary executable statements
```

## Candidate `main` Forms

```java
static void main(String[] args)
void main(String[] args)

static void main()
void main()
```

Also:

```java
String... args
```

can be used instead of:

```java
String[] args
```

## Launcher Selection

```text
main(String[])
      ↓
preferred over
      ↓
main()
```

Then:

```text
STATIC
→ invoke directly

INSTANCE
→ create object
→ invoke main
```

## Instance `main`

```text
instance main
→ real instance method
→ can access instance members
→ can use this
→ launcher needs to create object
```

For an explicitly declared class, watch for a suitable no-argument constructor.

## Method Signature

```java
static void main() { }
void main() { }
```

cannot coexist merely because one is static.

Memory:

```text
static vs instance
≠ overload distinction
```

## Restrictions

```text
COMPACT SOURCE FILE

package declaration       ✗
explicit constructor      ✗
instance initializer      ✗
static initializer        ✗
arbitrary top-level code  ✗
```

## Reliable Check

When you see compact-source or `main` code:

```text
1. Is there an explicit class?

   NO
   → compact source file rules apply


2. Is top-level content a permitted declaration?

   arbitrary statement
   → invalid


3. Is there a candidate main?

   main(String[])
   or
   main()


4. Is it static or instance?

   static
   → invoke directly

   instance
   → launcher needs an object


5. Are multiple candidate mains present?

   main(String[])
   → preferred over main()


6. For a compact source file, check restrictions:

   no package declaration
   no explicit constructor
   no initializer declarations
```

## Final Memory Kicks

> **JEP 512 has two related features: compact source files and instance `main` methods.**

> **A compact source file still represents a class — it is not a script.**

> **Top-level declarations become members of an implicitly declared class; arbitrary top-level statements are still invalid.**

> **Compact source files cannot declare a package, constructor, or instance/static initializer.**

> **`main` can be static or instance and can take `String[]`/`String...` or no arguments.**

> **An instance `main` is invoked on an object, so it can access instance members and `this`.**

> **`main(String[])` is preferred over `main()` when the launcher has multiple candidates.**

> **`static` vs instance does not create a different method signature.**

> **Compact source file = hidden boilerplate, not different Java.**