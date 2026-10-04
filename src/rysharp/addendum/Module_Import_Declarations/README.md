# Module Import Declarations (JEP 511)

Quick reference for Java 25 module imports, exported packages and their relationship with traditional imports.

## Contents

- [Module Imports](#module-imports)
- [`java.base`](#javabase)
- [Package Imports vs Module Imports](#package-imports-vs-module-imports)
- [Exported Packages and Accessibility](#exported-packages-and-accessibility)
- [Import Conflicts](#import-conflicts)
- [Compact Source Files](#compact-source-files)
- [Quick Reference](#quick-reference)

---

## Module Imports

Java 25 introduces **module import declarations**.

The syntax is:

```java
import module java.base;
```

Traditionally, you might write:

```java
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
```

or:

```java
import java.util.*;
import java.time.*;
```

A module import operates at a broader level:

```java
import module java.base;
```

This makes accessible types from packages exported by `java.base` available for simple-name use.

For example:

```java
import module java.base;

class Example {

    List<String> names = new ArrayList<>();

    LocalDate date = LocalDate.now();
}
```

Memory:

> **`import module X` → accessible types from packages exported by module X.**

It does **not** mean:

```text
import absolutely everything
physically contained in module X
```

---

## `java.base`

`java.base` is much larger than `java.lang`.

### `java.lang`

Every Java source file automatically imports:

```text
java.lang
```

Therefore these require no explicit import:

```java
String name = "Ryan";
Integer number = 10;

System.out.println(name);
```

because types such as:

```text
String
Integer
System
Object
Math
Exception
```

belong to `java.lang`.

### Other `java.base` Packages

`java.base` contains and exports many other important packages.

Useful ones to recognise include:

| Package | Common Types |
|---|---|
| `java.lang` | `String`, `Object`, `System`, `Math`, `Integer` |
| `java.util` | `List`, `Set`, `Map`, `ArrayList`, `Arrays`, `Optional` |
| `java.util.function` | `Function`, `Predicate`, `Consumer`, `Supplier` |
| `java.util.stream` | `Stream`, `IntStream`, `Collectors` |
| `java.io` | `File`, `InputStream`, `Reader`, `Writer` |
| `java.nio.file` | `Path`, `Files` |
| `java.time` | `LocalDate`, `LocalTime`, `Duration`, `Period` |
| `java.util.concurrent` | `ExecutorService`, `Future`, `ConcurrentHashMap` |
| `java.util.regex` | `Pattern`, `Matcher` |

You do not need to memorise every package in `java.base`.

A useful recognition cluster is:

```text
java.base

java.lang
java.util
java.util.function
java.util.stream
java.io
java.nio.file
java.time
java.util.concurrent
```

### `java.base` Is Not All of Java

This:

```java
import module java.base;
```

does not make types from every Java SE module available.

For example, other modules include:

```text
java.sql
java.desktop
java.net.http
```

So remember:

```text
java.base
≠ entire JDK
```

---

## Package Imports vs Module Imports

Package and module imports operate at different levels.

### Package Import

```java
import java.util.*;
```

means:

> Make accessible types directly in `java.util` available by simple name.

It does **not** recursively import subpackages.

For example:

```text
java.util
java.util.function
java.util.stream
```

are three separate packages.

Therefore:

```java
import java.util.*;
```

can make these available:

```text
List
Map
ArrayList
Optional
```

but not merely because of that import:

```text
Function
Predicate
Stream
Collectors
```

Those belong to:

```text
java.util.function
java.util.stream
```

and traditionally require their own imports.

Memory:

> **Package wildcard imports are not recursive.**

### Module Import

A module import operates above the package level:

```java
import module java.base;
```

Conceptually:

```text
MODULE
  ↓
exported packages
  ↓
accessible types
```

So:

```java
import module java.base;
```

can make types available from several different exported packages:

```text
List       → java.util
LocalDate  → java.time
Predicate  → java.util.function
Stream     → java.util.stream
```

### The Three Levels

Keep these separate:

```text
MODULE
  ↓
contains packages

PACKAGE
  ↓
contains types

TYPE
  ↓
class / interface / enum / record
```

For example:

```java
import module java.base;
```

operates at the **module** level.

```java
import java.util.*;
```

operates at the **package** level.

```java
java.lang.String
```

identifies a specific **type**.

Memory:

```text
module import
→ exported packages
→ accessible types

package wildcard
→ one package only
→ NOT its subpackages
```

---

## Exported Packages and Accessibility

A module can contain packages that it does not export.

Conceptually:

```text
module X
 │
 ├── exported package A
 │
 ├── exported package B
 │
 └── internal package C
```

Then:

```java
import module X;
```

can make accessible types from:

```text
package A
package B
```

available by simple name.

It does not simply expose arbitrary types from:

```text
internal package C
```

Think:

```text
                 module X
                    │
           ┌────────┴────────┐
           ↓                 ↓
    exported packages    non-exported
           │              packages
           ↓                 ↓
    accessible types         ✗
           │
           ↓
   available by
     simple name
```

Memory:

> **Contained in module ≠ imported by module import. The package must be exported and the type accessible.**

### Traditional Imports Can Still Be Used

A module import does not prevent traditional imports.

This is legal:

```java
import module java.base;
import java.util.List;
```

The explicit `List` import is redundant in this example because `List` is already available through the module import.

---

## Import Conflicts

Module imports can introduce simple-name conflicts.

Suppose:

```text
module A
  ↓
package.one.Widget

module B
  ↓
package.two.Widget
```

and both modules are imported:

```java
import module A;
import module B;
```

Then:

```java
Widget w;
```

may be ambiguous.

Java cannot guess which `Widget` is intended.

A qualified name can resolve the distinction:

```java
package.one.Widget w;
```

Memory:

> **A module import can introduce ambiguity just like other imports.**

Do not assume:

```text
module import
→ every simple name automatically unambiguous
```

---

## Compact Source Files

Module imports connect directly with another Java 25 feature: **compact source files**.

A compact source file might contain:

```java
void main() {
    var date = LocalDate.now();

    System.out.println(date);
}
```

Compact source files have the equivalent of an implicit module import of:

```java
java.base
```

Therefore an explicit:

```java
import java.time.LocalDate;
```

is not required here.

Think:

```text
COMPACT SOURCE FILE
        ↓
implicit java.base module import
        ↓
exported java.base packages
        ↓
accessible types
```

For the other compact-source rules, see **Compact Source Files & Instance Main Methods**.

---

# Quick Reference

## Module Import

```java
import module java.base;
```

Think:

```text
module
  ↓
exported packages
  ↓
accessible types
  ↓
simple-name use
```

Not:

```text
module
  ↓
EVERYTHING physically inside it
```

## `java.lang` vs `java.base`

```text
java.lang
→ package
→ automatically imported

java.base
→ module
→ contains/exports many packages
```

Therefore:

```java
String s;
```

needs no import.

But normally:

```java
List<String> list;
```

does.

Java 25 can make `List` available through:

```java
import module java.base;
```

## Package Wildcard

```java
import java.util.*;
```

includes accessible types directly in:

```text
java.util
```

but not automatically:

```text
java.util.function
java.util.stream
```

Memory:

```text
package wildcard
≠ recursive
```

## Module vs Package

```text
import java.util.*;
        ↑
      PACKAGE


import module java.base;
              ↑
            MODULE
```

## `java.base` Recognition

Common exported packages include:

```text
java.lang
java.util
java.util.function
java.util.stream
java.io
java.nio.file
java.time
java.util.concurrent
```

But:

```text
java.base
≠ entire JDK
```

## Export Rule

For a type to become available through a module import, think:

```text
Is it in the imported module?
        ↓
Is its package exported?
        ↓
Is the type accessible?
        ↓
Is its simple name unambiguous?
```

## Compact Source Files

```text
compact source file
        ↓
implicit module import of java.base
```

So common exported `java.base` types can be used without individual imports.

## Reliable Check

When you see:

```java
import module X;
```

check:

```text
1. Which module contains the type?

2. Is the type's package exported
   by that module?

3. Is the type accessible?

4. Is another imported type using
   the same simple name?

5. Don't confuse module imports
   with recursive package imports.
```

## Final Memory Kicks

> **`import module X` makes accessible types from packages exported by module X available by simple name.**

> **A module import does not expose every package physically contained in the module.**

> **`java.base` is a module; `java.lang` is a package.**

> **`java.lang` is automatically imported even without `import module java.base`.**

> **`java.base` contains far more than `java.lang`, including collections, streams, I/O and date/time APIs.**

> **Package wildcard imports are not recursive: `java.util.*` does not import `java.util.stream.*`.**

> **Module imports can introduce simple-name ambiguity.**

> **Compact source files implicitly receive the equivalent of a `java.base` module import.**

> **MODULE → PACKAGE → TYPE. Keep those three levels separate.**