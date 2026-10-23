# Java 25 (1Z0-831) — Targeted Retake Action Plan

**Exam:** 23 October 2026 | **Result:** 60% | **Pass mark:** 68%  
**Purpose:** Turn the Oracle objective-level feedback, your recalled questions, and the *OCP Java 25 Programmer Certification Fundamentals* Kindle table of contents into a practical, selective study programme.

## How to interpret the evidence

- **Oracle flagged** = at least one question within that objective was incorrect; **not** proof that the specific recalled question was wrong. Oracle gives no per-objective counts or scores.
- **Recalled** = a question you described after the exam, reconstructed imperfectly; code and answer options may differ.
- **Book references** = section numbers and headings visible in your Kindle screenshots; they identify *where to read*, not verification of the chapter text.
- **Priority** = judgement based on overlapping evidence, uncertainty, breadth, and expected revision value; **not** an estimate of how many marks were lost.

## First-pass reading order — use this instead of reading the book cover to cover

| Order | Study block | Must-read Kindle sections | Oracle evidence | Recalled question / reason | Runnable examples | Completion test |
|---|---|---|---|---|---|---|
| **1** | **Streams, gatherers, reductions, parallel streams** | **21.2.2–21.2.7**, **21.3.3**, **21.3.5–21.3.6**, **21.4.1–21.4.7**; review **21.1.2–21.1.5** as needed | **Flagged:** stream decomposition/reduction/grouping/gathering **and** concurrent collections/parallel streams | Windowed gatherer + `.get()`, grouping/counting, random `ints` + `limit`/`takeWhile`, `Files.lines` + `StringBuilder`; missing clear parallel-stream recall | `GathererWindows.java`, `FilesLinesReduce.java`, `RandomLetterStreams.java` | Explain output types at each stage; safely distinguish `collect` from mutable `reduce`; explain sequential vs parallel assumptions; solve unseen questions |
| **2** | **Inheritance, sealed types, interfaces, record patterns** | **13.3.4**, **13.4.2**, **13.5.1–13.5.6**, **13.6.2–13.6.3**, **14.1.3–14.1.4**, **9.2.4**, **10.2.1** | **Flagged:** interfaces; inheritance/polymorphism/records | `Art`/`Dance`/`Music` equality, sealed interface and static/default methods, record pattern with `List<String>` | `SealedEquality.java`, `InterfaceRules.java`, `RecordPatternLists.java` | For each candidate declaration, say compile / fail and why; trace reference vs runtime type; explain `equals` contract |
| **3** | **Loops, switch/pattern matching, operators** | **4.4.3**, **4.6.1–4.7.2**, **12.3.3–12.6**, **10.1.3**, **10.1.5–10.1.6** | **Flagged:** flow control | Nested loop `c--`, `continue`, `break`; numerous switch/pattern questions; boolean `&` with increments | `LoopTrace.java`, `BooleanOperators.java` | Trace a nested loop on paper; distinguish `break`/`continue` updates; explain switch exhaustiveness/dominance/`null` |
| **4** | **Executors, futures, concurrent collections** | **20.1.2–20.1.3**, **20.6.1–20.6.2**, **21.3.3**, **21.3.5**; skim **20.8.1–20.8.4** | **Flagged:** thread/executor/scoped values; concurrent collections/parallel streams | `Runnable` submitted to executor and `Future.get()`; no clear separate concurrent collection question | `RunnableFuture.java`, `FilesLinesReduce.java` | Distinguish `execute`/`submit`, `Runnable`/`Callable`, `Future.get()` values, ordering and concurrency guarantees |
| **5** | **Constructors, class lifecycle, overloads, nested access, scope** | **6.2.2–6.2.3**, **6.4.1–6.4.4**, **6.5.6**, **5.4.1–5.7**, **7.1.1–7.1.4**, **7.2.3** | **Flagged:** object creation/lifecycle; overloading; scope/immutability | Static/instance initialization, chained constructors, `(int,double)` overload and varargs, private inner/outer access, unnamed variables | `ConstructorOrder.java`, `OverloadVarargs.java`, `NestedAccess.java` | Predict full initialization order; choose overload in phases; distinguish class members and accessible members |
| **6** | **I/O and serialization** | **19.1.2–19.1.6**, **19.2.2–19.2.3**, **19.3.1–19.3.4** | **Flagged:** I/O streams; serialization | `racecar` with buffer size four; `Files.lines`; serialized `Game`/`Chess` | `BufferedRead.java`, `SerializationInheritance.java`, `FilesLinesReduce.java` | Predict output for one read vs loop; explain serialized runtime object and inherited fields; know file stream closure |
| **7** | **Dates, time zones, DST** | **22.2–22.6** (especially **22.5**, **22.6**) | **Flagged:** date-time objects, time zones, DST | Chicago 01:30 plus one hour, `equals`; `withDayOfMonth`, `plusMonths`, `minusDays` | `DaylightSaving.java` | Explain repeated local times, offsets, `plusHours` vs `plusDays`, `equals` vs `isEqual` |
| **8** | **Enums, collections, generics** | **8.2.1–8.2.3**, **16.1.9**, **16.2.1–16.2.6**, **18.2–18.5** | **Flagged:** enums; arrays/collections | `Transform.valueOf` case sensitivity and lambdas; `Map.put` replacement; generic list record patterns | `EnumTransform.java`, `MapPut.java`, `ListCopyVsView.java`, `RecordPatternLists.java` | Identify enum lookup exceptions, map mutation results, snapshot vs view, legal generic patterns |
| **9** | **Math, numeric conversions, exceptions** | **6.6**, **10.1.5–10.1.6**, **15.1.3**, **15.2.1–15.2.3**, **15.4.1** | **Not separately flagged** under these names; still recalled as difficult | Two Math questions; nested try/catch and new custom RuntimeException; `Files.lines` exception options | `MathMethods.java`, `NestedExceptions.java` | Explain return types and negative rounding; trace thrown instance and message; identify checked exceptions |
| **10** | **Modules and Java 25 additions** | **23.3–23.5**, **5.6**, **20.8**; inspect **12**, **13**, **6.4** for newer syntax | **Not explicitly listed** as a flagged module objective | `module-info`, service loading, module imports, compact source files, unnamed variables; scoped values not recalled | Existing guide's JPMS checklist; add standalone experiments if needed | Distinguish `uses`/`provides`, `exports`/`opens`, `_` declarations; confirm Java 25 additions in authoritative docs |

### Important coverage caveats

- The contents screenshots **do** show dedicated gatherers (**21.4**), scoped values (**20.8**), unnamed variables (**5.6**), parallel streams (**21.3.3**), `ThreadLocalRandom` (**21.3.6**), daylight saving (**22.5**) and serialization (**19.3**).
- They **do not clearly show dedicated headings** for module import declarations, compact source files or flexible constructor bodies. Check the book's text or official Java 25 material; don't assume these are fully covered.
- `Math` and exception handling may appear under broader Oracle objectives, but the score report does **not** identify either as a separate incorrect objective.

## Oracle's 14 flagged objectives — coverage audit

| # | Oracle's flagged objective (short label) | Recalled candidate | Kindle sections | Priority / confidence |
|---|---|---|---|---|
| 1 | Enums with fields, methods, constructors | Enum transform + `valueOf` | **8.2.1–8.2.3**, **17.1.4–17.1.6** | High / strong topical match |
| 2 | Interfaces, functional/default/static/private methods | Sealed interface, static and default methods | **14.1.3–14.1.4**, **17.1.1–17.1.6** | High / strong |
| 3 | Arrays, List, Set, Map, Deque, sequenced collections | `Map.put`; list/generic record pattern | **16.1.6–16.1.9**, **18.1–18.5** | Medium / possible |
| 4 | Platform/virtual threads, executors, scoped values | `Runnable` + `Future.get()` | **20.1.2–20.1.3**, **20.6.1**, **20.8** | High / strong |
| 5 | Flow control, loops, switch, break/continue | Nested `for` loop; many switch questions | **4.4–4.7**, **12.3–12.6** | High / strong |
| 6 | Object creation, nested classes, lifecycle, GC | Initialization and inner access | **5.4–5.7**, **6.4–6.5**, **7.2.3** | High / strong |
| 7 | Inheritance, sealed/records, overriding, polymorphism | `Art` equals, generic record patterns, serialized subclass | **9.2.4**, **13.3–13.6** | High / strong |
| 8 | Overloaded methods and varargs | `(int,double)` overload | **6.2.2–6.2.3**, **6.1.3** | High / strong |
| 9 | Date/time/zone/DST | Chicago rollback and date operations | **22.2–22.6** | Medium / strong topic, uncertain miss |
| 10 | Stream decomposition/reduction/grouping/gathering | Gatherers, grouping, random stream, file reduction | **21.1–21.4** | Very high / strong |
| 11 | Concurrent collections and parallel streams | No confidently identified separate question | **20.6.2**, **21.3.3–21.3.5**, **21.4.7** | Very high / missing recall |
| 12 | Console/file I/O streams | `racecar` buffer, `Files.lines` | **19.1–19.2** | High / strong |
| 13 | Serialization | `Game`/`Chess` | **19.3.1–19.3.4** | High / strong |
| 14 | Scope, encapsulation, immutable objects, `var`, unnamed variables | Unnamed variables, nested private access | **5.4–5.6**, **7.1–7.2** | Medium / possible |

**What the report cannot tell us:** which specific question you missed, how many misses each objective contains, or which objective is your weakest by percentage. Use quizzes and *fresh* mock exams to refine the order.

## Weekly workflow (flexible, not a six-month reset)

### Phase A — Diagnose and rebuild (blocks 1–4)

1. Read only the listed sections. Skip familiar introductory material unless the quiz reveals a gap.
2. Before running the corresponding `.java` file, predict the result and the **type of every intermediate expression**.
3. Run on Java 25, change one detail, and predict again. For gatherers, pay attention to output element type; for parallel streams, avoid assuming encounter order or mutable-reduction safety.
4. Write a one-line error-log entry: **assumption → actual rule → one contrasting example**.
5. Complete the book quiz and at least a few **unseen** questions on that subject.

### Phase B — Close the remaining Oracle objectives (blocks 5–10)

Repeat the same method, spending extra time on objectives where a quiz reveals new errors. Review the Java 25-specific items separately if the book's treatment is unclear.

### Phase C — Validate retake readiness

- Use **fresh**, timed, exam-specific 1Z0-831 mocks; don't count repeat scores as reliable readiness signals.
- After each mock, tag mistakes by the **14 Oracle objectives** above and the specific rule, not just chapter number.
- Return only to sections implicated by new mistakes. A useful target is **consistent first-attempt scores around 80%+** to give a buffer over 68%; this is a study target, not an official threshold or guarantee.
- Practise some mixed code-tracing questions without an IDE, then verify them by running small examples.

## Tracking sheet (copy and update)

| Block | Read | Chapter quiz | Runnable code | Fresh questions | Remaining confusion |
|---|---|---|---|---|---|
| 1 Streams / gatherers | ☐ | ☐ | ☐ | ☐ | |
| 2 Inheritance / interfaces | ☐ | ☐ | ☐ | ☐ | |
| 3 Flow control / switch | ☐ | ☐ | ☐ | ☐ | |
| 4 Concurrency | ☐ | ☐ | ☐ | ☐ | |
| 5 Object lifecycle / overloads | ☐ | ☐ | ☐ | ☐ | |
| 6 I/O / serialization | ☐ | ☐ | ☐ | ☐ | |
| 7 Date/time | ☐ | ☐ | ☐ | ☐ | |
| 8 Enums / collections | ☐ | ☐ | ☐ | ☐ | |
| 9 Math / exceptions | ☐ | ☐ | ☐ | ☐ | |
| 10 Modules / Java 25 | ☐ | ☐ | ☐ | ☐ | |

## Companion material

- `POST_EXAM_REPORT.md` — original 21-question reconstruction and detailed uncertainty notes; retained rather than overwritten.
- `../examples` — individual runnable Java programs from the original pack. Most require Java 21+; **`GathererWindows.java` requires Java 25**.

**Next action:** Start with **21.4.1–21.4.5** and **21.3.3**, then test `GathererWindows.java` and a mutable parallel reduction. Use the chapter's quiz to decide whether to continue with 21.4.6–21.4.7 immediately.
