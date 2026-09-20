# Java-SE25-Exam
Notes to support the Oracle 1Z0-831 Java 25 SE Developer exam.

<a href="https://education.oracle.com/java-se-21-developer-professional/pexam_1Z0-830" ><img src="https://raw.githubusercontent.com/rysharprules/Java-SE8-Upgrade-Exam/master/ocp_logo.gif" /></a>

## JDK 21 Base
My base learning is from [OCP Oracle Certified Professional Java SE 21 Developer Study Guide: Exam 1z0-830](https://www.amazon.co.uk/Oracle-Certified-Professional-Developer-Study/dp/1394286619) via [O'Reilly](https://learning.oreilly.com/library/view/ocp-oracle-certified/9781394286614/).
This breaks the syllabus into 14 chapters.

<img src="img/ocp21book.jpg" alt="OCP Java SE 21 Developer Study Guide" width="200"/>

### Chapters

| Chapter | Assessment | Notes | Description |
|---------|------------|-------|-------------|
| 00 - Pre-Assessment | [Assessment](src/rysharp/jdk21/base/chapter/_00_Pre-Assessment/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_00_Pre-Assessment/notes.md) | Pre-Assessment |
| 01 - Building Blocks | [Assessment](src/rysharp/jdk21/base/chapter/_01_Building_Blocks/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_01_Building_Blocks/notes.md) | Java basics, variables, flow |
| 02 - Operators | [Assessment](src/rysharp/jdk21/base/chapter/_02_Operators/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_02_Operators/notes.md) | Operators and expressions |
| 03 - Making Decisions | [Assessment](src/rysharp/jdk21/base/chapter/_03_Making_Decisions/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_03_Making_Decisions/notes.md) | Conditionals, switch, loops |
| 04 - Core APIs | [Assessment](src/rysharp/jdk21/base/chapter/_04_Core_APIs/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_04_Core_APIs/notes.md) | Strings, arrays, dates |
| 05 - Methods | [Assessment](src/rysharp/jdk21/base/chapter/_05_Methods/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_05_Methods/notes.md) | Methods, parameters |
| 06 - Class Design | [Assessment](src/rysharp/jdk21/base/chapter/_06_Class_Design/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_06_Class_Design/notes.md) | Classes, inheritance |
| 07 - Beyond Classes | [Assessment](src/rysharp/jdk21/base/chapter/_07_Beyond_Classes/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_07_Beyond_Classes/notes.md) | Abstract, sealed, records |
| 08 - Lambdas & Functional Interfaces | [Assessment](src/rysharp/jdk21/base/chapter/_08_Lambdas_and_Functional_Interfaces/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_08_Lambdas_and_Functional_Interfaces/notes.md) | Lambdas, functional APIs |
| 09 - Collections & Generics | [Assessment](src/rysharp/jdk21/base/chapter/_09_Collections_and_Generics/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_09_Collections_and_Generics/notes.md) | Collections, generics |
| 10 - Streams | [Assessment](src/rysharp/jdk21/base/chapter/_10_Streams/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_10_Streams/notes.md) | Streams, pipelines |
| 11 - Exceptions & Localisation | [Assessment](src/rysharp/jdk21/base/chapter/_11_Exceptions_and_Localisation/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_11_Exceptions_and_Localisation/notes.md) | Exceptions, localisation |
| 12 - Modules | [Assessment](src/rysharp/jdk21/base/chapter/_12_Modules/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_12_Modules/notes.md) | Java modules |
| 13 - Concurrency | [Assessment](src/rysharp/jdk21/base/chapter/_13_Concurrency/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_13_Concurrency/notes.md) | Threads, concurrency |
| 14 - Input/Output | [Assessment](src/rysharp/jdk21/base/chapter/_14_Input_Output/assessment.md) | [Notes](src/rysharp/jdk21/base/chapter/_14_Input_Output/notes.md) | Input/Output |
| Summary | [Assessment Summary](src/rysharp/jdk21/base/assessment_summary.md) | - | Overall scores and stats |

## JDK 25 Addendum
An addendum for additions introduced across JDK 22–25 that show up in
the 1Z0-831 exam objectives.

| Feature | Description | Notes |
|---------|-------------|-------|
| Flexible Constructor Bodies (JEP 513) | Allows statements in constructor before `super()` or `this()`. | [Notes](src/rysharp/jdk25/addendum/flexible_constructor_bodies.md) |
| Unnamed Variables and Patterns (_) | Use `_` for unused variables. | [Notes](src/rysharp/jdk25/addendum/unnamed_variables.md) |
| Module Import Declarations (JEP 511) | `import module java.base;` | [Notes](src/rysharp/jdk25/addendum/module_import_declarations.md) |
| Compact Source Files and Instance Main Methods (JEP 512) | Streamlined main methods. | [Notes](src/rysharp/jdk25/addendum/compact_source_files_and_instance_main_methods.md) |
| Stream Gatherers | Flexible intermediate operations. | [Notes](src/rysharp/jdk25/addendum/stream_gatherers.md) |
| Scoped Values (JEP 506) | Immutable alternative to thread-local variables. | [Notes](src/rysharp/jdk25/addendum/scoped_values.md) |
| Minor API Additions | Various small updates. | [Notes](src/rysharp/jdk25/addendum/minor_api_additions.md) |

For a comprehensive breakdown of the platform updates, you can check the [Oracle JDK 25 Release Notes](https://www.oracle.com/asean/java/technologies/javase/25-relnote-issues.html).

For tracking the exact exam blueprint changes against Java 21, the [Enthuware OCP Java 25 Resources Page](https://enthuware.com/ocajp-8-fundamentals/114-resources/ocajp-ocpjp-resources) maps out the line-by-line topic differences.

## Additional Study Material

### Udemy

<img src="img/udemy.png" alt="Udemy" width="200"/>


[Java 25 Professional Certification - 6 full Tests (1Z0-831)](https://www.udemy.com/course/ocp-oracle-certified-professional-java-developer-prep/)
