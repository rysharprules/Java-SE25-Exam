# Primitive Casting & Numeric Promotion

Quick reference for primitive conversions, numeric promotion, casts, literals and compound assignment.

## Contents

- [Primitive Numeric Types](#primitive-numeric-types)
- [Widening](#widening)
- [Narrowing and Casting](#narrowing-and-casting)
- [Numeric Promotion](#numeric-promotion)
- [Compile-Time Constant Expressions](#compile-time-constant-expressions)
- [Cast Placement](#cast-placement)
- [Numeric Literals](#numeric-literals)
- [Compound Assignment](#compound-assignment)
- [Increment and Decrement](#increment-and-decrement)
- [Quick Reference](#quick-reference)

---

## Primitive Numeric Types

A useful widening order is:

```text
byte → short → int → long → float → double
                ↑
              char
```

`char` is an unsigned integral type and can widen to `int` and beyond.

The important general direction is:

```text
smaller → larger   → widening
larger → smaller   → narrowing
```

---

## Widening

A smaller numeric type can normally be converted to a larger compatible type automatically.

```java
byte b = 10;
short s = b;
int i = s;
long l = i;
float f = l;
double d = f;
```

No explicit cast is required.

Examples:

```java
int i = 10;
long l = i;       // OK

long x = 10L;
double d = x;     // OK
```

Memory:

> **Widening primitive conversions normally do not require a cast.**

---

## Narrowing and Casting

Converting to a smaller numeric type normally requires an explicit cast.

```java
long l = 100;

int i = (int) l;
short s = (short) i;
byte b = (byte) s;
```

Without the cast:

```java
long l = 100;

int i = l;   // DOES NOT COMPILE
```

A cast uses the target type before the expression:

```java
int x = (int) 5.9;
```

Not:

```java
long x = 10(long);   // DOES NOT COMPILE
```

### Floating-Point to Integral

Converting floating-point values to integral types is narrowing:

```java
double d = 10.9;

int x = d;        // DOES NOT COMPILE
int y = (int) d;  // 10
```

The fractional part is **truncated toward zero**, not rounded:

```java
(int) 9.99     // 9
(int) -9.99    // -9
```

### Overflow When Narrowing

An explicit cast can compile even when the value doesn't fit in the target integral type:

```java
int x = 130;

byte b = (byte) x;
```

This compiles even though `130` is outside the range of `byte`.

The conversion produces the value determined by the target type's range; the cast does not perform a safety check.

Therefore these are separate questions:

```text
Does it compile?

What value does the conversion produce?
```

Memory:

> **A cast requests a conversion; it does not guarantee that the value fits safely.**

---

## Numeric Promotion

Java promotes numeric operands to a common type when performing arithmetic.

### `byte`, `short` and `char`

These normally promote to at least `int` during arithmetic.

```java
short a = 10;
short b = 20;

int result = a + b;       // OK
short result2 = a + b;    // DOES NOT COMPILE
```

Conceptually:

```text
short + short
      ↓
 int  + int
      ↓
     int
```

To store the result in a `short`:

```java
short result = (short) (a + b);
```

### Binary Numeric Promotion

For ordinary numeric arithmetic, use:

```text
double wins
   ↓
float
   ↓
long
   ↓
int
```

Examples:

```text
byte  + byte    → int
short + short   → int
char  + char    → int

int   + long    → long
long  + float   → float
float + double  → double
```

For example:

```java
int x = 5;
long y = 10;

long a = x + y;        // OK
int b = x + y;         // DOES NOT COMPILE
int c = (int) (x + y); // OK
```

The expression:

```java
x + y
```

has type `long`.

Only after determining that should you consider whether it can be assigned to the target variable.

Memory:

> **Evaluate the RHS type after promotion, then check assignment compatibility.**

---

## Compile-Time Constant Expressions

There is an important exception to the normal narrowing rules when assigning an integral compile-time constant expression to:

```text
byte
short
char
```

### Constant Expression That Fits

```java
byte gloves = 7 * 10;
```

`7 * 10` still undergoes normal numeric promotion and has type:

```text
int
```

But it is also a **compile-time constant expression**.

The compiler knows:

```text
7 * 10 = 70
```

Since `70` fits in a `byte`, the assignment is allowed.

Examples:

```java
byte a = 1;          // OK
byte b = 7 * 10;     // OK
short c = 2 + 1;     // OK
char d = 65;         // OK
```

### Constant That Does Not Fit

```java
byte x = 7 * 100;    // DOES NOT COMPILE
```

The compiler knows:

```text
7 * 100 = 700
```

and `700` does not fit in a `byte`.

Memory:

> **A compile-time constant integral expression may narrow automatically to `byte`, `short` or `char` if its value fits.**

Do **not** conclude that literals avoid numeric promotion.

They don't.

The special rule is about **constant-expression assignment conversion**.

### Ordinary Variables

Compare:

```java
short x = 2 + 1;   // OK
```

with:

```java
byte hat = 1;

short x = 2 + hat;   // DOES NOT COMPILE
```

`2 + hat` performs numeric promotion:

```text
int + byte
    ↓
int + int
    ↓
   int
```

`hat` is an ordinary variable, so the expression is not a compile-time constant expression that qualifies for the narrowing rule.

This works:

```java
short x = (short) (2 + hat);
```

### `final` Constant Variables

The distinction is not simply:

```text
literal vs variable
```

It is:

```text
compile-time constant expression
            vs
non-constant expression
```

A suitable `final` primitive initialized with a constant expression can itself be a constant variable:

```java
final int x = 10;

byte a = x;       // OK
byte b = x + 5;   // OK
```

Compare:

```java
int x = 10;

byte a = x;       // DOES NOT COMPILE
byte b = x + 5;   // DOES NOT COMPILE
```

Even though the current value is obviously `10`, an ordinary variable is not a constant variable.

Memory:

```text
compile-time integral constant
        +
value fits
        ↓
implicit narrowing to
byte / short / char
may be allowed
```

---

## Cast Placement

A cast applies only to its operand.

Parentheses therefore matter.

### Casting the Whole Expression

```java
short mouse = 10;
short hamster = 3;

short result = (short) (mouse * hamster);   // OK
```

First:

```text
mouse * hamster
short * short
      ↓
     int
```

Then:

```text
int
 ↓ cast
short
```

### Casting Only One Operand

Compare:

```java
short result = (short) mouse * hamster;   // DOES NOT COMPILE
```

The cast applies only to `mouse`:

```text
(short) mouse
      ↓
    short

short * short
      ↓
     int
```

So these are different:

```java
(short) (mouse * hamster)   // cast whole result
```

```java
(short) mouse * hamster     // cast only mouse
```

### Later Operations Can Promote Again

A cast does not permanently determine the type of everything that follows.

```java
short mouse = 10;
short hamster = 3;

short result =
        1 + (short) (mouse * hamster);   // DOES NOT COMPILE
```

The inner multiplication is narrowed:

```text
mouse * hamster
      ↓
     int
      ↓ cast
    short
```

But then:

```text
1 + short
    ↓
int + int
    ↓
   int
```

The final result is `int`.

Memory:

> **After every arithmetic operation, reconsider numeric promotion.**

---

## Numeric Literals

Literal type can affect whether an expression compiles.

### Integer Literals

An integer literal without a suffix is normally an `int`.

```java
int x = 100;
long y = 100;   // int literal widened to long
```

For an integer literal too large for `int`, use `L`:

```java
long value = 192301398193810323L;
```

Without it:

```java
long value = 192301398193810323;   // DOES NOT COMPILE
```

The literal itself is invalid as an `int`.

A cast cannot rescue an invalid literal:

```java
long value = (long) 192301398193810323;   // DOES NOT COMPILE
```

Prefer uppercase:

```java
100L
```

rather than:

```java
100l
```

because lowercase `l` resembles `1`.

### Floating-Point Literals

Floating-point literals are `double` by default:

```java
double d = 2.0;   // OK

float f = 2.0;    // DOES NOT COMPILE
```

Use `F` or `f` for a `float` literal:

```java
float f = 2.0F;   // OK
```

Or explicitly narrow:

```java
float f = (float) 2.0;   // OK
```

Memory:

```text
integer literal        → int by default
L                       → long

floating-point literal → double by default
F                       → float
```

---

## Compound Assignment

Compound assignment performs an implicit conversion back to the type of the left-hand variable.

Consider:

```java
long goat = 10;
int sheep = 5;

sheep = sheep * goat;   // DOES NOT COMPILE
```

The multiplication produces:

```text
int * long
    ↓
   long
```

Assigning that `long` to `int` requires narrowing.

But:

```java
sheep *= goat;   // OK
```

A useful conceptual model is:

```java
sheep *= goat;
```

behaves approximately like:

```java
sheep = (int) (sheep * goat);
```

Another classic example:

```java
byte b = 10;

b = b + 1;   // DOES NOT COMPILE
b += 1;      // OK
```

Memory:

```text
x = x + y
```

and:

```text
x += y
```

are **not identical for type-conversion purposes**.

> **Compound assignment includes an implicit conversion back to the type of the left-hand variable.**

---

## Increment and Decrement

Increment and decrement work directly with smaller integral variables:

```java
byte b = 10;

b++;    // OK
++b;    // OK
b--;    // OK
--b;    // OK
```

Compare:

```java
b = b + 1;   // DOES NOT COMPILE
```

because ordinary arithmetic promotes `b` to `int`.

For prefix/postfix expression values and evaluation order, see the separate **Operator Precedence** notes.

---

# Quick Reference

## Widening and Narrowing

```text
WIDENING
smaller → larger
usually automatic

NARROWING
larger → smaller
usually requires cast
```

Useful widening order:

```text
byte → short → int → long → float → double
                ↑
              char
```

## Numeric Promotion

```text
byte / short / char
        ↓
normally promote to int
```

Mixed arithmetic:

```text
double
  ↓
float
  ↓
long
  ↓
int
```

Examples:

```text
short + short   → int
int + long      → long
long + float    → float
float + double  → double
```

## Constant Expressions

```java
byte a = 7 * 10;       // OK
byte b = 7 * 100;      // DOES NOT COMPILE

final int x = 10;
byte c = x + 5;        // OK

int y = 10;
byte d = y + 5;        // DOES NOT COMPILE
```

Rule:

```text
compile-time integral constant
+
fits in byte / short / char
↓
implicit narrowing may be allowed
```

## Cast Placement

```java
(short) (a + b)   // cast result of expression
(short) a + b     // cast only a
```

A later arithmetic operation may promote the value again.

## Literals

```text
100       → int
100L      → long

2.0       → double
2.0F      → float
```

## Compound Assignment

```java
byte b = 10;

b = b + 1;   // ✗
b += 1;      // ✓
b++;         // ✓
```

## Reliable Method

For an assignment such as:

```java
short result = ...;
```

work in this order:

```text
1. Identify operand types

2. Check whether the RHS is a compile-time
   constant integral expression

3. Apply numeric promotion

4. Determine the resulting expression type

5. Apply any casts to exactly their operands

6. Check whether the result can be assigned
   to the target type

7. Remember the special conversion performed
   by compound assignment
```

## Final Memory Kicks

> **Widening is normally automatic; narrowing normally requires a cast.**

> **`byte`, `short` and `char` normally promote to `int` during arithmetic.**

> **For mixed arithmetic: `double > float > long > int`.**

> **A compile-time constant integral expression may narrow automatically to `byte`, `short` or `char` when its value fits.**

> **A cast applies only to its operand; later arithmetic can promote the result again.**

> **Integer literals are `int` by default; floating-point literals are `double` by default.**

> **Compound assignment includes an implicit conversion back to the left-hand type.**

> **Determine the RHS type after promotion first; then check whether it can be assigned to the LHS.**