### Class Initialization & Constructor Execution Order

The key is to separate:

1. **Static initialization** — happens when the class is initialized.
2. **Instance initialization** — happens every time an object is created.
3. **Constructor execution** — superclass construction happens before subclass construction.

---

## 1. Static Initialization

Static fields and static initializer blocks execute **once**, in the textual order they appear in the class.

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

Order:

```text
static field a
static block
static field b
```

Mental rule:

> **Static fields and static blocks execute top-to-bottom in source order.**

---

## 2. Instance Initialization

When an object is created, instance field initializers and instance initializer blocks execute in **textual order**.

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

Mental rule:

> **Instance fields + initializer blocks execute top-to-bottom, then the constructor body.**

---

# 3. Inheritance

With inheritance, the **superclass initializes before the subclass**.

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

When `Child` is first initialized and instantiated:

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

So remember:

```text
STATIC:
Parent → Child

INSTANCE:
Parent → Child
```

---

# 4. Full Order

For the first:

```java
new Child();
```

think:

```text
1. Parent static fields/blocks
2. Child static fields/blocks

3. Parent instance fields/blocks
4. Parent constructor

5. Child instance fields/blocks
6. Child constructor
```

Within each class, fields and initializer blocks execute in **source order**.

On a second:

```java
new Child();
```

the static initialization does **not** repeat:

```text
Parent instance fields/blocks
Parent constructor
Child instance fields/blocks
Child constructor
```

---

# 5. Constructor Chaining — `super()`

Every constructor ultimately invokes a superclass constructor.

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

The compiler effectively supplies:

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

Construction therefore travels **up the inheritance hierarchy first**, then constructor bodies complete on the way back down.

For:

```text
Object
  ↑
Parent
  ↑
Child
```

think:

```text
constructor invocation:
Child → Parent → Object

constructor bodies complete:
Object → Parent → Child
```

---

# 6. Constructor Overloading

Constructors can be **overloaded**:

```java
class Person {

    Person() {
        System.out.println("No args");
    }

    Person(String name) {
        System.out.println(name);
    }
}
```

Different parameter lists = different constructors.

Constructors are **not overridden** because constructors are not inherited.

---

# 7. `this()` Constructor Chaining

One constructor can invoke another constructor in the same class:

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

---

# 8. Instance Initializers Do NOT Run for Every `this()` Call

This is an important trap.

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

NOT:

```text
Initializer
Initializer
int constructor
No-arg
```

The object is only being initialized once.

Mental model:

> **Instance initializers run once per object, not once per constructor in a `this()` chain.**

---

# 9. `this()` vs `super()`

A constructor ultimately delegates using either:

```java
this(...)
```

or:

```java
super(...)
```

A `this()` chain eventually has to reach a constructor that invokes `super()`.

Conceptually:

```text
Child()
   ↓ this(...)
Child(int)
   ↓ super(...)
Parent()
```

Then execution completes back out:

```text
Parent constructor
      ↓
Child(int) constructor
      ↓
Child() constructor
```

Constructor cycles are illegal:

```java
Example() {
    this(1);
}

Example(int x) {
    this();
}
```

Does not compile.

---

# 10. Java 25 Flexible Constructor Bodies

Java 25 allows certain statements before an explicit:

```java
this(...)
```

or:

```java
super(...)
```

For example:

```java
Child(String name) {

    String cleaned = name.trim();

    super(cleaned);

    System.out.println("Child");
}
```

The code before `super()` is the **constructor prologue**.

However, this does not change the fundamental initialization order.

The prologue cannot use the object currently being constructed in ways that require it to have been initialized.

After the superclass constructor completes, normal instance initialization/construction continues.

---

# 11. Overridden Methods During Construction — Dangerous Trap

Although constructors cannot be overridden, constructors **can call overridden methods**.

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

Then:

```java
new Child();
```

the `Parent` constructor calls:

```java
print();
```

but dynamic dispatch invokes:

```java
Child.print()
```

The problem is that the `Child` instance fields have **not yet been initialized**.

Therefore:

```text
value = 0
```

at that moment, not `10`.

So this prints:

```text
0
```

Later:

```java
int value = 10;
```

is executed during the Child's instance initialization.

This is why calling overridable methods from constructors can be dangerous.

---

# Exam Memory Model

For:

```java
new Child();
```

think:

```text
FIRST CLASS USE
───────────────

STATIC INITIALIZATION

Parent static fields/blocks
        ↓
Child static fields/blocks


OBJECT CREATION
───────────────

Parent instance fields/blocks
        ↓
Parent constructor
        ↓
Child instance fields/blocks
        ↓
Child constructor
```

On subsequent objects:

```text
NO STATIC INITIALIZATION

Parent instance fields/blocks
        ↓
Parent constructor
        ↓
Child instance fields/blocks
        ↓
Child constructor
```

And remember:

```text
static fields + static blocks
    → source order
    → once per class initialization

instance fields + initializer blocks
    → source order
    → once per object

superclass
    → initialized/constructed before subclass

this(...)
    → delegates to another constructor
    → does NOT repeat instance initialization

constructors
    → can be overloaded
    → cannot be overridden

overridden method called by constructor
    → dynamic dispatch still applies
    → subclass fields may still contain default values
```