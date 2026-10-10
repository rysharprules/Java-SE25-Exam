# Practice Exam 1 — Review

[🔙 Back](../../../README.md)

Exam 1 of 6 from [Java 25 Professional Certification - 6 full Tests (1Z0-831)](https://www.udemy.com/course/ocp-oracle-certified-professional-java-developer-prep/).

## 📝 Results

**Score:** 32 / 50 (64%)  
**Pass mark:** 68%  
**Difference from pass:** 2 questions

## 🔎 Overview

The first full practice exam was very close to a pass. The result shows a reasonably broad foundation across the syllabus, with most weaknesses concentrated in a smaller number of areas rather than spread evenly across every chapter.

The main patterns were:

- Stream operations and Stream API details were the clearest recurring weakness.
- Collections & Generics contained several successful answers but also multiple API-specific gaps.
- I/O/NIO showed a mixture of correct understanding and weaker knowledge of particular constructors and `Files` operations.
- Modules and Lambdas & Functional Interfaces each contained more than one distinct area requiring reinforcement.
- Class Design, Core APIs, Exceptions/Localisation and Concurrency showed generally useful knowledge with narrower gaps.
- Several correct answers exposed APIs or rules that were not yet secure enough to answer confidently.
- Building Blocks, Operators, Making Decisions and Methods did not expose a specific weakness.
- The dedicated JDK 25 addendum topics were not directly tested.

The goal is therefore **targeted revision**, with the greatest attention given to recurring weaknesses while preserving areas that are already working.

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
| Q46 | Inner / nested classes | Knew which class was static but mixed up the construction syntax. | Non-static inner: `new Outer().new Inner()`; static nested: `new Outer.Nested()`. | [**07 - Beyond Classes**](../base/chapter/_07_Beyond_Classes/README.md) |
| Q47 | `TreeSet.headSet()` | Had never encountered the API and therefore could not infer its exact boundary behaviour. | `headSet`, `tailSet`, `subSet`; inclusive/exclusive boundaries; backed views. | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q49 | Lambda terminology | Understood that a lambda can contain multiple statements but did not recognise that the entire `{ ... }` construct is one **block** body. | Lambda grammar: body is either an **expression** or a **block**. | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |

---

## ✅ Correct Answers

All correct answers are logged here to retain evidence of strengths as well as weaknesses. Questions that exposed uncertainty are also examined separately below.

| Question | Repo Section |
|---|---|
| Q1 | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md) |
| Q3 | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| Q5 | [**12 - Modules**](../base/chapter/_12_Modules/README.md) |
| Q6 | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q7 | [**07 - Beyond Classes**](../base/chapter/_07_Beyond_Classes/README.md) |
| Q8 | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q9 | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| Q10 | [**05 - Methods**](../base/chapter/_05_Methods/README.md) |
| Q12 | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q14 | [**13 - Concurrency**](../base/chapter/_13_Concurrency/README.md) |
| Q15 | [**14 - Input/Output**](../base/chapter/_14_Input_Output/README.md) |
| Q17 | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q18 | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q21 | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q22 | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q23 | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q24 | [**13 - Concurrency**](../base/chapter/_13_Concurrency/README.md) |
| Q26 | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q27 | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| Q29 | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| Q32 | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md) |
| Q35 | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| Q36 | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q38 | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| Q39 | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q40 | [**14 - Input/Output**](../base/chapter/_14_Input_Output/README.md) |
| Q41 | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |
| Q42 | [**10 - Streams**](../base/chapter/_10_Streams/README.md) |
| Q43 | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md) |
| Q44 | [**07 - Beyond Classes**](../base/chapter/_07_Beyond_Classes/README.md) |
| Q48 | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q50 | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) |

---

## ✅ Correct Answers That Exposed Uncertainty 🤔

These answers were correct but exposed knowledge that was not completely secure. They are lower priority than incorrect answers but useful indicators for targeted revision.

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

| Priority | Repo Section | Evidence from Exam | Review Focus |
|---|---|---|---|
| 🔴 **High** | [**10 - Streams**](../base/chapter/_10_Streams/README.md) | ❌ Q4, Q13, Q28, Q33 · 🤔 Q8, Q42 · ✅ Q23, Q39 | `reduce` overloads, laziness, short-circuiting, collectors, primitive streams and stream creation |
| 🔴 **High** | [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) | ❌ Q11, Q47 · 🤔 Q36 · ✅ Q6, Q17, Q48 | `Deque`, `TreeSet`/`NavigableSet`, collection utility methods; reinforce API-specific gaps |
| 🟠 **Medium-High** | [**14 - Input/Output**](../base/chapter/_14_Input_Output/README.md) | ❌ Q25, Q34 · ✅ Q15, Q40 | Writer/output constructors, `Files` methods, move/delete semantics |
| 🟠 **Medium** | [**12 - Modules**](../base/chapter/_12_Modules/README.md) | ❌ Q31, Q45 · ✅ Q5 | unnamed/automatic/explicit modules; exports/readability; `jdeps` |
| 🟠 **Medium** | [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) | ❌ Q37, Q49 · 🤔 Q3 · ✅ Q9 | lambda body grammar, result compatibility, SAM identification |
| 🟡 **Targeted** | [**06 - Class Design**](../base/chapter/_06_Class_Design/README.md) | ❌ Q19 · 🤔 Q32 · ✅ Q1, Q43 | constructor modifiers and constructor/class edge cases |
| 🟡 **Targeted** | [**11 - Exceptions & Localisation**](../base/chapter/_11_Exceptions_and_Localisation/README.md) | ❌ Q20 · 🤔 Q29, Q35 · ✅ Q27, Q38 | ordinary `finally` versus TWR suppression; maintain localisation/exception knowledge |
| 🟡 **Targeted** | [**04 - Core APIs**](../base/chapter/_04_Core_APIs/README.md) | ❌ Q16 · 🤔 Q12, Q18, Q21, Q50 · ✅ Q22, Q26, Q41 | date/time formatting and recognition of less-common APIs |
| 🟡 **Targeted** | [**07 - Beyond Classes**](../base/chapter/_07_Beyond_Classes/README.md) | ❌ Q30, Q46 · 🤔 Q44 · ✅ Q7 | interface static/default inheritance rules; nested-class construction; record field rules |
| 🟡 **Targeted** | [**13 - Concurrency**](../base/chapter/_13_Concurrency/README.md) | ❌ Q2 · ✅ Q14, Q24 | `ConcurrentHashMap` constructors; maintain existing concurrency knowledge |
| 🟢 **No specific issue identified** | [**05 - Methods**](../base/chapter/_05_Methods/README.md) | ✅ Q10 | Normal revision only |
| 🟢 **No specific issue identified** | [**01 - Building Blocks**](../base/chapter/_01_Building_Blocks/README.md) | No questions mapped | Normal revision only |
| 🟢 **No specific issue identified** | [**02 - Operators**](../base/chapter/_02_Operators/README.md) | No questions mapped | Normal revision only |
| 🟢 **No specific issue identified** | [**03 - Making Decisions**](../base/chapter/_03_Making_Decisions/README.md) | No questions mapped | Normal revision only |

### Evidence Key

- ❌ Incorrect
- 🤔 Correct, but exposed uncertainty
- ✅ Correct without identified concern
---

## JDK 25 Addendum

Practice Exam 1 did not appear to directly test any of the dedicated JDK 25 addendum topics. The exam therefore provides no meaningful evidence of proficiency or weakness in these areas.

| Addendum Topic | Exam 1 Finding |
|---|---|
| [Flexible Constructor Bodies (JEP 513)](../addendum/Flexible_Constructor_Bodies/README.md) | Not directly tested |
| [Unnamed Variables and Patterns (`_`)](../addendum/Unnamed_Variables/README.md) | Not directly tested |
| [Module Import Declarations (JEP 511)](../addendum/Module_Import_Declarations/README.md) | Not directly tested |
| [Compact Source Files and Instance Main Methods (JEP 512)](../addendum/Compact_Source_Files_and_Instance_Main_Methods/README.md) | Not directly tested |
| [Stream Gatherers](../addendum/Stream_Gatherers/README.md) | Not directly tested |
| [Scoped Values (JEP 506)](../addendum/Scoped_Values/README.md) | Not directly tested |
| [Minor API Additions](../addendum/Minor_Api_Additions/README.md) | Not directly tested |

The module questions concern established JPMS concepts, `ServiceLoader` and `jdeps`, rather than JDK 25 module-import declarations.

Likewise, the Stream questions concern the established Stream API rather than JDK 25 Stream Gatherers.

Constructor chaining was tested, but not the Java 25 Flexible Constructor Bodies feature.

---

## Overall Assessment

The 64% score was only **two questions below the 68% pass mark**. The full answer record shows that the result was not caused by broad weakness across the syllabus: correct answers were spread across most of the Java 21 chapter areas tested.

The strongest revision target from this exam is [**10 - Streams**](../base/chapter/_10_Streams/README.md). Although several Stream questions were answered correctly, multiple independent questions exposed gaps in reduction, laziness, collectors and primitive stream APIs.

The next significant areas are [**09 - Collections & Generics**](../base/chapter/_09_Collections_and_Generics/README.md) and [**14 - Input/Output**](../base/chapter/_14_Input_Output/README.md). Both contain successful answers as well as mistakes, indicating specific API gaps rather than a complete lack of understanding.

[**12 - Modules**](../base/chapter/_12_Modules/README.md) and [**08 - Lambdas & Functional Interfaces**](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) contain multiple distinct gaps and warrant focused reinforcement.

The remaining chapters primarily show narrower issues alongside successful answers. Class Design, Concurrency, Core APIs, Beyond Classes and Exceptions/Localisation are therefore best approached through targeted revision of the identified gaps rather than comprehensive relearning.

Building Blocks, Operators, Making Decisions and Methods provide no specific evidence of weakness from this exam and require only normal revision.

The dedicated JDK 25 addendum topics were not directly tested, so PE1 provides little evidence either for or against proficiency in those areas.

Overall, PE1 indicates a **reasonably broad foundation with a small number of concentrated weaknesses**. The main purpose of the chapter sweep is therefore to close specific gaps, reinforce uncertain knowledge and preserve areas that are already working.