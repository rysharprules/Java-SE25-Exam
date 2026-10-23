# Oracle Java 25 Professional (1Z0-831) — Post-Exam Reconstruction & Retake Plan

**Exam:** 23 October 2026 · **Reported score:** 60% · **Status:** unsuccessful attempt  
**Source:** Your own recollections immediately after the exam, plus our follow-up discussion.  
**Evidence warning:** These are **not verbatim exam questions**. Names, literals, option sets, and sometimes control-flow details are reconstructed. The report distinguishes what you remembered from what we inferred. Do not assume an item was answered incorrectly merely because it felt difficult.

## Executive summary

You remembered a broad, modern Java paper with substantial **pattern matching and switch**, plus **streams/gatherers**, **serialization**, **concurrency**, **I/O**, **modules**, **dates**, **operators**, and **inheritance**. Several troublesome items combined multiple otherwise familiar rules and asked for precise output. Your recollection of the exact answers is incomplete, so **the 60% score cannot be allocated to individual topics** until the official domain report arrives. You estimated about 20 misses; that count is not independently verified from the score alone.

**Most actionable risks from your recollection:** random `IntStream` termination (`limit` versus `filter`/`takeWhile`), `Math` methods, record patterns with generic components, gatherer output types, `Future.get()` for `Runnable`, byte-buffer read loops, class initialization/constructor chaining, nested loop update semantics, and multi-rule code tracing. You also identified a real gap in the preceding practice session: `List.copyOf()` versus `Collections.unmodifiableList()`.

## Priority revision plan (provisional)

Priority reflects **uncertainty × exam frequency recalled × ease of gaining marks**, not confirmed incorrect answers.

| Rank | Topic | Why prioritise it | Practice file(s) |
|---:|---|---|---|
| **1** | Pattern matching, record patterns, switch, generic components | Many questions; at least one uncertain `List<T>` record pattern; high breadth | `RecordPatternLists.java`; add switch exercises |
| **2** | Code tracing: loops, `break`/`continue`, `&`/`&&`, increments, constructor order | Time-consuming, easy to lose marks under pressure | `LoopTrace.java`, `BooleanOperators.java`, `ConstructorOrder.java` |
| **3** | Streams, collectors and gatherers | Multiple appearances; uncertainty about window type and `.get()` | `GathererWindows.java`, `FilesLinesReduce.java`, `RandomLetterStreams.java` |
| **4** | `Math` API and numeric conversions | You recalled **at least two** unfamiliar `Math` questions | `MathMethods.java`, `OverloadVarargs.java` |
| **5** | I/O, file streams, buffering and exceptions | Uncertain buffer-of-four question and `Files.lines()`/reduction question | `BufferedRead.java`, `FilesLinesReduce.java` |
| **6** | Concurrency: `ExecutorService`, `Future`, `Runnable` | Possible confusion between task output and `Future.get()` | `RunnableFuture.java` |
| **7** | Inheritance, sealed types, `equals()`, interfaces, nested access | Several complex/select-two questions; one possible private-access trap | `SealedEquality.java`, `InterfaceRules.java`, `NestedAccess.java` |
| **8** | Serialization and polymorphism | Possible wrong assumption that child fields deserialize as defaults | `SerializationInheritance.java` |
| **9** | Enums, lambdas and `valueOf()` | Complex multi-option question; case-sensitive lookup | `EnumTransform.java` |
| **10** | JPMS, services, module imports, compact source files | Present on paper; mostly recognised, but retain practice | See JPMS checklist below |
| **11** | Dates, DST, resource bundles | Two date questions; Chicago answer likely correct | `DaylightSaving.java` |
| **12** | Collections snapshot/view, `Map.put()` | One genuine practice miss; Map overwrite seems understood | `ListCopyVsView.java`, `MapPut.java` |

**Do not ignore topics ranked 10–12:** their rank means lower current uncertainty, not low exam relevance. Adjust these priorities once Oracle provides domain feedback.

## Question-by-question reconstruction

### Q1 — Nested `for` loops with `break`, `continue`, `c--`

**You remember:** Outer `a` counts 3 → 2 → 1; inner `b` counts from 0 toward 3; the inner `for` update contains `b++` and `c--`; branches test `a == 1`, then `b == 1`, otherwise add `a+b` to `c`. You later remembered `c` might start at **1**. You calculated about 10 under exam pressure, but options were small integers.

**Our reconstruction:** `LoopTrace.java` prints every branch and ends with **`c = 1`**. The original may differ.

**Rules:** `continue` in a basic `for` executes the update expressions before retesting; `break` exits without executing them. A `break` in the inner loop does not break the outer loop. `b` is reinitialised for each outer iteration.

**Status:** possible error, **not confirmed**.

### Q2 — Record patterns with a generic `List` component

**You remember:** Record pattern matching, one component was a `List`, and options differed in whether generic type arguments were supplied.

**Illustrative example:** `record Person(String name, List<String> hobbies) {}` can be matched with `Person(String name, List<String> hobbies)` in a record pattern. But `obj instanceof List<String>` generally fails when `obj` has static type `Object` because `List<String>` is non-reifiable. `List<?>` is permitted. These are **different language checks**.

**Status:** uncertain; need original record declaration and candidate patterns. **Do not conclude that all generic type patterns are illegal.**

### Q3 — Serializable parent and child (`Chess`, `players`)

**You remember:** Parent `Game` implements `Serializable` and stores a title. Child `Chess` extends it, stores `players`, calls `super(title)`. Reference declared `Game p = new Chess("Chess", 2)`. Serialized and deserialized; you think you answered **`Chess 0`** because `Game` has no `players` field.

**Our reconstruction:** `SerializationInheritance.java` prints **`Chess 2`** via overridden `toString()`. Serialisation preserves the **runtime `Chess` object** and inherited plus child serializable instance fields. Compile-time type `Game` still restricts direct field access (`restored.players` would not compile without a cast).

**Status:** **possible actual error**; original print expression is unknown.

### Q4 — `Map.put()` replacing a cookie price

**You remember:** `put("cookie", 2.99)` then `put("cookie", 3.99)`.

**Rule:** second `put` replaces the existing value; `get("cookie")` → **3.99**, size remains **1**, second `put` returns **2.99** (the previous value).

**Status:** likely understood. **Separate from the `Math` questions**; voice dictation originally confused “Math” and “Map.”

### Q5 — `Math` methods (at least two questions)

**You remember:** Methods sounded like `Math.round()` and possibly another `Math` method; you were uncertain.

**Review:** `Math.round(float)` returns `int`; `Math.round(double)` returns `long`; ties round toward positive infinity (`Math.round(-2.5)` → `-2`). `Math.floor(-2.1)` → `-3.0`; `Math.ceil(-2.1)` → `-2.0`; `Math.pow` returns `double`. Check primitive promotion, overflow, `abs`, `min`, `max`, `floorDiv`, `floorMod` as relevant to the syllabus.

**Status:** **explicit revision need**; exact methods unknown.

### Q6 — Enum + lambda transforms + `valueOf()`

**You remember:** Enum named `Transform`, methods/lambdas transforming `Hello`, `and`, `WORLD`; expected preserve `Hello`, uppercase `and`, lowercase `WORLD`. Several convoluted choices; failed enum lookup caught and produced another word such as `snow`.

**Our reconstruction:** `EnumTransform.java` demonstrates enum constants holding `UnaryOperator<String>` functions and a fallback on `IllegalArgumentException`.

**Rule:** `Transform.valueOf("LOWER")` succeeds; `Transform.valueOf("lower")` fails **case-sensitively**. `String.valueOf(...)` is unrelated.

**Status:** uncertain.

### Q7 — Sealed `Art`, `Music`, `Dance`, and equality

**You remember:** Sealed `Art`, final permitted `Music` and `Dance`, a measurement such as 3.5 metres, comparisons intended to produce **false, true, false**; options used `getClass()` versus `instanceof`, and differed in whether a measurement was exposed through the superclass.

**Our reconstruction:** `SealedEquality.java` compares all `Art` subclasses by a superclass-held measurement and prints **false, true, false** for `a.equals(null)`, `a.equals(b)` and `b.equals(c)`. This is **only one plausible reconstruction**.

**Rules:** `getClass()` equality rejects cross-subclass matches; `instanceof Art` allows them subject to field comparisons. `equals()` must remain reflexive, symmetric, transitive and consistent; overriding `equals()` normally requires matching `hashCode()`. A superclass cannot directly reference fields declared only in subclasses without suitable access or abstraction.

**Status:** uncertain; high-value design question.

### Q8 — Sealed interface / default method / interface static method

**You remember:** Pick valid declarations; a sealed interface with two permitted final implementations; default method not overridden; another option with `public static` interface method invoked from a class.

**Rules:** Implementing classes may inherit a default interface method. Interface methods **cannot be declared `final`**. A static interface method is called as `InterfaceName.method()`, **not** inherited as `ImplementingClass.method()`. `InterfaceRules.java` demonstrates.

**Status:** uncertain; exact option wording matters.

### Q9 — Arithmetic, `&`, comparisons, `++`

**You remember:** Expression mixing arithmetic, `<`, `&`, and increment/decrement; your computed result matched an option.

**Rule:** Boolean `&` evaluates both operands, whereas `&&` short-circuits. On integral operands `&` is bitwise. Postincrement returns the old value; preincrement returns the updated value. Trace state **after every operand evaluation**.

**Status:** possibly correct; use `BooleanOperators.java`.

### Q10 — Overload selection with `double...`

**You remember:** Overloads resembling `(int,int)`, `(int,String)`, `(float,float)`, `(double...)`; call passed `(int,double)`.

**For those exact overloads:** `double...` is the only applicable choice because `double` cannot narrow to `float` or `int`; the `int` widens to `double`. Java generally tries applicable fixed-arity methods before variable-arity methods, so **different option signatures could change the answer**. `OverloadVarargs.java`.

**Status:** likely correct for reconstruction.

### Q11 — Inner class member accessibility (select TWO)

**You remember:** Outer and non-static inner classes each had public/private fields; inner had a method; `main` instantiated inner and asked which members were accessible. You wondered whether methods count as members.

**Rules:** Methods **are** members. A non-static inner class can access outer private members. The enclosing class can access private members of its nested class, including from `Outer.main()`. Unrelated external classes cannot. Exact reference expression and **location of `main`** determine the answer. `NestedAccess.java`.

**Status:** possible error, not confirmed.

### Q12 — Gatherers windows then `map(...get(...))`

**You remember:** A gatherer window operation followed by `map` with arguments and `.get()` on a value; you weren't sure what the lambda parameter represented.

**Rule:** `Gatherers.windowFixed(n)` emits **`List<T>` windows**; downstream `map(window -> window.get(0))` operates on a list. A final fixed window may be shorter, so `get(1)` can throw if its size is 1. `GathererWindows.java` requires Java 25 and prints `[[1,2],[3,4],[5]]` (spacing formatted by Java's list output).

**Status:** possible error; **high priority**.

### Q13 — Unnamed variables, module imports, compact source files

**You remember:** A question about unnamed variables (you felt confident), recognition of module import declarations and compact source files. **Scoped values did not appear on your paper.**

**Review:** `_` is a valid unnamed variable/pattern in permitted contexts, but cannot be read by name. Java 25 supports `import module java.base;` as a module import declaration; compact source files can omit an explicit class declaration in supported source-file programs. **Review the exact Java 25 syntax from the current official documentation**, rather than guessing the original options.

**Status:** mostly recognition; uncertain original questions.

### Q14 — Buffered file I/O, four-element buffer, `racecar`

**You remember:** File content `racecar`; byte/character buffer length four; you guessed `race`.

**Rule:** A single read may yield up to four units; a loop that repeatedly reads and writes can process the entire file, yielding `racecar`. `read(buffer)` returns a **count**, which must be used when writing. A read is not guaranteed to fill the buffer. `BufferedRead.java` uses an in-memory byte stream to demonstrate safely.

**Status:** possible error, **depends on whether there was a loop**.

### Q15 — ExecutorService `Runnable`, Futures and `get()`

**You remember:** `Runnable` tasks submitted/executed, futures and `get()`, output asked; you thought “nothing” because a `Runnable` returns no value.

**Rule:** `submit(Runnable)` returns a `Future<?>`; on successful completion, `future.get()` returns **`null`**. `submit(Runnable, result)` returns a Future that yields the supplied result. `execute(Runnable)` itself returns `void`, not a Future. The task might also print side effects. `RunnableFuture.java`.

**Status:** **possible actual error**, depending on the exact call and print statement.

### Q16 — `Files.exists`, `Files.lines`, StringBuilder reduction, exceptions

**You remember:** If file absent, code throws; question said file exists. Then `Files.lines()` and a StringBuilder-based reduction; answer options apparently all involved exceptions, which confused you.

**Important:** A file's existence does not guarantee `Files.lines()` succeeds; opening/reading can fail. But **we cannot identify the original exception** from the recollection. `Files.lines()` returns `Stream<String>` and should be closed. A mutable `StringBuilder` used as a `reduce` identity can be a pitfall, especially in parallel; `collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)` is a more appropriate mutable reduction. `FilesLinesReduce.java` demonstrates a **working** case, not a claim about the exam's outcome.

**Status:** **unresolved**; do not assume a particular exception.

### Q17 — Nested try/catch with custom `RuntimeException`

**You remember:** Inner try throws custom exception with message 1; inner `catch(Exception)` throws another custom exception with message 2; outer catch catches it and prints `getMessage()`.

**Our reconstruction:** `NestedExceptions.java` prints **`Second`**. A custom subclass of `RuntimeException` is also an `Exception`. `throw e;` rethrows the same object; `throw new Custom("Second")` constructs a new one.

**Status:** probably understood; original wording unknown.

### Q18 — Date adjustment with `with...`, `plus...`, `minus...`

**You remember:** Starting date transformed into a target such as `2024-05-04`; options combined `withMonth`, `withDayOfMonth`, `plusMonths`, `minusDays`. You felt confident.

**Rules:** Date/time objects are immutable; each call returns a new value. Operation order matters, especially when setting a day of month that is invalid for the current month. `withDayOfMonth()` can throw `DateTimeException`; `plusMonths()` may clamp to last valid day.

**Status:** likely correct; no exact reconstruction possible.

### Q19 — Chicago DST rollback, `ZonedDateTime.plusHours(1)`

**You remember:** 01:30 around autumn rollback, adding one hour yields 01:30 again; comparing the objects gave false.

**Our reconstruction:** On **3 November 2024** in `America/Chicago`, first `01:30-05:00` plus one hour becomes `01:30-06:00`; `.equals()` is **false**. `DaylightSaving.java` demonstrates. The *starting offset* and exact date matter.

**Status:** **likely correct** for this reconstruction.

### Q20 — Static/instance initializers and constructor chaining

**You remember:** Parent static initializer, possibly instance initializer, child constructor chains; uncertain answer.

**Rules:** Class initialization runs parent static then child static once (when initialized). On object construction, parent instance initializers and constructor run before child instance initializers and constructor body. A `this(...)` chain does **not** rerun child instance initializers per constructor. `ConstructorOrder.java` prints the order twice so you can compare first vs second construction.

**Status:** uncertain; good practice target.

### Additional recalled appearances, not reconstructed

- **Switch and pattern matching:** many questions, more than isolated examples above. Prioritise dominance, null handling, exhaustiveness, record patterns, guards (`when`), sealed hierarchies, and switch expression `yield`.
- **JPMS:** at least one or two `module-info` questions; a services question you believe you got right. Review `requires`, `requires transitive`, `exports`, `opens`, `uses`, `provides ... with ...`, automatic versus unnamed modules, `jdeps`, `jlink`, `jpackage`.
- **Streams/collectors:** `groupingBy(..., counting())` with many answer choices; you calculated a result present in the options. Remember `counting()` returns `Long` and nested collector result types matter.
- **Practice-session gap, not a remembered exam question:** `List.copyOf(original)` is an unmodifiable snapshot of element references; `Collections.unmodifiableList(original)` is a backed view. `ListCopyVsView.java`.

## How to run the examples

All example files are independent, default-package classes. Most use **Java 21+** syntax and were **compiled and smoke-tested here using OpenJDK 21**. `GathererWindows.java` requires **Java 25** and could **not** be executed in this environment (only JDK 21 is installed), so its output is reasoned from the Java API rather than locally verified.

From the `examples` directory, using your Java 25 JDK:

```bash
javac LoopTrace.java
java LoopTrace

javac SerializationInheritance.java
java SerializationInheritance

javac GathererWindows.java
java GathererWindows
```

To compile every example on Java 25:

```bash
javac *.java
```

**Suggested method:** First run the program; then change **one** operator, modifier, method signature, or value and predict the output **before** running again. For example, change `&` to `&&`, `continue` to `break`, or `List.copyOf` to `Collections.unmodifiableList`.

### Q21 — Random uppercase letters with `ThreadLocalRandom` and streams (new recollection)

**You remember:** A multiple-choice question asked for a string of random characters, possibly described as 10,000 lines/characters. Four alternatives: two used `StringBuilder`, two produced a `String` directly. All appeared to use `ThreadLocalRandom.current().ints(...)` with two character bounds, likely `'A'` and `'Z'`. Some alternatives involved `takeWhile` or `filter`; you now think **one option used `limit(10_000)`**.

**Plausible solution, not a verbatim exam answer:** `ThreadLocalRandom.current().ints('A', 'Z' + 1).limit(10_000).forEach(n -> sb.append((char) n));` produces 10,000 random uppercase letters in a `StringBuilder`. Alternatively, `ints(10_000, 'A', 'Z' + 1)` produces a sized stream; mapping characters to strings and joining creates a `String`.

**Rules to test:** `ints(origin, bound)` is an effectively unlimited `IntStream`, with an inclusive lower bound and **exclusive** upper bound; `limit(10_000)` limits the *number of elements*. `filter(n -> n < 10_000)` checks each *value*, not how many have appeared. `takeWhile(n -> n < 10_000)` on uppercase ASCII codes never stops, because all generated values satisfy the predicate. `'Z'` as an upper bound excludes `Z`; use `'Z' + 1` to include it. `StringBuilder` versus `String` matters if the question specifies a return type.

**Status:** uncertain — original wording, exact alternatives, and whether "10,000 lines" meant lines or characters are unverified. **Priority:** high within streams, especially termination and overload selection.

**Runnable:** `examples/RandomLetterStreams.java`. Change `.limit(10_000)` to `.limit(10)`, or change `'Z' + 1` to `'Z'`, and predict what changes. Do **not** remove the limit from an unbounded stream before running it.

## Recommended retake preparation sequence

**Phase 1 — After Oracle's domain report:** Enter each reported domain and score in the table below; update the priority list. Don't treat an uncertain recollection as a definite miss.

**Phase 2 — Focused rule drills:** Start with ranks 1–6. For each topic, explain the rule, predict output, run the supplied file, then change one detail and predict again. Add *timed* tracing exercises, since the nested-loop question consumed significant exam time.

**Phase 3 — Mixed exam-style questions:** Use realistic select-one/select-two questions that combine topics, especially pattern matching + generics, streams + collectors, and constructors + inheritance. Review why each rejected option is wrong. Keep a short error log with *rule missed*, not just topic.

**Phase 4 — Final check:** Re-test only missed rules, do a small timed mixed set, and stop cramming the night before. Confirm Oracle's actual retake eligibility and booking details in your certification portal.

### Oracle score-report intake (fill in when received)

| Official domain | Score / diagnostic | Change to plan |
|---|---|---|
| _Awaiting report_ | _Awaiting report_ | _TBD_ |

## Confidence legend

- **Likely correct:** Your recalled answer matches a plausible reconstruction, **not** proof of exam credit.
- **Possible error:** Your reasoning may have conflicted with a relevant rule, but original code is unavailable.
- **Uncertain / unresolved:** Too few details to infer outcome; retain only as a practice target.

---

**Bottom line:** This is a **reconstruction and preparation tool**, not an official error analysis. The most useful next input is Oracle's domain breakdown; until then, target the concrete uncertainties above rather than starting Java from scratch.
