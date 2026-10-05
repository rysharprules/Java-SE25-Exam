# Nested Classes

Quick reference for static nested classes, inner classes, local classes and anonymous classes.

## Contents

- [The Four Types](#the-four-types)
- [Static Nested Classes](#static-nested-classes)
- [Inner Classes](#inner-classes)
- [Local Classes](#local-classes)
- [Anonymous Classes](#anonymous-classes)
- [Local Variable Capture](#local-variable-capture)
- [Quick Reference](#quick-reference)

---

## The Four Types

A class declared within another class or within a block of code is a **nested class**.

The main forms are:

```text
STATIC NESTED
→ static member of another class

INNER
→ non-static member of another class

LOCAL
→ declared inside a block, usually a method

ANONYMOUS
→ unnamed class declared and instantiated in one expression
```

The major distinction is:

```text
static nested
→ no enclosing outer instance required

inner / local / anonymous
→ may have an enclosing instance/context
```

---

## Static Nested Classes

A static nested class is declared with `static`:

```java
class Outer {

    static class Nested {
    }
}
```

It does **not** require an instance of `Outer`.

Construct it with:

```java
Outer.Nested nested = new Outer.Nested();
```

### Access to Outer Members

A static nested class can directly access static members of the outer class:

```java
class Outer {

    private static int count = 10;

    static class Nested {

        void print() {
            System.out.println(count);
        }
    }
}
```

It cannot directly access an outer instance member:

```java
class Outer {

    private int value = 10;

    static class Nested {

        void print() {
            System.out.println(value); // DOES NOT COMPILE
        }
    }
}
```

It would need an `Outer` object:

```java
void print(Outer outer) {
    System.out.println(outer.value);
}
```

Memory:

> **Static nested → tied to the outer class, not an outer object.**

---

## Inner Classes

A non-static member class is an **inner class**:

```java
class Outer {

    class Inner {
    }
}
```

Each `Inner` object is associated with an instance of `Outer`.

### Construction

From outside `Outer`:

```java
Outer outer = new Outer();

Outer.Inner inner = outer.new Inner();
```

The unusual syntax is:

```java
outer.new Inner()
```

This does not compile:

```java
new Outer.Inner(); // DOES NOT COMPILE
```

because there is no enclosing `Outer` instance.

### Access to Outer Members

An inner class can directly access members of its enclosing outer object, including `private` members:

```java
class Outer {

    private int value = 10;

    class Inner {

        void print() {
            System.out.println(value);
        }
    }
}
```

### From Inside the Outer Class

Inside an instance context of `Outer`, the enclosing object already exists:

```java
class Outer {

    class Inner {
    }

    void create() {
        Inner inner = new Inner();
    }
}
```

No `outer.new` is required there.

Memory:

> **Inner → tied to an outer object.**

---

## Local Classes

A local class is declared inside a block, commonly inside a method:

```java
class Outer {

    void run() {

        class Local {
            void print() {
                System.out.println("Hello");
            }
        }

        Local local = new Local();
        local.print();
    }
}
```

Its scope is limited to the block in which it is declared.

This means code outside that block cannot use:

```java
Local
```

### Access to Enclosing Members

A local class declared inside an instance method can access members of the enclosing object:

```java
class Outer {

    private int value = 10;

    void run() {

        class Local {
            void print() {
                System.out.println(value);
            }
        }
    }
}
```

It can also use eligible local variables from the surrounding method.

Those variables must be:

```text
final
or
effectively final
```

---

## Anonymous Classes

An anonymous class has no declared class name.

It is declared and instantiated in one expression:

```java
Runnable task = new Runnable() {

    @Override
    public void run() {
        System.out.println("Running");
    }
};
```

Think:

```text
new Type(...) {
    // class body
}
```

This creates an object of an unnamed class.

### Extending a Class

An anonymous class can extend a class:

```java
Animal animal = new Animal() {

    @Override
    void speak() {
        System.out.println("Hello");
    }
};
```

### Implementing an Interface

It can implement an interface:

```java
Runnable task = new Runnable() {

    @Override
    public void run() {
    }
};
```

The syntax still uses:

```java
new Runnable()
```

even though `Runnable` is an interface.

### No Explicit Constructor

An anonymous class has no class name, so you cannot declare an ordinary constructor for it.

This makes no sense:

```text
ClassName(...)
```

because there is no `ClassName`.

Arguments in the creation expression may instead be passed to a superclass constructor when appropriate.

Memory:

> **Anonymous class = declaration + object creation in one expression, with no class name.**

---

## Local Variable Capture

Local and anonymous classes can access local variables from their enclosing method only when those variables are **final or effectively final**.

### Effectively Final

The variable does not need the `final` keyword:

```java
void run() {

    int value = 10;

    class Local {
        void print() {
            System.out.println(value);
        }
    }
}
```

`value` is effectively final because it is assigned once and never changed.

This does not compile:

```java
void run() {

    int value = 10;

    class Local {
        void print() {
            System.out.println(value);
        }
    }

    value = 20; // prevents value being effectively final
}
```

The same principle applies to an anonymous class:

```java
void run() {

    int value = 10;

    Runnable task = new Runnable() {

        @Override
        public void run() {
            System.out.println(value);
        }
    };
}
```

### Object Mutation vs Variable Reassignment

Effectively final applies to the **local variable**, not necessarily to the object it references.

For example:

```java
void run() {

    StringBuilder builder = new StringBuilder();

    Runnable task = new Runnable() {

        @Override
        public void run() {
            builder.append("Hello");
        }
    };
}
```

The object may be mutated.

What would cause the problem is reassigning the captured local variable:

```java
builder = new StringBuilder(); // no longer effectively final
```

Memory:

> **The captured variable must not be reassigned; the referenced object does not have to be immutable.**

---

# Quick Reference

## Four Forms

| Type | Declared Where? | Name? | Needs Outer Instance? |
|---|---|---|---|
| Static nested | Class | Yes | No |
| Inner | Class | Yes | Yes |
| Local | Block/method | Yes | Depends on enclosing context |
| Anonymous | Expression | No | Depends on enclosing context |

## Construction

Static nested:

```java
Outer.Nested nested =
        new Outer.Nested();
```

Inner:

```java
Outer outer = new Outer();

Outer.Inner inner =
        outer.new Inner();
```

Local:

```java
void run() {

    class Local {
    }

    Local local = new Local();
}
```

Anonymous:

```java
Runnable task = new Runnable() {

    @Override
    public void run() {
    }
};
```

## Static Nested vs Inner

```text
STATIC NESTED
→ no outer object required
→ new Outer.Nested()
→ no direct outer instance access


INNER
→ outer object required
→ outer.new Inner()
→ direct access to enclosing instance
```

## Local & Anonymous Capture

A surrounding local variable must be:

```text
final
or
effectively final
```

Effectively final means:

```text
assigned
+
never subsequently reassigned
```

The referenced object itself may still be mutable.

## Final Memory Kicks

> **Static nested → `new Outer.Nested()` → no outer instance required.**

> **Inner → `outer.new Inner()` → belongs to an outer instance.**

> **An inner class can directly access members of its enclosing outer object.**

> **A static nested class cannot directly access outer instance members.**

> **Local class → named class declared inside a block; scope is limited to that block.**

> **Anonymous class → unnamed class declared and instantiated in one expression.**

> **An anonymous class cannot declare a normal constructor because it has no class name.**

> **Local and anonymous classes can capture local variables only when they are final or effectively final.**

> **Effectively final restricts reassignment of the variable, not mutation of the referenced object.**

> **STATIC NESTED → CLASS**

> **INNER → OBJECT**

> **LOCAL → BLOCK**

> **ANONYMOUS → EXPRESSION**