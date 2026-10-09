# Java Module Commands

A quick reference for compiling, running, inspecting, analysing and packaging Java modules.

For module types, directives, services and migration, see
[Java Modules](<Java Modules.md>).

## Contents

- [Example Application](#example-application)
- [javac](#javac)
- [java](#java)
- [jar](#jar)
- [jdeps](#jdeps)
- [jmod](#jmod)
- [jlink](#jlink)
- [jpackage](#jpackage)
- [Tool Comparison](#tool-comparison)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Example Application

The examples use a module named `com.example.zoo`.

```text
project/
├── src/
│   └── com.example.zoo/
│       ├── module-info.java
│       └── com/example/zoo/
│           └── Main.java
├── mods/
└── dist/
```

Module descriptor:

```java
// module-info.java
module com.example.zoo {
    exports com.example.zoo;
}
```

Application:

```java
// Main.java
package com.example.zoo;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Zoo!");
    }
}
```

Commands assume execution from the `project` directory.

Output examples are illustrative; paths, JDK versions and platform-specific
details may differ.

---

## javac

**Compiles Java source code into class files.**

```text
javac
→ COMPILE
```

### Compile a Class

```bash
javac Main.java
```

Result:

```text
Main.class
```

Successful compilation normally produces no console output.

### Compile a Module

```bash
javac \
    -d mods/com.example.zoo \
    src/com.example.zoo/module-info.java \
    src/com.example.zoo/com/example/zoo/Main.java
```

Result:

```text
mods/
└── com.example.zoo/
    ├── module-info.class
    └── com/example/zoo/
        └── Main.class
```

### Important Options

| Option | Purpose | Memory |
|---|---|---|
| `-d` | Output directory | Destination |
| `-p`, `--module-path` | Required modules | Path |
| `--module-source-path` | Modular source locations | Source |
| `-m`, `--module` | Modules to compile | Module |
| `-cp`, `--class-path` | Classpath dependencies | Classpath |

### Compile Multiple Modules

Given module source directories under `src`:

```bash
javac \
    --module-source-path src \
    -d mods \
    -m com.example.zoo
```

The compiler locates the source files belonging to the specified module.

Memory:

```text
javac:

SOURCE
→ COMPILE
→ DESTINATION

-d
→ DESTINATION

-p
→ dependency MODULE PATH
```

---

## java

**Launches an application.**

```text
java
→ RUN
```

### Run a Traditional Application

```bash
java -cp classes com.example.Main
```

Here:

```text
-cp
→ classpath

com.example.Main
→ main class
```

### Run a Modular Application

Using the compiled module:

```bash
java \
    -p mods \
    -m com.example.zoo/com.example.zoo.Main
```

Output:

```text
Welcome to the Zoo!
```

Long form:

```bash
java \
    --module-path mods \
    --module com.example.zoo/com.example.zoo.Main
```

Syntax:

```text
-m MODULE/MAIN_CLASS
```

### Important Options

| Option | Purpose |
|---|---|
| `-cp`, `--class-path` | Classpath |
| `-p`, `--module-path` | Module path |
| `-m`, `--module` | Module/main class |
| `--add-modules` | Additional root modules |
| `--list-modules` | List observable modules |
| `--describe-module`, `-d` | Describe a module |
| `--show-module-resolution` | Show module resolution |

### List Modules

```bash
java --list-modules
```

Illustrative output:

```text
java.base@25
java.compiler@25
java.desktop@25
java.logging@25
java.sql@25
...
```

### Describe a Module

```bash
java --describe-module java.sql
```

Illustrative output:

```text
java.sql@25
exports java.sql
exports javax.sql
requires java.base mandated
requires java.logging transitive
requires java.transaction.xa transitive
requires java.xml transitive
uses java.sql.Driver
```

This reveals the module's dependencies, exports and service declarations.

Memory:

```text
java -p
→ WHERE modules live

java -m
→ WHICH module to run
```

---

## jar

**Creates, updates and inspects JAR archives.**

```text
jar
→ JAR ARCHIVE
```

### Create a Modular JAR

After compiling the example module:

```bash
jar \
    --create \
    --file dist/zoo.jar \
    --main-class com.example.zoo.Main \
    -C mods/com.example.zoo .
```

Successful creation normally produces no output unless verbose mode is enabled.

Short form without setting the main class:

```bash
jar -cf dist/zoo.jar -C mods/com.example.zoo .
```

### List JAR Contents

```bash
jar -tf dist/zoo.jar
```

Illustrative output:

```text
META-INF/
META-INF/MANIFEST.MF
module-info.class
com/
com/example/
com/example/zoo/
com/example/zoo/Main.class
```

Memory:

```text
t
→ TABLE of contents

f
→ FILE
```

### Describe a Modular JAR

```bash
jar \
    --describe-module \
    --file dist/zoo.jar
```

Illustrative output:

```text
com.example.zoo jar:file:///.../dist/zoo.jar!/module-info.class
exports com.example.zoo
requires java.base mandated
main-class com.example.zoo.Main
```

### Describe an Automatic Module

For a non-modular JAR:

```bash
jar --describe-module --file cats-1.2.jar
```

Illustrative output:

```text
No module descriptor found. Derived automatic module.

cats@1.2 automatic
requires java.base mandated
contains com.example.cats
```

Useful for checking filename-derived module names.

### Important Options

| Option | Purpose | Memory |
|---|---|---|
| `-c`, `--create` | Create archive | Create |
| `-t`, `--list` | List contents | Table |
| `-x`, `--extract` | Extract contents | Extract |
| `-u`, `--update` | Update archive | Update |
| `-f`, `--file` | Specify JAR | File |
| `-v`, `--verbose` | Verbose output | Verbose |
| `-C` | Change directory when selecting files | Change |
| `--describe-module` | Module information | Describe |
| `--main-class` | Application main class | Main |

Memory:

```text
jar -cf
→ CREATE FILE

jar -tf
→ TABLE of FILE

jar -xf
→ EXTRACT FILE
```

---

## jdeps

**Analyses dependencies.**

```text
jDEPS
→ Java DEPENDENCIES
```

Particularly useful when migrating legacy applications.

### Basic Dependency Analysis

```bash
jdeps dist/zoo.jar
```

Illustrative output:

```text
com.example.zoo
 [file:///.../dist/zoo.jar]
   requires mandated java.base (@25)

com.example.zoo -> java.base
   com.example.zoo -> java.lang   java.base
```

Identifies dependencies used by the code.

### Summary

```bash
jdeps -s dist/zoo.jar
```

Equivalent:

```bash
jdeps --summary dist/zoo.jar
```

Illustrative output:

```text
com.example.zoo -> java.base
```

Memory:

```text
-s
→ SUMMARY
```

### Recursive Analysis

```bash
jdeps -R dist/zoo.jar
```

Equivalent:

```bash
jdeps --recursive dist/zoo.jar
```

Analyses dependencies recursively.

Memory:

```text
-R
→ RECURSIVE
```

### Print Module Dependencies

```bash
jdeps \
    --print-module-deps \
    dist/zoo.jar
```

Output:

```text
java.base
```

For an application using several JDK modules, illustrative output might be:

```text
java.base,java.logging,java.sql
```

Useful for `jlink`:

```text
jdeps --print-module-deps
           ↓
      MODULE LIST
           ↓
jlink --add-modules
```

### Identify Internal JDK APIs

```bash
jdeps --jdk-internals old-library.jar
```

Illustrative output when internal APIs are detected:

```text
old-library.jar -> jdk.unsupported
   com.example.Legacy -> sun.misc.Unsafe  JDK internal API
```

The exact output depends on the JDK and referenced APIs.

Memory:

```text
--jdk-internals
→ INTERNAL JDK API dependencies
```

### Generate Module Descriptor

```bash
jdeps \
    --generate-module-info generated \
    old-library.jar
```

Where supported, the tool creates a suggested descriptor.

Illustrative result:

```text
generated/
└── old.library/
    └── module-info.java
```

For example:

```java
module old.library {
    requires java.logging;
    exports com.example.legacy;
}
```

Generated descriptors should be reviewed.

### Important Options

| Option | Purpose | Memory |
|---|---|---|
| `-s`, `--summary` | Summary dependencies | Summary |
| `-R`, `--recursive` | Recursive analysis | Recursive |
| `-p`, `--module-path` | Locate modules | Path |
| `--print-module-deps` | Module list for linking | Print dependencies |
| `--jdk-internals` | Internal JDK APIs | Internals |
| `--generate-module-info` | Suggest descriptor | Generate |
| `--list-deps` | List module dependencies | List |
| `--list-reduced-deps` | Reduced dependency graph | Reduced |

### `--list-deps` vs `--print-module-deps`

```text
--list-deps
→ dependencies listed individually

--print-module-deps
→ comma-separated module list
```

Memory:

```text
jdeps
→ WHAT DOES THIS CODE DEPEND ON?
```

---

## jmod

**Creates and inspects JMOD files.**

```text
jmod
→ JMOD FILE
```

A JMOD can contain classes, native libraries and other runtime-image resources.

### Create a JMOD

```bash
jmod create \
    --class-path mods/com.example.zoo \
    dist/zoo.jmod
```

Result:

```text
dist/zoo.jmod
```

### List Contents

```bash
jmod list dist/zoo.jmod
```

Illustrative output:

```text
classes/module-info.class
classes/com/example/zoo/Main.class
```

### Describe a JMOD

```bash
jmod describe dist/zoo.jmod
```

Illustrative output:

```text
com.example.zoo
exports com.example.zoo
requires java.base mandated
```

### Important Commands

| Command | Purpose |
|---|---|
| `create` | Create JMOD |
| `list` | List contents |
| `describe` | Describe module |
| `extract` | Extract contents |
| `hash` | Record module hashes |

Memory:

```text
jar
→ JAR archive

jmod
→ JMOD archive

jlink
→ RUNTIME IMAGE
```

JMOD files are intended for linking/runtime construction, not ordinary execution with `java -jar`.

---

## jlink

**Builds a custom Java runtime image.**

```text
jLINK
→ LINK MODULES
→ CUSTOM RUNTIME
```

### Create Runtime Image

```bash
jlink \
    --module-path mods \
    --add-modules com.example.zoo \
    --output zoo-runtime
```

The module path must make the application module and its dependencies available.

Result:

```text
zoo-runtime/
├── bin/
│   └── java
├── conf/
├── legal/
├── lib/
└── release
```

The exact layout varies by operating system.

### Run With Custom Runtime

On Linux/macOS:

```bash
./zoo-runtime/bin/java \
    -p mods \
    -m com.example.zoo/com.example.zoo.Main
```

Output:

```text
Welcome to the Zoo!
```

The custom runtime supplies Java, while the application module is loaded from `mods`.

### Add an Application Launcher

```bash
jlink \
    --module-path mods \
    --add-modules com.example.zoo \
    --launcher zoo=com.example.zoo/com.example.zoo.Main \
    --output zoo-runtime
```

The resulting image contains a `zoo` launcher.

On Linux/macOS:

```bash
./zoo-runtime/bin/zoo
```

Output:

```text
Welcome to the Zoo!
```

On Windows, the corresponding launcher is an `.exe`.

### Important Options

| Option | Purpose | Memory |
|---|---|---|
| `-p`, `--module-path` | Locate modules | Path |
| `--add-modules` | Root modules | Add |
| `--output` | Runtime directory | Output |
| `--launcher` | Application launcher | Launch |
| `--strip-debug` | Remove debug information | Strip |
| `--compress` | Compress runtime resources | Compress |

### Important Restriction

Automatic modules cannot be linked directly into a `jlink` runtime image.

### Three-Word Memory

```text
jlink:

PATH
ADD
OUTPUT

--module-path
--add-modules
--output
```

---

## jpackage

**Builds application images or native application packages.**

```text
jPACKAGE
→ PACKAGE APPLICATION
```

Unlike `jlink`, the goal is to prepare an application for distribution.

### Package a Non-Modular Application

```bash
jpackage \
    --name Zoo \
    --input dist \
    --main-jar zoo.jar \
    --main-class com.example.zoo.Main \
    --type app-image
```

Illustrative output:

```text
Zoo/
├── Zoo launcher
├── app/
└── runtime/
```

The actual layout and launcher filename depend on the operating system.

### Package a Modular Application

```bash
jpackage \
    --name Zoo \
    --module-path mods \
    --module com.example.zoo/com.example.zoo.Main \
    --type app-image
```

This creates an application image using the specified module.

### Native Package Types

| Platform | Examples |
|---|---|
| Windows | `exe`, `msi` |
| macOS | `dmg`, `pkg` |
| Linux | `deb`, `rpm` |

Native packaging depends on platform-specific tooling.

### Important Options

| Option | Purpose | Memory |
|---|---|---|
| `--name` | Application name | Name |
| `--input` | Input directory | Input |
| `--main-jar` | Main JAR | Main JAR |
| `--main-class` | Main class | Main class |
| `--module-path` | Modular dependencies | Path |
| `--module` | Main module | Module |
| `--type` | Package type | Type |
| `--dest` | Output directory | Destination |
| `--runtime-image` | Existing runtime image | Runtime |

### `--input` vs `--dest`

```text
--input
→ WHERE application files come FROM

--dest
→ WHERE generated package goes TO
```

### `--module` vs `--main-jar`

```text
MODULAR application
→ --module

NON-MODULAR JAR application
→ --main-jar
```

---

## Tool Comparison

| Tool | Input | Main Output |
|---|---|---|
| `javac` | `.java` files | `.class` files |
| `java` | Classes/modules | Running application |
| `jar` | Classes/resources | `.jar` archive |
| `jdeps` | JAR/classes/modules | Dependency analysis |
| `jmod` | Classes/native resources | `.jmod` archive |
| `jlink` | Modules | Custom runtime image |
| `jpackage` | Application/modules | App image or native package |

### The Three Easily Confused Tools

```text
jdeps
→ ANALYSE dependencies

jlink
→ BUILD runtime

jpackage
→ PACKAGE application
```

Conceptual workflow:

```text
SOURCE
  ↓ javac
CLASS FILES
  ↓ jar
MODULAR JAR
  ↓ jdeps
DEPENDENCY INFORMATION
  ↓ jlink
CUSTOM RUNTIME
  ↓ jpackage
DISTRIBUTABLE APPLICATION
```

Not every application requires every step.

---

## Quick Reference

### Compile

```bash
javac -d OUT SOURCE
```

```bash
javac -p MODULE_PATH -d OUT SOURCE
```

### Run

```bash
java -cp CLASSPATH MainClass
```

```bash
java -p MODULE_PATH -m MODULE/MAIN_CLASS
```

### JAR

```bash
jar -cf app.jar -C classes .
jar -tf app.jar
jar --describe-module --file app.jar
```

### Dependencies

```bash
jdeps app.jar
jdeps -s app.jar
jdeps -R app.jar
jdeps --jdk-internals app.jar
jdeps --print-module-deps app.jar
jdeps --generate-module-info generated app.jar
```

### JMOD

```bash
jmod create --class-path classes app.jmod
jmod list app.jmod
jmod describe app.jmod
```

### Runtime

```bash
jlink \
    -p MODULE_PATH \
    --add-modules MODULE \
    --output RUNTIME
```

### Application Package

```bash
jpackage \
    --name APP \
    --module-path MODULE_PATH \
    --module MODULE/MAIN_CLASS \
    --type app-image
```

---

## Final Memory Kicks

```text
javac
→ COMPILE

java
→ RUN

jar
→ ARCHIVE

jdeps
→ ANALYSE

jmod
→ JMOD ARCHIVE

jlink
→ CUSTOM RUNTIME

jpackage
→ APPLICATION PACKAGE
```

```text
COMMON FLAGS:

-d
→ DESTINATION (javac)

-p
→ MODULE PATH

-m
→ MODULE

-c
→ CREATE (jar)

-t
→ TABLE/LIST (jar)

-f
→ FILE (jar)

-s
→ SUMMARY (jdeps)

-R
→ RECURSIVE (jdeps)
```

```text
JDEPS:

-s
→ SUMMARY

-R
→ RECURSIVE

--jdk-internals
→ INTERNAL APIs

--print-module-deps
→ MODULE LIST
```

```text
JLINK:

PATH
ADD
OUTPUT

--module-path
--add-modules
--output
```

```text
JPACKAGE:

NAME
INPUT
MAIN
TYPE
DESTINATION
```

```text
DON'T CONFUSE:

jdeps
→ WHAT DO I NEED?

jlink
→ BUILD MY RUNTIME

jpackage
→ DISTRIBUTE MY APP
```