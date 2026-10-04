# Inner & Static Nested Classes

Quick reference for the essential differences between inner classes and static nested classes.

## Contents

- [Inner Classes](#inner-classes)
- [Static Nested Classes](#static-nested-classes)
- [Quick Reference](#quick-reference)

---

## Inner Classes

A non-static nested class is an **inner class**.

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

An `Inner` object is associated with an **instance of `Outer`**.

Therefore, from outside `Outer`, construction requires an outer object:

```java
Outer outer = new Outer();

Outer.Inner inner = outer.new Inner();
```

The unusual syntax to remember is:

```java
outer.new Inner()
```

not:

```java
new Outer.Inner(); // DOES NOT COMPILE
```

Memory:

> **Inner instance needs an outer instance.**

### Access to the Outer Instance

Because an `Inner` object belongs to an `Outer` object, it can directly access that object's members:

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

The inner class can access `value`, including if it is `private`.

---

## Static Nested Classes

A nested class declared `static` is a **static nested class**.

```java
class Outer {

    static class Nested {
        void print() {
            System.out.println("Hello");
        }
    }
}
```

It does **not** require an `Outer` instance.

Construct it with:

```java
Outer.Nested nested = new Outer.Nested();
```

There is no:

```java
outer.new Nested()
```

because the nested object is not associated with a particular outer object.

Memory:

> **Static nested class needs the outer class name, not an outer object.**

### Access to Outer Members

A static nested class has no enclosing `Outer` instance.

Therefore it cannot directly access an outer instance member:

```java
class Outer {

    private int value = 10;
    private static int count = 20;

    static class Nested {

        void print() {
            System.out.println(count); // OK
            System.out.println(value); // DOES NOT COMPILE
        }
    }
}
```

It can directly access static members:

```java
count
```

To access an instance member, it needs an `Outer` object:

```java
void print(Outer outer) {
    System.out.println(outer.value);
}
```

---

# Quick Reference

| | Inner Class | Static Nested Class |
|---|---|---|
| Declaration | `class Inner` | `static class Nested` |
| Needs outer instance? | **Yes** | **No** |
| Construction | `outer.new Inner()` | `new Outer.Nested()` |
| Direct outer instance access | **Yes** | **No** |
| Direct outer static access | **Yes** | **Yes** |

## Construction Syntax

```java
Outer outer = new Outer();
```

Inner:

```java
Outer.Inner inner =
        outer.new Inner();
```

Static nested:

```java
Outer.Nested nested =
        new Outer.Nested();
```

The quickest memory trick:

```text
INNER
→ tied to OBJECT
→ outer.new Inner()


STATIC NESTED
→ tied to CLASS
→ new Outer.Nested()
```

## Final Memory Kicks

> **An inner-class instance belongs to an enclosing outer instance.**

> **From outside the outer class: `outer.new Inner()`.**

> **A static nested class does not require an outer instance: `new Outer.Nested()`.**

> **An inner class can directly access members of its enclosing outer object.**

> **A static nested class has no enclosing outer object, so it cannot directly access outer instance members.**

> **INNER → OBJECT → `outer.new Inner()`**

> **STATIC NESTED → CLASS → `new Outer.Nested()`**