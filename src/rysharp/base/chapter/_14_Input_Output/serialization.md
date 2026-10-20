
# Serialization

[🔙 Back](README.md)

## Overview

Serialization converts an object's state into a byte stream. Deserialization reconstructs an object from that stream.

Java provides two main mechanisms:

- `Serializable` — default serialization, with optional customisation.
- `Externalizable` — explicit control over reading and writing an object's state.

---

## 1. Serializable

`Serializable` is a **marker interface**. It contains no methods.

```java
import java.io.Serializable;

class Person implements Serializable {
    private String name;
    private int age;
}
```

An instance can be serialized using `ObjectOutputStream` and deserialized using `ObjectInputStream`.

```java
out.writeObject(person);

Person restored = (Person) in.readObject();
```

`readObject()` returns `Object`, so a cast is normally required.

### What gets serialized?

By default, Java serializes the object's non-static, non-transient instance fields.

| Field | Serialized? |
|---|---|
| Ordinary instance field | Yes |
| `static` field | No |
| `transient` field | No |
| `final` instance field | Yes |
| Reference to another serializable object | Yes |
| Reference to a non-serializable object | Throws `NotSerializableException` |

**Important:** A class implementing `Serializable` does not automatically make every object referenced by its fields serializable.

---

## 2. Object Graphs and NotSerializableException

Serialization traverses the reachable object graph.

```java
class Item {
    String name;
}

class Order implements Serializable {
    List<Item> items;
}
```

Although `Order` implements `Serializable`, its `Item` objects do not.

Serializing an `Order` containing `Item` instances throws:

```text
java.io.NotSerializableException
```

The fact that `ArrayList` itself is serializable does not make its elements serializable.

### Possible solutions

1. Make `Item` implement `Serializable`.
2. Mark the field `transient` if it does not need to be preserved.
3. Implement custom serialization and deserialization.

Marking a field `transient` prevents it from being serialized; it does not preserve its contents.

---

## 3. Constructors During Deserialization

This is one of the most important certification distinctions.

### Ordinary Serializable classes

When an ordinary serializable object is deserialized:

- Constructors of serializable classes in its hierarchy are **not executed**.
- The no-argument constructor of the first non-serializable superclass **is executed**.
- Instance field initializers and instance initialization blocks belonging to the serializable classes are not executed as part of reconstruction.
- Serialized field values are restored from the stream.

```java
class Parent {
    Parent() {
        System.out.print("Parent ");
    }
}

class Child extends Parent implements Serializable {
    Child() {
        System.out.print("Child ");
    }
}
```

Creating a new `Child` normally prints:

```text
Parent Child
```

Deserializing a previously serialized `Child` prints:

```text
Parent
```

The first non-serializable superclass must have an accessible no-argument constructor for ordinary default deserialization.

### Important exception

**Records and enums behave differently.**

Do not apply the ordinary `Serializable` constructor rule to them without checking their special serialization rules.

---

## 4. Records and Serialization

A record can implement `Serializable`.

```java
record Person(int age, String name) implements Serializable {

    Person {
        System.out.print("Canonical ");
    }

    Person() {
        this(0, "");
        System.out.print("NoArgs ");
    }
}
```

### Key rule

**During record deserialization, Java invokes the record's canonical constructor.**

For a compact canonical constructor:

```java
Person {
    System.out.print("Canonical ");
}
```

the body executes when the record is reconstructed.

The additional no-argument constructor is not invoked.

### Example

Assume this object was serialized:

```java
new Person(60, "John");
```

During deserialization, the output is:

```text
Canonical
```

Not:

```text
Canonical NoArgs
```

### Why?

Record serialization uses the record components to reconstruct the record through its canonical constructor.

This differs from ordinary serializable classes, whose constructors are bypassed during deserialization.

---

## 5. Enums and Serialization

All enum types are serializable automatically.

```java
enum Gender {
    MALE("M"),
    FEMALE("F"),
    OTHER;

    Gender() {
        System.out.print("Gender ");
    }

    Gender(String value) {
        System.out.print("GenderS ");
    }
}
```

An enum does not need to declare `implements Serializable`.

### Key rule

**Enum constants are not reconstructed as new objects during deserialization.**

Java resolves the existing enum constant by name.

Consequently:

```java
restoredGender == Gender.MALE
```

can be `true`, because the deserialized value refers to the existing enum singleton.

### Why might enum constructors print?

Enum constructors execute when the enum class is initialized.

This can happen during deserialization if the enum class has not already been initialized.

For the example above, first-time initialization prints:

```text
GenderS GenderS Gender
```

This is because Java initializes all three enum constants:

- `MALE("M")` calls the String constructor.
- `FEMALE("F")` calls the String constructor.
- `OTHER` calls the no-argument constructor.

If the enum has already been initialized, these constructors do not execute again.

---

## 6. Externalizable

`Externalizable` extends `Serializable` and provides explicit control over object state.

```java
class Data implements Externalizable {

    public Data() {
    }

    @Override
    public void writeExternal(ObjectOutput out)
            throws IOException {
        // Write the required state.
    }

    @Override
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException {
        // Restore the required state.
    }
}
```

### Exam rules

- The class must provide an accessible **public no-argument constructor**.
- `writeExternal()` and `readExternal()` are public.
- The no-argument constructor executes during deserialization.
- The programmer is responsible for writing and restoring the required state.
- Fields are not automatically persisted through `defaultWriteObject()` as part of the `Externalizable` contract.

---

## 7. transient and static Fields

### transient

A `transient` field is excluded from default serialization.

```java
class User implements Serializable {
    String username;
    transient String password;
}
```

After deserialization, `password` is normally `null`, unless custom restoration logic is used.

For transient primitive fields, the default values are used:

- `int` → `0`
- `boolean` → `false`
- `double` → `0.0`

### static

Static fields belong to the class, not individual instances.

They are not included in an object's serialized state.

```java
class Counter implements Serializable {
    static int total;
    int value;
}
```

Serializing a `Counter` preserves `value`, not `total`.

---

## 8. serialVersionUID

A serializable class can declare:

```java
private static final long serialVersionUID = 1L;
```

The `serialVersionUID` identifies the serialized class version for compatibility checking.

If a compatible identifier is not supplied, Java computes one automatically.

An incompatible serial version can cause:

```text
InvalidClassException
```

Declaring the same `serialVersionUID` does not guarantee that every class change is serialization-compatible.

---

## 9. Constructor and Serialization Comparison

| Type | Constructor behaviour during deserialization | Key point |
|---|---|---|
| Ordinary `Serializable` class | First non-serializable superclass's no-arg constructor runs | Serializable class constructors are bypassed |
| Serializable record | Canonical constructor runs | Record components are reconstructed through the canonical constructor |
| Enum | Existing constant is resolved | Enum constructors run only if class initialization is required |
| `Externalizable` class | Public no-arg constructor runs | State is restored by `readExternal()` |

This table is particularly important for the certification exam.

---