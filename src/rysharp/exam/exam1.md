# Practice Exam 1 — Review

**Score:** 32 / 50 (64%)  
**Pass mark:** 68%  
**Difference from pass:** 2 questions

## Overview

The first full practice exam was very close to a pass. The incorrect answers were not all caused by the same problem.

The main patterns were:

- Stream operations and stream API details were the clearest recurring weakness.
- Several marks were lost through unfamiliar or imprecisely remembered library APIs.
- Some core language rules were understood generally but not securely enough for exam edge cases.
- I/O/NIO API behaviour needs some reinforcement.
- A few questions were lost through second-guessing answers that initially looked correct.
- Some correct answers also exposed APIs that were unfamiliar and should be recognised in future exams.

The goal is therefore **targeted revision**, rather than rereading every chapter.

---

## Incorrect Answers

| Question | Area | What Went Wrong | What to Review | Repo Section |
|---|---|---|---|---|
| Q2 | `ConcurrentHashMap` constructors | Did not know that `(int, float)` is a valid constructor and interpreted the second `int` argument as a concurrency level. | `ConcurrentHashMap` constructors; primitive widening during overload resolution; meaning of `concurrencyLevel`. | **13 - Concurrency** |
| Q4 | `Stream.reduce()` | Did not realise that the identity value is the initial accumulator. | `reduce(identity, BinaryOperator)` and how the identity participates in reduction. | **10 - Streams** |
| Q11 | `Deque` | Initially had the correct answer but changed it after treating the deque as inherently LIFO. | `offer/poll/peek` as queue operations versus `push/pop` as stack operations. | **09 - Collections & Generics** |
| Q13 | `Collectors.joining()` | Did not securely know the three-argument parameter order. | `joining(delimiter, prefix, suffix)`. | **10 - Streams** |
| Q16 | `SimpleDateFormat` | Confused `MMM` with `MMMM`. | Date/time formatting symbols: particularly `M`, `MM`, `MMM`, `MMMM` and `d`/`dd`. | **04 - Core APIs** |
| Q19 | Constructors | Missed that a constructor cannot be declared `synchronized`. | Valid constructor modifiers and method-reference distractions such as `Test::new`. | **06 - Class Design** |
| Q20 | Exceptions / `finally` | Applied try-with-resources suppression rules to an ordinary `finally` block. | Ordinary `finally` exception replacement versus try-with-resources suppressed exceptions. | **11 - Exceptions & Localisation** |
| Q25 | I/O constructors and `Files.write()` | Was uncertain which classes can be constructed from a filename and whether `Files.write()` is valid. | `BufferedWriter`, `FileWriter`, `PrintWriter`, `FileOutputStream`, `Files.write()`, `Files.newBufferedWriter()`. | **14 - Input/Output** |
| Q28 | Stream laziness | Thought `peek()` would process the entire source before `limit()` was applied. | Lazy vertical stream processing and short-circuiting operations such as `limit()`. | **10 - Streams** |
| Q30 | Interface methods | Thought a child interface could declare a static method with the same signature as an inherited default method. | Static versus instance/default methods across interface inheritance. | **07 - Beyond Classes** |
| Q31 | JPMS | Did not securely distinguish unnamed modules from automatic modules, particularly readability and exports. | Explicit named vs automatic vs unnamed modules; readability versus exports. | **12 - Modules** |
| Q33 | `Stream.reduce()` | Did not recognise that the no-identity overload returns `Optional<T>`; method-reference form also made the operation less obvious. | `reduce(BinaryOperator)` versus `reduce(identity, ...)`; `String::concat` as a `BinaryOperator<String>`. | **10 - Streams** |
| Q34 | `Files.move()` / `Files.delete()` | Reasoned about the initial filesystem state but did not update the state after `move()`. | `Files.move`, `REPLACE_EXISTING`, `Files.delete`, `deleteIfExists`, and state changes between operations. | **14 - Input/Output** |
| Q37 | Lambda return conversion | Second-guessed valid code because `Long.valueOf(int)` looked suspicious. | `Long.valueOf(long)`, widening + unboxing, and lambda result compatibility. | **08 - Lambdas & Functional Interfaces** |
| Q45 | `jdeps` | Did not know the exact command-line options and selected the real `--generate-open-module` instead of fake `--check-deps`. | `jdeps`: `--check`, `--list-deps`, `--list-reduced-deps`, `--print-module-deps`, `--generate-module-info`, `--generate-open-module`. | **12 - Modules** |
| Q46 | Inner / nested classes | Knew which class was static but mixed up the construction syntax. | Non-static inner: `new Outer().new Inner()`; static nested: `new Outer.Nested()`. | **06 - Class Design** |
| Q47 | `TreeSet.headSet()` | Had never encountered the API and therefore could not infer its exact boundary behaviour. | `headSet`, `tailSet`, `subSet`; inclusive/exclusive boundaries; backed views. | **09 - Collections & Generics** |
| Q49 | Lambda terminology | Understood that a lambda can contain multiple statements but did not recognise that the entire `{ ... }` construct is one **block** body. | Lambda grammar: body is either an **expression** or a **block**. | **08 - Lambdas & Functional Interfaces** |

---

## Correct Answers That Exposed Uncertainty

These did not cost marks, so they are lower priority. They are worth recognising so that the same API does not cause hesitation on a later exam.

| Question | Area | Observation | Repo Section |
|---|---|---|---|
| Q3 | Functional interfaces | Correct but took time determining which inherited/default/static methods count toward the SAM. | **08 - Lambdas & Functional Interfaces** |
| Q8 | `DoubleStream` | Correct answer for the wrong reason. The `int` argument can widen to `double`; the actual problem was `Predicate<Double>` versus `DoublePredicate`. | **10 - Streams** |
| Q12 | `Duration.dividedBy()` | API was unfamiliar, but the behaviour was inferred correctly. | **04 - Core APIs** |
| Q18 | `Boolean.logicalAnd/Or/Xor` | Methods were unfamiliar but correctly inferred. | **04 - Core APIs** |
| Q21 | `Period` vs `Duration` | Correct but uncertain about why `Duration.between(LocalDate, LocalDate)` fails at runtime. | **04 - Core APIs** |
| Q29 | Try-with-resources | Correctly identified the body exception as primary and the close exception as suppressed. Confirms that this half of exception suppression is understood. | **11 - Exceptions & Localisation** |
| Q32 | Class initialisation | Correctly applied static initialisation before instance initialisation. Previous revision has stuck. | **06 - Class Design** |
| Q35 | `ResourceBundle` | Correctly remembered that a class-based resource bundle takes precedence over a properties file for the same candidate. | **11 - Exceptions & Localisation** |
| Q36 | `Collections.copy()` | Correct through elimination, but `Collections.copy()` and `singletonList()` behaviour were not secure. | **09 - Collections & Generics** |
| Q42 | `Stream.builder()` | Correct answer, but did not know `Stream.builder()` existed. Generic inference in the chained call was unfamiliar. | **10 - Streams** |
| Q44 | Records | Correct, but was surprised that an additional static field is not implicitly final and receives its normal default value. | **07 - Beyond Classes** |
| Q50 | `DateTimeFormatter` | Correct through API reasoning without knowing `ISO_WEEK_DATE` in advance. | **04 - Core APIs** |

---

## Review Priority

| Priority | Repo Section | Evidence from Exam | Review Focus |
|---|---|---|---|
| 🔴 **High** | **10 - Streams** | Q4, Q13, Q28, Q33 incorrect; Q8 correct for wrong reason; Q42 unfamiliar | `reduce` overloads, laziness, short-circuiting, collectors, primitive streams, stream creation |
| 🔴 **High** | **09 - Collections & Generics** | Q11 and Q47 incorrect; Q36 uncertain | `Deque`, `TreeSet`/`NavigableSet`, collection utility methods |
| 🟠 **Medium-High** | **14 - Input/Output** | Q25 and Q34 incorrect | Writer/output constructors, `Files` methods, move/delete semantics |
| 🟠 **Medium** | **12 - Modules** | Q31 conceptual miss; Q45 tool-option recall | unnamed/automatic/explicit modules; exports/readability; `jdeps` |
| 🟠 **Medium** | **08 - Lambdas & Functional Interfaces** | Q37 and Q49 incorrect; Q3 slow | lambda body grammar, result compatibility, SAM identification |
| 🟠 **Medium** | **06 - Class Design** | Q19 and Q46 incorrect; Q32 strong | inner/static nested classes and constructor edge cases |
| 🟡 **Targeted** | **11 - Exceptions & Localisation** | Q20 incorrect; Q29 and Q35 correct | ordinary `finally` versus TWR suppression |
| 🟡 **Targeted** | **04 - Core APIs** | Q16 incorrect; several unfamiliar-but-correct APIs | date/time formatting and recognition of less-common APIs |
| 🟡 **Targeted** | **07 - Beyond Classes** | Q30 incorrect; Q44 correct | interface static/default inheritance rules |
| 🟡 **Targeted** | **13 - Concurrency** | Q2 incorrect | `ConcurrentHashMap` constructors |
| 🟢 **No specific issue identified** | **01 - Building Blocks** | No clear weakness from reviewed questions | Normal revision only |
| 🟢 **No specific issue identified** | **02 - Operators** | No clear weakness from reviewed questions | Normal revision only |
| 🟢 **No specific issue identified** | **03 - Making Decisions** | No clear weakness from reviewed questions | Normal revision only |
| 🟢 **No specific issue identified** | **05 - Methods** | No clear weakness from reviewed questions | Normal revision only |

---

## JDK 25 Addendum

No incorrect answer from this practice exam clearly indicates a weakness in one of the dedicated JDK 25 addendum topics.

| Addendum Topic | Exam 1 Finding |
|---|---|
| Flexible Constructor Bodies (JEP 513) | No specific weakness identified |
| Unnamed Variables and Patterns (`_`) | No specific weakness identified |
| Module Import Declarations (JEP 511) | No specific weakness identified |
| Compact Source Files and Instance Main Methods (JEP 512) | No specific weakness identified |
| Stream Gatherers | No specific weakness identified |
| Scoped Values (JEP 506) | No specific weakness identified |
| Minor API Additions | No specific weakness identified |

The module questions in Q31 and Q45 belong more naturally under **12 - Modules**, because they concern existing JPMS concepts and `jdeps`, rather than the JDK 25 module-import addition.

Likewise, the Stream questions concern the established Stream API rather than JDK 25 Stream Gatherers.

---

## Key Rules to Reinforce

### Streams

```java
reduce(identity, operator)       // identity starts the reduction
reduce(operator)                 // returns Optional<T>
```

Streams are lazy. Think of elements moving **vertically through the pipeline**, rather than each intermediate operation processing the whole collection first.

```text
source element
    ↓
  peek
    ↓
  limit
    ↓
terminal operation
```

Primitive streams use specialised functional interfaces:

```text
IntStream       → IntPredicate, IntFunction, ...
LongStream      → LongPredicate, LongFunction, ...
DoubleStream    → DoublePredicate, DoubleFunction, ...
```

### Collections

```text
offer + poll    → queue / FIFO
push  + pop     → stack / LIFO
```

For sorted sets:

```text
headSet(x)      → elements before x          (x excluded)
tailSet(x)      → elements from x onwards    (x included)
subSet(a, b)    → a inclusive, b exclusive
```

`Collections.copy(dest, src)` overwrites existing positions in `dest`; it does not create a new list or append elements.

### Inner vs Static Nested Classes

```java
// non-static inner
new Outer().new Inner();

// static nested
new Outer.Nested();
```

### Exceptions

```text
Ordinary finally:
try/catch pending exception A
finally throws B
→ B escapes
```

```text
Try-with-resources:
body throws A
close throws B
→ A primary
→ B suppressed
```

### Modules

```text
Explicit named module
- has a name
- has module-info.java
- selectively declares exports/requires

Automatic module
- has a name
- no module-info.java
- exports all packages

Unnamed module
- no name
- no module-info.java
- exports all packages
- reads named modules broadly
- cannot normally be required by a named module
```

Remember:

> **Export and readability are separate concepts.**

### Lambdas

A lambda body has exactly two grammatical forms:

```java
x -> expression
```

or:

```java
x -> {
    // block
}
```

A block may contain multiple statements, but the lambda body itself is still **one block**.

---

## Overall Assessment

The 64% score was only **two questions below the 68% pass mark**, but the objective is not merely to recover two marks.

The strongest revision target from this exam is **10 - Streams**. Multiple independent questions exposed gaps in reduction, laziness, collectors and primitive stream APIs.

The next useful cluster is **Collections + I/O**, where several less-familiar APIs caused uncertainty or incorrect answers.

The remaining errors are comparatively narrow language/API rules that can be repaired individually rather than requiring complete chapter rereads.

The JDK 25 addendum material did **not** emerge as a significant weakness in this exam. Most lost marks came from established Java language and library material already represented by the Java 21 chapter structure.