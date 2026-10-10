# Array Creation

[🔙 Back](README.md)

Quick reference for declaring, creating and initializing Java arrays.

## Contents

- [Declaration](#declaration)
- [Creating an Array with a Size](#creating-an-array-with-a-size)
- [Creating an Array with Values](#creating-an-array-with-values)
- [Multidimensional Arrays](#multidimensional-arrays)
- [Multidimensional Initializers](#multidimensional-initializers)
- [Quick Reference](#quick-reference)

---

## Declaration

Both declaration styles are valid:

```java
int[] a;
int b[];
```

No array has been created yet.

Prefer:

```java
int[] a;
```

stylistically because it makes the type clearer.

---

## Creating an Array with a Size

```java
int[] a = new int[3];
```

Creates three elements containing their default values:

```text
[0, 0, 0]
```

Common defaults:

```text
numeric primitives → 0 / 0.0
char               → '\u0000'
boolean            → false
references         → null
```

The size determines the array's fixed length.

---

## Creating an Array with Values

At declaration:

```java
int[] a = {3, 2, 1};
```

Or using an explicit array creation expression:

```java
int[] b = new int[]{3, 2, 1};
```

In both cases, the size is inferred from the initializer.

### Size OR Values

You cannot specify both a dimension size and an initializer:

```java
int[] a = new int[3]{3, 2, 1};   // DOES NOT COMPILE
```

Memory:

> **Initializer present → don't specify dimension sizes.**

### Initializer Shorthand Is Declaration-Only

This is valid:

```java
int[] a = {1, 2, 3};
```

But the shorthand cannot be used later as an ordinary assignment:

```java
int[] a;

a = {1, 2, 3};   // DOES NOT COMPILE
```

Use an explicit array creation expression instead:

```java
a = new int[]{1, 2, 3};   // OK
```

---

## Multidimensional Arrays

Java multidimensional arrays are really **arrays of arrays**.

```java
int[][] a = new int[3][4];
```

Conceptually:

```text
outer array
│
├── int[4]
├── int[4]
└── int[4]
```

### Specifying All Dimensions

```java
int[][] a = new int[3][4];
```

Creates:

- an outer array of length `3`
- three inner arrays
- each inner array has length `4`

### Leaving Later Dimensions Unspecified

```java
int[][] a = new int[3][];
```

This creates an outer array containing three references:

```text
[null, null, null]
```

The inner arrays can be created later:

```java
a[0] = new int[2];
a[1] = new int[]{10, 20, 30};
a[2] = new int[5];
```

The inner arrays can have different lengths.

This is a **jagged array**.

### Dimension Ordering Rule

When creating an array without an initializer, dimensions must be specified from **left to right**.

Valid:

```java
int[][][] a = new int[2][3][4];
int[][][] b = new int[2][3][];
int[][][] c = new int[2][][];
```

Invalid:

```java
int[][][] d = new int[2][][4];   // DOES NOT COMPILE
int[][][] e = new int[][3][4];   // DOES NOT COMPILE
```

Memory:

> **Once a dimension size is omitted, every later dimension size must also be omitted.**

---

## Multidimensional Initializers

Initializer shorthand:

```java
int[][] a = {
    {1, 2},
    {3, 4}
};
```

Explicit creation:

```java
int[][] a = new int[][]{
    {1, 2},
    {3, 4}
};
```

### Jagged Initializers

Inner arrays do not need equal lengths:

```java
int[][] a = {
    {1},
    {2, 3},
    {4, 5, 6}
};
```

This produces:

```text
a[0].length → 1
a[1].length → 2
a[2].length → 3
```

### Initializer + Dimension Sizes

The normal initializer rule still applies to multidimensional arrays.

Invalid:

```java
int[][] a = new int[2][]{
    {1, 2},
    {3, 4}
};   // DOES NOT COMPILE
```

Valid:

```java
int[][] a = new int[][]{
    {1, 2},
    {3, 4}
};
```

Remember:

> **Multidimensional arrays do not remove the "sizes OR initializer" rule.**

---

# Quick Reference

## One-Dimensional Arrays

```text
int[] a;                    ✓ declaration

new int[3]                  ✓ size
new int[]{1, 2, 3}          ✓ values
int[] a = {1, 2, 3};        ✓ declaration initializer

new int[3]{1, 2, 3}         ✗ size + initializer

int[] a;
a = {1, 2, 3};              ✗ shorthand outside declaration
a = new int[]{1, 2, 3};     ✓
```

## Multidimensional Arrays

```text
new int[2][3]               ✓
new int[2][]                ✓
new int[2][3][]             ✓
new int[2][][]              ✓

new int[][3]                ✗
new int[2][][3]             ✗
```

## Multidimensional Initializers

```text
new int[][]{{1}, {2}}       ✓

new int[2][]{{1}, {2}}      ✗
new int[2][2]{{1}, {2}}     ✗
```

## Default Values

```text
int[]        → 0
double[]     → 0.0
boolean[]    → false
char[]       → '\u0000'
Object[]     → null
```

## Final Memory Kicks

> **Initializer present → don't specify dimension sizes.**

> **No initializer → specify dimensions left-to-right; once one is blank, all later dimensions must be blank.**

> **`{1, 2, 3}` shorthand works as part of a declaration; later assignment requires `new int[]{1, 2, 3}`.**

> **Multidimensional arrays are arrays of arrays, so inner arrays can have different lengths.**