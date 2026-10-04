# Flexible Constructor Bodies (JEP 513)

Quick reference for Java 25 constructor prologues, early construction context and `this(...)` / `super(...)`.

## Contents

- [The Basic Idea](#the-basic-idea)
- [Constructor Prologue and Epilogue](#constructor-prologue-and-epilogue)
- [What the Prologue Can Access](#what-the-prologue-can-access)
- [`super(...)` with a Prologue](#super-with-a-prologue)
- [`this(...)` with a Prologue](#this-with-a-prologue)
- [Implicit `super()`](#implicit-super)
- [Constructor Chaining](#constructor-chaining)
- [Records](#records)
- [Quick Reference](#quick-reference)

---

## The Basic Idea

Before Java 25, an explicit:

```java
super(...);
```

or:

```java
this(...);
```

had to be the first statement in a constructor.

For example:

```java
class Person extends Human {

    Person(String name) {
        super(name);
        System.out.println("Created");
    }
}
```

Java 25 allows statements before the explicit constructor invocation:

```java
class Person extends Human {

    Person(String name) {
        String cleaned = name.trim();

        super(cleaned);

        System.out.println("Created");
    }
}
```

This allows preparation, calculation and validation before invoking another constructor.

Memory:

> **Java 25 allows a constructor prologue before an explicit `this(...)` or `super(...)`.**

---

## Constructor Prologue and Epilogue

A constructor containing an explicit constructor invocation can be viewed as:

```text
PROLOGUE
    ↓
this(...) / super(...)
    ↓
EPILOGUE
```

For example:

```java
Person(String name) {

    // PROLOGUE
    String cleaned = name.trim();

    super(cleaned);

    // EPILOGUE
    this.name = cleaned;
    printDetails();
}
```

Everything before:

```java
super(cleaned);
```

is the **prologue**.

Everything after it is the **epilogue**.

The prologue executes in an **early construction context**.

The key restriction is:

> **The prologue cannot reference the object currently being constructed.**

Once the explicit constructor invocation has completed, the epilogue can use the current instance normally.

---

## What the Prologue Can Access

The important question is not:

```text
Is this an instance method?
```

Instead ask:

```text
Does this operation require the object
that is currently being constructed?
```

If it does, it cannot be used there.

### Parameters and Local Variables

Constructor parameters are available:

```java
Person(String name) {
    String cleaned = name.trim();

    super(cleaned);
}
```

Local variables are also fine:

```java
Person(String name) {
    String cleaned = name.trim();
    int length = cleaned.length();

    super(cleaned);
}
```

These do not require access to the `Person` currently being constructed.

### Calculations and Validation

The prologue can perform calculations and validation:

```java
Employee(String name) {
    String cleaned = name.trim();

    if (cleaned.isEmpty()) {
        throw new IllegalArgumentException();
    }

    super(cleaned);
}
```

It can also throw exceptions before superclass construction occurs.

This is one of the main motivations for flexible constructor bodies:

```text
receive argument
      ↓
prepare / validate
      ↓
reject if invalid
      ↓
invoke superclass constructor
```

### Static Members

Static methods and fields do not require the current object.

For example:

```java
Person(String name) {
    String cleaned = clean(name);

    super(cleaned);
}

static String clean(String name) {
    return name.trim();
}
```

This is valid because:

```java
clean(name)
```

does not require `this`.

### Other Objects

The prologue can work with other objects:

```java
Person(String name) {
    String cleaned = new String(name);

    super(cleaned);
}
```

It can also call instance methods on those objects:

```java
Person(String name) {
    String cleaned = name.trim();

    super(cleaned);
}
```

`trim()` is an instance method, but it operates on the `String` referenced by `name`.

It does **not** operate on the `Person` being constructed.

### `System.out.println()`

This is therefore legal:

```java
Person(String name) {
    System.out.println(name);

    super(name);
}
```

Although `println()` is an instance method, it is invoked on:

```java
System.out
```

which refers to a `PrintStream` object.

Conceptually:

```text
PrintStream object
      ↓
println(...)
```

not:

```text
Person being constructed
      ↓
println(...)
```

Memory:

> **Instance methods are not generally forbidden — instance access to the object under construction is forbidden.**

### Current Object

The current object cannot be accessed from the prologue.

This is invalid:

```java
Person(String name) {
    System.out.println(this.name); // DOES NOT COMPILE

    super(name);
}
```

So is an unqualified call to one of the current object's instance methods:

```java
Person(String name) {
    name = cleanName(); // DOES NOT COMPILE

    super(name);
}
```

because:

```java
cleanName()
```

implicitly means a call using the current object.

Explicit `this` is likewise invalid:

```java
Person(String name) {
    this.validate(name); // DOES NOT COMPILE

    super(name);
}
```

Think:

```text
parameter                   ✓
local variable              ✓
calculation                 ✓
validation                  ✓
static member               ✓
another object              ✓
method on another object    ✓

this                        ✗
current instance field      ✗
current instance method     ✗
```

---

## `super(...)` with a Prologue

A common use of flexible constructor bodies is preparing an argument before superclass construction.

```java
class Person {

    Person(String name) {
        System.out.println(name);
    }
}

class Employee extends Person {

    Employee(String name) {
        String cleaned = name.trim();

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException();
        }

        super(cleaned);

        System.out.println("Employee created");
    }
}
```

Conceptually:

```text
Employee constructor starts
        ↓
clean name
        ↓
validate name
        ↓
super(cleaned)
        ↓
Person constructor
        ↓
Employee epilogue
```

The significant Java 25 change is that validation can happen:

```text
BEFORE superclass construction
```

rather than requiring `super(...)` to be the first statement.

---

## `this(...)` with a Prologue

Flexible constructor bodies also work with `this(...)`.

```java
class Person {

    Person(String name, int age) {
        System.out.println(name + " " + age);
    }

    Person(String name) {
        String cleaned = name.trim();

        this(cleaned, 0);

        System.out.println("Finished");
    }
}
```

Execution is conceptually:

```text
Person(String)
      ↓
prologue
      ↓
this(cleaned, 0)
      ↓
Person(String, int)
      ↓
returns
      ↓
epilogue
```

So both forms allow a prologue:

```text
              Java 25
                 │
        ┌────────┴────────┐
        ↓                 ↓
    super(...)         this(...)
        ↑                 ↑
  prologue allowed   prologue allowed
```

Memory:

```text
super(...)
→ constructor in SUPERCLASS

this(...)
→ constructor in SAME class
```

---

## Implicit `super()`

An important distinction occurs when there is **no explicit** `this(...)` or `super(...)`.

Consider:

```java
class Person {

    Person(String name) {
        System.out.println(name);
    }
}
```

Java still performs the normal implicit superclass constructor invocation.

But do not think:

```text
System.out.println(name)
→ constructor prologue
```

There is no explicit constructor invocation for that statement to precede.

The compiler supplies the implicit `super()` according to the normal constructor rules.

Memory:

> **No explicit `this(...)` / `super(...)` → don't classify the written statements as a prologue.**

Flexible constructor bodies are primarily about code of the form:

```java
// prologue
...
super(...);

// epilogue
...
```

or:

```java
// prologue
...
this(...);

// epilogue
...
```

---

## Constructor Chaining

Flexible constructor bodies do not remove the normal constructor-chaining rules.

This is valid:

```java
class Person {

    Person(String name) {
    }

    Person(String name, int age) {
        this(name);
    }
}
```

### Constructor Cycles

A constructor cannot invoke itself recursively:

```java
Person(String name) {
    this(name); // DOES NOT COMPILE
}
```

Nor can constructors form an indirect cycle.

For example, conceptually:

```text
Constructor A
     ↓
Constructor B
     ↓
Constructor A
```

Java detects constructor invocation cycles at **compile time**.

This is not a runtime:

```text
StackOverflowError
```

Memory:

> **Constructor cycles are compile-time errors.**

For the wider execution order across inheritance, see **Class Initialization & Constructor Execution**.

---

## Records

Flexible constructor bodies also apply to **non-canonical record constructors**.

For example:

```java
record Person(String name, int age) {

    Person(String name) {
        String cleaned = name.trim();

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this(cleaned, 0);
    }
}
```

This constructor is non-canonical because its parameter list does not match the record components.

It ultimately delegates using:

```java
this(...)
```

Java 25 allows preparation and validation before that invocation.

A record's canonical constructor is a different case and does not use an explicit `this(...)` or `super(...)` invocation in the same way.

---

# Quick Reference

## Constructor Structure

```text
PROLOGUE
before explicit this(...) / super(...)

        ↓

EXPLICIT CONSTRUCTOR INVOCATION
this(...) / super(...)

        ↓

EPILOGUE
normal constructor code
```

## Prologue Access

```text
ALLOWED
───────
constructor parameters
local variables
calculations
validation
throwing exceptions
static fields
static methods
other objects
instance methods on other objects


NOT ALLOWED
───────────
this
current object's instance fields
current object's instance methods
implicit use of current object
```

## Important Distinction

This can be valid:

```java
name.trim();
System.out.println(name);
```

because the methods operate on other objects.

This is not valid in the prologue:

```java
this.validate();
validate();
this.name;
```

because these require the object currently being constructed.

Memory:

```text
instance method
≠ automatically illegal

method on CURRENT object
= illegal in prologue
```

## `this(...)` vs `super(...)`

```text
this(...)
→ another constructor
→ SAME class

super(...)
→ constructor
→ SUPERCLASS
```

Both can have a Java 25 prologue before them.

## Explicit vs Implicit Invocation

```text
statements
super(...);
```

or:

```text
statements
this(...);
```

→ statements before invocation form the prologue.

But:

```java
Person(String name) {
    System.out.println(name);
}
```

→ do not treat the written statement as a prologue merely because an implicit `super()` exists.

## Constructor Cycles

```text
A → B → A
```

or:

```text
A → A
```

→ compile-time error.

## Reliable Check

When code appears before an explicit `this(...)` or `super(...)`:

```text
1. Locate the explicit constructor invocation

2. Everything before it is the prologue

3. For each prologue statement ask:

   "Does this require the object
    currently being constructed?"

4. NO
   → potentially valid

5. YES
   → invalid

6. After this(...) / super(...) completes,
   normal instance access is available
```

## Final Memory Kicks

> **Java 25 allows statements before an explicit `this(...)` or `super(...)`.**

> **Those statements form the constructor prologue and execute in an early construction context.**

> **The prologue cannot access the object currently being constructed.**

> **Parameters, locals, static members and other objects can be used.**

> **An instance method call is not automatically illegal — calling it on the current object is the problem.**

> **`System.out.println()` is valid because `println()` operates on the `PrintStream` referenced by `System.out`.**

> **The prologue ends at the explicit `this(...)` or `super(...)`.**

> **Without an explicit constructor invocation, don't classify the written constructor statements as a prologue.**

> **Constructor invocation cycles remain compile-time errors.**

> **Prepare first → invoke `this()` / `super()` → then use the constructed instance normally.**