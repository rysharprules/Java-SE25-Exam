# I/O Streams, Readers & Writers

A focused Java I/O reference covering byte and character streams, constructors, buffering, printing, serialization and common exam pitfalls.

## Contents

- [I/O Class Hierarchy](#io-class-hierarchy)
- [Byte Streams](#byte-streams)
- [Character Streams](#character-streams)
- [Constructors](#constructors)
- [Buffered Streams](#buffered-streams)
- [Print Streams and Writers](#print-streams-and-writers)
- [Reading and Writing Methods](#reading-and-writing-methods)
- [Object Serialization](#object-serialization)
- [Files Convenience Methods](#files-convenience-methods)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## I/O Class Hierarchy

Java's traditional I/O API is primarily in `java.io`.

There are two main families:

```text
BYTE STREAMS
InputStream
OutputStream

CHARACTER STREAMS
Reader
Writer
```

The distinction is the type of data being processed.

| Family | Processes | Common Classes |
|---|---|---|
| `InputStream` | Bytes | `FileInputStream`, `BufferedInputStream`, `ObjectInputStream` |
| `OutputStream` | Bytes | `FileOutputStream`, `BufferedOutputStream`, `ObjectOutputStream` |
| `Reader` | Characters | `FileReader`, `BufferedReader`, `InputStreamReader` |
| `Writer` | Characters | `FileWriter`, `BufferedWriter`, `PrintWriter` |

### Naming Conventions

```text
Input / Reader
→ READ

Output / Writer
→ WRITE

File
→ FILE ACCESS

Buffered
→ BUFFERING

Object
→ SERIALIZATION
```

The names are useful, but don't assume all classes have identical constructors.

---

## Byte Streams

Byte streams operate on raw bytes.

### FileInputStream

Reads bytes from a file.

```java
try (FileInputStream input =
        new FileInputStream("data.bin")) {

    int value;

    while ((value = input.read()) != -1) {
        System.out.println(value);
    }
}
```

Important:

- `read()` returns an `int`.
- The value is `0–255` for a byte read.
- `-1` indicates end of stream.

The return type is `int` so that `-1` can represent EOF without conflicting with valid byte values.

### FileOutputStream

Writes bytes to a file.

```java
try (FileOutputStream output =
        new FileOutputStream("data.bin")) {

    output.write(65);
    output.write(66);
}
```

The resulting bytes represent:

```text
65 66
```

When interpreted as ASCII/UTF-8 characters:

```text
AB
```

### Append Mode

```java
new FileOutputStream("data.bin", true);
```

The second argument enables appending.

```text
false / omitted
→ OVERWRITE

true
→ APPEND
```

### Important Constructors

```java
new FileInputStream("data.bin");
new FileInputStream(new File("data.bin"));

new FileOutputStream("data.bin");
new FileOutputStream(new File("data.bin"));

new FileOutputStream("data.bin", true);
```

These classes support filename-based constructors.

---

## Character Streams

Character streams operate on characters rather than raw bytes.

### FileReader

```java
try (FileReader reader =
        new FileReader("notes.txt")) {

    int character;

    while ((character = reader.read()) != -1) {
        System.out.print((char) character);
    }
}
```

As with `InputStream`, `Reader.read()` returns an `int` so it can represent `-1` for EOF.

### FileWriter

```java
try (FileWriter writer =
        new FileWriter("notes.txt")) {

    writer.write("Hello Java");
}
```

### Append Mode

```java
new FileWriter("notes.txt", true);
```

Appends instead of overwriting.

### Important Constructors

```java
new FileReader("notes.txt");
new FileReader(new File("notes.txt"));

new FileWriter("notes.txt");
new FileWriter(new File("notes.txt"));

new FileWriter("notes.txt", true);
```

Modern JDKs also provide constructors accepting `Charset` and other supported arguments.

---

## Constructors

This is a particularly important PE1 revision area.

Some I/O classes can open a file directly. Others require an existing stream or writer.

### Constructor Comparison

| Class | Accepts filename `String` directly? | Typical Constructor |
|---|---|---|
| `FileInputStream` | Yes | `new FileInputStream("a.txt")` |
| `FileOutputStream` | Yes | `new FileOutputStream("a.txt")` |
| `FileReader` | Yes | `new FileReader("a.txt")` |
| `FileWriter` | Yes | `new FileWriter("a.txt")` |
| `BufferedInputStream` | No | `new BufferedInputStream(input)` |
| `BufferedOutputStream` | No | `new BufferedOutputStream(output)` |
| `BufferedReader` | No | `new BufferedReader(reader)` |
| `BufferedWriter` | No | `new BufferedWriter(writer)` |
| `PrintWriter` | Yes | `new PrintWriter("a.txt")` |
| `PrintStream` | Yes | `new PrintStream("a.txt")` |
| `ObjectInputStream` | No | `new ObjectInputStream(input)` |
| `ObjectOutputStream` | No | `new ObjectOutputStream(output)` |

### Common Trap

This is valid:

```java
Writer writer = new FileWriter("output.txt");
```

This is invalid:

```java
Writer writer = new BufferedWriter("output.txt");
```

`BufferedWriter` requires a `Writer`.

Correct:

```java
Writer writer =
        new BufferedWriter(new FileWriter("output.txt"));
```

### PE1 Constructor Recognition

All of these are valid:

```java
new FileWriter("output.txt");

new PrintWriter("output.txt");

new FileOutputStream("output.txt");
```

But this is not:

```java
new BufferedWriter("output.txt");
```

Memory:

```text
FILE classes
→ commonly open files directly

BUFFERED classes
→ WRAP existing streams/readers/writers

PrintWriter / PrintStream
→ CAN accept filenames directly
```

---

## Buffered Streams

Buffering reduces the number of underlying I/O operations by processing data in chunks.

### BufferedInputStream

```java
try (BufferedInputStream input =
        new BufferedInputStream(
                new FileInputStream("data.bin"))) {

    System.out.println(input.read());
}
```

### BufferedOutputStream

```java
try (BufferedOutputStream output =
        new BufferedOutputStream(
                new FileOutputStream("data.bin"))) {

    output.write(65);
}
```

### BufferedReader

```java
try (BufferedReader reader =
        new BufferedReader(
                new FileReader("notes.txt"))) {

    String line;

    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```

`readLine()` returns:

```text
String
→ when a line is available

null
→ end of file
```

The returned string does not include the line terminator.

### BufferedWriter

```java
try (BufferedWriter writer =
        new BufferedWriter(
                new FileWriter("notes.txt"))) {

    writer.write("Hello");
    writer.newLine();
    writer.write("Java");
}
```

`newLine()` writes the platform-specific line separator.

### Stream Chaining

```text
FILE
  ↓
FileInputStream
  ↓
BufferedInputStream
  ↓
APPLICATION
```

For output:

```text
APPLICATION
  ↓
BufferedWriter
  ↓
FileWriter
  ↓
FILE
```

The wrapper must accept the type supplied by the inner object.

---

## Print Streams and Writers

`PrintWriter` and `PrintStream` provide convenient printing methods.

### PrintWriter

```java
try (PrintWriter writer =
        new PrintWriter("output.txt")) {

    writer.print("Hello");
    writer.println(" Java");
    writer.printf("Value: %d%n", 42);
}
```

File contents:

```text
Hello Java
Value: 42
```

### PrintStream

```java
try (PrintStream output =
        new PrintStream("output.txt")) {

    output.println("Hello");
    output.printf("Number: %d%n", 10);
}
```

### Common Methods

| Method | Behaviour |
|---|---|
| `print()` | Prints without newline |
| `println()` | Prints with newline |
| `printf()` | Formatted output |
| `format()` | Formatted output |
| `checkError()` | Checks the stream's error state |

### Exception Behaviour

Unlike many other I/O methods, the printing methods of `PrintWriter` and `PrintStream` do not normally throw `IOException`.

Instead, they record errors internally.

```java
writer.checkError();
```

Returns a boolean indicating whether an error has occurred.

Constructors may still throw checked exceptions, such as `FileNotFoundException` or `IOException`.

---

## Reading and Writing Methods

### Method Comparison

| Method | Class Family | Purpose |
|---|---|---|
| `read()` | `InputStream`, `Reader` | Read one byte/character |
| `read(byte[])` | `InputStream` | Read into byte array |
| `read(char[])` | `Reader` | Read into char array |
| `write(int)` | Output/Writer | Write one byte/character |
| `write(byte[])` | `OutputStream` | Write bytes |
| `write(char[])` | `Writer` | Write characters |
| `write(String)` | `Writer` | Write string |
| `readLine()` | `BufferedReader` | Read line |
| `newLine()` | `BufferedWriter` | Write line separator |
| `flush()` | Output/Writer | Flush buffered output |

### Byte vs Character Arrays

```java
byte[] bytes = new byte[10];
char[] chars = new char[10];
```

```java
inputStream.read(bytes);
reader.read(chars);
```

Do not interchange these arrays.

### flush()

```java
writer.flush();
```

Forces buffered output toward the underlying destination.

Closing an output stream or writer normally flushes its buffered data first.

### mark() and reset()

Some input streams and readers support marking a position and returning to it.

```java
reader.mark(100);
reader.read();
reader.reset();
```

Not every stream supports marking.

Check:

```java
inputStream.markSupported();
```

`BufferedReader` supports `mark()` and `reset()`.

---

## Object Serialization

Serialization converts objects into a byte representation.

Deserialization reconstructs objects from serialized data.

### Serializable

```java
import java.io.Serializable;

class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

`Serializable` is a marker interface: it declares no abstract methods.

### Serialize

```java
Animal animal = new Animal("Lion");

try (ObjectOutputStream output =
        new ObjectOutputStream(
                new FileOutputStream("animal.ser"))) {

    output.writeObject(animal);
}
```

### Deserialize

```java
try (ObjectInputStream input =
        new ObjectInputStream(
                new FileInputStream("animal.ser"))) {

    Animal animal = (Animal) input.readObject();

    System.out.println(animal.getName());
}
```

Output:

```text
Lion
```

`readObject()` returns `Object`, requiring a cast when assigning to a more specific type.

It may throw `ClassNotFoundException`.

### transient

A `transient` instance field is excluded from default serialization.

```java
class Animal implements Serializable {

    private String name;
    private transient int age;
}
```

After deserialization, `age` has its default value:

```text
0
```

### static Fields

Static fields belong to the class, not the object, so they are not serialized as object state.

### Non-Serializable Fields

If a serializable object contains a non-transient instance field referencing a non-serializable object, serialization can fail with `NotSerializableException`.

### Constructors During Deserialization

For a serializable class, its constructors and instance initializers are not run during normal deserialization.

However, the no-argument constructor of the first non-serializable superclass is invoked.

### serialVersionUID

```java
private static final long serialVersionUID = 1L;
```

Used to check serialization compatibility between class versions.

A mismatch can cause `InvalidClassException`.

### Serialization Summary

| Field/Feature | Serialized? |
|---|---|
| Ordinary instance field | Yes |
| `transient` field | No |
| `static` field | No |
| Serializable referenced object | Yes, recursively |
| Non-serializable referenced object | Can cause failure |

---

## Files Convenience Methods

The `java.nio.file.Files` class provides static methods that can replace traditional stream construction.

### Files.write()

```java
Path path = Path.of("output.txt");

Files.write(path, "Hello".getBytes());
```

Valid.

`Files.write()` supports byte arrays and iterable character sequences through different overloads.

### Files.writeString()

```java
Files.writeString(
        Path.of("output.txt"),
        "Hello Java"
);
```

Writes a string using UTF-8 by default.

### Files.readString()

```java
String text =
        Files.readString(Path.of("output.txt"));
```

Reads file contents into a string.

### Files.readAllLines()

```java
List<String> lines =
        Files.readAllLines(Path.of("output.txt"));
```

Returns a `List<String>`.

### Files.newBufferedWriter()

```java
try (BufferedWriter writer =
        Files.newBufferedWriter(Path.of("output.txt"))) {

    writer.write("Hello Java");
}
```

This is an alternative to:

```java
new BufferedWriter(new FileWriter("output.txt"));
```

### Files.newBufferedReader()

```java
try (BufferedReader reader =
        Files.newBufferedReader(Path.of("output.txt"))) {

    System.out.println(reader.readLine());
}
```

### Return Types

| Method | Returns |
|---|---|
| `Files.readString(path)` | `String` |
| `Files.readAllLines(path)` | `List<String>` |
| `Files.readAllBytes(path)` | `byte[]` |
| `Files.lines(path)` | `Stream<String>` |
| `Files.write(path, bytes)` | `Path` |
| `Files.writeString(path, text)` | `Path` |
| `Files.newBufferedReader(path)` | `BufferedReader` |
| `Files.newBufferedWriter(path)` | `BufferedWriter` |

`Files.lines()` returns a stream that should be closed, typically using try-with-resources.

File operations and their options are covered in the separate **Files & Paths** guide.

---

## Quick Reference

### Constructor Validity

```java
new FileInputStream("a.txt");   // VALID
new FileOutputStream("a.txt");  // VALID

new FileReader("a.txt");        // VALID
new FileWriter("a.txt");        // VALID

new PrintWriter("a.txt");       // VALID
new PrintStream("a.txt");       // VALID
```

```java
new BufferedReader("a.txt");       // INVALID
new BufferedWriter("a.txt");       // INVALID

new BufferedInputStream("a.txt");  // INVALID
new BufferedOutputStream("a.txt"); // INVALID
```

### Wrapping

```java
new BufferedReader(new FileReader("a.txt"));

new BufferedWriter(new FileWriter("a.txt"));

new BufferedInputStream(new FileInputStream("a.txt"));

new BufferedOutputStream(new FileOutputStream("a.txt"));
```

### EOF

```text
InputStream.read()
→ -1

Reader.read()
→ -1

BufferedReader.readLine()
→ null
```

### File Convenience Methods

```java
Files.readString(path);          // String
Files.readAllLines(path);        // List<String>
Files.readAllBytes(path);        // byte[]
Files.lines(path);               // Stream<String>

Files.write(path, bytes);        // Path
Files.writeString(path, text);   // Path

Files.newBufferedReader(path);   // BufferedReader
Files.newBufferedWriter(path);   // BufferedWriter
```

---

## Final Memory Kicks

```text
BYTE I/O
→ InputStream / OutputStream

CHARACTER I/O
→ Reader / Writer
```

```text
FILE CLASSES
→ can open files directly

BUFFERED CLASSES
→ require wrapping

PRINT CLASSES
→ can accept filenames
```

```text
READING:

read()
→ int
→ -1 at EOF

readLine()
→ String
→ null at EOF
```

```text
WRITING:

FileWriter
→ characters

FileOutputStream
→ bytes

BufferedWriter.newLine()
→ platform line separator
```

```text
SERIALIZATION:

Serializable
→ marker interface

transient
→ excluded

static
→ excluded

readObject()
→ Object
```

```text
FILES API:

readString()
→ String

readAllLines()
→ List<String>

lines()
→ Stream<String>

write()
→ Path

newBufferedWriter()
→ BufferedWriter
```

```text
PE1 Q25:

FileWriter("file")
→ VALID

PrintWriter("file")
→ VALID

FileOutputStream("file")
→ VALID

BufferedWriter("file")
→ INVALID

Files.write(path, bytes)
→ VALID
```