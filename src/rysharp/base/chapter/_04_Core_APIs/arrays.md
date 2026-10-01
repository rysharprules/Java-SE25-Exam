# Understanding Arrays

## Array Creation — Quick Reference

### 1. Declare only

```java
int[] a;
int b[];
```

Both are valid. No array has been created yet.

Prefer `int[] a` stylistically.

---

### 2. Create with a size

```java
int[] a = new int[3];
```

Creates 3 elements with default values:

```text
[0, 0, 0]
```

Other defaults include `false` for `boolean` and `null` for references.

---

### 3. Create with values

```java
int[] a = {3, 2, 1};

int[] b = new int[]{3, 2, 1};
```

Both are valid.

The size is inferred from the initializer.

### ❌ Cannot specify size AND values

```java
int[] a = new int[3]{3, 2, 1}; // DOES NOT COMPILE
```

Remember:

> **Single dimension: specify the size OR the values, never both.**

---

# Multidimensional Arrays

Java multidimensional arrays are really **arrays of arrays**.

### Specify all sizes

```java
int[][] a = new int[3][4];
```

Creates 3 inner arrays, each containing 4 ints.

### Specify only earlier dimensions

```java
int[][] a = new int[3][];
```

Valid.

The outer array contains 3 references whose initial value is `null`.

You can create the inner arrays later:

```java
a[0] = new int[2];
a[1] = new int[]{10, 20, 30};
a[2] = new int[5];
```

This allows jagged arrays.

### ❌ Cannot omit an earlier dimension then specify a later one

```java
int[][] a = new int[][3]; // DOES NOT COMPILE
```

Once a dimension's size is omitted, later dimensions must also be omitted:

```java
int[][][] a = new int[2][3][4]; // ✓
int[][][] b = new int[2][3][];  // ✓
int[][][] c = new int[2][][];   // ✓

int[][][] d = new int[2][][4];  // ✗
int[][][] e = new int[][3][4];  // ✗
```

---

## Multidimensional Initializers

```java
int[][] a = {
    {1, 2},
    {3, 4}
};
```

or:

```java
int[][] a = new int[][]{
    {1, 2},
    {3, 4}
};
```

Jagged values are also valid:

```java
int[][] a = {
    {1},
    {2, 3},
    {4, 5, 6}
};
```

### ❌ Still cannot mix dimension sizes with an initializer

```java
int[][] a = new int[2][]{
    {1, 2},
    {3, 4}
}; // DOES NOT COMPILE
```

This is the important clarification:

> **Multidimensional arrays do NOT remove the "size OR initializer" rule.**

What is different is that when creating an array **without an initializer**, Java lets you specify the sizes of some leading dimensions while leaving later dimensions unspecified:

```java
new int[3][]      // ✓
new int[3][4]     // ✓
new int[3][][5]   // ✗
```

---

## Exam Memory Rules

```text
new int[3]             ✓ size
new int[]{1, 2, 3}     ✓ values
{1, 2, 3}              ✓ initializer at declaration

new int[3]{1, 2, 3}    ✗ size + values
```

For multidimensional arrays:

```text
new int[2][3]           ✓
new int[2][]            ✓
new int[][]{{1}, {2}}   ✓

new int[][3]            ✗
new int[2][][3]         ✗
new int[2][]{{1}, {2}}  ✗
```

### Two rules to remember

> **Initializer present → do not specify dimension sizes.**

> **No initializer → specify dimensions from left to right; once you leave one blank, all remaining dimensions must be blank.**
