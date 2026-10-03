# Text Blocks

A text block is simply another way of creating a `String`.

```java
String text = """
        Hello
        World
        """;
```

---

## 1. Opening `"""` Must Be Followed by a New Line

This is valid:

```java
String text = """
        hello""";
```

This is NOT:

```java
String text = """hello"""; // DOES NOT COMPILE
```

The opening delimiter must be followed by a line terminator.

Whitespace is allowed between the opening `"""` and that line terminator, but no other content.

---

## 2. Closing `"""` Controls the Final Newline

Closing delimiter on the **same line**:

```java
String text = """
        hello""";
```

produces:

```text
hello
```

No newline after `hello`.

Closing delimiter on the **next line**:

```java
String text = """
        hello
        """;
```

produces conceptually:

```text
hello\n
```

### Memory rule

```text
hello"""
    → no final newline

hello
"""
    → final newline
```

This is a common exam trap.

---

# 3. Incidental vs Essential Whitespace

Java removes **incidental indentation** used merely to make the source code readable.

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

The common indentation is removed.

The extra two spaces before `Rocks` remain because they are **essential whitespace**.

### Mental model

Think of Java finding the common left margin:

```text
        Java
          Rocks
        """
        ↑
        incidental

Java
  Rocks
↑
actual resulting content
```

> **Common indentation disappears; indentation beyond the common margin remains.**

The position of the closing `"""` can therefore affect how much indentation is removed.

---

# 4. Leading vs Trailing Whitespace

Essential **leading** whitespace can remain:

```java
String text = """
          Java
        """;
```

Depending on the position of the closing delimiter, extra indentation can become part of the resulting `String`.

Ordinary **trailing spaces**, however, are stripped from text-block lines.

If trailing spaces actually matter, use `\s`.

---

# 5. `\s` Preserves a Space

`\s` represents a single space.

It is particularly useful at the **end of a line**, where ordinary trailing spaces would otherwise be removed.

```java
String text = """
        Java\s
        Rocks
        """;
```

The first line contains a space immediately before its newline.

### Memory rule

```text
ordinary trailing spaces
    → stripped

\s
    → real preserved space
```

---

# 6. `\` Suppresses a Newline

A backslash immediately before the source line terminator prevents that newline from becoming part of the `String`.

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

### Memory rule

```text
\ at end of source line
    → join this line with the next
```

---

# 7. Normal Escape Sequences Still Work

Text blocks still support normal Java escapes:

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

For example:

```java
String text = """
        hello\n
        world
        """;
```

The explicit `\n` adds a newline **in addition to** the physical newline already present in the text block.

Therefore be careful when counting lines.

---

# 8. Quotes Usually Don't Need Escaping

One major benefit of text blocks is that ordinary quotes can appear directly:

```java
String text = """
        "Java"
        "Python"
        """;
```

No `\"` required.

However, three consecutive quotes can look like the closing delimiter, so at least one quote must be escaped when necessary.

For example:

```java
String text = """
        She wrote \"""
        """;
```

Escaping prevents the quotes from being interpreted as the text-block closing delimiter.

---

# 9. Text Blocks Are Still `String`

There is no special text-block runtime type.

```java
String text = """
        Hello
        World
        """;
```

`text` is simply a:

```text
String
```

Therefore normal `String` methods work:

```java
text.length();
text.substring(...);
text.contains(...);
text.formatted(...);
```

And a text block can be passed anywhere a `String` is expected.

---

# 10. Compiler Processing Order

A useful way to understand the tricky whitespace rules is that Java effectively processes text blocks in this order:

```text
1. Normalize line endings
        ↓
2. Remove incidental whitespace
        ↓
3. Interpret escape sequences
```

The fact that **escapes are interpreted last** explains why `\s` can preserve trailing whitespace.

```java
String text = """
        hello\s
        """;
```

Java does not turn `\s` into a space until **after** incidental/trailing whitespace processing.

---

# Exam Traps

### Opening delimiter

```java
String a = """hello""";  // ❌
```

```java
String a = """
        hello""";        // ✅
```

---

### Closing delimiter

```java
String a = """
        hello""";
```

No final newline.

```java
String a = """
        hello
        """;
```

Has a final newline.

---

### Physical newline

```java
String a = """
        hello
        world""";
```

contains a newline between `hello` and `world`.

---

### Suppressed newline

```java
String a = """
        hello \
        world""";
```

produces:

```text
hello world
```

---

### Explicit newline

```java
String a = """
        hello\n
        world""";
```

contains the explicit `\n` **plus the physical text-block newline** after it.

---

### Trailing whitespace

```text
ordinary spaces → stripped
\s              → preserved
```

---

# Quick Memory Rules

```text
"""
must be followed by a line terminator


Text block
    → still a String


COMMON LEADING INDENT
    → incidental
    → stripped


EXTRA LEADING INDENT
    → essential
    → preserved


ORDINARY TRAILING SPACES
    → stripped


\s
    → preserved space


\ at end of source line
    → suppress newline


closing """ on content line
    → no final newline


closing """ on next line
    → final newline


ordinary "
    → usually no escaping needed


three consecutive "
    → may need one escaped
```

## Ultimate Shortcut

```text
OPENING """
    → newline required

INDENTATION
    → common margin stripped

\s
    → preserve space

\
    → suppress newline

CLOSING """
    → position determines final newline
```