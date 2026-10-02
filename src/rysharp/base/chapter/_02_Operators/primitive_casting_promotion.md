# Primitive Casting and Numeric Promotion

## 1. Primitive Numeric Types

A useful widening order is:

```text
byte → short → int → long → float → double
                ↑
              char
```

`char` is an unsigned integral type and can widen to `int` and beyond.

---

# 2. Widening Conversions

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

```text
smaller → larger
        ↓
automatic widening
```

Examples:

```java
int i = 10;
long l = i;       // OK

long x = 10L;
double d = x;     // OK
```

> **Exam rule:** Widening primitive conversions normally do not require a cast.

---

# 3. Narrowing Conversions

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

int i = l;        // DOES NOT COMPILE
```

A cast uses the target type in parentheses:

```java
int x = (int) 5.9;
```

The cast appears **before** the expression:

```java
long x = 10(long);    // DOES NOT COMPILE
```

---

# 4. Floating-Point to Integral Types

Converting a floating-point value to an integral type requires narrowing.

```java
double d = 10.9;

int x = d;        // DOES NOT COMPILE
int y = (int) d;  // 10
```

The fractional part is **truncated**, not rounded.

```java
(int) 9.99    // 9
(int) -9.99   // -9
```

---

# 5. Numeric Promotion During Arithmetic

Java promotes numeric operands so an arithmetic operation can be performed using a common type.

For arithmetic involving:

```text
byte
short
char
```

the operands are normally promoted to at least `int`.

For example:

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

Therefore:

```java
short result = (short) (a + b);   // OK
```

---

# 6. Binary Numeric Promotion

For normal arithmetic, a useful exam rule is:

```text
If either operand is double → result uses double

Otherwise, if either is float → result uses float

Otherwise, if either is long → result uses long

Otherwise → operands are promoted to int
```

Examples:

```text
byte   + byte    → int
short  + short   → int
char   + char    → int

int    + long    → long
long   + float   → float
float  + double  → double
```

Example:

```java
int x = 5;
long y = 10;

long a = x + y;        // OK
int b = x + y;         // DOES NOT COMPILE
int c = (int) (x + y); // OK
```

### Exam technique

Work out the type of the expression **before looking at the variable receiving it**.

```text
1. Determine operand types
2. Apply numeric promotion
3. Determine expression type
4. Check whether that type can be assigned to the target
```

---

# 7. Compile-Time Constant Expressions — The Important Exception

There is an important exception when assigning constant integral expressions to:

```text
byte
short
char
```

Consider:

```java
byte gloves = 7 * 10;
```

The expression:

```java
7 * 10
```

has type `int`.

However, it is also a **compile-time constant expression**.

The compiler calculates:

```text
7 * 10 = 70
```

Since `70` fits inside a `byte`, Java allows the assignment without an explicit cast.

Therefore:

```java
byte a = 1;          // OK
byte b = 7 * 10;     // OK: constant 70 fits
short c = 2 + 1;     // OK: constant 3 fits
char d = 65;         // OK: constant fits
```

But:

```java
byte x = 7 * 100;    // DOES NOT COMPILE
```

because:

```text
7 * 100 = 700
```

and `700` does not fit inside a `byte`.

### Important

Do **not** think:

> Literals don't undergo numeric promotion.

They do.

Instead remember:

> **A compile-time constant integral expression may be implicitly narrowed to `byte`, `short`, or `char` if its value fits.**

---

# 8. Variables Usually Change the Situation

Compare:

```java
short x = 2 + 1;     // OK
```

with:

```java
byte hat = 1;

short x = 2 + hat;   // DOES NOT COMPILE
```

`2 + 1` is a compile-time constant expression.

But `hat` is an ordinary variable, so:

```text
2 + hat
    ↓
int + byte
      ↓
int + int
    ↓
   int
```

The resulting `int` cannot automatically be narrowed to `short`.

Therefore:

```java
short x = (short) (2 + hat);   // OK
```

---

# 9. `final` Constant Variables

The real distinction is not simply:

```text
literal vs variable
```

It is:

```text
compile-time constant expression
        vs
non-constant expression
```

A suitable `final` primitive variable initialized with a constant expression can itself be a **constant variable**.

For example:

```java
final int x = 10;

byte a = x;       // OK
byte b = x + 5;   // OK
```

The compiler knows the values at compile time.

Compare:

```java
int x = 10;

byte a = x;       // DOES NOT COMPILE
byte b = x + 5;   // DOES NOT COMPILE
```

Even though a human can see that `x` currently contains `10`, it is not a constant variable.

### Memory rule

> **Known compile-time integral constant + value fits → implicit narrowing to `byte`, `short`, or `char` may be allowed.**

---

# 10. Casting Applies Only to Its Operand

Casting is a unary operation.

Consider:

```java
short mouse = 10;
short hamster = 3;

short result = (short) (mouse * hamster);   // OK
```

The multiplication happens first:

```text
mouse * hamster
short * short
      ↓
     int
```

Then the resulting `int` is cast to `short`.

Compare:

```java
short result = (short) mouse * hamster;   // DOES NOT COMPILE
```

The cast applies only to `mouse`.

```text
(short) mouse
      ↓
    short

short * short
      ↓
     int
```

The multiplication promotes the values back to `int`.

Therefore:

```java
(short) (mouse * hamster)
```

and:

```java
(short) mouse * hamster
```

are **not equivalent**.

---

# 11. A Cast Does Not Protect Later Operations

Consider:

```java
short mouse = 10;
short hamster = 3;

short result =
        1 + (short) (mouse * hamster);   // DOES NOT COMPILE
```

The inner expression is successfully narrowed:

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

The final result is therefore `int`.

### Exam rule

> **After every arithmetic operation, reconsider numeric promotion.**

A previous cast does not permanently make the rest of the expression that type.

---

# 12. Integer Literal Types

An integer literal without a suffix is normally an `int`.

```java
int x = 100;
long y = 100;       // int literal widened to long
```

For a literal that cannot fit in an `int`, use `L`:

```java
long value = 192301398193810323L;
```

This does not compile:

```java
long value = 192301398193810323;
```

The literal itself is too large to be represented as an `int`.

Casting does not rescue an invalid literal:

```java
long value = (long) 192301398193810323;   // DOES NOT COMPILE
```

The literal must first be valid before the cast can be applied.

Prefer uppercase `L`:

```java
100L
```

rather than:

```java
100l
```

because lowercase `l` can look like `1`.

---

# 13. Floating-Point Literal Types

A floating-point literal is `double` by default.

```java
double d = 2.0;    // OK

float f = 2.0;     // DOES NOT COMPILE
```

Use `F` or `f` for a `float` literal:

```java
float f = 2.0F;    // OK
```

Or explicitly narrow:

```java
float f = (float) 2.0;   // OK
```

---

# 14. Casting Can Cause Overflow or Underflow

An explicit primitive cast can compile even when the value does not fit in the target integral type.

```java
int x = 130;

byte b = (byte) x;
```

This compiles, but `130` cannot be represented by a `byte`.

The value wraps according to the target type's range.

Therefore:

```text
Does it compile?
```

and:

```text
What value is produced?
```

are separate exam questions.

A cast means:

> **Perform this conversion.**

It does **not** mean:

> **Check that the value fits safely.**

---

# 15. Compound Assignment Performs an Implicit Conversion

Consider:

```java
long goat = 10;
int sheep = 5;

sheep = sheep * goat;   // DOES NOT COMPILE
```

Why?

```text
int * long
    ↓
   long
```

Then:

```text
long → int
```

is a narrowing conversion.

However:

```java
sheep *= goat;   // OK
```

Compound assignment performs the necessary conversion back to the type of the left-hand variable.

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

### Exam rule

These are **not identical for type conversion purposes**:

```java
x = x + y;
```

```java
x += y;
```

The compound assignment includes an implicit conversion back to the type of `x`.

---

# 16. Increment and Decrement

Increment and decrement also work directly with smaller integral variables:

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

because ordinary arithmetic produces an `int`.

---

# Exam Decision Process

When you see:

```java
short result = ...;
```

work through the expression systematically.

## Step 1 — Identify the operand types

```java
short a = 10;
long b = 20;
```

---

## Step 2 — Check for compile-time constants

Ask:

```text
Is this a compile-time constant integral expression?
```

If yes, assignment to `byte`, `short`, or `char` may be allowed without a cast if the value fits.

---

## Step 3 — Apply numeric promotion

For ordinary arithmetic:

```text
double wins
   ↓
float
   ↓
long
   ↓
int
```

`byte`, `short`, and `char` normally become `int`.

---

## Step 4 — Determine the resulting expression type

Example:

```java
short a = 10;
long b = 20;

a + b
```

becomes:

```text
short + long
  ↓
int + long
  ↓
long
```

---

## Step 5 — Process casts carefully

Determine exactly what the cast applies to.

```java
(short) (a + b)
```

is different from:

```java
(short) a + b
```

---

## Step 6 — Check assignment compatibility

Only after evaluating the right-hand expression should you compare its type with the left-hand variable.

---

## Step 7 — Look for compound assignment

Remember:

```java
x += y;
```

includes an implicit conversion back to the type of `x`.

---

# Essential Exam Examples

```java
// Compile-time constant narrowing

byte a = 1;                   // OK
byte b = 7 * 10;              // OK: constant 70 fits
byte c = 7 * 100;             // DOES NOT COMPILE


// Numeric promotion

short x = 10;
short y = 20;

short z1 = x + y;             // DOES NOT COMPILE
int z2 = x + y;               // OK
short z3 = (short) (x + y);   // OK


// Cast placement

short z4 = (short) x + y;     // DOES NOT COMPILE


// Constant variable

final int size = 10;

byte z5 = size;               // OK
byte z6 = size + 5;           // OK


// Ordinary variable

int size2 = 10;

byte z7 = size2;              // DOES NOT COMPILE


// Mixed types

int i = 5;
long l = 10;

long z8 = i + l;              // OK
int z9 = i + l;               // DOES NOT COMPILE
int z10 = (int) (i + l);      // OK


// Floating point

float f1 = 2.0;               // DOES NOT COMPILE
float f2 = 2.0F;              // OK
int z11 = (int) 2.9;          // OK: 2


// Compound assignment

byte q = 10;

q = q + 1;                    // DOES NOT COMPILE
q += 1;                       // OK
q++;                          // OK
```

---

# Final Memory Rules

```text
WIDENING
smaller → larger
usually automatic


NARROWING
larger → smaller
usually requires cast


ARITHMETIC
byte / short / char
normally promote to int


MIXED ARITHMETIC
double > float > long > int


CONSTANT EXPRESSIONS
An integral compile-time constant may narrow automatically
to byte / short / char if its value fits.


CASTS
A cast applies only to its operand.
Use parentheses when casting an entire expression.


COMPOUND ASSIGNMENT
x += y includes conversion back to the type of x.


LITERALS
integer literal → int by default
L → long
floating-point literal → double by default
F → float
```

## Best Exam Mental Model

> **Evaluate the right-hand expression first.**
>
> **Determine its type after promotion.**
>
> **Then determine whether assignment conversion allows that result to be stored in the left-hand variable.**

And remember the important exception:

> **If the right-hand side is a compile-time constant integral expression, Java may narrow it automatically to `byte`, `short`, or `char` when the value fits.**