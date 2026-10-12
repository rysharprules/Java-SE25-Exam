# Practice Exam 2 — Review

[🔙 Back](../../../README.md)

Exam 2 of 6 from [Java 25 Professional Certification - 6 full Tests (1Z0-831)](https://www.udemy.com/course/ocp-oracle-certified-professional-java-developer-prep/).

## 📝 Results

| Metric | Result |
|---|---|
| Date | 12 October 2026 |
| Score | **36/50 (72%)** |
| Pass mark | 68% (34/50) |
| Outcome | **PASS** |
| Time taken | 1 hour 22 minutes |
| Time allowed | 2 hours |
| Previous exam | PE1: 32/50 (64%) |
| Improvement | **+4 questions / +8 percentage points** |

## 🔎 Overview

Practice Exam 2 was the first passing result, improving from 64% in PE1 to 72%.

All 50 questions were reviewed to distinguish genuine knowledge gaps from correct answers reached through reasoning, elimination or guesswork.

The main weaknesses were functional interface APIs, switch compilation rules, `var` array syntax and specific I/O methods. Exception handling was answered correctly but remains a priority for building confidence in execution order.

Several previously revised topics showed improvement, particularly collections, executor services and Java 25 unnamed variables.

## ❌ Incorrect Answers

| Question | Area | What Went Wrong | What to Review | Repo Section |
|---|---|---|---|---|
| **Q3** | Polymorphism | Assumed `o2.toString()` would use `Object.toString()` rather than the runtime `Integer` implementation. | Runtime method dispatch; declared versus runtime types; `equals()` | [Ch 6 — Class Design](../base/chapter/_06_Class_Design/README.md) |
| **Q11** | Switch | Expected `Seine, Corsica` if compilation succeeded, but thought `case 'a' \| 'b':` was invalid. | Constant expressions in case labels; bitwise OR; fall-through | [Ch 3 — Making Decisions](../base/chapter/_03_Making_Decisions/README.md) |
| **Q15** | Functional interfaces | Expected `7`, overlooking that `BiFunction` has no `compose()` method. | `Function.compose()` versus `andThen()`; `BiFunction` methods | [Ch 8 — Lambdas and Functional Interfaces](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| **Q17** | Console API | Guessed between `read()` and `reader()` and selected the wrong method. | `Console` methods; distinction between `Console.reader()` and `Reader.read()` | [Ch 14 — Input/Output](../base/chapter/_14_Input_Output/README.md) |
| **Q20** | Arrays / `var` | Knew `array5` was valid but overlooked `var array1 = new String[]{...}` while questioning other declarations. | Valid array declarations; `var` with arrays; array initializers | [Ch 1 — Building Blocks](../base/chapter/_01_Building_Blocks/README.md) |
| **Q26** | `var` | Interpreted the question as asking for valid rather than invalid declarations. Also exposed recurring uncertainty with array brackets. | `var` declaration restrictions; question wording; negative selection criteria | [Ch 1 — Building Blocks](../base/chapter/_01_Building_Blocks/README.md) |
| **Q28** | Concurrency | Knew `limit(25)` meant 25 values but assumed a sequential stream guaranteed ordered printing by separate virtual threads. | Task submission versus execution order; virtual-thread executors | [Ch 13 — Concurrency](../base/chapter/_13_Concurrency/README.md) |
| **Q29** | Pattern switch | Expected the `String` case to match; overlooked fall-through into subsequent pattern labels. | Pattern switch fall-through restrictions; colon syntax versus `->` | [Ch 3 — Making Decisions](../base/chapter/_03_Making_Decisions/README.md) |
| **Q31** | Predicate | Knew `test()` was abstract but couldn't distinguish static from default methods. | `and()`, `or()`, `negate()`, `not()`, `isEqual()` | [Ch 8 — Lambdas and Functional Interfaces](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| **Q37** | Functional interfaces | Was uncertain whether `BooleanSupplier` existed and failed to identify the nonexistent `BiSupplier`. | Supplier variants; valid functional interface names | [Ch 8 — Lambdas and Functional Interfaces](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| **Q42** | Generics | Assumed a `List<String>` variable could be inserted into a map requiring `ArrayList<String>` because its runtime object was an `ArrayList`. | Declared types; generic invariance; wildcard restrictions; diamond inference | [Ch 9 — Collections and Generics](../base/chapter/_09_Collections_and_Generics/README.md) |
| **Q43** | Files API | Confused `Files.readAllLines()` with `Files.lines()`, expecting a stream rather than a list. | Files API method return types | [Ch 14 — Input/Output](../base/chapter/_14_Input_Output/README.md) |
| **Q46** | Interfaces | Thought an interface could provide a `default` implementation of `equals(Object)`. | Interface default-method restrictions involving `Object` methods | [Ch 7 — Beyond Classes](../base/chapter/_07_Beyond_Classes/README.md) |
| **Q47** | Sealed classes | Was uncertain whether `final` and `sealed` could be combined. | Required modifiers for direct subclasses of sealed classes | [Ch 7 — Beyond Classes](../base/chapter/_07_Beyond_Classes/README.md) |

## ✅ Correct Answers

| Question | Repo Section |
|---|---|
| Q2 | [Ch 10 — Streams](../base/chapter/_10_Streams/README.md) |
| Q4 | [Ch 3 — Making Decisions](../base/chapter/_03_Making_Decisions/README.md) |
| Q5 | [Unnamed Variables](../addendum/Unnamed_Variables/README.md) |
| Q8 | [Ch 9 — Collections and Generics](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q9 | [Ch 1 — Building Blocks](../base/chapter/_01_Building_Blocks/README.md) |
| Q10 | [Ch 9 — Collections and Generics](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q12 | [Ch 13 — Concurrency](../base/chapter/_13_Concurrency/README.md) |
| Q13 | [Ch 9 — Collections and Generics](../base/chapter/_09_Collections_and_Generics/README.md) |
| Q16 | [Ch 13 — Concurrency](../base/chapter/_13_Concurrency/README.md) |
| Q18 | [Ch 3 — Making Decisions](../base/chapter/_03_Making_Decisions/README.md) |
| Q19 | [Ch 6 — Class Design](../base/chapter/_06_Class_Design/README.md) |
| Q22 | [Ch 8 — Lambdas and Functional Interfaces](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| Q23 | [Unnamed Variables](../addendum/Unnamed_Variables/README.md) |
| Q24 | [Ch 1 — Building Blocks](../base/chapter/_01_Building_Blocks/README.md) |
| Q27 | [Ch 13 — Concurrency](../base/chapter/_13_Concurrency/README.md) |
| Q30 | [Ch 3 — Making Decisions](../base/chapter/_03_Making_Decisions/README.md) |
| Q33 | [Ch 5 — Methods](../base/chapter/_05_Methods/README.md) |
| Q34 | [Ch 2 — Operators](../base/chapter/_02_Operators/README.md) |
| Q35 | [Ch 12 — Modules](../base/chapter/_12_Modules/README.md) |
| Q36 | [Ch 8 — Lambdas and Functional Interfaces](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) |
| Q39 | [Ch 13 — Concurrency](../base/chapter/_13_Concurrency/README.md) |
| Q40 | [Ch 14 — Input/Output](../base/chapter/_14_Input_Output/README.md) |
| Q44 | [Ch 7 — Beyond Classes](../base/chapter/_07_Beyond_Classes/README.md) |
| Q45 | [Ch 1 — Building Blocks](../base/chapter/_01_Building_Blocks/README.md) |
| Q48 | [Ch 6 — Class Design](../base/chapter/_06_Class_Design/README.md) |
| Q49 | [Ch 7 — Beyond Classes](../base/chapter/_07_Beyond_Classes/README.md) |
| Q50 | [Ch 2 — Operators](../base/chapter/_02_Operators/README.md) |

## ✅ Correct Answers That Exposed Uncertainty 🤔

| Question | Area | Observation | Repo Section |
|---|---|---|---|
| **Q1** | Exceptions | Correctly reasoned through the exception flow but needed time. Wants greater certainty. | [Ch 11 — Exceptions and Localisation](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| **Q6** | Exceptions | Initially selected `bim boom bam`, then corrected to `bim bam boom`. Nearly lost the mark. | [Ch 11 — Exceptions and Localisation](../base/chapter/_11_Exceptions_and_Localisation/README.md) |
| **Q7** | Modules | Answered by elimination; recognised `--module-source-path` but wasn't confident. | [Ch 12 — Modules](../base/chapter/_12_Modules/README.md) |
| **Q14** | Concurrent collections | Correct answer, but wanted clarification about visibility of changes during `CopyOnWriteArrayList` iteration. | [Ch 13 — Concurrency](../base/chapter/_13_Concurrency/README.md) |
| **Q21** | Summary statistics | Had not encountered `DoubleSummaryStatistics.accept()` or `combine()`; answered logically. | [Ch 10 — Streams](../base/chapter/_10_Streams/README.md) |
| **Q25** | Optional / exceptions | Followed the execution correctly but questioned when `orElseThrow()` creates and throws the exception. | [Ch 10 — Streams](../base/chapter/_10_Streams/README.md) |
| **Q32** | Date/time API | Had not studied `Year` or `atMonthDay()`; answered through deduction. | [Ch 4 — Core APIs](../base/chapter/_04_Core_APIs/README.md) |
| **Q38** | Sealed classes / reflection | Had not used `isSealed()`; inferred the answer from the options. | [Ch 7 — Beyond Classes](../base/chapter/_07_Beyond_Classes/README.md) |
| **Q41** | Exceptions | Reasoned through the execution order; the result helped clarify Q6. | [Ch 11 — Exceptions and Localisation](../base/chapter/_11_Exceptions_and_Localisation/README.md) |

## 🚩 Review Priority

| Priority | Repo Section | Evidence from Exam | Review Focus |
|---|---|---|---|
| **High** | [Ch 8 — Lambdas and Functional Interfaces](../base/chapter/_08_Lambdas_and_Functional_Interfaces/README.md) | Incorrect Q15, Q31, Q37 | Functional interface names and available methods |
| **High** | [Ch 1 — Building Blocks](../base/chapter/_01_Building_Blocks/README.md) | Incorrect Q20, Q26; hesitation Q45 | `var`, array declarations and initializers |
| **High** | [Ch 3 — Making Decisions](../base/chapter/_03_Making_Decisions/README.md) | Incorrect Q11, Q29 | Switch constant expressions and pattern fall-through |
| **High** | [Ch 11 — Exceptions and Localisation](../base/chapter/_11_Exceptions_and_Localisation/README.md) | Uncertainty Q1, Q6, Q41 | Exception execution order and propagation |
| **Medium** | [Ch 9 — Collections and Generics](../base/chapter/_09_Collections_and_Generics/README.md) | Incorrect Q42 | Generic assignability and type inference |
| **Medium** | [Ch 14 — Input/Output](../base/chapter/_14_Input_Output/README.md) | Incorrect Q17, Q43 | Console and Files API methods |
| **Medium** | [Ch 13 — Concurrency](../base/chapter/_13_Concurrency/README.md) | Incorrect Q28; uncertainty Q14 | Execution ordering and snapshot iteration |
| **Medium** | [Ch 7 — Beyond Classes](../base/chapter/_07_Beyond_Classes/README.md) | Incorrect Q46, Q47; uncertainty Q38 | Interface default methods and sealed classes |
| **Medium** | [Ch 12 — Modules](../base/chapter/_12_Modules/README.md) | Uncertainty Q7 | Module compilation flags |
| **Low** | [Ch 4 — Core APIs](../base/chapter/_04_Core_APIs/README.md) | Uncertainty Q32 | `Year`, `MonthDay` and related APIs |
| **Low** | [Ch 10 — Streams](../base/chapter/_10_Streams/README.md) | Uncertainty Q21, Q25 | Summary statistics and `Optional` methods |

### Evidence Key

- **Incorrect:** Answered incorrectly; identifies a knowledge gap or exam technique issue.
- **Uncertain:** Answered correctly but required guessing, elimination or further clarification.
- **Repeated:** A rule or topic that appeared in multiple questions.

## JDK 25 Addendum

| Topic | Evidence | Assessment |
|---|---|---|
| [Unnamed Variables](../addendum/Unnamed_Variables/README.md) | Q5, Q23 | Confident; earlier revision retained |
| [Flexible Constructor Bodies](../addendum/Flexible_Constructor_Bodies/README.md) | — | No specific gap identified |
| [Module Import Declarations](../addendum/Module_Import_Declarations/README.md) | — | No specific gap identified; Q7 concerned JPMS compilation flags |
| [Compact Source Files and Instance Main Methods](../addendum/Compact_Source_Files_and_Instance_Main_Methods/README.md) | — | No specific gap identified |
| [Stream Gatherers](../addendum/Stream_Gatherers/README.md) | — | No specific gap identified |
| [Scoped Values](../addendum/Scoped_Values/README.md) | — | No specific gap identified |
| [Minor API Additions](../addendum/Minor_Api_Additions/README.md) | — | No specific Java 25 addition identified as a gap |

**Note:** Q18 involved primitive patterns, whose Java 25 preview status should be distinguished from the certification's non-preview language rules.

## Overall Assessment

**PASS — 72% (36/50)**

An improvement of four correct answers and eight percentage points over PE1, moving from two questions below the pass mark to two questions above it.

**Strengths:** Good retention of PE1 revision, particularly collections and executor services; confident Java 25 unnamed-variable knowledge; strong reasoning about class design, access control and common APIs.

**Weaknesses:** Exact functional-interface APIs, switch compilation rules, `var` array declarations and selected I/O methods. Exception handling is a confidence-building priority despite correct answers.

**Exam technique:** Q20 and Q26 exposed avoidable errors from overthinking alternatives and reversing the question's selection criteria. Multi-select questions need particular care.

**Next steps:** Address the high-priority gaps in the relevant revision guides, reinforce the medium-priority API details, then attempt PE3.

The aim is to move from a narrow pass to a consistent, comfortable margin above 68%.