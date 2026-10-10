# Class Initialization & Constructor Execution

[🔙 Back](README.md)

Quick reference for static initialization, instance initialization, constructor chaining and execution order.

## Contents

- [Static Initialization](#static-initialization)
- [Instance Initialization](#instance-initialization)
- [Inheritance](#inheritance)
- [Constructor Chaining](#constructor-chaining)
- [Constructor Prologues](#constructor-prologues)
- [Full Execution Order](#full-execution-order)
- [Overridden Methods During Construction](#overridden-methods-during-construction)
- [Quick Reference](#quick-reference)

---

## Static Initialization

Static field initializers and static initializer blocks execute **once**, in the textual order they appear.

```java
class Example {

    static int a = print("static field a");

    static {
        print("static block");
    }

    static int b = print("static field b");

    static int print(String text) {
        System.out.println(text);
        return 1;
    }
}
```

When the class is initialized:

```text
static field a
static block
static field b
```

Memory:

> **Static fields and static blocks execute top-to-bottom in source order.**

They execute once when the class is initialized, not every time an object is created.

---

## Instance Initialization

Instance field initializers and instance initializer blocks execute in **textual order** for each new object.

```java
class Example {

    int a = print("field a");

    {
        print("initializer block");
    }

    int b = print("field b");

    Example() {
        print("constructor");
    }

    int print(String text) {
        System.out.println(text);
        return 1;
    }
}
```

Creating:

```java
new Example();
```

prints:

```text
field a
initializer block
field b
constructor
```

Memory:

> **Instance fields and initializer blocks execute top-to-bottom before that class's constructor body.**

### Once Per Object

Instance initialization occurs once per object.

It does **not** occur once for every constructor involved in a `this()` chain.

```java
class Example {

    {
        System.out.println("Initializer");
    }

    Example() {
        this(10);
        System.out.println("No-arg");
    }

    Example(int x) {
        System.out.println("int constructor");
    }
}
```

Creating:

```java
new Example();
```

prints:

```text
Initializer
int constructor
No-arg
```

Not:

```text
Initializer
Initializer
int constructor
No-arg
```

Memory:

> **One object → one run of its instance initialization.**

---

## Inheritance

With inheritance, superclass initialization happens before subclass initialization.

```java
class Parent {

    static {
        System.out.println("Parent static");
    }

    {
        System.out.println("Parent instance");
    }

    Parent() {
        System.out.println("Parent constructor");
    }
}

class Child extends Parent {

    static {
        System.out.println("Child static");
    }

    {
        System.out.println("Child instance");
    }

    Child() {
        System.out.println("Child constructor");
    }
}
```

For the first:

```java
new Child();
```

the broad order is:

```text
Parent static
Child static

Parent instance
Parent constructor

Child instance
Child constructor
```

Think:

```text
STATIC
Parent → Child

INSTANCE + CONSTRUCTION
Parent → Child
```

There is only **one object** being created: the `Child`.

Java initializes the superclass portion of that object before the subclass portion.

### Subsequent Objects

For another:

```java
new Child();
```

the static initialization does not repeat:

```text
Parent instance
Parent constructor

Child instance
Child constructor
```

Memory:

```text
static initialization
→ once per class initialization

instance initialization
→ once per object
```

---

## Constructor Chaining

Every constructor ultimately leads to superclass construction.

### `super()`

Consider:

```java
class Parent {
    Parent() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    Child() {
        System.out.println("Child");
    }
}
```

The `Child` constructor has an implicit call to `super()`:

```java
Child() {
    super();
    System.out.println("Child");
}
```

Output:

```text
Parent
Child
```

Conceptually, for:

```text
Object
  ↑
Parent
  ↑
Child
```

constructor invocation travels up:

```text
Child → Parent → Object
```

and constructor bodies complete back down:

```text
Object → Parent → Child
```

### `this()`

A constructor can delegate to another constructor in the **same class**:

```java
class Person {

    Person() {
        this("Unknown");
        System.out.println("No-arg constructor");
    }

    Person(String name) {
        System.out.println("Name constructor");
    }
}
```

Creating:

```java
new Person();
```

prints:

```text
Name constructor
No-arg constructor
```

The delegated constructor completes before execution returns to the calling constructor.

### `this()` vs `super()`

Think:

```text
this(...)
→ another constructor in SAME class

super(...)
→ constructor in SUPERCLASS
```

A `this()` chain must eventually reach superclass construction:

```text
Child()
   ↓ this(...)
Child(int)
   ↓ super(...)
Parent()
```

Execution then completes back out:

```text
Parent constructor
      ↓
Child(int) constructor
      ↓
Child() constructor
```

### Constructor Cycles

Constructor chains cannot be circular:

```java
Example() {
    this(1);
}

Example(int x) {
    this();
}
```

This does not compile.

### Overloading vs Overriding

Constructors can be **overloaded**:

```java
Person() { }

Person(String name) { }
```

Constructors:

```text
can be overloaded
are not inherited
cannot be overridden
```

---

## Constructor Prologues

Java 25 flexible constructor bodies allow certain statements before an explicit `this(...)` or `super(...)`.

```java
Child(String name) {

    String cleaned = name.trim();

    super(cleaned);

    System.out.println("Child");
}
```

The code before:

```java
super(cleaned);
```

is the **constructor prologue**.

The important consequence is:

> **Some constructor code can execute before superclass construction.**

### What a Prologue Can Use

A prologue can work with things such as:

```text
constructor parameters
local variables
static members
other objects
```

But it cannot freely use the current object before superclass construction.

For example, don't think of the current object's instance fields and instance methods as already available for normal use.

Memory:

```text
PROLOGUE
→ before this(...) / super(...)
→ current object not yet fully constructed
```

For the complete Java 25 rules, see the separate **Flexible Constructor Bodies** addendum.

### Prologues and `this()`

Prologues can also occur while following a `this()` constructor chain.

Conceptually:

```text
Child()
   │
   ├─ prologue
   │
   ↓ this(...)
Child(int)
   │
   ├─ prologue
   │
   ↓ super(...)
Parent()
```

The prologues execute while following the constructor chain.

---

## Full Execution Order

For a hierarchy:

```text
Animal
  ↑
Mammal
  ↑
Dog
```

consider the first:

```java
new Dog();
```

### Static Initialization

Required classes initialize from superclass to subclass:

```text
Animal static fields/blocks
        ↓
Mammal static fields/blocks
        ↓
Dog static fields/blocks
```

Within each class, static fields and blocks execute in source order.

### Follow Constructor Chain

With Java 25 constructor prologues:

```text
Dog prologue
     ↓
Mammal prologue
     ↓
Animal construction
```

Prologues execute while travelling through the constructor chain toward superclass construction.

### Initialize and Construct Back Down

Then:

```text
Animal instance fields/blocks
        ↓
Animal constructor body
        ↓
Mammal instance fields/blocks
        ↓
Mammal constructor body
        ↓
Dog instance fields/blocks
        ↓
Dog constructor body
```

This gives the main memory model:

```text
FIRST OBJECT

STATICS
Parent → Child
      ↓
PROLOGUES UP
Child → Parent
      ↓
INIT + CONSTRUCT DOWN
Parent → Child
```

Or simply:

> **STATICS → PROLOGUES UP → INIT + CONSTRUCTORS DOWN**

### Later Objects

Once the classes are already initialized:

```text
PROLOGUES UP
      ↓
INIT + CONSTRUCTORS DOWN
```

Static initialization does not repeat merely because another object is created.

---

## Overridden Methods During Construction

Constructors cannot be overridden, but they **can call overridden methods**.

This creates an important initialization trap.

```java
class Parent {

    Parent() {
        print();
    }

    void print() {
        System.out.println("Parent");
    }
}

class Child extends Parent {

    int value = 10;

    @Override
    void print() {
        System.out.println(value);
    }
}
```

Now:

```java
new Child();
```

The `Parent` constructor calls:

```java
print();
```

Dynamic dispatch still applies, so Java invokes:

```java
Child.print()
```

But the `Child` instance initialization has not happened yet.

Therefore:

```java
int value = 10;
```

has not executed.

At that point, `value` still contains its default value:

```text
0
```

So the call prints:

```text
0
```

Only later does the `Child` field initializer assign:

```text
10
```

Memory:

```text
Parent constructor
      ↓
calls overridden method
      ↓
Child implementation runs
      ↓
Child fields may NOT be initialized yet
      ↓
default values may be observed
```

This is why calling overridable methods from constructors is dangerous.

---

# Quick Reference

## Static Initialization

```text
static fields + static blocks

→ source order
→ once per class initialization
→ superclass before subclass
```

## Instance Initialization

```text
instance fields + initializer blocks

→ source order
→ once per object
→ before that class's constructor body
→ superclass portion before subclass portion
```

## Constructor Chaining

```text
this(...)
→ another constructor in SAME class

super(...)
→ constructor in SUPERCLASS
```

Constructor chains:

```text
travel toward superclass construction
then
constructor bodies complete back out
```

Cycles:

```text
this() → this() → ... → original constructor
→ DOES NOT COMPILE
```

## Constructors

```text
overloaded       ✓
inherited        ✗
overridden       ✗
```

## Java 25 Constructor Prologues

```text
statements may occur before explicit
this(...) / super(...)

prologues execute while following
the constructor chain

current object cannot be freely used
before superclass construction
```

See the separate **Flexible Constructor Bodies** addendum for the detailed rules.

## First vs Later Object

First relevant construction:

```text
STATICS
   ↓
PROLOGUES UP
   ↓
INIT + CONSTRUCTORS DOWN
```

Later construction:

```text
PROLOGUES UP
   ↓
INIT + CONSTRUCTORS DOWN
```

## Overridden Method Trap

```text
constructor calls method
        ↓
dynamic dispatch still applies
        ↓
subclass override may execute
        ↓
subclass fields may still have
DEFAULT VALUES
```

## Reliable Tracing Method

For a complicated initialization question:

```text
1. Identify the inheritance hierarchy

2. Determine whether static initialization
   is required

3. Process static fields/blocks in source order

4. Follow the constructor chain toward
   superclass construction

5. Execute any constructor prologues
   encountered along that chain

6. Work back down the hierarchy:
      instance fields/blocks
      constructor body

7. If a constructor calls an overridable method,
   apply dynamic dispatch using the object's
   current initialization state
```

## Final Memory Kicks

> **Static fields and blocks execute once, in source order.**

> **Instance fields and blocks execute once per object, in source order.**

> **Superclass initialization/construction precedes the corresponding subclass initialization/construction.**

> **`this()` delegates within the same class; `super()` delegates to the superclass.**

> **A `this()` chain does not cause instance initialization to run multiple times.**

> **Constructors can be overloaded, but they are not inherited or overridden.**

> **Java 25 constructor prologues can execute before `this(...)` or `super(...)`.**

> **For Java 25: STATICS → PROLOGUES UP → INIT + CONSTRUCTORS DOWN.**

> **Dynamic dispatch still works during construction, so an overridden subclass method can see subclass fields before their initializers run.**