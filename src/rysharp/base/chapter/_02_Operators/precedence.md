# Operator Precedence

Quick reference for operator precedence, associativity, evaluation order and `++` / `--`.

## Contents

- [Precedence](#precedence)
- [Precedence Order](#precedence-order)
- [Increment and Decrement](#increment-and-decrement)
- [Evaluation Order](#evaluation-order)
- [Associativity](#associativity)
- [Short-Circuit Operators](#short-circuit-operators)
- [Expression Tracing](#expression-tracing)
- [Quick Reference](#quick-reference)

---

## Precedence

Operator **precedence** determines how an expression is grouped.

```java
int x = 2 + 3 * 4;
```

`*` has higher precedence than `+`, so this groups as:

```java
2 + (3 * 4)
```

Result:

```text
14
```

Parentheses override normal precedence:

```java
int x = (2 + 3) * 4;   // 20
```

Memory:

> **Precedence determines grouping.**

---

## Precedence Order

From **highest to lowest**:

| Priority | Operators | Meaning |
|---|---|---|
| Highest | `expr++` `expr--` | Postfix |
| | `++expr` `--expr` `+` `-` `!` `~` | Prefix / unary |
| | `*` `/` `%` | Multiplicative |
| | `+` `-` | Additive |
| | `<<` `>>` `>>>` | Shift |
| | `<` `<=` `>` `>=` `instanceof` | Relational |
| | `==` `!=` | Equality |
| | `&` | AND |
| | `^` | XOR |
| | `\|` | OR |
| | `&&` | Short-circuit AND |
| | `\|\|` | Short-circuit OR |
| | `?:` | Ternary |
| Lowest | `=` `+=` `-=` `*=` `/=` etc. | Assignment |

Useful condensed order:

```text
postfix ++ --
      ↓
prefix ++ -- + - ! ~
      ↓
* / %
      ↓
+ -
      ↓
<< >> >>>
      ↓
< <= > >= instanceof
      ↓
== !=
      ↓
&
↓
^
↓
|
      ↓
&&
↓
||
      ↓
?:
      ↓
assignment
```

---

## Increment and Decrement

The important distinction is the **value produced by the expression**.

### Prefix

```java
++x
--x
```

The variable changes first and the expression produces the **new value**.

```java
int x = 5;
int y = ++x;
```

Think:

```text
x → 6
expression → 6
y → 6
```

Final:

```text
x = 6
y = 6
```

### Postfix

```java
x++
x--
```

The expression produces the **old value**, while the variable is still changed as part of evaluating the expression.

```java
int x = 5;
int y = x++;
```

Think:

```text
expression → 5
x → 6
y → 5
```

Final:

```text
x = 6
y = 5
```

Memory:

```text
++x → CHANGE, then USE new value

x++ → USE old value, then CHANGE
```

The same principle applies to `--`.

---

## Evaluation Order

Precedence and evaluation order are different concepts.

Java evaluates operands **left-to-right**.

Consider:

```java
int x = 2;

int y = x++ + ++x * 2;
```

### Group by Precedence

Multiplication has higher precedence:

```java
x++ + (++x * 2)
```

### Evaluate Left-to-Right

Start:

```text
x = 2
```

Evaluate:

```java
x++
```

Postfix:

```text
expression produces 2
x → 3
```

Next:

```java
++x
```

Prefix:

```text
x → 4
expression produces 4
```

Then:

```text
4 * 2 = 8
2 + 8 = 10
```

Final:

```text
x = 4
y = 10
```

### Precedence Is Not Execution Order

Given:

```java
a() + b() * c()
```

precedence groups it as:

```java
a() + (b() * c())
```

But operand evaluation occurs left-to-right:

```text
a()
b()
c()
```

The returned values are then combined according to the grouping.

Memory:

> **Higher precedence does not mean that operand is evaluated first.**

Think:

```text
PRECEDENCE       → how are values grouped?

EVALUATION ORDER → in what order are operands evaluated?
```

---

## Associativity

When operators have the **same precedence**, associativity determines their grouping.

### Left-to-Right

Most binary arithmetic operators associate left-to-right.

```java
20 / 5 * 2
```

groups as:

```java
(20 / 5) * 2
```

Result:

```text
8
```

Likewise:

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

### Assignment Is Right-to-Left

Assignment operators associate right-to-left:

```java
a = b = c = 10;
```

groups as:

```java
a = (b = (c = 10));
```

All three variables become `10`.

Memory:

```text
precedence    → operators of DIFFERENT priority
associativity → operators of the SAME priority
```

---

## Short-Circuit Operators

`&&` and `||` evaluate left-to-right but may skip the right operand.

### `&&`

```java
int x = 5;

boolean result = false && ++x > 5;
```

The left operand is already `false`, so the right side is not evaluated:

```text
x = 5
```

### `||`

```java
int x = 5;

boolean result = true || ++x > 5;
```

The left operand is already `true`, so again:

```text
x = 5
```

### `&` and `|`

With boolean operands:

```java
&
|
```

evaluate **both operands**.

For example:

```java
int x = 5;

boolean result = false & ++x > 5;
```

The right side is still evaluated:

```text
x = 6
```

Remember:

```text
&&  → short-circuit AND
||  → short-circuit OR

&   → both boolean operands evaluated
|   → both boolean operands evaluated
```

`^` with boolean operands is XOR:

```text
true  ^ false → true
false ^ true  → true
true  ^ true  → false
false ^ false → false
```

---

## Expression Tracing

For complicated expressions, separate **grouping** from **evaluation**.

Consider:

```java
int x = 3;

int result = ++x * 2 + x--;
```

### Determine Grouping

```java
(++x * 2) + x--
```

### Track Side Effects

Start:

```text
x = 3
```

`++x`:

```text
x → 4
expression value → 4
```

Multiply:

```text
4 * 2 = 8
```

`x--`:

```text
expression value → 4
x → 3
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

### Reliable Method

For a complicated expression:

```text
1. GROUP using precedence

2. EVALUATE operands left-to-right

3. For each ++ / -- track:
      expression value
      variable value

4. APPLY operators according to the grouping
```

For example:

```text
x = 4

x++:
    expression → 4
    x → 5

++x:
    x → 6
    expression → 6
```

Keeping the **expression value** separate from the **variable's new value** prevents most increment/decrement mistakes.

---

# Quick Reference

## Three Different Concepts

```text
PRECEDENCE
→ how is the expression grouped?

EVALUATION ORDER
→ operands are evaluated left-to-right

ASSOCIATIVITY
→ how do operators at the same precedence group?
```

## Increment / Decrement

```text
++x
→ change first
→ expression sees NEW value

x++
→ expression sees OLD value
→ variable still changes
```

Same principle:

```text
--x
x--
```

## Short Circuiting

```text
A && B
→ B evaluated only if A is true

A || B
→ B evaluated only if A is false

A & B
→ both evaluated

A | B
→ both evaluated
```

## Useful Precedence

```text
postfix ++ --
      ↓
prefix ++ -- + - ! ~
      ↓
* / %
      ↓
+ -
      ↓
shifts
      ↓
relational / instanceof
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

## Final Memory Kicks

> **Precedence determines grouping; it does not override Java's left-to-right operand evaluation.**

> **Prefix changes the variable first; postfix produces the old value first.**

> **Associativity determines grouping between operators at the same precedence level.**

> **Most arithmetic associates left-to-right; assignment associates right-to-left.**

> **`&&` and `||` may skip the right operand; boolean `&` and `|` evaluate both.**

> **With `++` / `--`, track the expression value and variable value separately.**