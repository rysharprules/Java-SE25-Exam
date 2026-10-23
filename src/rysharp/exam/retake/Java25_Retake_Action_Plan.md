# Java 25 (1Z0-831) — Retake Action Plan

**Start:** Fri 23 Oct 2026 · **Preferred retake:** Fri 6 Nov · **Fallback:** Wed 11 Nov 2026  
**Oracle:** 60% (pass 68%). **Time:** 60–90 min working evenings; 30–31 Oct unavailable.

> **Start here:** Follow the **calendar row for today**. Each row links to the [post-exam Q reference](POST_EXAM_REPORT.md), whose heading links to its runnable Java example.

## 1. Calendar — your single sequence of actions

**Kanban IDs:** `S#` = study session; `ME#` = Enthuware mock exam; `MR#` = mock review; `D#` = decision; `F#` = fallback remediation; `OE#` = Oracle exam attempt. For any study session, use Post-its such as **`S1 - Kindle`**, **`S1 - Kindle Quiz`**, **`S1 - Post-exam example review`**. Only make the cards you actually need. `ME4*` is optional.


| ID | Date | Task / output | Kindle reading (skim selectively) | Oracle ID | Post-exam reference | Type |
|---|---|---|---|---|---|---|
| **S1** | **Fri 23 Oct** | Gatherer windows: predict window types, `.get()` and short final window; run Q12 example | 21.4.1–21.4.5 | O10 | [Q12](POST_EXAM_REPORT.md#q12) | Study |
| **S2** | **Sat 24 Oct** | Grouping/counting and `StringBuilder` reductions; run Q16 example | 21.2.2–21.2.7 | O10 | [Q16](POST_EXAM_REPORT.md#q16) | Study |
| **S3** | **Sun 25 Oct** | Parallel streams, mutable reductions, random stream termination; run Q21 | 21.3.3, 21.3.5–21.3.6, 21.4.6–21.4.7 | O10, O11 | [Q16](POST_EXAM_REPORT.md#q16), [Q21](POST_EXAM_REPORT.md#q21) | Study |
| **S4** | **Mon 26 Oct** | Sealed inheritance, polymorphism, equality; run Q7 and Q3 | 13.3.4, 13.4.2, 13.5.1–13.5.6, 13.6.2–13.6.3 | O7 | [Q7](POST_EXAM_REPORT.md#q7), [Q3](POST_EXAM_REPORT.md#q3) | Study |
| **S5** | **Tue 27 Oct** | Records, generic patterns, interface static/default methods; run Q2 and Q8 | 9.2.4, 14.1.3–14.1.4, 10.2.1 | O2, O7 | [Q2](POST_EXAM_REPORT.md#q2), [Q8](POST_EXAM_REPORT.md#q8) | Study |
| **S6** | **Wed 28 Oct** | Trace nested loop; pattern matching/switch; boolean `&` and increments; run Q1/Q9 | 4.4.3, 4.6–4.7, 12.3.3–12.6, 10.1.3 | O5 | [Q1](POST_EXAM_REPORT.md#q1), [Q9](POST_EXAM_REPORT.md#q9) | Study |
| **S7** | **Thu 29 Oct** | `ExecutorService`, `Future.get()`, concurrent collections; run Q15; 15-min collection/scope check (Q4/Q11/Q13) | 20.1.2–20.1.3, 20.6.1–20.6.2, 20.8.1–20.8.4; 16.1.9, 5.4–5.6 | O3, O4, O11, O14 | [Q15](POST_EXAM_REPORT.md#q15), [Q4](POST_EXAM_REPORT.md#q4), [Q11](POST_EXAM_REPORT.md#q11), [Q13](POST_EXAM_REPORT.md#q13) | Study |
| **—** | **Fri 30–Sat 31 Oct** | Busy: no planned revision | — | — | — | Rest |
| **ME1** | **Sun 1 Nov** | **Enthuware Standard Test 1** — fresh, timed. Start wrong/guessed review | Look up weak areas in Oracle table below | All O1–O14 | — | Mock 1 |
| **MR1** | **Mon 2 Nov** | Complete Mock 1 review: wrong and guessed → O# → Q# → Kindle → experiment | Mock-driven lookup below | Mock-driven | — | Review |
| **S8** | **Tue 3 Nov** | Address Mock 1 gaps. Default: constructors/overloads, I/O/serialization, nested access; also 15-minute enums/date/Math checks | 6.2.2–6.2.3, 6.4.1–6.5.6, 19.1.2–19.1.6, 19.3.1–19.3.4; 8.2, 22.5–22.6, 6.6 | O1, O6, O8, O9, O12, O13 | [Q20](POST_EXAM_REPORT.md#q20), [Q10](POST_EXAM_REPORT.md#q10), [Q14](POST_EXAM_REPORT.md#q14), [Q3](POST_EXAM_REPORT.md#q3), [Q11](POST_EXAM_REPORT.md#q11), [Q6](POST_EXAM_REPORT.md#q6), [Q18](POST_EXAM_REPORT.md#q18), [Q19](POST_EXAM_REPORT.md#q19), [Q5](POST_EXAM_REPORT.md#q5) | Study |
| **ME2** | **Wed 4 Nov** | **Enthuware Standard Test 2** — fresh, timed; record weaknesses | — | All O1–O14 | — | Mock 2 |
| **MR2** | **Thu 5 Nov** | Finish Mock 2 review. Check 6 Nov gate. Light review only; don’t cram | Mock-driven; quick Q6/Q19 if needed | O1, O9 | [Q6](POST_EXAM_REPORT.md#q6), [Q19](POST_EXAM_REPORT.md#q19) | Decision |
| **OE1** | **Fri 6 Nov** | **Preferred Oracle retake** if gate passed, eligible and booked | — | — | — | Exam |
| **F1** | **Sat 7 Nov** | Fallback: remediate persistent errors; optional Standard Test 3 only with time to review | Mock-driven | Mock-driven | — | Fallback |
| **ME3** | **Sun 8 Nov** | Fallback: **Standard Test 3** if not taken Saturday; start full review | — | All O1–O14 | — | Mock 3 |
| **MR3** | **Mon 9 Nov** | Finish Mock 3 review. **Optional Test 4** only if it can be fully reviewed; otherwise targeted study | Mock-driven | Mock-driven | — | Fallback |
| **D2** | **Tue 10 Nov** | 11 Nov go/no-go: check third fresh mock and unresolved misconceptions; light consolidation | Mock-driven | — | — | Decision |
| **OE2** | **Wed 11 Nov** | **Fallback Oracle retake** if threshold met, eligible and booked; else defer until after Spain | — | — | — | Exam |


**Daily method:** Read → predict output and types → run the linked example on JDK 25 → change one detail → explain the rule in one sentence. Do not try to complete a whole book chapter when only a subsection is relevant.

## 2. Oracle feedback — lookup only (no dates)

These are **all 14 objectives Oracle flagged**. A matching recalled question does **not** prove it was the question marked wrong. O11 has no confidently remembered standalone question; Q16 is a *possible* parallel-reduction connection.

| Oracle ID | Official feedback area (shortened) | Kindle reference | Post-exam reference |
|---|---|---|---|
| **O1** | Enums with fields/methods | 8.2.1–8.2.3; 17.1.4–17.1.6 | [Q6](POST_EXAM_REPORT.md#q6) |
| **O2** | Interfaces and method rules | 14.1.3–14.1.4; 17.1.1–17.1.6 | [Q8](POST_EXAM_REPORT.md#q8) |
| **O3** | Collections / maps / lists | 16.1.6–16.1.9; 18.2–18.5 | [Q4](POST_EXAM_REPORT.md#q4), [Q2](POST_EXAM_REPORT.md#q2) |
| **O4** | Executors, threads, scoped values | 20.1.2–20.1.3; 20.6.1; 20.8.1–20.8.4 | [Q15](POST_EXAM_REPORT.md#q15) |
| **O5** | Loops, switch, control flow | 4.4.3; 4.6–4.7; 12.3.3–12.6; 10.1.3 | [Q1](POST_EXAM_REPORT.md#q1), [Q9](POST_EXAM_REPORT.md#q9) |
| **O6** | Object lifecycle, nesting | 5.4–5.7; 6.4.1–6.5.6; 7.1.1–7.1.4 | [Q20](POST_EXAM_REPORT.md#q20), [Q11](POST_EXAM_REPORT.md#q11) |
| **O7** | Inheritance, records, sealed types | 9.2.4; 13.3.4; 13.4–13.6 | [Q2](POST_EXAM_REPORT.md#q2), [Q7](POST_EXAM_REPORT.md#q7), [Q3](POST_EXAM_REPORT.md#q3) |
| **O8** | Overloading / varargs | 6.1.3; 6.2.2–6.2.3 | [Q10](POST_EXAM_REPORT.md#q10) |
| **O9** | Date/time, zones, DST | 22.2–22.6 | [Q18](POST_EXAM_REPORT.md#q18), [Q19](POST_EXAM_REPORT.md#q19) |
| **O10** | Streams, reductions, gatherers | 21.2.2–21.2.7; 21.3.6; 21.4.1–21.4.7 | [Q12](POST_EXAM_REPORT.md#q12), [Q16](POST_EXAM_REPORT.md#q16), [Q21](POST_EXAM_REPORT.md#q21) |
| **O11** | Parallel streams / concurrent collections | 20.6.2; 21.3.3–21.3.5; 21.4.7 | [Q16](POST_EXAM_REPORT.md#q16) *(possible only)* |
| **O12** | I/O streams, files | 19.1.2–19.1.6; 19.2.2–19.2.3 | [Q14](POST_EXAM_REPORT.md#q14), [Q16](POST_EXAM_REPORT.md#q16) |
| **O13** | Serialization | 19.3.1–19.3.4 | [Q3](POST_EXAM_REPORT.md#q3) |
| **O14** | Scope, immutability, `var`, unnamed variables | 5.4–5.6; 7.1–7.2; 11.1.5 | [Q11](POST_EXAM_REPORT.md#q11), [Q13](POST_EXAM_REPORT.md#q13) |

**Other recalled exam content, not individually flagged:** Math [Q5](POST_EXAM_REPORT.md#q5), nested exceptions [Q17](POST_EXAM_REPORT.md#q17), module/service recognition [Q13](POST_EXAM_REPORT.md#q13). The original report also contains historical notes and some practice-only gaps; the **calendar above**, not those notes, determines what to study next.

## 3. Enthuware mock sequence and decision gates

| Assessment | When | First attempt | Review done? | Weak O# / Q# / Kindle notes |
|---|---|---|---|---|
| Standard Test 1 | 1 Nov | ____% | ☐ | |
| Standard Test 2 | 4 Nov | ____% | ☐ | |
| Standard Test 3 (fallback) | 7–8 Nov | ____% | ☐ | |
| Standard Test 4 (optional) | 9 Nov | ____% | ☐ | |

**6 Nov GO:** both Test 1 **and** Test 2 ≥80% on first attempts, with major errors understood. Otherwise switch to fallback. **11 Nov GO:** after remediation, Test 3 ≥80% first attempt, with major errors understood; otherwise defer until after Spain. This 80% threshold is a chosen safety margin, **not** an Oracle guarantee. Verify retake eligibility and booking availability.

**Want more mocks?** If you have an uninterrupted extra slot *and time to review*, move a Standard Test earlier and use the next untouched numbered test. Do not stack tests without reviewing. Enthuware says Standard Tests 1–16 are mutually non-repeating; Objective-wise/Practice/Custom tests can overlap with Standard Tests. Preserve the four Unique Tests as untouched final checks.

**Mock review rule:** wrong or guessed-correct → explanation → O# in §2 → Q# in report if applicable → Kindle subsection → runnable experiment. Confident-correct → skim explanation. Do not use repeat-attempt scores for readiness.

**Short spare-minute sessions:** Kindle chapter quizzes, code tracing, or a few Foundation/Custom questions; those may expose Standard Test bank questions, so prioritise freshness when close to mocks.

## 4. Coverage check and extras

**Every O1–O14 has a planned activity:** O3 and O14 get brief checks on 29 Oct; O1, O6, O8, O9, O12 and O13 are explicitly checked on 3 Nov, then refined by Mock 1; all others have dedicated sessions on 23–29 Oct. This is minimum coverage, not proof of mastery.

Book TOC headings may not separately identify **module import declarations, compact source files, flexible constructor bodies**: use Q13 for recall and check the book text or JDK 25 documentation if these remain unclear.

## 5. Progress notes

- [ ] 23–29 Oct targeted reading + Java experiments
- [ ] Standard Test 1 taken **and** reviewed
- [ ] Test 1 weaknesses resolved
- [ ] Standard Test 2 taken **and** reviewed
- [ ] Decision made: 6 Nov / fallback
- [ ] If fallback: Standard Test 3 taken, reviewed, threshold checked

**Recurring misconception → corrected rule → Q# / O#:**

---

**Files:** This is the **only action plan**. [POST_EXAM_REPORT.md](POST_EXAM_REPORT.md) is a separate historical reconstruction, and `examples/` contains its runnable code. Keep all three together so the relative links work.
