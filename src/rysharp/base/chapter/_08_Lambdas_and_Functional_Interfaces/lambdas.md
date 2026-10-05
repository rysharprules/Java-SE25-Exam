# Lambdas & Functional Interfaces

Quick reference for lambda syntax, functional-interface families, primitive specialisations, variable capture and functional-interface convenience methods.

## Contents

- [Lambda Syntax](#lambda-syntax)
- [Lambda Parameters](#lambda-parameters)
- [Lambda Bodies](#lambda-bodies)
- [Functional Interfaces](#functional-interfaces)
- [Core Functional Interfaces](#core-functional-interfaces)
- [Primitive Specialisations](#primitive-specialisations)
- [Predicate Convenience Methods](#predicate-convenience-methods)
- [Function Convenience Methods](#function-convenience-methods)
- [Consumer Convenience Methods](#consumer-convenience-methods)
- [Variable Capture](#variable-capture)
- [Method References](#method-references)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Lambda Syntax

A lambda provides an implementation of the single abstract method of a functional interface.

```java
Predicate<String> empty = s -> s.isEmpty();
```

This is equivalent in purpose to providing an implementation of:

```java
boolean test(String s);
```

Common forms:

```java
() -> 42

x -> x * 2

(x, y) -> x + y

(String x) -> x.length()

x -> {
    System.out.println(x);
}

x -> {
    return x.length();
}
```

A lambda does not have a type by itself.

Its type comes from the **target functional interface**:

```java
Predicate<String> p = s -> s.isEmpty();
Function<String, Integer> f = s -> s.length();
Consumer<String> c = s -> System.out.println(s);
```

The surrounding context therefore determines what parameter and return types the lambda must satisfy.

---

## Lambda Parameters

Parentheses may be omitted for exactly one implicitly typed parameter:

```java
x -> x * 2
```

They are required for zero or multiple parameters:

```java
() -> 42

(x, y) -> x + y
```

They are also required when the parameter type is explicitly declared:

```java
(String x) -> x.length()
```

When there are multiple parameters, the declaration style must be consistent.

Valid:

```java
(x, y) -> x + y

(int x, int y) -> x + y

(var x, var y) -> x + y
```

Invalid:

```java
(int x, y) -> x + y

(var x, y) -> x + y
```

If `var` is used for one lambda parameter, it must be used for all parameters.

---

## Lambda Bodies

A lambda body is either:

```text
EXPRESSION
```

or:

```text
BLOCK
```

### Expression body

```java
x -> x * 2
```

There is no `return` keyword.

```java
Function<Integer, Integer> f = x -> x * 2;
```

### Block body

A block uses `{ }`:

```java
x -> {
    return x * 2;
}
```

For a value-returning functional interface, a block body must return the required value on every normal completion path.

```java
Function<Integer, Integer> f = x -> {
    return x * 2;
};
```

This is invalid:

```java
Function<Integer, Integer> f = x -> {
    x * 2;       // DOES NOT COMPILE
};
```

### `void` block bodies

A `void`-returning functional interface does not return a value:

```java
Consumer<String> c = s -> {
    System.out.println(s);
};
```

### Expression compatibility

Some expression bodies can be compatible with a `void`-returning functional interface because they are **statement expressions**.

For example:

```java
List<String> list = new ArrayList<>();

Consumer<String> c = s -> list.add(s);
```

`List.add()` returns `boolean`, but this lambda can still implement `Consumer<String>` because the method invocation expression can be used as a statement expression and its result is discarded.

Compare:

```java
() -> System.out.println("Hi")

() -> list.add("A")

() -> { list.add("A"); }

() -> { return list.add("A"); }
```

The first three can be compatible with a `void`-returning functional interface.

The fourth explicitly returns a value and therefore requires a value-returning target.

### Memory rule

```text
Lambda body = EXPRESSION or BLOCK

Expression:
    no explicit return keyword

Value block:
    return value;

Void block:
    statements, no returned value
```

---

## Functional Interfaces

A functional interface has exactly **one abstract method contract**.

```java
@FunctionalInterface
interface Calculator {
    int calculate(int x);
}
```

The annotation is optional:

```java
interface Calculator {
    int calculate(int x);
}
```

`@FunctionalInterface` simply asks the compiler to verify that the interface satisfies the functional-interface rules.

Default, static and private methods do not add abstract contracts.

```java
@FunctionalInterface
interface Calculator {

    int calculate(int x);

    default void print() {}

    static void help() {}

    private void log() {}
}
```

For the detailed rules around inherited abstract methods, defaults, statics and method conflicts, see the separate **Interfaces & Functional Interfaces** notes from Chapter 7.

---

## Core Functional Interfaces

The main interfaces are in:

```java
java.util.function
```

| Functional Interface | Parameters | Return | Single Abstract Method |
|---|---|---|---|
| `Supplier<T>` | 0 | `T` | `get()` |
| `Consumer<T>` | `T` | `void` | `accept(T)` |
| `BiConsumer<T,U>` | `T, U` | `void` | `accept(T,U)` |
| `Predicate<T>` | `T` | `boolean` | `test(T)` |
| `BiPredicate<T,U>` | `T, U` | `boolean` | `test(T,U)` |
| `Function<T,R>` | `T` | `R` | `apply(T)` |
| `BiFunction<T,U,R>` | `T, U` | `R` | `apply(T,U)` |
| `UnaryOperator<T>` | `T` | `T` | `apply(T)` |
| `BinaryOperator<T>` | `T, T` | `T` | `apply(T,T)` |

### Supplier

```text
nothing → something
```

```java
Supplier<String> s = () -> "hello";

s.get();
```

### Consumer

```text
something → nothing
```

```java
Consumer<String> c = s -> System.out.println(s);

c.accept("hello");
```

### Predicate

```text
something → boolean
```

```java
Predicate<String> p = s -> s.isEmpty();

p.test("");
```

### Function

```text
something → something else
```

```java
Function<String, Integer> f = s -> s.length();

f.apply("hello");
```

### Operators

Operators are specialised functions where the input and output use the same type.

```java
UnaryOperator<Integer> doubleIt = x -> x * 2;

BinaryOperator<Integer> add = (x, y) -> x + y;
```

Think:

```text
Supplier   → get
Consumer   → accept
Predicate  → test
Function   → apply
Operator   → apply
```

And:

```text
Supplier   nothing → T
Consumer   T → nothing
Predicate  T → boolean
Function   T → R
Operator   T → T
```

---

## Primitive Specialisations

Primitive functional interfaces avoid unnecessary boxing and unboxing.

The supported primitive families primarily use:

```text
int
long
double
```

### Primitive suppliers

| Interface | Return | Method |
|---|---|---|
| `IntSupplier` | `int` | `getAsInt()` |
| `LongSupplier` | `long` | `getAsLong()` |
| `DoubleSupplier` | `double` | `getAsDouble()` |

```java
IntSupplier supplier = () -> 42;

int result = supplier.getAsInt();
```

---

### Primitive consumers

| Interface | Parameter | Return | Method |
|---|---|---|---|
| `IntConsumer` | `int` | `void` | `accept(int)` |
| `LongConsumer` | `long` | `void` | `accept(long)` |
| `DoubleConsumer` | `double` | `void` | `accept(double)` |

---

### Primitive predicates

| Interface | Parameter | Return | Method |
|---|---|---|---|
| `IntPredicate` | `int` | `boolean` | `test(int)` |
| `LongPredicate` | `long` | `boolean` | `test(long)` |
| `DoublePredicate` | `double` | `boolean` | `test(double)` |

Example:

```java
DoublePredicate positive = d -> d > 0;
```

Do not confuse:

```java
Predicate<Double>
```

with:

```java
DoublePredicate
```

The former works with boxed `Double`.

The latter works directly with primitive `double`.

---

### Primitive functions

Primitive input → object output:

| Interface | Parameter | Return | Method |
|---|---|---|---|
| `IntFunction<R>` | `int` | `R` | `apply(int)` |
| `LongFunction<R>` | `long` | `R` | `apply(long)` |
| `DoubleFunction<R>` | `double` | `R` | `apply(double)` |

Object input → primitive output:

| Interface | Parameter | Return | Method |
|---|---|---|---|
| `ToIntFunction<T>` | `T` | `int` | `applyAsInt(T)` |
| `ToLongFunction<T>` | `T` | `long` | `applyAsLong(T)` |
| `ToDoubleFunction<T>` | `T` | `double` | `applyAsDouble(T)` |

The name tells you the direction:

```text
IntFunction<R>
int → R

ToIntFunction<T>
T → int
```

---

### Primitive-to-primitive functions

| Interface | Conversion | Method |
|---|---|---|
| `IntToLongFunction` | `int → long` | `applyAsLong()` |
| `IntToDoubleFunction` | `int → double` | `applyAsDouble()` |
| `LongToIntFunction` | `long → int` | `applyAsInt()` |
| `LongToDoubleFunction` | `long → double` | `applyAsDouble()` |
| `DoubleToIntFunction` | `double → int` | `applyAsInt()` |
| `DoubleToLongFunction` | `double → long` | `applyAsLong()` |

The interface name describes the entire conversion:

```text
IntToDoubleFunction
INT → DOUBLE
```

---

### Primitive operators

| Interface | Parameters | Return | Method |
|---|---|---|---|
| `IntUnaryOperator` | `int` | `int` | `applyAsInt()` |
| `LongUnaryOperator` | `long` | `long` | `applyAsLong()` |
| `DoubleUnaryOperator` | `double` | `double` | `applyAsDouble()` |
| `IntBinaryOperator` | `int, int` | `int` | `applyAsInt()` |
| `LongBinaryOperator` | `long, long` | `long` | `applyAsLong()` |
| `DoubleBinaryOperator` | `double, double` | `double` | `applyAsDouble()` |

---

### Object + primitive consumers

| Interface | Parameters | Return | Method |
|---|---|---|---|
| `ObjIntConsumer<T>` | `T, int` | `void` | `accept()` |
| `ObjLongConsumer<T>` | `T, long` | `void` | `accept()` |
| `ObjDoubleConsumer<T>` | `T, double` | `void` | `accept()` |

### Primitive naming rule

The names usually tell you the types without needing to memorise every interface individually:

```text
IntXxx
    primitive int input

ToIntXxx
    primitive int output

IntToDoubleXxx
    int input → double output

ObjIntConsumer<T>
    T + int → void
```

Also remember:

```text
Primitive RETURN
→ often applyAsInt / applyAsLong / applyAsDouble

Primitive INPUT alone
→ does not imply "As"
```

Compare:

```java
IntFunction<String>       // apply(int)
ToIntFunction<String>     // applyAsInt(String)

IntConsumer               // accept(int)
IntPredicate              // test(int)
IntUnaryOperator          // applyAsInt(int)
```

---

## Predicate Convenience Methods

`Predicate<T>` provides:

```java
and()
or()
negate()
```

Example:

```java
Predicate<Integer> positive = x -> x > 0;
Predicate<Integer> even = x -> x % 2 == 0;

Predicate<Integer> positiveAndEven =
        positive.and(even);

positiveAndEven.test(4);      // true
```

### `and()`

```java
positive.and(even)
```

Think:

```text
positive && even
```

### `or()`

```java
positive.or(even)
```

Think:

```text
positive || even
```

### `negate()`

```java
positive.negate()
```

Think:

```text
!positive
```

Example:

```java
positive.negate().test(-5);   // true
```

### `Predicate.not()`

`Predicate` also provides the static method:

```java
Predicate.not(...)
```

This is particularly useful with method references:

```java
List<String> words =
        List.of("cat", "", "dog");

words.stream()
     .filter(Predicate.not(String::isEmpty))
     .forEach(System.out::println);
```

Output:

```text
cat
dog
```

### Memory

```text
Predicate
    and     &&
    or      ||
    negate  !

Static:
    Predicate.not(...)
```

`BiPredicate` provides the same:

```text
and()
or()
negate()
```

---

## Function Convenience Methods

The two important methods are:

```java
andThen()
compose()
```

The direction is the important part.

### `andThen()`

```java
f.andThen(g)
```

means:

```text
f → g
```

Run `f` first, **then** `g`.

```java
Function<Integer, Integer> doubleIt =
        x -> x * 2;

Function<Integer, Integer> addThree =
        x -> x + 3;

int result =
        doubleIt.andThen(addThree).apply(5);
```

Execution:

```text
5
↓ doubleIt
10
↓ addThree
13
```

Result:

```text
13
```

### `compose()`

```java
f.compose(g)
```

means:

```text
g → f
```

The supplied function runs **before** `f`.

```java
int result =
        doubleIt.compose(addThree).apply(5);
```

Execution:

```text
5
↓ addThree
8
↓ doubleIt
16
```

Result:

```text
16
```

### Memory rule

```text
f.andThen(g)
f → g

f.compose(g)
g → f
```

Or:

```text
andThen = ME → THEM
compose = THEM → ME
```

---

### `Function.identity()`

`Function` also provides:

```java
Function.identity()
```

It returns its input unchanged.

```java
Function<String, String> same =
        Function.identity();

same.apply("hello");
```

Result:

```text
hello
```

Think:

```text
x → x
```

---

### `BiFunction`

`BiFunction` provides:

```java
andThen()
```

Example:

```java
BiFunction<Integer, Integer, Integer> add =
        (a, b) -> a + b;

Function<Integer, String> toText =
        x -> "Result: " + x;

String result =
        add.andThen(toText).apply(2, 3);
```

Result:

```text
Result: 5
```

`BiFunction` does **not** provide `compose()`.

---

### Operators

`UnaryOperator<T>` extends `Function<T,T>`, so the normal `Function` chaining methods are available.

`BinaryOperator<T>` extends `BiFunction<T,T,T>`, so `BiFunction` behaviour applies.

---

## Consumer Convenience Methods

`Consumer<T>` provides:

```java
andThen()
```

Both consumers receive the same value and execute in order.

```java
Consumer<String> first =
        s -> System.out.print("A" + s);

Consumer<String> second =
        s -> System.out.print("B" + s);

first.andThen(second).accept("X");
```

Output:

```text
AXBX
```

Execution is:

```text
first("X")
then
second("X")
```

`BiConsumer` also provides:

```java
andThen()
```

with both consumers receiving the same two arguments.

---

## Variable Capture

Lambdas can access variables from their surrounding scope, but local variables have additional restrictions.

| Variable | Can Lambda Access It? |
|---|---|
| Instance field | ✅ Yes |
| Static field | ✅ Yes |
| Local variable | ✅ If `final` or effectively final |
| Method parameter | ✅ If `final` or effectively final |
| Lambda parameter | ✅ Yes |

### Effectively final

A variable is effectively final when it is assigned once and never reassigned.

Valid:

```java
int number = 10;

Runnable r =
        () -> System.out.println(number);
```

Invalid:

```java
int number = 10;

Runnable r =
        () -> System.out.println(number);

number = 20;       // DOES NOT COMPILE
```

The later assignment means `number` is not effectively final.

### Object mutation is different from variable reassignment

This is valid:

```java
List<String> list = new ArrayList<>();

Consumer<String> c =
        s -> list.add(s);
```

The variable `list` is not reassigned.

The object referenced by `list` is being mutated.

This is different:

```java
List<String> list = new ArrayList<>();

Consumer<String> c =
        s -> System.out.println(list);

list = new ArrayList<>();      // DOES NOT COMPILE
```

The captured local variable itself is reassigned.

### Fields

Instance and static fields do not have the effectively-final restriction:

```java
class Example {

    private int count;

    void run() {
        Runnable r = () -> count++;
    }
}
```

This is valid.

Memory:

```text
LOCAL / METHOD PARAMETER
→ final or effectively final

INSTANCE / STATIC FIELD
→ may change
```

---

## Method References

A method reference is shorthand for a lambda when an existing method already performs the required operation.

### Static method

```java
Function<String, Integer> f =
        Integer::parseInt;
```

Equivalent to:

```java
s -> Integer.parseInt(s)
```

Pattern:

```text
ClassName::staticMethod
```

---

### Instance method on a particular object

```java
Consumer<String> c =
        System.out::println;
```

Equivalent to:

```java
s -> System.out.println(s)
```

Pattern:

```text
object::instanceMethod
```

---

### Instance method on an arbitrary object

```java
Function<String, String> f =
        String::trim;
```

Equivalent to:

```java
s -> s.trim()
```

Pattern:

```text
ClassName::instanceMethod
```

The first lambda parameter becomes the object on which the method is invoked.

For two parameters:

```java
BiPredicate<String, String> p =
        String::startsWith;
```

Conceptually:

```java
(s, prefix) -> s.startsWith(prefix)
```

---

### Constructor reference

```java
Supplier<ArrayList<String>> supplier =
        ArrayList::new;
```

Equivalent to:

```java
() -> new ArrayList<String>()
```

Pattern:

```text
ClassName::new
```

The target functional interface determines which constructor signature is required.

---

## Quick Reference

### Core interfaces

| Interface | Shape | SAM |
|---|---|---|
| `Supplier<T>` | `() → T` | `get()` |
| `Consumer<T>` | `T → void` | `accept()` |
| `BiConsumer<T,U>` | `(T,U) → void` | `accept()` |
| `Predicate<T>` | `T → boolean` | `test()` |
| `BiPredicate<T,U>` | `(T,U) → boolean` | `test()` |
| `Function<T,R>` | `T → R` | `apply()` |
| `BiFunction<T,U,R>` | `(T,U) → R` | `apply()` |
| `UnaryOperator<T>` | `T → T` | `apply()` |
| `BinaryOperator<T>` | `(T,T) → T` | `apply()` |

### Convenience methods

| Interface | Methods |
|---|---|
| `Predicate` | `and()`, `or()`, `negate()` |
| `BiPredicate` | `and()`, `or()`, `negate()` |
| `Function` | `andThen()`, `compose()` |
| `BiFunction` | `andThen()` |
| `Consumer` | `andThen()` |
| `BiConsumer` | `andThen()` |

Static helpers:

```text
Predicate.not(...)
Function.identity()
```

### Function chaining

```text
f.andThen(g)
    f → g

f.compose(g)
    g → f
```

### Primitive naming

```text
IntFunction<R>
    int → R

ToIntFunction<T>
    T → int

IntToDoubleFunction
    int → double

ObjIntConsumer<T>
    (T, int) → void
```

### Variable capture

```text
FIELDS
→ can change

CAPTURED LOCALS / METHOD PARAMETERS
→ final or effectively final
```

### Method references

```text
Class::staticMethod

object::instanceMethod

Class::instanceMethod

Class::new
```

---

## Final Memory Kicks

```text
LAMBDA BODY
→ expression OR block
```

```text
Supplier  → GET
Consumer  → ACCEPT
Predicate → TEST
Function  → APPLY
Operator  → APPLY
```

```text
Supplier
() → T

Consumer
T → void

Predicate
T → boolean

Function
T → R

Operator
T → T
```

```text
Predicate
AND / OR / NEGATE

Function
AND THEN / COMPOSE

Consumer
AND THEN
```

```text
f.andThen(g)
f → g

f.compose(g)
g → f
```

```text
IntXxx
→ int input

ToIntXxx
→ int output

IntToDoubleXxx
→ int → double
```

```text
Primitive output
→ often applyAsInt / applyAsLong / applyAsDouble
```

```text
Captured local
→ final or effectively final

Captured object
→ object may still be mutated
```

```text
Method reference:
Class::static
object::instance
Class::instance
Class::new
```