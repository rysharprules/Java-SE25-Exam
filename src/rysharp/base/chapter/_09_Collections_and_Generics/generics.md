# Generics

[🔙 Back](README.md)

Generics provide compile-time type safety while allowing classes, interfaces and methods to work with different reference types.

## Contents

- [Why Generics Exist](#why-generics-exist)
- [Generic Classes](#generic-classes)
- [Generic Interfaces](#generic-interfaces)
- [Generic Methods](#generic-methods)
- [Type Parameter Naming](#type-parameter-naming)
- [Bounded Type Parameters](#bounded-type-parameters)
- [Generic Invariance](#generic-invariance)
- [Wildcards](#wildcards)
- [Unbounded Wildcards](#unbounded-wildcards)
- [Upper-Bounded Wildcards](#upper-bounded-wildcards)
- [Lower-Bounded Wildcards](#lower-bounded-wildcards)
- [PECS](#pecs)
- [Wildcard Add and Read Rules](#wildcard-add-and-read-rules)
- [Type Erasure](#type-erasure)
- [Generic Restrictions](#generic-restrictions)
- [Raw Types](#raw-types)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Why Generics Exist

Without generics, values often have to be handled as `Object`:

```java
List list = new ArrayList();

list.add("Hello");

String value = (String) list.get(0);
```

This requires a cast and allows incorrect types to be inserted.

Generics move much of this checking to compile time:

```java
List<String> list = new ArrayList<>();

list.add("Hello");

String value = list.get(0);
```

This does not compile:

```java
list.add(123); // DOES NOT COMPILE
```

Generics therefore provide:

```text
TYPE SAFETY
+
FEWER EXPLICIT CASTS
```

---

## Generic Classes

A class can declare one or more type parameters.

```java
class Box<T> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
```

The type is supplied when the class is used:

```java
Box<String> stringBox = new Box<>();

stringBox.set("Hello");

String value = stringBox.get();
```

Another instance can use a completely different type:

```java
Box<Integer> numberBox = new Box<>();

numberBox.set(42);

Integer value = numberBox.get();
```

`T` represents a type that is determined when the generic class is used.

---

### Multiple Type Parameters

A class can declare multiple type parameters:

```java
class Pair<K, V> {

    private K key;
    private V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    K getKey() {
        return key;
    }

    V getValue() {
        return value;
    }
}
```

Usage:

```java
Pair<String, Integer> pair =
        new Pair<>("Age", 42);

String key = pair.getKey();
Integer value = pair.getValue();
```

---

## Generic Interfaces

Interfaces can also declare type parameters.

```java
interface Processor<T> {

    T process(T value);
}
```

An implementation can specify the type:

```java
class StringProcessor
        implements Processor<String> {

    @Override
    public String process(String value) {
        return value.toUpperCase();
    }
}
```

Or the implementing class can remain generic:

```java
class IdentityProcessor<T>
        implements Processor<T> {

    @Override
    public T process(T value) {
        return value;
    }
}
```

---

## Generic Methods

A method can declare its own type parameter independently of whether the
containing class is generic.

The type parameter appears **before the return type**:

```java
public static <T> T first(List<T> list) {
    return list.get(0);
}
```

Usage:

```java
List<String> names =
        List.of("Alice", "Bob");

String first = first(names);
```

Java normally infers `T` from the arguments.

---

### Generic Method Syntax

The important placement is:

```text
modifiers <TYPE PARAMETERS> return-type method(...)
```

For example:

```java
public static <T> void print(T value) {
    System.out.println(value);
}
```

Not:

```java
public static void <T> print(T value) { } // DOES NOT COMPILE
```

For an instance method:

```java
public <T> T echo(T value) {
    return value;
}
```

---

### Explicit Type Arguments

The caller can explicitly provide the generic method's type argument:

```java
GenericsExample.<String>echo("Hello");
```

Usually this is unnecessary because Java performs type inference.

---

## Type Parameter Naming

Common conventions are:

```text
T → Type
E → Element
K → Key
V → Value
N → Number
R → Result
```

These are conventions, not special Java keywords.

This is legal:

```java
class Box<Anything> {
    private Anything value;
}
```

but conventional names make generic code easier to recognise.

---

## Bounded Type Parameters

A type parameter can be restricted using `extends`.

```java
class NumberBox<T extends Number> {

    private T value;

    NumberBox(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }
}
```

These are valid:

```java
new NumberBox<Integer>(10);
new NumberBox<Double>(2.5);
```

This is not:

```java
new NumberBox<String>("Hello"); // DOES NOT COMPILE
```

because `String` does not extend `Number`.

---

### Multiple Bounds

A type parameter can have multiple bounds:

```java
<T extends Number & Comparable<T>>
```

The class bound, if present, must come **first**.

```java
<T extends Number & Comparable<T>> // valid
```

Not:

```java
<T extends Comparable<T> & Number> // DOES NOT COMPILE
```

Only one class can appear in the bounds, but multiple interfaces can follow:

```java
<T extends SomeClass & InterfaceA & InterfaceB>
```

---

### `extends` Includes Interfaces

Generic bounds always use the keyword `extends`.

Even if the bound is an interface:

```java
<T extends Comparable<T>>
```

Not:

```java
<T implements Comparable<T>> // DOES NOT COMPILE
```

Memory rule:

```text
GENERIC TYPE BOUND
→ always extends
→ class OR interface
```

---

## Generic Invariance

This is one of the most important generic rules.

Suppose:

```java
Integer extends Number
```

It does **not** follow that:

```text
List<Integer> extends List<Number>
```

Therefore:

```java
List<Integer> integers =
        new ArrayList<>();

List<Number> numbers = integers; // DOES NOT COMPILE
```

Generic types are normally **invariant**.

Why?

If this assignment were allowed:

```java
List<Integer> integers =
        new ArrayList<>();

List<Number> numbers = integers;
```

then this would also have to be allowed:

```java
numbers.add(3.14);
```

But the original object is supposed to contain only `Integer`.

Therefore Java prevents the assignment.

Memory:

```text
Integer IS-A Number

BUT

List<Integer> IS-NOT-A List<Number>
```

Wildcards provide controlled flexibility when different generic types need to
be accepted.

---

## Wildcards

A wildcard is represented by:

```java
?
```

There are three important forms:

```text
<?>            unbounded

<? extends T>  upper bounded

<? super T>    lower bounded
```

They answer different questions about what generic types a variable or method
can accept.

---

## Unbounded Wildcards

```java
List<?>
```

means:

> A `List` of some particular type, but that type is unknown here.

For example:

```java
static void printList(List<?> list) {

    for (Object value : list) {
        System.out.println(value);
    }
}
```

It can accept:

```java
List<String>
List<Integer>
List<Double>
```

because the method does not need to know the element type.

---

### Reading from `List<?>`

The exact element type is unknown.

Therefore the safe type when reading is `Object`:

```java
Object value = list.get(0);
```

This is not generally safe:

```java
String value = list.get(0); // DOES NOT COMPILE
```

---

### Adding to `List<?>`

You cannot safely add a normal object:

```java
List<?> list = new ArrayList<String>();

list.add("Hello"); // DOES NOT COMPILE
list.add(123);     // DOES NOT COMPILE
```

Why?

The compiler only knows that the list contains **some unknown type**.

It could actually be:

```text
List<String>
List<Integer>
List<Double>
```

There is no non-null object that is guaranteed to be valid for every
possible element type.

`null` is the exception:

```java
list.add(null); // permitted if the underlying list supports it
```

Memory:

```text
List<?>

READ → Object

ADD → nothing except null
```

---

## Upper-Bounded Wildcards

An upper-bounded wildcard uses:

```java
? extends Type
```

For example:

```java
List<? extends Number>
```

This means:

> A list whose element type is `Number` or some subtype of `Number`.

Therefore these can be assigned:

```java
List<Integer> integers =
        new ArrayList<>();

List<Double> doubles =
        new ArrayList<>();

List<Number> numbers =
        new ArrayList<>();

List<? extends Number> a = integers;
List<? extends Number> b = doubles;
List<? extends Number> c = numbers;
```

---

### Reading from `? extends`

You know every element must be at least a `Number`.

Therefore:

```java
List<? extends Number> list =
        List.of(10, 20, 30);

Number number = list.get(0);
```

This is safe.

You cannot assume a more specific subtype:

```java
Integer value = list.get(0); // DOES NOT COMPILE
```

because the list could actually be a:

```text
List<Double>
```

---

### Adding to `? extends`

You cannot safely add a `Number`:

```java
List<? extends Number> list =
        new ArrayList<Integer>();

list.add(10);   // DOES NOT COMPILE
list.add(2.5);  // DOES NOT COMPILE
```

The compiler does not know the exact subtype.

For example, if the actual list were:

```java
List<Double>
```

adding an `Integer` would be unsafe.

Again, `null` is the only generally permitted value:

```java
list.add(null);
```

provided the underlying collection supports `null`.

Memory:

```text
? extends Number

READ → Number

ADD → nothing except null
```

This is why upper-bounded wildcards are useful when a method primarily
**reads** values.

---

## Lower-Bounded Wildcards

A lower-bounded wildcard uses:

```java
? super Type
```

For example:

```java
List<? super Integer>
```

This can refer to lists whose element type is:

```text
Integer
Number
Object
```

For example:

```java
List<Integer> integers =
        new ArrayList<>();

List<Number> numbers =
        new ArrayList<>();

List<Object> objects =
        new ArrayList<>();

List<? super Integer> a = integers;
List<? super Integer> b = numbers;
List<? super Integer> c = objects;
```

This is not allowed:

```java
List<Double> doubles =
        new ArrayList<>();

List<? super Integer> x = doubles; // DOES NOT COMPILE
```

`Double` is not a supertype of `Integer`.

---

### Adding to `? super`

An `Integer` can safely be added:

```java
List<? super Integer> list =
        new ArrayList<Number>();

list.add(10);
list.add(20);
```

Why?

Whatever the actual list type is, it must be capable of storing an
`Integer`.

It could be:

```text
List<Integer>
List<Number>
List<Object>
```

and all three can contain an `Integer`.

---

### Reading from `? super`

When reading, however, the compiler does not know the precise element type.

```java
Object value = list.get(0);
```

`Object` is the only type guaranteed to be safe.

This does not compile:

```java
Integer value = list.get(0); // DOES NOT COMPILE
```

The underlying list could be:

```java
List<Object>
```

and could contain values that are not `Integer`.

Memory:

```text
? super Integer

ADD  → Integer allowed

READ → Object
```

---

## PECS

A useful rule for choosing wildcard bounds is:

```text
PECS

Producer Extends
Consumer Super
```

### Producer Extends

If a structure **produces values for your code to read**, use:

```java
? extends T
```

Example:

```java
static double total(
        List<? extends Number> numbers) {

    double result = 0;

    for (Number number : numbers) {
        result += number.doubleValue();
    }

    return result;
}
```

The list produces `Number` values.

It can accept:

```text
List<Integer>
List<Double>
List<Number>
```

---

### Consumer Super

If a structure **consumes values that your code adds**, use:

```java
? super T
```

Example:

```java
static void addNumbers(
        List<? super Integer> list) {

    list.add(10);
    list.add(20);
}
```

The list consumes `Integer` values.

It can accept:

```text
List<Integer>
List<Number>
List<Object>
```

Memory:

```text
PRODUCER → EXTENDS → READ

CONSUMER → SUPER   → ADD
```

---

## Wildcard Add and Read Rules

This table is worth memorising.

| Declaration | Can Read As | Can Add |
|---|---|---|
| `List<?>` | `Object` | only `null` |
| `List<? extends Number>` | `Number` | only `null` |
| `List<? super Integer>` | `Object` | `Integer` and its subtypes |
| `List<Number>` | `Number` | `Number` and its subtypes |

For:

```java
List<? extends Number>
```

think:

```text
I know what comes OUT:
Number

I don't know exact type to safely put IN.
```

For:

```java
List<? super Integer>
```

think:

```text
I know what can go IN:
Integer

I don't know exactly what comes OUT:
Object
```

---

## Type Erasure

Java implements generics primarily through **type erasure**.

Generic type information is used by the compiler for type checking, but much
of that information is not available as distinct runtime types.

For example:

```java
List<String> strings =
        new ArrayList<>();

List<Integer> integers =
        new ArrayList<>();
```

At runtime, these are not two separate classes such as:

```text
ArrayListOfString
ArrayListOfInteger
```

They are both based on `ArrayList`.

This explains several restrictions on generics.

---

## Generic Restrictions

### Primitive Type Arguments Are Not Allowed

Generic type arguments must be reference types.

This does not compile:

```java
List<int> numbers; // DOES NOT COMPILE
```

Use the wrapper type:

```java
List<Integer> numbers;
```

---

### Cannot Instantiate a Type Parameter

This does not compile:

```java
class Box<T> {

    T create() {
        return new T(); // DOES NOT COMPILE
    }
}
```

The runtime does not know which constructor should be invoked for `T`.

---

### Cannot Create Generic Arrays Directly

This does not compile:

```java
List<String>[] lists =
        new List<String>[10]; // DOES NOT COMPILE
```

Likewise:

```java
class Box<T> {

    T[] values = new T[10]; // DOES NOT COMPILE
}
```

Arrays are reified at runtime, while generic type arguments are erased.

---

### Cannot Use a Type Parameter in a Static Context

A class type parameter belongs to an **instance type**, not the class itself.

```java
class Box<T> {

    static T value; // DOES NOT COMPILE
}
```

Why?

Different instances could use different type arguments:

```java
Box<String>
Box<Integer>
```

but there is only one static field shared by the class.

A static method can instead declare **its own** type parameter:

```java
class Box<T> {

    static <U> U echo(U value) {
        return value;
    }
}
```

`U` belongs to the method rather than to `Box<T>`.

---

### `instanceof` and Parameterized Types

Because generic type information is erased, this is not allowed:

```java
if (value instanceof List<String>) { } // DOES NOT COMPILE
```

The runtime cannot distinguish:

```text
List<String>
List<Integer>
```

in that way.

An unbounded wildcard can be used:

```java
if (value instanceof List<?>) {
    System.out.println("It is a List");
}
```

---

### Cannot Overload Only by Generic Type Argument

These methods cannot coexist:

```java
void process(List<String> list) { }

void process(List<Integer> list) { } // DOES NOT COMPILE
```

After erasure, both effectively have the same parameter type:

```text
process(List)
```

Therefore they have the same erased signature.

---

### Generic Exceptions

A generic class cannot directly extend `Throwable`.

For example:

```java
class Problem<T> extends Exception { } // DOES NOT COMPILE
```

This prevents generic exception types such as:

```text
Problem<String>
Problem<Integer>
```

from being used as distinct exception types at runtime.

---

## Raw Types

A generic type used without its type argument is called a **raw type**.

For example:

```java
List list = new ArrayList();
```

instead of:

```java
List<String> list =
        new ArrayList<>();
```

Raw types exist primarily for compatibility with code written before
generics were introduced.

They weaken compile-time type safety.

```java
List<String> strings =
        new ArrayList<>();

List raw = strings;

raw.add(123);
```

This can compile with an unchecked warning.

The problem may only become visible later:

```java
String value = strings.get(0);
```

which can result in a:

```text
ClassCastException
```

Memory:

```text
RAW TYPE
→ generics safety bypassed
→ warnings now
→ possible runtime failure later
```

Do not treat an unchecked warning as the same thing as a compilation error.

---

## Quick Reference

### Generic Class

```java
class Box<T> {
    T value;
}
```

Usage:

```java
Box<String> box =
        new Box<>();
```

---

### Generic Method

```java
static <T> T echo(T value) {
    return value;
}
```

Remember:

```text
<T> BEFORE return type
```

---

### Bounds

```java
<T extends Number>
```

Multiple:

```java
<T extends SomeClass & InterfaceA & InterfaceB>
```

Rules:

```text
extends used for class AND interface bounds

class must be first

only one class bound
```

---

### Invariance

```text
Integer IS-A Number

BUT

List<Integer> IS-NOT-A List<Number>
```

---

### Wildcards

```text
<?>            unknown type

<? extends T>  T or subtype

<? super T>    T or supertype
```

---

### Wildcard Operations

```text
List<?>

READ → Object
ADD  → only null
```

```text
List<? extends Number>

READ → Number
ADD  → only null
```

```text
List<? super Integer>

READ → Object
ADD  → Integer
```

---

### PECS

```text
Producer Extends
Consumer Super
```

Or:

```text
extends → good for READING

super   → good for ADDING
```

---

### Generic Restrictions

```text
NO primitive type arguments

NO new T()

NO new T[]

NO new List<String>[]

NO class T in static context

NO instanceof List<String>

NO overloads differing only by erased generic type

NO generic Throwable subclass
```

---

### Raw Types

```java
List list = new ArrayList();
```

means:

```text
type safety weakened
unchecked warnings possible
runtime ClassCastException possible
```

---

## Final Memory Kicks

```text
GENERICS
→ compile-time type safety
→ reference types only


GENERIC METHOD:

<T> comes BEFORE return type

static <T> T echo(T value)


BOUND:

<T extends Number>

extends is used for:
→ class bounds
→ interface bounds


MULTIPLE BOUNDS:

<T extends Class & Interface & Interface>

CLASS FIRST


INVARIANCE:

Integer IS-A Number

List<Integer>
IS NOT
List<Number>


WILDCARDS:

<?>            → unknown

<? extends T>  → T or subtype

<? super T>    → T or supertype


EXTENDS:

READ as T
ADD nothing except null


SUPER:

ADD T
READ as Object


PECS:

Producer Extends
Consumer Super


MAP THE DIRECTION:

? extends Number
→ Integer, Double etc. can be underneath it

? super Integer
→ Integer, Number, Object can be underneath it


TYPE ERASURE
→ generic arguments largely disappear at runtime


NO:

List<int>

new T()

new T[]

new List<String>[10]

static T field

instanceof List<String>

overloading only by generic argument


RAW TYPES:

List list

→ compiles with reduced type safety
→ unchecked warnings
→ possible runtime ClassCastException
```