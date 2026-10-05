# Records

Quick reference for Java records, their generated members, constructors, fields and inheritance rules.

## Contents

- [The Basic Idea](#the-basic-idea)
- [Record Components](#record-components)
- [Generated Members](#generated-members)
- [Fields](#fields)
- [Canonical Constructors](#canonical-constructors)
- [Compact Constructors](#compact-constructors)
- [Non-Canonical Constructors](#non-canonical-constructors)
- [Methods](#methods)
- [Inheritance](#inheritance)
- [Interfaces](#interfaces)
- [Quick Reference](#quick-reference)

---

## The Basic Idea

A record is a compact way to declare a class whose primary purpose is to hold data.

```java
record Person(String name, int age) {
}
```

The record header:

```java
(String name, int age)
```

declares the **record components**.

Java generates much of the normal data-class machinery automatically.

Think:

```text
record header
→ state description
→ private final fields
→ public accessors
→ canonical constructor
→ equals()
→ hashCode()
→ toString()
```

A record is still a class.

It can contain constructors, methods, static fields and static methods, subject to record-specific rules.

---

## Record Components

Given:

```java
record Person(String name, int age) {
}
```

the components are:

```text
name : String
age  : int
```

Their order matters.

The canonical constructor therefore has the parameter order:

```java
Person(String name, int age)
```

and construction looks like:

```java
Person person = new Person("Alice", 30);
```

### Components Are Not Constructor Parameters Only

Each component contributes to the record's state.

Conceptually:

```java
record Person(String name, int age) {
}
```

results in corresponding instance fields and accessors.

---

## Generated Members

For:

```java
record Person(String name, int age) {
}
```

Java provides:

```text
private final String name
private final int age

public String name()
public int age()

canonical constructor

equals()
hashCode()
toString()
```

### Accessors

Record accessors are named directly after the components:

```java
person.name();
person.age();
```

not:

```java
person.getName();
person.getAge();
```

unless you explicitly add such methods yourself.

Memory:

> **Record accessor = component name + `()`**

---

## Fields

The instance fields corresponding to record components are implicitly:

```text
private final
```

Therefore the record's component state cannot subsequently be reassigned.

For example, you cannot do this inside an ordinary method:

```java
record Person(String name) {

    void change() {
        this.name = "Bob"; // DOES NOT COMPILE
    }
}
```

### Additional Instance Fields

A record cannot declare additional instance fields.

This does not compile:

```java
record Person(String name) {

    int age; // DOES NOT COMPILE
}
```

The instance state of a record is defined by its record components.

If `age` is part of the record's state, it belongs in the header:

```java
record Person(String name, int age) {
}
```

### Static Fields

Records **can** declare static fields:

```java
record Person(String name) {

    static int count;
}
```

An additional static field is **not implicitly `final`**.

Therefore:

```java
static int count;
```

receives the normal default value:

```text
0
```

and may be modified:

```java
record Person(String name) {

    static int count;

    static void increment() {
        count++;
    }
}
```

If you want a constant, declare it explicitly:

```java
static final int MAX = 100;
```

Memory:

> **Record component fields are final; additional static fields are ordinary static fields.**

---

## Canonical Constructors

The **canonical constructor** has parameters corresponding to every record component, in the same order and with the same types.

For:

```java
record Person(String name, int age) {
}
```

the canonical constructor corresponds to:

```java
Person(String name, int age)
```

You can explicitly declare it:

```java
record Person(String name, int age) {

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

The constructor can perform validation:

```java
record Person(String name, int age) {

    public Person(String name, int age) {
        if (age < 0) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.age = age;
    }
}
```

### Canonical Constructor Access

An explicitly declared canonical constructor cannot have more restrictive access than the record itself.

For example:

```java
public record Person(String name) {

    private Person(String name) { // DOES NOT COMPILE
        this.name = name;
    }
}
```

The canonical constructor cannot hide construction more than the record declaration permits.

---

## Compact Constructors

Records provide special **compact constructor** syntax.

Instead of:

```java
record Person(String name, int age) {

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

you can write:

```java
record Person(String name, int age) {

    public Person {
        if (age < 0) {
            throw new IllegalArgumentException();
        }
    }
}
```

Notice what is missing:

```text
no parameter list
```

The parameters corresponding to the record components are automatically available:

```java
name
age
```

and Java performs the component-field assignments automatically.

Think:

```text
compact constructor
→ validate / normalize parameters
→ Java assigns components to fields
```

### Normalising Values

A compact constructor can modify its parameter values before the automatic assignments:

```java
record Person(String name) {

    public Person {
        name = name.trim();
    }
}
```

The normalized value is then assigned to the component field.

### Do Not Assign Component Fields Directly

The compact constructor relies on Java performing the final component assignments.

This does not compile:

```java
record Person(String name) {

    public Person {
        this.name = name; // DOES NOT COMPILE
    }
}
```

Instead, modify the parameter if necessary:

```java
record Person(String name) {

    public Person {
        name = name.trim();
    }
}
```

Memory:

> **Compact constructor: work with the parameters; Java assigns the fields.**

---

## Non-Canonical Constructors

A record may declare additional constructors with different parameter lists:

```java
record Person(String name, int age) {

    Person(String name) {
        this(name, 0);
    }
}
```

A non-canonical record constructor must ultimately delegate to the canonical constructor.

The key rule is:

```text
non-canonical constructor
→ must delegate with this(...)
```

For example:

```java
record Point(int x, int y) {

    Point(int value) {
        this(value, value);
    }
}
```

This ensures that record component initialization ultimately goes through the canonical construction process.

---

## Methods

Records can declare ordinary instance methods:

```java
record Rectangle(int width, int height) {

    int area() {
        return width * height;
    }
}
```

They can also declare static methods:

```java
record Rectangle(int width, int height) {

    static Rectangle square(int size) {
        return new Rectangle(size, size);
    }
}
```

### Custom Accessors

A generated accessor may be explicitly declared:

```java
record Person(String name) {

    @Override
    public String name() {
        return name.toUpperCase();
    }
}
```

The accessor still has the component-style name:

```java
name()
```

and must have the appropriate return type.

---

## Inheritance

Every record implicitly extends:

```java
java.lang.Record
```

You cannot explicitly make a record extend another class:

```java
record Person(String name) extends SomeClass { // DOES NOT COMPILE
}
```

A normal class also cannot extend a record.

Records are implicitly `final`.

Think:

```text
record
→ extends java.lang.Record
→ implicitly final
```

Therefore:

```java
record Parent(int value) {
}

class Child extends Parent { // DOES NOT COMPILE
}
```

---

## Interfaces

Although a record cannot extend another class, it **can implement interfaces**:

```java
interface Printable {
    void print();
}

record Person(String name) implements Printable {

    @Override
    public void print() {
        System.out.println(name);
    }
}
```

A record can implement multiple interfaces:

```java
record Example(int value)
        implements A, B {
}
```

This gives the usual relationship:

```text
record
→ cannot choose superclass
→ can implement interfaces
```

---

# Quick Reference

## What the Header Generates

```java
record Person(String name, int age) {
}
```

Think:

```text
COMPONENTS
name
age

        ↓

private final fields

        ↓

public accessors
name()
age()

        ↓

canonical constructor

        ↓

equals()
hashCode()
toString()
```

## Fields

```text
record component fields
→ private final

additional instance fields
→ NOT ALLOWED

additional static fields
→ ALLOWED
→ ordinary static-field rules
→ NOT implicitly final
```

Therefore:

```java
record Example(int value) {
    static int count;       // valid, defaults to 0
    static final int MAX = 10;
}
```

## Constructors

Canonical:

```java
record Person(String name, int age) {

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

Compact:

```java
record Person(String name, int age) {

    public Person {
        // validate / normalize
    }
}
```

Non-canonical:

```java
record Person(String name, int age) {

    Person(String name) {
        this(name, 0);
    }
}
```

Remember:

```text
CANONICAL
→ all components, same order/types

COMPACT
→ no parameter list
→ component parameters available
→ Java performs field assignments

NON-CANONICAL
→ different parameter list
→ delegates using this(...)
```

## Inheritance

```text
record
→ implicitly extends java.lang.Record
→ implicitly final
→ cannot extend another class
→ cannot be extended
→ can implement interfaces
```

## Records Are Not Just Immutable Objects

The component references cannot be reassigned because their fields are final.

That does **not** automatically make referenced objects immutable.

For example:

```java
record Data(List<String> values) {
}
```

The field:

```text
values
```

cannot be reassigned after construction, but the `List` object itself may still be mutable.

Think:

> **Final reference ≠ immutable referenced object.**

## Final Memory Kicks

> **Record header defines the record's instance state.**

> **Component fields are implicitly `private final`.**

> **Accessors use `component()` rather than `getComponent()`.**

> **Records cannot declare additional instance fields, but they can declare static fields.**

> **Additional static fields are not implicitly `final` and receive normal default values.**

> **Canonical constructor = every component, same order and types.**

> **Compact constructor has no parameter list; validate or normalize the component parameters and Java performs the field assignments.**

> **A non-canonical constructor must delegate toward the canonical constructor using `this(...)`.**

> **Records implicitly extend `java.lang.Record` and are implicitly `final`.**

> **Records cannot extend classes but can implement interfaces.**

> **Final record component reference does not make the referenced object immutable.**