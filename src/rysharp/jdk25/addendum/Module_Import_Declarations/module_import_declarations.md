# Module Import Declarations

Java 25 introduces **module import declarations**, allowing a source file to import the accessible types exported by an entire module rather than importing packages individually.

The syntax is:

```java
import module java.base;
```

This is particularly useful with `java.base`, which contains many of the packages commonly used by Java programs.

---

## 1. The basic idea

Traditionally, imports look like:

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

Java 25 allows:

```java
import module java.base;
```

This can make types exported by `java.base` available by their simple names:

```java
import module java.base;

class Example {
    List<String> names = new ArrayList<>();
    LocalDate date = LocalDate.now();
}
```

So the mental model is:

> **`import module X` → make the accessible exported types of module X available for simple-name use.**

It does **not** mean:

> "Import absolutely everything contained inside the module."

Only accessible types from **exported packages** are considered.

---

# 2. `java.base` is much bigger than `java.lang`

One easy exam trap is assuming that because `String` works without an import, `java.base` is effectively just `java.lang`.

It isn't.

`java.lang` is **automatically imported** by every Java source file.

For example:

```java
String name = "Ryan";
Integer number = 10;
System.out.println(name);
```

requires no explicit imports because these types are in `java.lang`.

But `java.base` contains many other packages, including:

```text
java.util
java.util.function
java.util.stream
java.io
java.nio.file
java.time
java.util.concurrent
java.util.regex
...
```

So:

```java
List<String> list = new ArrayList<>();
```

normally requires imports from `java.util`.

With:

```java
import module java.base;
```

those exported types can be used without individual imports.

---

# 3. The common `java.base` packages worth knowing

You do not need to memorise every package in `java.base` for the exam.

These are the ones most useful to recognise:

| Package                | Think of it as          | Common examples                                                    |
| ---------------------- | ----------------------- | ------------------------------------------------------------------ |
| `java.lang`            | Core Java               | `String`, `Object`, `System`, `Math`, `Integer`, `Exception`       |
| `java.util`            | Collections & utilities | `List`, `Set`, `Map`, `ArrayList`, `HashMap`, `Arrays`, `Optional` |
| `java.util.function`   | Functional interfaces   | `Function`, `Predicate`, `Consumer`, `Supplier`                    |
| `java.util.stream`     | Streams                 | `Stream`, `IntStream`, `Collectors`                                |
| `java.io`              | Traditional I/O         | `File`, `InputStream`, `OutputStream`, `Reader`, `Writer`          |
| `java.nio.file`        | Modern file API         | `Path`, `Paths`, `Files`                                           |
| `java.time`            | Date/time               | `LocalDate`, `LocalTime`, `LocalDateTime`, `Duration`, `Period`    |
| `java.util.concurrent` | Concurrency             | `ExecutorService`, `Executors`, `Future`, `ConcurrentHashMap`      |
| `java.util.regex`      | Regular expressions     | `Pattern`, `Matcher`                                               |
| `java.lang.annotation` | Annotations             | `Annotation`                                                       |
| `java.lang.reflect`    | Reflection              | `Method`, `Field`, `Constructor`                                   |

A useful exam-memory cluster is:

> **`java.base` → `java.lang` + `java.util` + `java.util.function` + `java.util.stream` + `java.io` + `java.nio.file` + `java.time`**

You don't need to treat this as an exhaustive list.

---

# 4. Packages are not automatically recursive

A very important general Java rule still applies:

> **A package does not automatically include its subpackages.**

For example:

```text
java.util
java.util.function
java.util.stream
```

are three separate packages.

This:

```java
import java.util.*;
```

allows:

```java
List
Map
ArrayList
Optional
```

but does **not** allow:

```java
Function
Predicate
Stream
Collectors
```

because those belong to:

```text
java.util.function
java.util.stream
```

respectively.

You would traditionally need:

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
```

A module import can replace those separate package imports:

```java
import module java.base;
```

because all three packages are exported by `java.base`.

---

# 5. Module import vs wildcard package import

These look similar but operate at different levels.

### Package wildcard

```java
import java.util.*;
```

means:

> Import accessible types from `java.util`.

It does **not** include subpackages.

### Module import

```java
import module java.base;
```

means:

> Import accessible types from the packages exported by `java.base`.

So conceptually:

```text
package import:

java.util.*
     ↓
java.util only


module import:

java.base
     ↓
exported packages
     ↓
types from those packages
```

This is why a module import can bring in types from several different packages.

---

# 6. Exported packages matter

A module can contain packages that it does not export.

For example, conceptually:

```text
module X
 ├── exported package A
 ├── exported package B
 └── internal package C
```

An import of the module gives access to types from:

```text
A
B
```

but not arbitrary types from:

```text
C
```

So remember:

> **Module import works with exported packages, not every package physically contained in the module.**

This is one of the main reasons that:

```java
import module java.base;
```

should not be mentally interpreted as:

```text
"import everything in java.base"
```

---

# 7. Explicit imports can still be used

A module import doesn't prevent you from also having traditional imports.

For example:

```java
import module java.base;
import java.util.List;
```

is legal.

However, the explicit `List` import is redundant because `List` is already available through the module import.

You therefore wouldn't normally write both unless there were some other reason to do so.

---

# 8. Module imports can cause ambiguity

This is one of the more interesting exam traps.

Suppose two imported modules both export a type with the same simple name.

Conceptually:

```text
module A
    └── package.one.Widget

module B
    └── package.two.Widget
```

If both modules are imported:

```java
import module A;
import module B;
```

then:

```java
Widget w;
```

could be ambiguous.

Java cannot simply guess which `Widget` you mean.

You may need to use a qualified name:

```java
package.one.Widget w;
```

The important point is:

> **Module imports can introduce simple-name conflicts just like other forms of import.**

So don't assume that importing a module makes every simple name unambiguous.

---

# 9. `java.lang` remains automatic

This distinction is worth memorising.

You do **not** need:

```java
import java.lang.String;
```

because `java.lang` is automatically imported.

Likewise, you don't need:

```java
import java.lang.System;
```

But this:

```java
List<String> list = new ArrayList<>();
```

normally requires an import because `java.util` isn't automatically imported.

You can solve that with:

```java
import java.util.List;
import java.util.ArrayList;
```

or:

```java
import java.util.*;
```

or, in Java 25:

```java
import module java.base;
```

---

# 10. Module imports and compact source files

This becomes particularly interesting with **compact source files**, another Java 25 feature.

A compact source file can look like:

```java
void main() {
    System.out.println("Hello");
}
```

There is no explicit class declaration.

Compact source files have an implicit module import for `java.base`.

Conceptually, you can therefore use common `java.base` types without writing explicit imports.

For example:

```java
void main() {
    var date = LocalDate.now();
    System.out.println(date);
}
```

You don't need to add:

```java
import java.time.LocalDate;
```

because the compact source file gets the equivalent of a module import for `java.base`.

This connects two Java 25 features:

```text
Compact source file
        ↓
implicit java.base module import
        ↓
types exported by java.base
```

---

# 11. A useful example

Consider:

```java
import module java.base;

class Example {

    List<String> names = new ArrayList<>();

    LocalDate today = LocalDate.now();

    Predicate<String> valid =
        s -> !s.isBlank();

    Stream<String> stream =
        names.stream();
}
```

All of these are available because their packages are exported by `java.base`:

```text
List          → java.util
ArrayList     → java.util
LocalDate     → java.time
Predicate     → java.util.function
Stream        → java.util.stream
```

This is why `import module java.base;` is considerably more powerful than:

```java
import java.lang.*;
```

---

# 12. What `import module java.base` does NOT mean

It does not mean:

### "Everything in Java"

```text
❌ java.sql
❌ java.desktop
❌ java.net.http
```

Those belong to other modules.

For example, `java.net.http` is associated with the `java.net.http` module, not `java.base`.

### "Every package in java.base"

Only accessible types from exported packages are made available.

### "All subpackages of a package"

Normal package hierarchy rules still apply.

### "The same thing as `import java.base.*`"

There is no such syntax.

A module and a package are different levels of the Java module/package system.

---

# 13. Exam mental model

When you see:

```java
import module java.base;
```

think:

```text
                 java.base
                    │
           ┌────────┴────────┐
           ↓                 ↓
     exported packages   non-exported
           │              packages
           ↓                 ↓
    accessible types       ❌
           │
           ↓
    available by
    simple name
```

Then ask:

1. **Is the type in `java.base`?**
2. **Is its package exported?**
3. **Is the type accessible?**
4. **Is there a name conflict with another imported type?**

If yes to the first three and no problematic conflict exists, the simple name can be used.

---

# 14. Exam-ready examples

### Example 1

```java
import module java.base;

class Test {
    List<String> names = new ArrayList<>();
}
```

✅ Compiles.

`List` and `ArrayList` are in exported `java.util`.

---

### Example 2

```java
import module java.base;

class Test {
    LocalDate date = LocalDate.now();
}
```

✅ Compiles.

`LocalDate` is in `java.time`, which is part of `java.base`.

---

### Example 3

```java
import module java.base;

class Test {
    Function<String, Integer> f = String::length;
}
```

✅ Compiles.

`Function` is in `java.util.function`, which is exported by `java.base`.

---

### Example 4

```java
import java.util.*;

class Test {
    Stream<String> stream;
}
```

❌ Does not compile just because `Stream` is related to `java.util`.

`Stream` is in:

```text
java.util.stream
```

not `java.util`.

---

### Example 5

```java
import module java.base;

class Test {
    String value = "Hello";
}
```

✅ Compiles.

But remember that `String` would work **even without the module import**, because `java.lang` is automatically imported.

---

# 15. The core distinction to remember

There are three levels worth keeping separate:

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

Traditional wildcard import:

```java
import java.util.*;
```

operates at the **package** level.

Module import:

```java
import module java.base;
```

operates at the **module** level.

And:

```java
java.lang.String
```

refers to a specific **type**.

---

# 16. Runnable Example

You can find a runnable example here:
`src\rysharp\jdk25\addendum\Module_Import_Declarations\code\ModuleImportExample.java`

---

## One-line memory aid

> **`import module X` gives you accessible types from the packages exported by module X; it does not import everything in the module, and `java.lang` is already automatic.**
