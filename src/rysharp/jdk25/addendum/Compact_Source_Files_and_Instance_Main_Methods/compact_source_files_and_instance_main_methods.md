# Compact Source Files and Instance Main Methods (JEP 512)

Java 25 makes it possible to write very small Java programs without the usual class and `main` boilerplate.

JEP 512 has **two closely related parts**:

1. **Compact source files** — the class declaration can be omitted.
2. **Instance `main` methods** — `main` no longer has to be `static` or accept `String[]`.

The key idea is:

> **Java is still class-based. JEP 512 hides some of the boilerplate needed to write a small program.**

---

## 1. Traditional Java

A traditional Hello World program looks like:

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}
```

There are several pieces of boilerplate here:

* explicit class declaration
* `public`
* `static`
* `String[] args`

JEP 512 allows these to be simplified.

---

# Compact Source Files

## 2. The simplest compact source file

You can write:

```java
void main() {
    System.out.println("Hello");
}
```

There is:

* no explicit class declaration
* no `public`
* no `static`
* no `String[] args`

Yet this is still a Java class-based program.

---

## 3. The implicit class

Java effectively creates an **implicitly declared class** around the declarations in the source file.

For example:

```java
String greeting = "Hello";

void main() {
    System.out.println(greeting);
}
```

Conceptually, think of it as something like:

```java
class SomeImplicitClass {
    String greeting = "Hello";

    void main() {
        System.out.println(greeting);
    }
}
```

This is a **mental model**, not literally the source code you write.

The important consequence is that the things you write at the top level are members of this implicit class.

---

## 4. What can appear at the top level?

You can declare members such as:

### Fields

```java
String name = "Bob";
int count = 10;
```

### Methods

```java
void greet() {
    System.out.println("Hello");
}
```

### Member classes/interfaces

These are also class members.

The implicit class's body contains the members declared in the compact source file.

---

## 5. It is NOT a scripting language

This is a major exam distinction.

This is **not** valid merely because the file is compact:

```java
System.out.println("Hello");
```

Why?

Because that is an executable **statement**, not a class member declaration.

Put the statement inside a method:

```java
void main() {
    System.out.println("Hello");
}
```

### Mental model

> **Compact source file = hidden class, not Java script.**

You don't get arbitrary executable statements at the top level.

---

## 6. A bare `{ ... }` block doesn't solve this

You might wonder whether this works:

```java
{
    System.out.println("Hello");
}
```

It doesn't.

A compact source file isn't simply an ordinary class body where you can place an instance initializer block.

The implicitly declared class has specific restrictions: its body contains member declarations such as fields, methods, member classes, and member interfaces. It does **not** contain instance or static initializer declarations or constructors.

So:

```java
void main() {
    System.out.println("Hello");
}
```

is the correct way to execute the statement.

---

# Instance `main` Methods

## 7. `main` no longer needs to be `static`

Traditionally:

```java
public static void main(String[] args)
```

With JEP 512:

```java
void main() {
    System.out.println("Hello");
}
```

This is an **instance `main` method**.

It is an ordinary instance method, so it can directly access instance members:

```java
String message = "Hello";

void main() {
    System.out.println(message);
}
```

`message` belongs to the object on which `main()` is invoked.

---

## 8. How does Java invoke an instance `main`?

Conceptually:

```java
SomeClass object = new SomeClass();
object.main();
```

The launcher creates an instance of the class and then invokes the instance `main`.

This is why an instance `main` can use:

```java
this
```

and access instance fields and methods.

---

## 9. The constructor requirement

Because Java has to create an object before calling an instance `main`, the class must be instantiable by the launcher.

For a normal class, this means there must be an accessible **no-argument constructor**.

For example:

```java
class Test {
    Test() {
    }

    void main() {
        System.out.println("Hello");
    }
}
```

works.

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

doesn't provide the required no-argument constructor.

The launcher cannot create the object needed to invoke the instance `main`, so launching fails.

A compact source file has an implicitly declared default constructor, which is one reason the simple:

```java
void main() {
    System.out.println("Hello");
}
```

form works naturally.

---

# `main` Signatures

## 10. `String[]` is optional

An instance `main` can still receive command-line arguments:

```java
void main(String[] args) {
    System.out.println(args[0]);
}
```

But if you don't need the arguments:

```java
void main() {
    System.out.println("Hello");
}
```

is sufficient.

`String... args` is also equivalent to `String[] args` because both represent a `String` array parameter.

---

## 11. `main` can be static or instance

Java 25 allows candidate `main` methods to be either:

```java
static void main(String[] args)
```

or:

```java
void main(String[] args)
```

and also:

```java
static void main()
```

or:

```java
void main()
```

The access can be `public`, `protected`, or package-private; `public` is no longer mandatory.

---

# How the Launcher Chooses `main`

## 12. Parameterized `main` takes priority

The Java 25 launch protocol first looks for a candidate `main` with a `String[]` parameter.

If one exists, that is the method selected.

For example:

```java
void main(String[] args) {
    System.out.println("with args");
}

void main() {
    System.out.println("without args");
}
```

The `String[]` version is selected.

If there is no `String[]` version, Java can select the no-argument version.

The selected method may be either static or instance.

### Mental model

Think:

```text
main(String[])  → preferred
main()          → fallback
```

Then:

```text
static main     → invoke directly
instance main   → create object, then invoke
```

---

## 13. You cannot overload static and instance `main` with the exact same signature

For example:

```java
static void main() { }

void main() { }
```

is not a valid pair of overloads.

Static vs instance does **not** form part of a method signature.

So these have the same signature:

```java
static void main()
void main()
```

and cannot coexist in the same class.

---

# Compact Source Files + Instance `main`

## 14. The two features work together

This is the simplest JEP 512 program:

```java
void main() {
    System.out.println("Hello");
}
```

It combines both features:

### Compact source file

There is no explicit class:

```java
class Hello {
    ...
}
```

### Instance `main`

There is no `static`:

```java
void main()
```

Java provides the implicit class and creates an instance to invoke `main()`.

---

## 15. Instance fields work naturally

For example:

```java
String message = "Hello";

void main() {
    System.out.println(message);
}
```

Conceptually:

```java
Hello object = new Hello();
object.main();
```

Inside `main()`, `message` can therefore be accessed as an instance member.

---

# What You Cannot Do

## 16. You cannot reference the implicit class by name

A compact source file has an implicitly declared class, but you don't get to name it yourself.

For example, you can't write something like:

```java
SomeImplicitClass object = new SomeImplicitClass();
```

because there is no source-level name for the implicit class.

The class exists for the purposes of the program and launcher, but it isn't something you normally manipulate by name.

---

## 17. You cannot declare constructors in a compact source file

Remember that the implicit class has its own implicitly declared default constructor.

You cannot write:

```java
Hello() {
}
```

in a compact source file.

Constructors belong to explicitly declared classes; a compact source file cannot contain a constructor declaration.

Similarly, compact source files cannot contain instance or static initializer declarations.

---

# Exam Traps

### Trap 1 — "No class means scripting"

❌ Wrong.

Compact source files still represent a class.

> **Hidden class, not script.**

---

### Trap 2 — "No `static` means `main` can't launch"

❌ Wrong.

Java 25 supports instance `main` methods.

---

### Trap 3 — "Instance `main` means `main` gets a `this` automatically without an object"

❌ Wrong.

The launcher first creates an instance, then invokes `main()` on that instance.

---

### Trap 4 — "The `String[]` parameter is mandatory"

❌ Wrong.

Both are valid:

```java
void main()
```

and:

```java
void main(String[] args)
```

---

### Trap 5 — "A compact file allows any statement at the top level"

❌ Wrong.

This is still invalid:

```java
System.out.println("Hello");
```

Executable statements belong inside an appropriate executable context, such as `main()`.

---

### Trap 6 — "Static vs instance creates different overloads"

❌ Wrong.

These have the same signature:

```java
static void main()
void main()
```

so they cannot coexist in the same class.

---

# Quick Comparison

| Feature                        | Traditional Java  | JEP 512                    |
| ------------------------------ | ----------------- | -------------------------- |
| Explicit class                 | Usually required  | Can be omitted             |
| `main` must be `static`        | Traditionally yes | No                         |
| `main` must have `String[]`    | Traditionally yes | No                         |
| `public` required for `main`   | Traditionally yes | No                         |
| Instance `main`                | ❌                 | ✅                          |
| Top-level arbitrary statements | ❌                 | ❌                          |
| Still class-based              | ✅                 | ✅                          |
| Implicit class                 | ❌                 | ✅ for compact source files |

---

# Exam Checklist

When you see a JEP 512 question, check:

1. **Is this a compact source file?**

    * No explicit class declaration.
    * Top-level content must fit the implicit class/member rules.

2. **Is there a valid `main`?**

    * `void main()`
    * or `void main(String[] args)` / `String... args`
    * static or instance
    * `public`, `protected`, or package access

3. **If `main` is instance-based:**

    * Java must create an object first.
    * A suitable no-argument constructor must therefore be available.

4. **If multiple candidate `main` methods exist:**

    * `main(String[])` takes priority over `main()`.
    * Then static vs instance determines whether Java invokes directly or creates an object.

5. **Watch for the scripting-language trap:**

    * No class declaration ≠ no class.
    * No `static` ≠ no object.
    * Compact ≠ arbitrary top-level statements.

---

# 18. Runnable Example

You can find a runnable example here:
`src\rysharp\jdk25\addendum\Compact_Source_Files_and_Instance_Main_Methods\code\CompactSourceExample.java`

---

## One-line memory aid

> **JEP 512 = hidden class + simpler `main`; Java is still Java, not a script.**
