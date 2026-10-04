# Practice Exam 1 — Review

Exam 1 of 6 from [Java 25 Professional Certification - 6 full Tests (1Z0-831)](https://www.udemy.com/course/ocp-oracle-certified-professional-java-developer-prep/).

## 📝 Results

**Score:** 32 / 50 (64%)  
**Pass mark:** 68%  
**Difference from pass:** 2 questions

## 🔎 Overview

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

## ❌ Incorrect Answers

| Question | Area | What Went Wrong | What to Review | Repo Section |
|---|---|---|---|---|
| Q2 | `ConcurrentHashMap` constructors | Did not know that `(int, float)` is a valid constructor and interpreted the second `int` argument as a concurrency level. | `ConcurrentHashMap` constructors; primitive widening during overload resolution; meaning of `concurrencyLevel`. | [**13 - Concurrency**](../base/chapter/_13_Concurrency/README.md) |
| Q4 | `Stream.reduce()` | Did not realise that the identity value is the initial accumulator. | `reduce(identity, BinaryOperator)` and how the identity participates in reduction. | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q11 | `Deque` | Initially had the correct answer but changed it after treating the deque as inherently LIFO. | `offer/poll/peek` as queue operations versus `push/pop` as stack operations. | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q13 | `Collectors.joining()` | Did not securely know the three-argument parameter order. | `joining(delimiter, prefix, suffix)`. | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q16 | `SimpleDateFormat` | Confused `MMM` with `MMMM`. | Date/time formatting symbols: particularly `M`, `MM`, `MMM`, `MMMM` and `d`/`dd`. | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q19 | Constructors | Missed that a constructor cannot be declared `synchronized`. | Valid constructor modifiers and method-reference distractions such as `Test::new`. | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md) |
| Q20 | Exceptions / `finally` | Applied try-with-resources suppression rules to an ordinary `finally` block. | Ordinary `finally` exception replacement versus try-with-resources suppressed exceptions. | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| Q25 | I/O constructors and `Files.write()` | Was uncertain which classes can be constructed from a filename and whether `Files.write()` is valid. | `BufferedWriter`, `FileWriter`, `PrintWriter`, `FileOutputStream`, `Files.write()`, `Files.newBufferedWriter()`. | [**14 - Input/Output**](../base/chapter/_14_Input_Output/README.md) |
| Q28 | Stream laziness | Thought `peek()` would process the entire source before `limit()` was applied. | Lazy vertical stream processing and short-circuiting operations such as `limit()`. | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q30 | Interface methods | Thought a child interface could declare a static method with the same signature as an inherited default method. | Static versus instance/default methods across interface inheritance. | [**07 - Beyond Classes**](../base/chapter/_07_Beyond_Classes/README.md) |
| Q31 | JPMS | Did not securely distinguish unnamed modules from automatic modules, particularly readability and exports. | Explicit named vs automatic vs unnamed modules; readability versus exports. | [**12 - Modules**](../base/chapter/_12_Modules/README.md) |
| Q33 | `Stream.reduce()` | Did not recognise that the no-identity overload returns `Optional<T>`; method-reference form also made the operation less obvious. | `reduce(BinaryOperator)` versus `reduce(identity, ...)`; `String::concat` as a `BinaryOperator<String>`. | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q34 | `Files.move()` / `Files.delete()` | Reasoned about the initial filesystem state but did not update the state after `move()`. | `Files.move`, `REPLACE_EXISTING`, `Files.delete`, `deleteIfExists`, and state changes between operations. | [**14 - Input/Output**](../base/chapter/_14_Input_Output/README.md) |
| Q37 | Lambda return conversion | Second-guessed valid code because `Long.valueOf(int)` looked suspicious. | `Long.valueOf(long)`, widening + unboxing, and lambda result compatibility. | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| Q45 | `jdeps` | Did not know the exact command-line options and selected the real `--generate-open-module` instead of fake `--check-deps`. | `jdeps`: `--check`, `--list-deps`, `--list-reduced-deps`, `--print-module-deps`, `--generate-module-info`, `--generate-open-module`. | [**12 - Modules**](../base/chapter/_12_Modules/README.md) |
| Q46 | Inner / nested classes | Knew which class was static but mixed up the construction syntax. | Non-static inner: `new Outer().new Inner()`; static nested: `new Outer.Nested()`. | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md) |
| Q47 | `TreeSet.headSet()` | Had never encountered the API and therefore could not infer its exact boundary behaviour. | `headSet`, `tailSet`, `subSet`; inclusive/exclusive boundaries; backed views. | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q49 | Lambda terminology | Understood that a lambda can contain multiple statements but did not recognise that the entire `{ ... }` construct is one **block** body. | Lambda grammar: body is either an **expression** or a **block**. | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |

---

## ✅ Correct Answers That Exposed Uncertainty 🤔

These did not cost marks, so they are lower priority. They are worth recognising so that the same API does not cause hesitation on a later exam.

| Question | Area | Observation | Repo Section |
|---|---|---|---|
| Q3 | Functional interfaces | Correct but took time determining which inherited/default/static methods count toward the SAM. | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| Q8 | `DoubleStream` | Correct answer for the wrong reason. The `int` argument can widen to `double`; the actual problem was `Predicate<Double>` versus `DoublePredicate`. | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q12 | `Duration.dividedBy()` | API was unfamiliar, but the behaviour was inferred correctly. | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q18 | `Boolean.logicalAnd/Or/Xor` | Methods were unfamiliar but correctly inferred. | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q21 | `Period` vs `Duration` | Correct but uncertain about why `Duration.between(LocalDate, LocalDate)` fails at runtime. | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q29 | Try-with-resources | Correctly identified the body exception as primary and the close exception as suppressed. Confirms that this half of exception suppression is understood. | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| Q32 | Class initialisation | Correctly applied static initialisation before instance initialisation. Previous revision has stuck. | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md) |
| Q35 | `ResourceBundle` | Correctly remembered that a class-based resource bundle takes precedence over a properties file for the same candidate. | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| Q36 | `Collections.copy()` | Correct through elimination, but `Collections.copy()` and `singletonList()` behaviour were not secure. | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q42 | `Stream.builder()` | Correct answer, but did not know `Stream.builder()` existed. Generic inference in the chained call was unfamiliar. | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q44 | Records | Correct, but was surprised that an additional static field is not implicitly final and receives its normal default value. | [**07 - Beyond Classes**](../base/chapter/_07_Beyond_Classes/README.md) |
| Q50 | `DateTimeFormatter` | Correct through API reasoning without knowing `ISO_WEEK_DATE` in advance. | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |

---

## 🚩 Review Priority

| Priority                            | Repo Section                             | Evidence from Exam                                                       | Review Focus                                                                                   |
|-------------------------------------|------------------------------------------|--------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| 🔴 **High**                         | [**10 - Streams**](../base/chapter/_10_Streams/README.md)                         | Q4, Q13, Q28, Q33 incorrect; Q8 correct for wrong reason; Q42 unfamiliar | `reduce` overloads, laziness, short-circuiting, collectors, primitive streams, stream creation |
| 🔴 **High**                         | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md)          | Q11 and Q47 incorrect; Q36 uncertain                                     | `Deque`, `TreeSet`/`NavigableSet`, collection utility methods                                  |
| 🟠 **Medium-High**                  | [**14 - Input/Output**](../base/chapter/_14_Input_Output/README.md)                    | Q25 and Q34 incorrect                                                    | Writer/output constructors, `Files` methods, move/delete semantics                             |
| 🟠 **Medium**                       | [**12 - Modules**](../base/chapter/_12_Modules/README.md)                         | Q31 conceptual miss; Q45 tool-option recall                              | unnamed/automatic/explicit modules; exports/readability; `jdeps`                               |
| 🟠 **Medium**                       | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) | Q37 and Q49 incorrect; Q3 slow                                           | lambda body grammar, result compatibility, SAM identification                                  |
| 🟠 **Medium**                       | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md)                    | Q19 and Q46 incorrect; Q32 strong                                        | inner/static nested classes and constructor edge cases                                         |
| 🟡 **Targeted**                     | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md)       | Q20 incorrect; Q29 and Q35 correct                                       | ordinary `finally` versus TWR suppression                                                      |
| 🟡 **Targeted**                     | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md)                       | Q16 incorrect; several unfamiliar-but-correct APIs                       | date/time formatting and recognition of less-common APIs                                       |
| 🟡 **Targeted**                     | [**07 - Beyond Classes**](../base/chapter/_07_Beyond_Classes/README.md)                  | Q30 incorrect; Q44 correct                                               | interface static/default inheritance rules                                                     |
| 🟡 **Targeted**                     | [**13 - Concurrency**](../base/chapter/_13_Concurrency/README.md)                     | Q2 incorrect                                                             | `ConcurrentHashMap` constructors                                                               |
| 🟢 **No specific issue identified** | [**01 - Building Blocks**](../base/chapter/_01_Building_Blocks/README.md)                 | No clear weakness from reviewed questions                                | Normal revision only                                                                           |
| 🟢 **No specific issue identified** | [**02 - Operators**](../base/chapter/_02_Operators/README.md)                       | No clear weakness from reviewed questions                                | Normal revision only                                                                           |
| 🟢 **No specific issue identified** | [**03 - Making Decisions**](../base/chapter/_03_Making_Decisions/README.md)                | No clear weakness from reviewed questions                                | Normal revision only                                                                           |
| 🟢 **No specific issue identified** | [**05 - Methods**](../base/chapter/_05_Methods/README.md)                         | No clear weakness from reviewed questions                                | Normal revision only                                                                           |

---

## JDK 25 Addendum

No incorrect answer from this practice exam clearly indicates a weakness in one of the dedicated JDK 25 addendum topics.

| Addendum Topic                                           | Exam 1 Finding                  |
|----------------------------------------------------------|---------------------------------|
| [Flexible Constructor Bodies (JEP 513)](../addendum/Flexible_Constructor_Bodies/README.md)                    | No specific weakness identified |
| [Unnamed Variables and Patterns (`_`)](../addendum/Unnamed_Variables/README.md)                     | No specific weakness identified |
| [Module Import Declarations (JEP 511)](../addendum/Module_Import_Declarations/README.md)                     | No specific weakness identified |
| [Compact Source Files and Instance Main Methods (JEP 512)](../addendum/Compact_Source_Files_and_Instance_Main_Methods/README.md) | No specific weakness identified |
| [Stream Gatherers](../addendum/Stream_Gatherers/README.md)                                         | No specific weakness identified |
| [Scoped Values (JEP 506)](../addendum/Scoped_Values/README.md)                                  | No specific weakness identified |
| [Minor API Additions](../addendum/Minor_Api_Additions/README.md)                                      | No specific weakness identified |

The module questions in Q31 and Q45 belong more naturally under **12 - Modules**, because they concern existing JPMS concepts and `jdeps`, rather than the JDK 25 [module-import addition](../addendum/Module_Import_Declarations/README.md).

Likewise, the Stream questions concern the established Stream API rather than JDK 25 [Stream Gatherers](../addendum/Stream_Gatherers/README.md).

---

## Overall Assessment

The 64% score was only **two questions below the 68% pass mark**, but the objective is not merely to recover two marks.

The strongest revision target from this exam is [**10 - Streams**](../base/chapter/_10_Streams/README.md). Multiple independent questions exposed gaps in reduction, laziness, collectors and primitive stream APIs.

The next useful cluster is **[Collections](../base/chapter/_09_Collections_and_Generics/README.md) + [I/O](../base/chapter/_14_Input_Output/README.md)**, where several less-familiar APIs caused uncertainty or incorrect answers.

The remaining errors are comparatively narrow language/API rules that can be repaired individually rather than requiring complete chapter rereads.

The JDK 25 addendum material did **not** emerge as a significant weakness in this exam. Most lost marks came from established Java language and library material already represented by the Java 21 chapter structure.