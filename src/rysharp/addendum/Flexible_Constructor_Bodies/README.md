# Flexible Constructor Bodies (JEP 513)

Java 25 introduces **Flexible Constructor Bodies**, allowing statements to appear before an explicit constructor invocation:

```java
super(...);
```

or:

```java
this(...);
```

Before Java 25, `super(...)` or `this(...)` had to be the first statement in a constructor.

The new rules allow a **constructor prologue** before the explicit constructor invocation, provided that the prologue does not reference the object currently being constructed.

---

## 1. The basic idea

Before Java 25:

```java
class Person extends Human {
    Person(String name) {
        super(name);       // had to be first
        System.out.println("Created");
    }
}
```

Java 25 allows:

```java
class Person extends Human {
    Person(String name) {
        String cleaned = name.trim();
        super(cleaned);
        System.out.println("Created");
    }
}
```

The constructor is effectively divided into:

```text
PROLOGUE
    ↓
explicit super(...) or this(...)
    ↓
EPILOGUE
```

The **prologue** consists of statements before the explicit constructor invocation.

The **epilogue** consists of statements after it.

Oracle calls the code before the invocation the **early construction context**. The key restriction is that this code must not reference the instance currently being constructed.

---

# 2. What can happen in the prologue?

The prologue can perform operations that don't depend on the object being constructed.

For example:

```java
class Person extends Human {

    Person(String name) {
        String cleaned = name.trim();

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException();
        }

        super(cleaned);

        System.out.println("Created: " + this.name);
    }
}
```

The following are all reasonable examples of things that can occur in the prologue:

* Use constructor parameters
* Declare local variables
* Perform calculations
* Validate parameters
* Throw exceptions
* Call static methods
* Access static members
* Work with other objects
* Call instance methods on other objects

For example:

```java
String cleaned = name.trim();
```

is valid because `name` is a constructor parameter and `trim()` is being called on the `String` object referenced by `name`, not on the `Person` being constructed.

Similarly:

```java
System.out.println("Creating person");
```

is valid.

An important subtlety here is that `println()` itself is an **instance method**, but it is being called on the `PrintStream` object referenced by `System.out`. It is not being called on the `Person` being constructed.

So:

```java
this.someMethod();       // ❌ current object
someInstanceMethod();    // ❌ implicitly this
System.out.println(...); // ✅ another object
```

The rule is therefore better remembered as:

> **The prologue cannot access the object under construction. It isn't simply a rule that "instance methods are forbidden."**

---

# 3. What cannot happen in the prologue?

You cannot access the instance being constructed.

For example:

```java
class Person extends Human {

    String name;

    Person(String name) {
        System.out.println(this.name); // ❌
        super(name);
    }
}
```

Reading an instance field is not allowed.

This is also invalid:

```java
Person(String name) {
    name = cleanName();   // ❌ if cleanName() is an instance method
    super(name);
}
```

because an unqualified instance method call is effectively a call on `this`.

Likewise:

```java
Person(String name) {
    this.validate(name);  // ❌
    super(name);
}
```

The important mental test is:

> **Does this operation require the `Person` instance that is currently being constructed?**

If yes → it cannot be in the prologue.

---

# 4. `super(...)` with a prologue

This is the most obvious use case for JEP 513.

Suppose:

```java
class Person {
    Person(String name) {
        System.out.println(name);
    }
}
```

and:

```java
class Employee extends Person {

    Employee(String name) {
        String cleaned = name.trim();

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException();
        }

        super(cleaned);

        System.out.println("Employee created");
    }
}
```

The execution is conceptually:

```text
Employee constructor starts
        ↓
clean name
        ↓
validate name
        ↓
super(cleaned)
        ↓
Person constructor executes
        ↓
Employee epilogue executes
```

This is useful because validation or computation can happen **before the superclass constructor is invoked**.

For example, Oracle demonstrates validating an argument before passing it to a superclass constructor.

---

# 5. `this(...)` with a prologue

Flexible constructor bodies also work with constructor chaining using `this(...)`.

For example:

```java
class Person {

    Person(String name, int age) {
        System.out.println(name + " " + age);
    }

    Person(String name) {
        String cleaned = name.trim();

        this(cleaned, 0);
    }
}
```

Here:

```java
String cleaned = name.trim();
```

is the **prologue**.

Then:

```java
this(cleaned, 0);
```

invokes another constructor in the same class.

After that constructor returns, execution continues with any remaining statements in the original constructor.

For example:

```java
Person(String name) {
    String cleaned = name.trim();

    this(cleaned, 0);

    System.out.println("Finished");
}
```

Conceptually:

```text
Person(String)
    ↓
prologue
    ↓
this(cleaned, 0)
    ↓
Person(String, int)
    ↓
return
    ↓
"Finished"
```

So the same basic rule applies to both:

```text
                 Java 25
                    │
          ┌─────────┴─────────┐
          ↓                   ↓
      super(...)           this(...)
          ↑                   ↑
    prologue allowed    prologue allowed
```

---

# 6. The prologue ends at the explicit constructor invocation

A useful mental model is:

> **The prologue ends once the explicit `super(...)` or `this(...)` invocation is reached.**

For example:

```java
Person(String name) {

    // PROLOGUE
    String cleaned = name.trim();
    validate(cleaned);

    super(cleaned);

    // EPILOGUE
    this.name = cleaned;
    printDetails();
}
```

Everything before `super(cleaned)` is the prologue.

Everything after it is the epilogue.

Once the constructor invocation has completed, normal instance access is permitted.

---

# 7. What if there is no explicit `super()` or `this()`?

This is an important exam distinction.

Consider:

```java
class Person {

    Person(String name) {
        System.out.println(name);
    }
}
```

There is no explicit constructor invocation.

Java still has the normal implicit superclass constructor invocation.

The important exam mental model is:

> **If there is no explicit `super(...)` or `this(...)`, don't think that the statements you wrote become a prologue.**

There is no explicit invocation for them to precede.

The compiler supplies the implicit `super()` according to the normal constructor rules.

So the new flexible-constructor rule is primarily about:

```java
// statements
super(...);
```

or:

```java
// statements
this(...);
```

where the constructor invocation is **explicit**.

---

# 8. `super(...)` vs `this(...)`

The two cases are worth distinguishing.

### `super(...)`

Calls a constructor in the superclass:

```java
class Employee extends Person {

    Employee(String name) {
        String cleaned = name.trim();

        super(cleaned);
    }
}
```

### `this(...)`

Calls another constructor in the same class:

```java
class Person {

    Person(String name, int age) {
    }

    Person(String name) {
        String cleaned = name.trim();

        this(cleaned, 0);
    }
}
```

In both cases, Java 25 allows a suitable prologue before the invocation.

---

# 9. Constructor chaining still has its normal rules

Flexible constructor bodies do **not** remove constructor-chaining rules.

For example:

```java
class Person {

    Person(String name) {
    }

    Person(String name, int age) {
        this(name);
    }
}
```

is valid.

But this:

```java
Person(String name) {
    this(name);
}
```

would attempt to invoke the same constructor recursively.

That is not a runtime `StackOverflowError`.

Java detects constructor invocation cycles at **compile time**.

Likewise, two constructors with exactly the same signature are simply duplicate constructors and cause a compile-time error.

---

# 10. Parameters, locals and other objects

These are useful exam examples.

### Constructor parameter — allowed

```java
Person(String name) {
    String cleaned = name.trim();
    super(cleaned);
}
```

`name` is a parameter.

### Local variable — allowed

```java
Person(String name) {
    String cleaned = name.trim();
    int length = cleaned.length();

    super(cleaned);
}
```

Both variables are local/parameter data, not instance state.

### Static method — allowed

```java
Person(String name) {
    String cleaned = clean(name);
    super(cleaned);
}

static String clean(String name) {
    return name.trim();
}
```

`clean()` is static, so it does not require `this`.

### Another object — allowed

```java
Person(String name) {
    String cleaned = new String(name);
    super(cleaned);
}
```

The `String` object being created is a different object from the `Person` currently under construction.

### Instance method of another object — allowed

```java
Person(String name) {
    String cleaned = name.trim();
    super(cleaned);
}
```

`trim()` is an instance method, but it operates on the `String` object referred to by `name`, not the `Person`.

---

# 11. A useful exam trap: `System.out.println()`

This looks like an instance method call:

```java
System.out.println("Hello");
```

and technically `println()` **is** an instance method.

But this is legal in the prologue.

Why?

Because:

```java
System.out
```

is a reference to a `PrintStream` object.

The call is effectively:

```text
PrintStream object → println(...)
```

not:

```text
Person object → println(...)
```

Therefore:

```java
Person(String name) {
    System.out.println(name);  // ✅
    super(name);
}
```

is valid.

Whereas:

```java
Person(String name) {
    printName();               // ❌ instance method of Person
    super(name);
}
```

is not.

---

# 12. A particularly useful Java 25 example

Before flexible constructor bodies, you might have wanted to do this:

```java
class PositiveNumber extends NumberBase {

    PositiveNumber(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }

        super(value);
    }
}
```

That was not legal before Java 25 because `super(...)` had to come first.

With JEP 513, it is valid.

This lets the constructor reject invalid input **before invoking the superclass constructor**.

---

# 13. Records and `this(...)`

Flexible constructor bodies also matter for **non-canonical record constructors**.

For example:

```java
record Person(String name, int age) {

    Person(String name) {
        String cleaned = name.trim();

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this(cleaned, 0);
    }
}
```

The constructor is non-canonical because its parameter list does not match the record components.

It therefore delegates using:

```java
this(...)
```

and Java 25 allows the validation and preparation before that invocation.

A record's canonical constructor is a different case; it does not use an explicit `this(...)` or `super(...)` invocation in the same way.

Oracle specifically documents statements before `this(...)` in non-canonical record constructors.

---

# 14. The exam mental model

When you see a Java 25 constructor question, locate the explicit constructor invocation:

```java
this(...);
```

or:

```java
super(...);
```

Then divide the constructor into:

```text
┌──────────────────────────────┐
│ PROLOGUE                     │
│ before this()/super()        │
│                              │
│ Parameters                   │
│ Local variables              │
│ Calculations                 │
│ Validation                   │
│ Static members/methods       │
│ Other objects                │
│                              │
│ NO access to current object  │
└──────────────┬───────────────┘
               ↓
        this(...) / super(...)
               ↓
┌──────────────────────────────┐
│ EPILOGUE                     │
│ after constructor invocation │
│                              │
│ Instance fields              │
│ Instance methods             │
│ this                         │
│                              │
│ Normal constructor code      │
└──────────────────────────────┘
```

The most important rule is:

> **Before the explicit `this(...)` or `super(...)`, you may perform work that does not reference the object currently being constructed.**

---

## Quick exam checklist

When evaluating code before `this(...)` / `super(...)`, ask:

1. **Is this an explicit constructor invocation later in the constructor?**

    * If yes, the preceding statements form the prologue.

2. **Does the statement use a constructor parameter?**

    * ✅ Allowed.

3. **Does it use a local variable?**

    * ✅ Allowed.

4. **Does it use a static field or static method?**

    * ✅ Allowed.

5. **Does it operate on another object?**

    * ✅ Allowed.

6. **Does it access `this`?**

    * ❌ Not allowed.

7. **Does it access an instance field of the object being constructed?**

    * ❌ Not allowed.

8. **Does it call an instance method of the object being constructed?**

    * ❌ Not allowed.

9. **Does it call `System.out.println()`?**

    * ✅ Allowed — `println()` operates on another object (`PrintStream`).

10. **Does it create another object?**

    * ✅ Generally allowed — it is not the object currently being constructed.

11. **Does it invoke `this(...)` or `super(...)`?**

    * That marks the boundary: the prologue ends at the explicit constructor invocation.

### One-line memory aid

> **Java 25 lets you prepare the ingredients before `this()`/`super()`, but you can't touch the object you're currently cooking.**
