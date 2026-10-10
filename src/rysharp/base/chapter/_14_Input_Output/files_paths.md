# Files & Paths

[🔙 Back](README.md)

A focused reference for Java NIO.2 paths, filesystem operations, copy/move/delete behaviour and exam-relevant API distinctions.

## Contents

- [Path and Files](#path-and-files)
- [Creating Paths](#creating-paths)
- [Path Methods](#path-methods)
- [Resolving and Relativizing](#resolving-and-relativizing)
- [Normalizing and Real Paths](#normalizing-and-real-paths)
- [Creating Files and Directories](#creating-files-and-directories)
- [Copying Files](#copying-files)
- [Moving Files](#moving-files)
- [Deleting Files](#deleting-files)
- [Copy and Move Options](#copy-and-move-options)
- [File State Tracking](#file-state-tracking)
- [Checking Files and Paths](#checking-files-and-paths)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Path and Files

Java NIO.2 primarily uses classes from `java.nio.file`.

Two important types:

```java
Path path = Path.of("data.txt");

Files.exists(path);
```

| Type | Purpose |
|---|---|
| `Path` | Represents a filesystem path |
| `Files` | Provides static methods to operate on files and directories |

A `Path` is a description of a location. Creating a `Path` object does **not** create a file.

```java
Path path = Path.of("missing.txt");
```

This compiles and creates a `Path` object even when `missing.txt` does not exist.

---

## Creating Paths

### Path.of()

```java
Path a = Path.of("data.txt");

Path b = Path.of("folder", "data.txt");
```

`Path.of()` is a static factory method.

### Paths.get()

```java
Path path = Paths.get("folder", "data.txt");
```

Also valid.

For modern Java, `Path.of()` is generally preferred, but both can appear in exam questions.

### Absolute vs Relative

```java
Path relative = Path.of("folder/data.txt");

Path absolute = Path.of("/home/user/data.txt");
```

A relative path depends on the working directory or another base path.

An absolute path identifies a location from the filesystem root.

```java
path.isAbsolute();
```

Returns `true` or `false`.

### toAbsolutePath()

```java
Path path = Path.of("data.txt");

System.out.println(path.toAbsolutePath());
```

Converts the representation to an absolute path using the current working directory.

It does **not** require the file to exist.

---

## Path Methods

Consider a Unix-style path:

```java
Path path = Path.of("/home/user/docs/file.txt");
```

### getFileName()

```java
path.getFileName();
```

Result:

```text
file.txt
```

### getParent()

```java
path.getParent();
```

Result:

```text
/home/user/docs
```

### getRoot()

```java
path.getRoot();
```

Result:

```text
/
```

For a relative path, `getRoot()` returns `null`.

### getNameCount()

```java
path.getNameCount();
```

Result:

```text
4
```

The four name elements are:

```text
home
user
docs
file.txt
```

The root `/` is not counted as a name element.

### getName()

```java
path.getName(0); // home
path.getName(1); // user
path.getName(2); // docs
path.getName(3); // file.txt
```

Indices start at zero.

An invalid index throws `IllegalArgumentException`.

### subpath()

```java
path.subpath(1, 3);
```

Result:

```text
user/docs
```

The beginning index is inclusive and the ending index is exclusive.

```text
subpath(1, 3)
→ elements 1 and 2
```

An invalid range throws `IllegalArgumentException`.

### Important Path Distinctions

| Method | Return Type |
|---|---|
| `getFileName()` | `Path` |
| `getParent()` | `Path` or `null` |
| `getRoot()` | `Path` or `null` |
| `getNameCount()` | `int` |
| `getName(int)` | `Path` |
| `subpath(int, int)` | `Path` |

---

## Resolving and Relativizing

These methods are frequently confused.

### resolve()

Combines paths.

```java
Path parent = Path.of("/home/user");

Path child = Path.of("docs/file.txt");

System.out.println(parent.resolve(child));
```

Output:

```text
/home/user/docs/file.txt
```

If the argument is absolute, it replaces the base path:

```java
Path.of("/home/user")
        .resolve(Path.of("/tmp/file.txt"));
```

Result:

```text
/tmp/file.txt
```

### resolveSibling()

Resolves a path against the current path's parent.

```java
Path path = Path.of("/home/user/a.txt");

System.out.println(path.resolveSibling("b.txt"));
```

Output:

```text
/home/user/b.txt
```

### relativize()

Calculates a relative path between two paths.

```java
Path a = Path.of("/home/user");

Path b = Path.of("/home/user/docs/file.txt");

System.out.println(a.relativize(b));
```

Output:

```text
docs/file.txt
```

Reversing the direction:

```java
System.out.println(b.relativize(a));
```

Output:

```text
../..
```

This moves from `file.txt`'s path position back to `/home/user`.

### Absolute and Relative Mismatch

```java
Path absolute = Path.of("/home/user");

Path relative = Path.of("docs");

absolute.relativize(relative);
```

Throws:

```text
IllegalArgumentException
```

The paths must be compatible.

### Memory

```text
resolve()
→ COMBINE

resolveSibling()
→ REPLACE USING PARENT

relativize()
→ FIND RELATIVE ROUTE
```

---

## Normalizing and Real Paths

### normalize()

Removes redundant path elements lexically.

```java
Path path = Path.of("/home/user/../docs/./file.txt");

System.out.println(path.normalize());
```

Output:

```text
/home/docs/file.txt
```

`normalize()` does not access the filesystem.

The file does not need to exist.

### toRealPath()

```java
Path real = path.toRealPath();
```

By default, this:

- Produces an absolute path.
- Resolves redundant elements.
- Resolves symbolic links.
- Requires the referenced file to exist.

It can throw `IOException`.

### Comparison

| Method | Requires Existing File? | Accesses Filesystem? |
|---|---|---|
| `normalize()` | No | No |
| `toAbsolutePath()` | No | No |
| `toRealPath()` | Yes, normally | Yes |

`toRealPath(LinkOption.NOFOLLOW_LINKS)` changes symbolic-link handling, but does not generally remove the requirement that the path exists.

---

## Creating Files and Directories

### createFile()

```java
Files.createFile(Path.of("data.txt"));
```

Creates a new file.

If it already exists:

```text
FileAlreadyExistsException
```

### createDirectory()

```java
Files.createDirectory(Path.of("reports"));
```

Creates one directory.

Its parent directory must already exist.

### createDirectories()

```java
Files.createDirectories(
        Path.of("reports/2026/october")
);
```

Creates missing parent directories as needed.

If the directory already exists, the operation can complete successfully.

### Comparison

| Method | Behaviour |
|---|---|
| `createFile()` | Creates one new file |
| `createDirectory()` | Creates one directory |
| `createDirectories()` | Creates missing directory hierarchy |

All three return `Path`.

---

## Copying Files

### Files.copy()

```java
Path source = Path.of("source.txt");
Path target = Path.of("target.txt");

Files.copy(source, target);
```

Copies the source to the target.

**The original source remains.**

### Default Behaviour

If the target already exists, copying normally fails with:

```text
FileAlreadyExistsException
```

### REPLACE_EXISTING

```java
Files.copy(
        source,
        target,
        StandardCopyOption.REPLACE_EXISTING
);
```

Replaces an existing target file.

### COPY_ATTRIBUTES

```java
Files.copy(
        source,
        target,
        StandardCopyOption.COPY_ATTRIBUTES
);
```

Attempts to copy file attributes.

### Directory Copying

```java
Files.copy(
        Path.of("sourceDir"),
        Path.of("targetDir")
);
```

Copying a directory does **not** recursively copy all its contents.

It creates the target directory entry, subject to the operation's rules.

### Copy Return Type

```java
Path result = Files.copy(source, target);
```

Returns the target `Path`.

---

## Moving Files

### Files.move()

```java
Path source = Path.of("source.txt");
Path target = Path.of("target.txt");

Files.move(source, target);
```

Moves or renames the source.

After a successful move:

```text
source.txt → NO LONGER EXISTS
target.txt → EXISTS
```

This distinction is central to PE1 Q34.

### REPLACE_EXISTING

```java
Files.move(
        source,
        target,
        StandardCopyOption.REPLACE_EXISTING
);
```

Replaces an existing target when supported.

Without this option, an existing target commonly causes `FileAlreadyExistsException`.

### ATOMIC_MOVE

```java
Files.move(
        source,
        target,
        StandardCopyOption.ATOMIC_MOVE
);
```

Requests an atomic filesystem move.

If unsupported, it may throw:

```text
AtomicMoveNotSupportedException
```

When `ATOMIC_MOVE` is used, the behaviour with an existing target is implementation-specific.

### Move Return Type

```java
Path result = Files.move(source, target);
```

Returns the target `Path`.

### Copy vs Move

| Operation | Source After Success | Target After Success |
|---|---|---|
| `Files.copy()` | Remains | Exists |
| `Files.move()` | Removed from original location | Exists |

---

## Deleting Files

### Files.delete()

```java
Files.delete(Path.of("data.txt"));
```

Deletes a file or empty directory.

If the path does not exist:

```text
NoSuchFileException
```

If a directory is not empty:

```text
DirectoryNotEmptyException
```

Return type:

```java
void
```

### Files.deleteIfExists()

```java
boolean deleted =
        Files.deleteIfExists(Path.of("data.txt"));
```

Returns:

```text
true
→ a file was deleted

false
→ nothing existed at that path
```

A non-empty directory can still cause `DirectoryNotEmptyException`.

### Comparison

| Method | Missing Path | Return |
|---|---|---|
| `Files.delete(path)` | Throws `NoSuchFileException` | `void` |
| `Files.deleteIfExists(path)` | Returns `false` | `boolean` |

---

## Copy and Move Options

Important options come from `StandardCopyOption`.

```java
StandardCopyOption.REPLACE_EXISTING
StandardCopyOption.COPY_ATTRIBUTES
StandardCopyOption.ATOMIC_MOVE
```

| Option | Used With | Meaning |
|---|---|---|
| `REPLACE_EXISTING` | Copy, move | Replace existing target |
| `COPY_ATTRIBUTES` | Copy | Copy file attributes |
| `ATOMIC_MOVE` | Move | Request atomic operation |

`COPY_ATTRIBUTES` is not a valid option for `Files.move()`.

### Open Options Are Different

File writing uses options from `StandardOpenOption`.

```java
StandardOpenOption.CREATE
StandardOpenOption.CREATE_NEW
StandardOpenOption.APPEND
StandardOpenOption.TRUNCATE_EXISTING
StandardOpenOption.WRITE
```

For example:

```java
Files.writeString(
        Path.of("log.txt"),
        "Hello",
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
);
```

### Common Open Options

| Option | Meaning |
|---|---|
| `CREATE` | Create if missing |
| `CREATE_NEW` | Create only if absent; fail if present |
| `APPEND` | Append to end |
| `TRUNCATE_EXISTING` | Clear existing contents when opened for writing |
| `WRITE` | Open for writing |

Remember:

```text
StandardCopyOption
→ COPY / MOVE

StandardOpenOption
→ OPEN / WRITE
```

---

## File State Tracking

This is the key lesson from PE1 Q34.

Consider:

```java
Path a = Path.of("a.txt");
Path b = Path.of("b.txt");
Path c = Path.of("c.txt");
```

Assume initially:

```text
a.txt → EXISTS
b.txt → DOES NOT EXIST
c.txt → DOES NOT EXIST
```

Now execute:

```java
Files.copy(a, b);
Files.move(b, c);
Files.delete(b);
```

Track the state **after each operation**.

| Step | `a.txt` | `b.txt` | `c.txt` |
|---|---|---|---|
| Initial | Exists | Missing | Missing |
| `copy(a, b)` | Exists | Exists | Missing |
| `move(b, c)` | Exists | Missing | Exists |
| `delete(b)` | Exists | Missing | Exists |

The final operation throws:

```text
NoSuchFileException
```

Because `b.txt` was moved to `c.txt` in the previous step.

### With deleteIfExists()

Change the last operation:

```java
Files.deleteIfExists(b);
```

Now it returns:

```text
false
```

No exception is thrown merely because `b.txt` is missing.

### Another Example

Initial state:

```text
a.txt → EXISTS
b.txt → EXISTS
```

Operations:

```java
Files.move(
        a,
        b,
        StandardCopyOption.REPLACE_EXISTING
);

Files.delete(a);
```

After the move:

```text
a.txt → MISSING
b.txt → EXISTS (replacement contents)
```

The subsequent `Files.delete(a)` throws `NoSuchFileException`.

### Exam Technique

For multi-operation questions:

1. Write down the initial files.
2. Update the state after every successful operation.
3. Check whether the next source or target still exists.
4. Stop at the first exception unless it is handled.

Do not continue reasoning as though later statements execute after an uncaught exception.

---

## Checking Files and Paths

The `Files` class provides several useful methods.

### exists()

```java
Files.exists(path);
```

Returns whether the file exists according to the specified link-handling rules.

### notExists()

```java
Files.notExists(path);
```

Checks whether the file is known not to exist.

Important: `exists()` and `notExists()` are **not always exact opposites**.

If the filesystem status cannot be determined, both can return `false`.

### isDirectory()

```java
Files.isDirectory(path);
```

Returns whether the path identifies a directory.

### isRegularFile()

```java
Files.isRegularFile(path);
```

Returns whether the path identifies a regular file.

### isReadable(), isWritable(), isExecutable()

```java
Files.isReadable(path);
Files.isWritable(path);
Files.isExecutable(path);
```

Return booleans based on filesystem checks.

### size()

```java
long size = Files.size(path);
```

Returns the file size in bytes.

Can throw `IOException`.

### isSameFile()

```java
Files.isSameFile(path1, path2);
```

Checks whether two paths locate the same file.

Two different path strings may identify the same underlying file.

---

## Quick Reference

### Path Methods

```java
path.getFileName();     // Path
path.getParent();       // Path or null
path.getRoot();         // Path or null
path.getNameCount();    // int
path.getName(0);        // Path
path.subpath(1, 3);     // Path
```

### Path Transformations

```java
path.resolve(other);
path.resolveSibling(other);
path.relativize(other);

path.normalize();
path.toAbsolutePath();
path.toRealPath();
```

### Creation

```java
Files.createFile(path);
Files.createDirectory(path);
Files.createDirectories(path);
```

### Copy, Move and Delete

```java
Files.copy(source, target);   // Path
Files.move(source, target);   // Path

Files.delete(path);           // void
Files.deleteIfExists(path);   // boolean
```

### Options

```java
StandardCopyOption.REPLACE_EXISTING;
StandardCopyOption.COPY_ATTRIBUTES;
StandardCopyOption.ATOMIC_MOVE;

StandardOpenOption.CREATE;
StandardOpenOption.CREATE_NEW;
StandardOpenOption.APPEND;
StandardOpenOption.TRUNCATE_EXISTING;
```

### File Checks

```java
Files.exists(path);
Files.notExists(path);
Files.isDirectory(path);
Files.isRegularFile(path);
Files.isReadable(path);
Files.isWritable(path);
Files.isExecutable(path);
Files.size(path);
Files.isSameFile(a, b);
```

---

## Final Memory Kicks

```text
PATH
→ REPRESENTS LOCATION
→ DOES NOT CREATE FILE
```

```text
resolve()
→ COMBINE

relativize()
→ RELATIVE ROUTE

normalize()
→ LEXICAL CLEANUP

toRealPath()
→ FILESYSTEM CHECK
```

```text
COPY
→ SOURCE REMAINS

MOVE
→ SOURCE DISAPPEARS

DELETE
→ SOURCE REMOVED
```

```text
delete()
→ VOID
→ MISSING FILE THROWS

deleteIfExists()
→ BOOLEAN
→ MISSING FILE RETURNS FALSE
```

```text
REPLACE_EXISTING
→ ALLOWS REPLACEMENT

ATOMIC_MOVE
→ REQUESTS ATOMIC MOVE

COPY_ATTRIBUTES
→ COPIES ATTRIBUTES
```

```text
createDirectory()
→ PARENT MUST EXIST

createDirectories()
→ CREATES MISSING PARENTS
```

```text
PE1 Q34:

AFTER EACH OPERATION
→ UPDATE FILESYSTEM STATE

MOVE(source, target)
→ SOURCE NO LONGER EXISTS

NEXT OPERATION ON SOURCE
→ MAY FAIL
```