### Operator Precedence

Operator **precedence** determines how an expression is grouped.

For example:

```java
int x = 2 + 3 * 4;
```

`*` has higher precedence:

```java
2 + (3 * 4)
```

Result:

```text
14
```

Parentheses override normal precedence:

```java
int x = (2 + 3) * 4; // 20
```

---

## Useful Precedence Order

From **highest to lowest**:

| Priority | Operators                         | Meaning                     |
| -------- | --------------------------------- | --------------------------- |
| Highest  | `expr++` `expr--`                 | postfix increment/decrement |
|          | `++expr` `--expr` `+` `-` `!` `~` | prefix/unary                |
|          | `*` `/` `%`                       | multiplication              |
|          | `+` `-`                           | addition                    |
|          | `<<` `>>` `>>>`                   | shifts                      |
|          | `<` `<=` `>` `>=` `instanceof`    | relational                  |
|          | `==` `!=`                         | equality                    |
|          | `&`                               | AND                         |
|          | `^`                               | XOR                         |
|          | `\|`                              | OR                          |
|          | `&&`                              | short-circuit AND           |
|          | `\|\|`                            | short-circuit OR            |
|          | `?:`                              | ternary                     |
| Lowest   | `=` `+=` `-=` `*=` `/=` etc.      | assignment                  |

For most exam questions, remember roughly:

```text
postfix ++/--
      ↓
prefix ++/--
      ↓
* / %
      ↓
+ -
      ↓
comparisons
      ↓
== !=
      ↓
&
^
|
      ↓
&&
||
      ↓
?:
      ↓
assignment
```

---

# `++` and `--`

This is where precedence questions become more interesting.

## Prefix

```java
++x
--x
```

The variable changes **first**, and the expression produces the new value.

```java
int x = 5;
int y = ++x;
```

Think:

```text
x becomes 6
expression produces 6
y becomes 6
```

Final:

```text
x = 6
y = 6
```

---

## Postfix

```java
x++
x--
```

The expression produces the **old value**, while the variable is still incremented/decremented as part of evaluating that expression.

```java
int x = 5;
int y = x++;
```

Think:

```text
expression produces 5
x becomes 6
y receives 5
```

Final:

```text
x = 6
y = 5
```

Memory rule:

```text
++x → CHANGE, then USE

x++ → USE old value, then CHANGE
```

---

# Precedence vs Evaluation Order

Java evaluates operands **left to right**.

This is separate from operator precedence.

Consider:

```java
int x = 2;

int y = x++ + ++x * 2;
```

First use precedence to understand the grouping:

```java
x++ + (++x * 2)
```

because multiplication has higher precedence than addition.

But Java still evaluates the operands **left to right**.

Start:

```text
x = 2
```

### 1. Evaluate `x++`

Postfix means use `2`, then increment:

```text
expression value = 2
x = 3
```

### 2. Evaluate `++x`

Prefix means increment first:

```text
x = 4
expression value = 4
```

### 3. Multiplication

```text
4 * 2 = 8
```

### 4. Addition

```text
2 + 8 = 10
```

Therefore:

```text
x = 4
y = 10
```

---

# Another Example

```java
int x = 5;

int y = x++ + ++x;
```

Start:

```text
x = 5
```

Left operand:

```text
x++

use 5
x becomes 6
```

Right operand:

```text
++x

x becomes 7
use 7
```

Then:

```text
5 + 7 = 12
```

Final:

```text
x = 7
y = 12
```

---

# Don't Apply Precedence as "Execution Order"

This is an important distinction.

Given:

```java
a() + b() * c()
```

precedence groups it as:

```text
a() + (b() * c())
```

but Java evaluates the operands left-to-right:

```text
a()
b()
c()
```

Then the multiplication/addition produce their results according to the grouping.

So:

> **Higher precedence does NOT mean "this method/operand is evaluated first."**

Precedence tells you **how values are combined**, not generally which operand's side effects happen first.

---

# Associativity

When operators have the same precedence, **associativity** determines grouping.

Most arithmetic operators associate left-to-right:

```java
20 / 5 * 2
```

groups as:

```java
(20 / 5) * 2
```

Therefore:

```text
4 * 2 = 8
```

Not:

```text
20 / (5 * 2) = 2
```

Similarly:

```java
10 - 3 - 2
```

means:

```java
(10 - 3) - 2
```

Result:

```text
5
```

---

## Assignment Associates Right-to-Left

Assignment is different:

```java
a = b = c = 10;
```

groups as:

```java
a = (b = (c = 10));
```

All three become `10`.

---

# Short-Circuit Operators

Evaluation is still left-to-right, but `&&` and `||` may prevent the right side from being evaluated.

```java
int x = 5;

boolean result = false && ++x > 5;
```

The right side isn't evaluated:

```text
x = 5
```

Likewise:

```java
boolean result = true || ++x > 5;
```

Again, `++x` isn't evaluated.

This matters greatly when `++` or `--` appears on the right side.

Contrast with:

```java
&
|
```

which do **not** short-circuit when used with booleans.

---

# Common Exam Example

```java
int x = 3;

int result = ++x * 2 + x--;
```

### Group by precedence

```text
(++x * 2) + x--
```

### Evaluate left-to-right

Start:

```text
x = 3
```

`++x`:

```text
x = 4
use 4
```

Multiply:

```text
4 * 2 = 8
```

Then `x--`:

```text
use 4
x becomes 3
```

Addition:

```text
8 + 4 = 12
```

Final:

```text
result = 12
x = 3
```

---

# Exam Method

For complicated expressions, don't try to do everything mentally.

### Step 1 — Determine grouping from precedence

```java
x++ + ++x * 2
```

becomes conceptually:

```text
x++ + (++x * 2)
```

### Step 2 — Evaluate operands left-to-right

Keep a running value for each variable.

### Step 3 — For every `++` / `--`, write two things

```text
expression value
new variable value
```

For example:

```text
x = 4

x++:
    produces 4
    x → 5

++x:
    x → 6
    produces 6
```

### Step 4 — Apply the operators according to their grouping

This prevents precedence and side effects from getting mixed together.

---

## Memory Rules

```text
PRECEDENCE
    → how is the expression GROUPED?

EVALUATION ORDER
    → Java evaluates operands LEFT TO RIGHT

ASSOCIATIVITY
    → how operators at the SAME precedence level GROUP

++x
    → increment first
    → expression sees NEW value

x++
    → expression sees OLD value
    → variable is incremented

&& / ||
    → right operand might NEVER be evaluated

& / |
    → both boolean operands are evaluated
```

Most importantly:

> **Precedence determines grouping; it does not override Java's left-to-right operand evaluation.**
