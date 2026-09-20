# The Finite List: What Java 25 Adds Beyond Java 21

Here is the exact checklist of additions introduced across JDK 22–25 that show up in 
the 1Z0-831 exam objectives:

## Flexible Constructor Bodies (JEP 513):
You can write statements in a constructor before calling 
`super()` or `this()`, as long as they don't reference this. Great for validation or 
argument preparation.   

## Unnamed Variables and Patterns (_):
The underscore is a valid identifier for unused variables in catch blocks, 
lambdas, enhanced for-loops, and record patterns (e.g., `Point(int x, int _)`).
See [Unnamed Variables](UnnamedVariables.md) for more detail.

## Module Import Declarations (JEP 511):
`import module java.base;` 
imports all public packages exported by that module in a single stroke.   
See [Module Import Declarations](ModuleImportDeclarations.md) for more detail.

## Compact Source Files and Instance Main Methods (JEP 512):
You can drop `public static void main(String[] args)` down to a simple `void main()`.
You can have fields and methods accessible to it, 
but it leans into the streamlined "launch single-class files directly" paradigm.

## Stream Gatherers (Introduced in Java 24 / Part of 25 Stream API ecosystem):
A new intermediate operation (`Stream.gather(...)`) that lets you write custom
map/filter/reduce-style intermediate operations far more flexibly than 
traditional collectors.   

## Scoped Values (JEP 506):
This is Oracle's modern, immutable alternative to thread-local variables. 
They are designed to safely share data between a method and its callees 
(or child threads, especially virtual threads) without the mutation risks of traditional ThreadLocal.   

## Minor API Additions:
New methods like `Instant.until(Instant)`, convenience 
reader/console formatting updates, and primitive types allowed directly in 
instanceof and switch patterns.   

For a comprehensive breakdown of the platform updates, you can check the [Oracle JDK 25 Release Notes](https://www.oracle.com/asean/java/technologies/javase/25-relnote-issues.html).

For tracking the exact exam blueprint changes against Java 21, the [Enthuware OCP Java 25 Resources Page](https://enthuware.com/ocajp-8-fundamentals/114-resources/ocajp-ocpjp-resources) maps out the line-by-line topic differences.