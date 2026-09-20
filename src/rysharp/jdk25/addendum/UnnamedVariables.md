# Unnamed Variables `_`

Java 22 made **unnamed variables and patterns** permanent.

Previously, you might write:

```java
try {
    // something
} catch (Exception e) {
    System.out.println("Failed");
}
```

If you don't actually need `e`, Java 22+ lets you write:

```java
try {
    // something
} catch (Exception _) {
    System.out.println("Failed");
}
```

The `_` says:

> **I deliberately don't care about this value.**

You can also use it with lambda parameters:

```java
BiFunction<Integer, Integer, Integer> f =
    (x, _) -> x * 2;
```

The second argument is deliberately ignored.

### ⚠️ Exam trap

`_` isn't a normal variable name anymore in these contexts.

This is **not** valid:

```java
var _ = 10;
```

The underscore is specifically used for **unnamed variables and patterns**.

### Where can Java 25 use `_`?

There's actually more than just catch blocks and lambdas. The useful way to remember it is:

> **If Java requires you to declare a local variable/pattern, but you genuinely don't need to give it a usable name, `_` can often say "I don't need this."**

For Java 25, that includes:

```java
// Local variable
int _ = calculateSomething();

// for loop
for (int _ = 0; _ < 10; _++) {
    doSomething();
}

// enhanced for
for (String _ : names) {
    doSomething();
}

// try-with-resources
try (var _ = openResource()) {
    doSomething();
}

// catch
try {
    something();
} catch (Exception _) {
    handleFailure();
}

// lambda
list.forEach(_ -> doSomething());
```

And importantly, `_` also applies to **patterns**, including pattern matching and record patterns. That's one of the more interesting uses:

```java
if (obj instanceof String _) {
    // We only care that obj IS a String.
}
```

#### The simple mental rule

Think:

**Local → Loop → Catch → Lambda → Pattern**

`_` is legal for:

* **Local variables**

  ```java
  int _ = calculate();
  ```

* **`for` variables**

  ```java
  for (int _ : numbers) { }
  ```

* **try-with-resources**

  ```java
  try (var _ = resource()) { }
  ```

* **catch parameters**

  ```java
  catch (Exception _) { }
  ```

* **lambda parameters**

  ```java
  list.forEach(_ -> doSomething());
  ```

* **patterns**, including record patterns and `switch`

  ```java
  if (obj instanceof Point(_, _)) { }
  ```

### The big exam rule: **you cannot refer to `_`**

This is probably the **most important thing to remember**:

```java
catch (Exception _) {
    System.out.println(_);  // ❌ DOES NOT COMPILE
}
```

There is deliberately **no variable called `_`**. The whole point is that the value is unnamed and therefore cannot be referenced.

So if you see:

```java
int _ = 10;
System.out.println(_);
```

🚨 **Compile error.**

This is **not** legal:

```
class Example {
   int _ = 10;       // ❌ DOES NOT COMPILE
   static String _;  // ❌ DOES NOT COMPILE
}
```

Java specifically permits `_` only in certain unnamed-variable/pattern contexts.

### And one particularly nasty trap

Don't think of `_` as a universal wildcard.

This is **not legal**:

```java
if (obj instanceof _) { }  // ❌
```

Nor:

```java
case _ -> ...              // ❌
```

Java doesn't currently allow `_` as a standalone, top-level "match anything" pattern.

Instead, `_` is useful **inside the pattern structures where Java has a thing that can be deliberately ignored**.

---