# Console

[🔙 Back](../README.md)

## Overview

`java.io.Console` provides access to a character-based console for reading user input and writing output.

Unlike `Scanner`, it is specifically designed for console interaction and supports secure password entry.

```java
import java.io.Console;

Console console = System.console();
```

**Important:** `System.console()` can return `null`, particularly when running from an IDE or an environment without an attached console.

`Console` cannot be instantiated directly using `new Console()`.

## Reading Input

| Method | Return Type | Description |
|---|---|---|
| `readLine()` | `String` | Reads a line of text |
| `readLine(String, Object...)` | `String` | Displays a formatted prompt before reading |
| `readPassword()` | `char[]` | Reads a password without echoing input |
| `readPassword(String, Object...)` | `char[]` | Displays a formatted prompt before reading a password |
| `reader()` | `Reader` | Returns the console's character reader |
| `read()` | — | **Does not exist on `Console`** |

### Example

```java
Console console = System.console();

if (console != null) {
    String username = console.readLine("Username: ");
    char[] password = console.readPassword("Password: ");
}
```

`readPassword()` returns `char[]` rather than `String`, allowing the password to be cleared from memory after use.

```java
Arrays.fill(password, '\0');
```

## `reader()` versus `read()`

This distinction caused the incorrect answer in PE2 Q17.

```java
Console console = System.console();

Reader reader = console.reader(); // Valid

int character = reader.read();    // Valid

console.read();                   // DOES NOT COMPILE
```

The `read()` method belongs to `Reader`, not `Console`.

**Remember:**

- `console.reader()` returns a `Reader`.
- `reader.read()` reads a character.
- `console.read()` does not exist.

## Writing Output

| Method | Return Type | Description |
|---|---|---|
| `print(Object)` | `Console` | Prints an object |
| `println(Object)` | `Console` | Prints an object followed by a newline |
| `printf(String, Object...)` | `Console` | Writes formatted output |
| `format(String, Object...)` | `Console` | Equivalent to `printf()` |
| `writer()` | `PrintWriter` | Returns the console's writer |
| `flush()` | `void` | Flushes buffered output |

### Example

```java
Console console = System.console();

console.printf("Hello %s%n", "Java");
console.format("Score: %d%n", 72);

PrintWriter writer = console.writer();
writer.println("Finished");
```

`printf()` and `format()` return `Console`, so they can be chained.

```java
console.printf("Hello%n").printf("World%n");
```

## Locale-Aware Formatting

Java 25 includes locale-aware formatting support on `Console`.

| Method | Purpose |
|---|---|
| `format(Locale, String, Object...)` | Formats output using the specified locale |
| `printf(Locale, String, Object...)` | Locale-aware equivalent of `printf()` |
| `readLine(Locale, String, Object...)` | Displays a locale-formatted prompt and reads a line |
| `readPassword(Locale, String, Object...)` | Displays a locale-formatted prompt and reads a password |

### Example

```java
Console console = System.console();

console.printf(Locale.FRANCE, "%.2f%n", 1234.56);
```

Locale-sensitive formatting can affect decimal separators and other formatted values.

**Exam note:** Be able to recognise the overloads accepting `Locale`, as well as the original formatting methods.

## Console versus Scanner

| Feature | `Console` | `Scanner` |
|---|---|---|
| Read a complete line | `readLine()` | `nextLine()` |
| Read an integer directly | No | `nextInt()` |
| Read a password without echoing | `readPassword()` | No |
| Formatted output | `printf()` / `format()` | No |
| Obtain a character reader | `reader()` | Not applicable |
| May be unavailable without a console | Yes | No, when constructed with a suitable input source |

`Console` primarily handles text input. Numeric input normally requires parsing:

```java
int age = Integer.parseInt(console.readLine());
```

## Common Exam Traps

### 1. `Console` has no `read()`

```java
console.read();          // Invalid
console.reader().read(); // Valid
```

### 2. `readPassword()` does not return `String`

```java
char[] password = console.readPassword(); // Valid

String password2 = console.readPassword(); // Invalid
```

### 3. `reader()` and `writer()` return different types

```java
Reader input = console.reader();
PrintWriter output = console.writer();
```

### 4. `System.console()` can return `null`

```java
Console console = System.console();

if (console != null) {
    console.println("Console available");
}
```

### 5. `Console` is not `Scanner`

```java
console.nextInt();  // Invalid
console.nextLine(); // Invalid
```

## Quick Reference

| Operation | Method |
|---|---|
| Obtain console | `System.console()` |
| Read line | `readLine()` |
| Read password | `readPassword()` |
| Get reader | `reader()` |
| Get writer | `writer()` |
| Print text | `print()` / `println()` |
| Formatted output | `printf()` / `format()` |
| Flush output | `flush()` |

## Final Memory Kicks

- `Console` is in **`java.io`**.
- `System.console()` may return **`null`**.
- `readLine()` returns **`String`**.
- `readPassword()` returns **`char[]`**.
- `reader()` returns **`Reader`**.
- `writer()` returns **`PrintWriter`**.
- **`Console.read()` does not exist.**
- `Reader.read()` **does** exist.
- `printf()` and `format()` return **`Console`**.
- Remember the **locale-aware overloads** for Java 25.
- Don't confuse `Console` methods with `Scanner` methods.