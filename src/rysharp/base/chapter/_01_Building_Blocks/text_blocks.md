# Text Blocks

[🔙 Back](README.md)

Quick reference for Java text blocks, whitespace, line endings and escape sequences.

## Contents

- [Creating a Text Block](#creating-a-text-block)
- [Opening and Closing Delimiters](#opening-and-closing-delimiters)
- [Indentation and Whitespace](#indentation-and-whitespace)
- [Escape Sequences](#escape-sequences)
- [Quotes](#quotes)
- [Processing Order](#processing-order)
- [Quick Reference](#quick-reference)

---

## Creating a Text Block

A text block is another way of creating a `String`.

```java
String text = """
        Hello
        World
        """;
```

There is no special text-block runtime type.

```java
text.length();
text.substring(...);
text.contains(...);
text.formatted(...);
```

A text block can be used anywhere a `String` is expected.

---

## Opening and Closing Delimiters

Text blocks use:

```text
"""
```

but the placement of the delimiters matters.

### Opening Delimiter

The opening `"""` must be followed by a line terminator.

Valid:

```java
String text = """
        hello""";
```

Invalid:

```java
String text = """hello""";   // DOES NOT COMPILE
```

Whitespace is allowed between the opening delimiter and the line terminator, but other content is not.

Memory:

> **Opening `"""` → newline required.**

### Closing Delimiter

The position of the closing `"""` determines whether the preceding physical newline becomes part of the string.

Closing delimiter on the same line:

```java
String text = """
        hello""";
```

produces:

```text
hello
```

with no final newline.

Closing delimiter on the next line:

```java
String text = """
        hello
        """;
```

produces conceptually:

```text
hello\n
```

Memory:

```text
hello"""
→ no final newline

hello
"""
→ final newline
```

The position of the closing delimiter can also affect incidental indentation.

---

## Indentation and Whitespace

Java removes **incidental whitespace** used to indent the text block in source code.

Whitespace beyond that common indentation can remain as **essential whitespace**.

### Incidental Indentation

```java
String text = """
        Java
          Rocks
        """;
```

produces:

```text
Java
  Rocks
```

The common indentation is removed, while the additional indentation before `Rocks` remains.

Memory:

> **Common indentation disappears; indentation beyond the common margin remains.**

### Closing Delimiter and Indentation

The closing delimiter participates in determining the incidental indentation when it appears on its own line.

Its position can therefore affect how much leading whitespace is removed.

For exam questions involving spaces, pay attention to both:

```text
content indentation
closing delimiter position
```

### Trailing Whitespace

Ordinary trailing spaces on text-block lines are stripped.

If a trailing space needs to be preserved, use `\s`.

```java
String text = """
        Java\s
        Rocks
        """;
```

The first line contains a real space immediately before its newline.

Memory:

```text
ordinary trailing spaces → stripped
\s                       → preserved space
```

---

## Escape Sequences

Normal Java escape sequences still work inside text blocks.

Common examples:

```text
\n    newline
\t    tab
\r    carriage return
\b    backspace
\f    form feed
\"    double quote
\\    backslash
\s    space
```

Two escapes are particularly important for text blocks.

### `\s` — Preserve a Space

`\s` represents a space.

It is especially useful at the end of a line because ordinary trailing whitespace would otherwise be stripped.

```java
String text = """
        hello\s
        world
        """;
```

Memory:

> **`\s` creates a real space after whitespace stripping has occurred.**

### `\` — Suppress a Newline

A backslash immediately before a source line terminator prevents that newline from becoming part of the resulting string.

```java
String text = """
        doe \
        deer""";
```

produces:

```text
doe deer
```

rather than:

```text
doe
deer
```

Memory:

> **`\` at the end of a source line joins that line with the next.**

### Physical vs Explicit Newlines

A physical line break in the text block normally contributes a newline.

```java
String text = """
        hello
        world""";
```

contains a newline between `hello` and `world`.

An explicit `\n` also adds a newline:

```java
String text = """
        hello\n
        world""";
```

Here there is:

```text
explicit \n
+
physical text-block newline
```

So be careful when counting resulting characters or lines.

---

## Quotes

Ordinary double quotes normally do not need escaping inside a text block:

```java
String text = """
        "Java"
        "Python"
        """;
```

This is one of the main conveniences of text blocks.

### Three Consecutive Quotes

Three consecutive quotes can be interpreted as the closing delimiter.

When literal quote characters would otherwise form `"""`, escape at least one as necessary.

For example:

```java
String text = """
        She wrote \"""
        """;
```

The escape prevents the quote sequence from being interpreted as the closing delimiter.

---

## Processing Order

A useful mental model for text-block processing is:

```text
1. Normalize line endings
        ↓
2. Remove incidental whitespace
        ↓
3. Interpret escape sequences
```

This explains several otherwise awkward rules.

For example:

```java
String text = """
        hello\s
        """;
```

`\s` is interpreted **after** whitespace processing.

Therefore the space introduced by `\s` survives.

Likewise, the line-continuation escape can suppress a line terminator during escape processing.

Memory:

> **Whitespace processing happens before escape sequences are interpreted.**

---

# Quick Reference

## Delimiters

```text
OPENING """

"""hello"""
→ ✗ opening delimiter must be followed by newline

"""
hello"""
→ ✓
```

Closing delimiter:

```text
hello"""
→ no final newline

hello
"""
→ final newline
```

## Whitespace

```text
common leading indentation
→ stripped

additional leading indentation
→ preserved

ordinary trailing spaces
→ stripped

\s
→ preserved space
```

## Newlines

```text
physical source newline
→ normally becomes newline in String

\n
→ explicit newline

\ at end of source line
→ suppresses physical newline
```

## Quotes

```text
ordinary "
→ normally write directly

"""
→ can conflict with closing delimiter
→ escape a quote when necessary
```

## Type

```text
text block
→ String
```

All normal `String` operations still apply.

## Processing

```text
normalize line endings
        ↓
remove incidental whitespace
        ↓
interpret escapes
```

## Final Memory Kicks

> **A text block is still just a `String`.**

> **Opening `"""` must be followed by a line terminator.**

> **Closing `"""` on the content line means no final newline; placing it on the next line includes the preceding newline.**

> **Common indentation is stripped; additional indentation is preserved.**

> **Ordinary trailing spaces are stripped; `\s` preserves a space.**

> **`\` at the end of a source line suppresses that newline.**

> **Physical newlines and explicit `\n` are separate — both can contribute newlines.**

> **Whitespace processing occurs before escape sequences are interpreted.**