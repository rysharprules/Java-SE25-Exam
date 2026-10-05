# Interfaces & Functional Interfaces

Quick reference for interface members, functional-interface rules, inheritance and method conflicts.

## Contents

- [Interface Members](#interface-members)
- [Abstract Methods](#abstract-methods)
- [Default Methods](#default-methods)
- [Static Methods](#static-methods)
- [Private Methods](#private-methods)
- [Interface Fields](#interface-fields)
- [Functional Interfaces](#functional-interfaces)
- [Extending Interfaces](#extending-interfaces)
- [Method Conflicts](#method-conflicts)
- [Quick Reference](#quick-reference)

---

## Interface Members

An interface can contain several kinds of members:

```java
interface Example {

    int VALUE = 10;

    void abstractMethod();

    default void defaultMethod() {
    }

    static void staticMethod() {
    }

    private void privateMethod() {
    }

    private static void privateStaticMethod() {
    }
}
```

The important access and modifier rules are:

| Declaration | Effective Modifiers |
|---|---|
| `int VALUE = 10;` | `public static final` |
| `void method();` | `public abstract` |
| `default void method() {}` | `public default` |
| `static void method() {}` | `public static` |
| `private void method() {}` | `private` |
| `private static void method() {}` | `private static` |

Therefore, omitting an access modifier does **not** make an ordinary abstract, default or static interface method package-private.

For example:

```java
interface Example {
    static void run() {
    }
}
```

`run()` is implicitly:

```java
public static
```

This differs from a class:

```java
class Example {
    static void run() {
    }
}
```

Here `run()` is package-private because class methods do not receive the interface's implicit `public` modifier.

---

## Abstract Methods

A normal interface method without a body is implicitly:

```java
public abstract
```

Therefore:

```java
interface Runner {
    void run();
}
```

is effectively:

```java
interface Runner {
    public abstract void run();
}
```

An implementing class must provide a compatible `public` implementation unless the class itself is abstract:

```java
class Task implements Runner {

    @Override
    public void run() {
    }
}
```

This does not compile:

```java
class Task implements Runner {

    void run() { } // DOES NOT COMPILE
}
```

The implementation cannot reduce the inherited `public` accessibility.

---

## Default Methods

A `default` method provides an inherited **instance implementation** inside an interface:

```java
interface Vehicle {

    default void start() {
        System.out.println("Starting");
    }
}
```

A default method is implicitly `public`.

This:

```java
default void start() {
}
```

is effectively:

```java
public default void start() {
}
```

### Default Methods Cannot Be Private

A default method must be `public`.

This does not compile:

```java
interface Example {

    private default void run() { // DOES NOT COMPILE
    }
}
```

An interface may contain a private instance method:

```java
private void helper() {
}
```

but that is a **private interface method**, not a default method.

Think:

```text
default
→ public instance method
→ inherited

private
→ internal interface helper
→ not inherited
```

### Inheritance

An implementing class inherits a default method:

```java
class Car implements Vehicle {
}
```

and can call it through an instance:

```java
new Car().start();
```

A class may override it:

```java
class Car implements Vehicle {

    @Override
    public void start() {
        System.out.println("Car starting");
    }
}
```

The overriding method must remain `public`.

### Calling a Specific Parent Default

An implementing class can explicitly invoke an inherited interface default:

```java
interface A {

    default void run() {
        System.out.println("A");
    }
}

class Test implements A {

    void execute() {
        A.super.run();
    }
}
```

The special syntax is:

```java
InterfaceName.super.method()
```

---

## Static Methods

An interface can declare static methods:

```java
interface Calculator {

    static int add(int a, int b) {
        return a + b;
    }
}
```

With no explicit access modifier, an interface static method is implicitly `public`.

Therefore:

```java
static void run() {
}
```

is effectively:

```java
public static void run() {
}
```

Call it using the interface name:

```java
Calculator.add(2, 3);
```

### Static Methods Can Be Private

Unlike default methods, an interface static method can explicitly be `private`:

```java
interface Example {

    static void run() {
        helper();
    }

    private static void helper() {
    }
}
```

Therefore:

```text
static method with no access modifier
→ public

public static
→ valid

private static
→ valid
```

There is no package-private interface static method.

### Static Interface Methods Are Not Inherited

```java
interface Parent {

    static void print() {
    }
}

interface Child extends Parent {
}
```

This works:

```java
Parent.print();
```

This does not:

```java
Child.print(); // DOES NOT COMPILE
```

Likewise:

```java
class Example implements Parent {
}
```

does not make this valid:

```java
Example.print(); // DOES NOT COMPILE
```

Memory:

> **Default methods are inherited instance methods. Static interface methods belong to the declaring interface and are not inherited.**

### Static Methods and Instance Methods

A child interface cannot declare a static method that conflicts with an inherited instance method:

```java
interface Parent {

    default void run() {
    }
}

interface Child extends Parent {

    static void run() { } // DOES NOT COMPILE
}
```

Remember that `static` is **not** part of a method signature.

A method signature is based on:

```text
method name
+
parameter types
```

It does not include:

```text
return type
static/default/abstract
throws clause
```

Changing only `static` versus instance therefore does not create an overload.

---

## Private Methods

Interfaces can contain private methods:

```java
interface Example {

    default void run() {
        helper();
    }

    private void helper() {
        System.out.println("Helping");
    }
}
```

Private methods exist to share implementation code inside the interface.

They are not inherited by implementing classes.

A private interface method:

- must have a body
- cannot be `abstract`
- cannot be `default`
- is not inherited

Interfaces can also have private static methods:

```java
interface Example {

    static void run() {
        helper();
    }

    private static void helper() {
    }
}
```

Think:

```text
private instance
→ helper for instance/default behaviour

private static
→ helper available to static behaviour
```

---

## Interface Fields

Every interface field is implicitly:

```java
public static final
```

Therefore:

```java
interface Config {
    int SIZE = 10;
}
```

is effectively:

```java
interface Config {
    public static final int SIZE = 10;
}
```

It must be initialized:

```java
interface Config {
    int SIZE; // DOES NOT COMPILE
}
```

and cannot subsequently be reassigned:

```java
Config.SIZE = 20; // DOES NOT COMPILE
```

---

## Functional Interfaces

A functional interface has exactly **one abstract method contract**.

For example:

```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}
```

It can therefore be implemented with a lambda:

```java
Calculator calculator =
        (a, b) -> a + b;
```

### `@FunctionalInterface`

The annotation is optional:

```java
interface Calculator {
    int calculate(int a, int b);
}
```

This is still a functional interface.

Adding:

```java
@FunctionalInterface
```

asks the compiler to verify that the interface satisfies the functional-interface rules.

This does not compile:

```java
@FunctionalInterface
interface Broken {
    void first();
    void second();
}
```

Memory:

> **The annotation verifies functional-interface status; it does not create it.**

### Methods That Do Not Add Abstract Contracts

A functional interface may contain default methods:

```java
@FunctionalInterface
interface Example {

    void run();

    default void log() {
    }
}
```

It may contain static methods:

```java
@FunctionalInterface
interface Example {

    void run();

    static void help() {
    }
}
```

It may also contain private helper methods:

```java
@FunctionalInterface
interface Example {

    void run();

    private void helper() {
    }
}
```

These do not create additional abstract method contracts.

The useful question is therefore not:

```text
How many methods are declared?
```

but:

> **How many distinct abstract method contracts remain?**

---

## Object Methods and Functional Interfaces

Public methods matching methods from `Object` do not count as additional abstract methods for functional-interface purposes.

For example:

```java
@FunctionalInterface
interface Example {

    void run();

    boolean equals(Object obj);
}
```

This can still be a functional interface.

The `equals(Object)` declaration corresponds to a public method of `Object`, so it does not create another functional-interface abstract-method requirement.

Think:

```text
method declarations visible
≠
abstract contracts counted
```

---

## Inherited Abstract Methods

Inherited abstract methods also matter.

```java
interface Parent {
    void run();
}

@FunctionalInterface
interface Child extends Parent {
}
```

`Child` inherits:

```java
void run();
```

and therefore has one abstract method contract.

It is functional.

### Compatible Methods Can Represent One Contract

```java
interface A {
    void run();
}

interface B {
    void run();
}

@FunctionalInterface
interface C extends A, B {
}
```

Although `C` inherits declarations from both interfaces, they represent the same compatible abstract method contract.

`C` can therefore still be functional.

Think:

> **Count abstract contracts, not source declarations.**

### Different Abstract Contracts

```java
interface A {
    void run();
}

interface B {
    void stop();
}

interface C extends A, B {
}
```

`C` has two independent abstract method contracts:

```text
run()
stop()
```

so it is not a functional interface.

---

## Extending Interfaces

An interface uses `extends`:

```java
interface Child extends Parent {
}
```

It can extend multiple interfaces:

```java
interface C extends A, B {
}
```

A class uses `implements`:

```java
class Example implements A, B {
}
```

An interface may inherit:

```text
abstract methods   ✓
default methods    ✓
static methods     ✗
private methods    ✗
```

---

## Method Conflicts

Multiple interface inheritance can create conflicts.

### Compatible Abstract Methods

Compatible abstract methods can represent one requirement:

```java
interface A {
    void run();
}

interface B {
    void run();
}

interface C extends A, B {
}
```

`C` has one effective `run()` contract.

### Competing Default Methods

```java
interface A {

    default void run() {
        System.out.println("A");
    }
}

interface B {

    default void run() {
        System.out.println("B");
    }
}
```

A class cannot simply inherit both:

```java
class Example implements A, B { // DOES NOT COMPILE
}
```

It must resolve the conflict:

```java
class Example implements A, B {

    @Override
    public void run() {
        A.super.run();
    }
}
```

or provide an entirely new implementation.

Memory:

> **Two unrelated defaults with the same signature require conflict resolution.**

### Class Methods Beat Interface Defaults

```java
class Parent {

    public void run() {
        System.out.println("Parent");
    }
}

interface Runner {

    default void run() {
        System.out.println("Runner");
    }
}

class Child extends Parent implements Runner {
}
```

Calling:

```java
new Child().run();
```

uses:

```text
Parent.run()
```

Memory:

> **Class beats interface default.**

### More Specific Interface Wins

```java
interface Parent {

    default void run() {
        System.out.println("Parent");
    }
}

interface Child extends Parent {

    @Override
    default void run() {
        System.out.println("Child");
    }
}

class Example implements Parent, Child {
}
```

There is no ambiguity.

`Child` is more specific, so its implementation wins.

Memory:

> **More specific interface beats less specific interface.**

---

# Quick Reference

## Interface Members

| Member | Effective Access / Modifiers | Inherited? |
|---|---|---|
| Field | `public static final` | Accessible through normal field rules |
| Abstract method | `public abstract` | Yes |
| Default method | `public default` | Yes |
| Static method | `public static` | **No** |
| Private method | `private` | **No** |
| Private static method | `private static` | **No** |

Key access distinction:

```text
default
→ always public
→ cannot be private

static
→ public by default
→ may explicitly be private
```

There is no package-private abstract, default or static interface method produced simply by omitting an access modifier.

## Functional Interface Check

Ask:

```text
How many distinct abstract method contracts remain?
```

```text
abstract method                    COUNTS

inherited abstract method          COUNTS

compatible duplicate abstract
declarations                       may collapse to ONE

default method                     DOES NOT COUNT

static method                      DOES NOT COUNT

private method                     DOES NOT COUNT

public Object-method equivalent    DOES NOT COUNT
```

`@FunctionalInterface`:

```text
optional
+
compiler verification
```

## Static vs Default

```text
DEFAULT
→ public
→ instance
→ inherited
→ cannot be private

STATIC
→ public unless explicitly private
→ belongs to declaring interface
→ not inherited
```

Typical calls:

```java
object.defaultMethod();

Interface.staticMethod();
```

## Inheritance

```text
interface → interface
extends

class → interface
implements
```

An interface can extend multiple interfaces:

```java
interface C extends A, B {
}
```

## Default-Method Conflicts

```text
CLASS implementation present?
        ↓
      wins

otherwise

MORE SPECIFIC interface?
        ↓
      wins

otherwise

two unrelated competing defaults?
        ↓
must resolve conflict
```

## Final Memory Kicks

> **Functional interface = one abstract method contract, not necessarily one method declaration.**

> **Default, static and private methods do not add abstract contracts.**

> **Compatible inherited abstract declarations can represent one contract.**

> **Public `Object` method equivalents do not count toward the functional-interface contract.**

> **`@FunctionalInterface` verifies the rule; it isn't required.**

> **Default methods are public inherited instance methods — `private default` is illegal.**

> **Static interface methods are public when no access modifier is written, but may explicitly be private.**

> **Static interface methods are not inherited.**

> **`static` is not part of the method signature.**

> **Class beats interface default → more specific interface wins → unrelated competing defaults must be resolved.**

> **Interface fields are always `public static final`.**