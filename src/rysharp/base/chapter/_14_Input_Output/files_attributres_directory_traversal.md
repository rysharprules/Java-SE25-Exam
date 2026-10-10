# File Attributes & Directory Traversal

[🔙 Back](README.md)

A focused Java NIO.2 reference covering file metadata, directory traversal, streams of paths, file visitors, symbolic links and important API distinctions.

## Contents

- [File Attributes](#file-attributes)
- [BasicFileAttributes](#basicfileattributes)
- [Reading and Updating Attributes](#reading-and-updating-attributes)
- [Directory Traversal](#directory-traversal)
- [Files.list()](#fileslist)
- [Files.walk()](#fileswalk)
- [Files.find()](#filesfind)
- [Files.walkFileTree()](#fileswalkfiletree)
- [FileVisitor](#filevisitor)
- [Symbolic Links](#symbolic-links)
- [Method Comparison](#method-comparison)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## File Attributes

File attributes describe metadata about a file or directory.

Examples include:

- Size
- Creation time
- Last modified time
- Last access time
- Whether the path identifies a regular file, directory or symbolic link

Java provides two main ways to access this information:

```java
Files.size(path);
```

Or:

```java
BasicFileAttributes attrs =
        Files.readAttributes(path, BasicFileAttributes.class);
```

The first reads a specific property. The second obtains a collection of related attributes.

### Basic Attribute Methods

| Method | Return Type | Purpose |
|---|---|---|
| `Files.size(path)` | `long` | File size in bytes |
| `Files.getLastModifiedTime(path)` | `FileTime` | Last modification |
| `Files.isRegularFile(path)` | `boolean` | Regular file check |
| `Files.isDirectory(path)` | `boolean` | Directory check |
| `Files.isSymbolicLink(path)` | `boolean` | Symbolic link check |
| `Files.isHidden(path)` | `boolean` | Hidden file check |
| `Files.isReadable(path)` | `boolean` | Read permission check |
| `Files.isWritable(path)` | `boolean` | Write permission check |
| `Files.isExecutable(path)` | `boolean` | Execute permission check |

Important distinction: methods such as `isRegularFile()` generally return `false` if the file's status cannot be determined, whereas methods such as `size()` may throw `IOException`.

---

## BasicFileAttributes

`BasicFileAttributes` is an interface in:

```java
java.nio.file.attribute
```

It provides a snapshot of basic file metadata.

### Reading Attributes

```java
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.IOException;

public class Example {

    public static void main(String[] args) throws IOException {

        Path path = Path.of("data.txt");

        BasicFileAttributes attrs =
                Files.readAttributes(
                        path,
                        BasicFileAttributes.class
                );

        System.out.println(attrs.size());
        System.out.println(attrs.isRegularFile());
        System.out.println(attrs.lastModifiedTime());
    }
}
```

The file must exist for this example to succeed.

### Important Methods

| Method | Return Type |
|---|---|
| `size()` | `long` |
| `creationTime()` | `FileTime` |
| `lastModifiedTime()` | `FileTime` |
| `lastAccessTime()` | `FileTime` |
| `isRegularFile()` | `boolean` |
| `isDirectory()` | `boolean` |
| `isSymbolicLink()` | `boolean` |
| `isOther()` | `boolean` |
| `fileKey()` | `Object` |

### FileTime

Time-related attributes use `FileTime`, not `LocalDateTime`.

```java
FileTime time = attrs.lastModifiedTime();

System.out.println(time.toMillis());
```

`toMillis()` returns milliseconds since the Unix epoch.

A `FileTime` can also be converted to an `Instant`:

```java
Instant instant = time.toInstant();
```

### Snapshot Behaviour

`BasicFileAttributes` represents the attributes obtained when they were read.

If the file changes afterward, the existing attributes object is not automatically refreshed.

Read the attributes again to obtain updated values.

---

## Reading and Updating Attributes

### Files.getAttribute()

Reads a named attribute.

```java
Object size = Files.getAttribute(
        Path.of("data.txt"),
        "basic:size"
);
```

The return type is `Object`.

A cast may be required:

```java
long size = (Long) Files.getAttribute(
        Path.of("data.txt"),
        "basic:size"
);
```

### Files.setAttribute()

Updates a named attribute.

```java
Files.setAttribute(
        Path.of("data.txt"),
        "basic:lastModifiedTime",
        FileTime.fromMillis(System.currentTimeMillis())
);
```

Returns the `Path`.

Support for particular attributes and updates depends on the filesystem.

### Files.readAttributes() Overloads

There are two important forms.

**Typed:**

```java
BasicFileAttributes attrs =
        Files.readAttributes(
                path,
                BasicFileAttributes.class
        );
```

Returns an attributes object.

**String-based:**

```java
Map<String, Object> attrs =
        Files.readAttributes(
                path,
                "basic:size,lastModifiedTime"
        );
```

Returns a map.

### Wildcard Attribute Selection

```java
Map<String, Object> attrs =
        Files.readAttributes(path, "basic:*");
```

Requests all attributes in the basic view.

### Attribute Views

Java also provides interfaces for accessing and updating groups of attributes.

```java
BasicFileAttributeView view =
        Files.getFileAttributeView(
                path,
                BasicFileAttributeView.class
        );
```

Read through the view:

```java
BasicFileAttributes attrs = view.readAttributes();
```

Update supported timestamps:

```java
view.setTimes(
        FileTime.fromMillis(System.currentTimeMillis()),
        null,
        null
);
```

The parameters are:

```text
lastModifiedTime
lastAccessTime
createTime
```

Passing `null` means that particular timestamp is not changed.

---

## Directory Traversal

NIO.2 provides several ways to examine directory contents.

The most important methods are:

```java
Files.list(path);
Files.walk(path);
Files.find(path, depth, matcher);
Files.walkFileTree(path, visitor);
```

They are not interchangeable.

| Method | Recursive? | Return Type |
|---|---|---|
| `Files.list()` | No | `Stream<Path>` |
| `Files.walk()` | Yes, up to specified depth | `Stream<Path>` |
| `Files.find()` | Yes, up to specified depth | `Stream<Path>` |
| `Files.walkFileTree()` | Yes | `Path` |

The first three return streams. `walkFileTree()` uses a visitor.

---

## Files.list()

Lists the immediate entries of a directory.

```java
try (Stream<Path> paths =
        Files.list(Path.of("documents"))) {

    paths.forEach(System.out::println);
}
```

Suppose the directory contains:

```text
documents/
├── a.txt
├── b.txt
└── archive/
    └── old.txt
```

`Files.list()` produces paths for:

```text
documents/a.txt
documents/b.txt
documents/archive
```

It does **not** recursively visit `archive/old.txt`.

### Key Properties

- Returns `Stream<Path>`.
- Examines immediate directory entries.
- Does not include the starting directory itself.
- Does not guarantee encounter order.
- Must be closed after use.
- May throw `IOException` when opening the directory.

The stream is lazily populated. I/O failures during traversal may be wrapped in `UncheckedIOException`.

### Filtering

```java
try (Stream<Path> paths =
        Files.list(Path.of("documents"))) {

    paths.filter(Files::isRegularFile)
         .forEach(System.out::println);
}
```

This selects regular files among the immediate entries.

---

## Files.walk()

Recursively traverses a directory tree.

```java
try (Stream<Path> paths =
        Files.walk(Path.of("documents"))) {

    paths.forEach(System.out::println);
}
```

Using the same structure:

```text
documents/
├── a.txt
├── b.txt
└── archive/
    └── old.txt
```

`Files.walk()` includes:

```text
documents
documents/a.txt
documents/b.txt
documents/archive
documents/archive/old.txt
```

The exact ordering among siblings is not guaranteed.

### Important Difference

`Files.walk()` includes the starting path itself.

`Files.list()` does not.

### Maximum Depth

```java
Files.walk(path, 1);
```

Depth meanings:

```text
0 → starting path only

1 → starting path + immediate children

2 → starting path + children + grandchildren
```

A negative depth throws `IllegalArgumentException`.

### Depth-First Traversal

`Files.walk()` traverses depth-first, with parent directories encountered before their descendants.

### Filtering

```java
try (Stream<Path> paths =
        Files.walk(Path.of("documents"))) {

    paths.filter(p -> p.toString().endsWith(".txt"))
         .forEach(System.out::println);
}
```

### Key Properties

- Returns `Stream<Path>`.
- Recursively traverses by default.
- Includes the starting path.
- Supports maximum depth.
- Does not follow symbolic links by default.
- Must be closed after use.

---

## Files.find()

Recursively searches paths using a predicate.

```java
try (Stream<Path> paths =
        Files.find(
                Path.of("documents"),
                5,
                (path, attrs) -> attrs.isRegularFile()
        )) {

    paths.forEach(System.out::println);
}
```

### Method Signature

```java
Stream<Path> find(
        Path start,
        int maxDepth,
        BiPredicate<Path, BasicFileAttributes> matcher,
        FileVisitOption... options
) throws IOException;
```

The matcher receives **two arguments**:

```text
Path
BasicFileAttributes
```

This is a major distinction from `Files.walk()`.

### Example: Find Large Files

```java
try (Stream<Path> paths =
        Files.find(
                Path.of("documents"),
                10,
                (path, attrs) ->
                        attrs.isRegularFile()
                        && attrs.size() > 1_000
        )) {

    paths.forEach(System.out::println);
}
```

This selects regular files larger than 1,000 bytes.

### Files.find() vs Files.walk()

These can achieve similar results:

```java
Files.walk(path)
        .filter(Files::isRegularFile);
```

```java
Files.find(
        path,
        Integer.MAX_VALUE,
        (p, attrs) -> attrs.isRegularFile()
);
```

However, `Files.find()` supplies attributes to the predicate, avoiding the need for a separate attribute lookup for each matching decision.

### Key Properties

- Returns `Stream<Path>`.
- Requires a maximum depth.
- Uses `BiPredicate<Path, BasicFileAttributes>`.
- Includes the starting path if it matches.
- Must be closed after use.

---

## Files.walkFileTree()

Uses the visitor pattern to traverse a directory tree.

```java
Files.walkFileTree(path, visitor);
```

Unlike `list()`, `walk()` and `find()`, this method does not return a stream.

It returns the starting `Path` after successful traversal.

### SimpleFileVisitor

`SimpleFileVisitor<T>` provides default implementations of `FileVisitor` methods.

```java
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class Example {

    public static void main(String[] args) throws IOException {

        Path start = Path.of("documents");

        Files.walkFileTree(
                start,
                new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(
                            Path file,
                            BasicFileAttributes attrs) {

                        System.out.println(file);

                        return FileVisitResult.CONTINUE;
                    }
                }
        );
    }
}
```

This visits files under `documents`.

### Important Overloads

```java
Files.walkFileTree(start, visitor);
```

And:

```java
Files.walkFileTree(
        start,
        EnumSet.noneOf(FileVisitOption.class),
        5,
        visitor
);
```

The second form specifies options and maximum depth.

---

## FileVisitor

`FileVisitor<T>` declares four methods.

| Method | Called When |
|---|---|
| `preVisitDirectory()` | Before visiting a directory's entries |
| `visitFile()` | When visiting a file |
| `visitFileFailed()` | When a file cannot be visited |
| `postVisitDirectory()` | After visiting a directory's entries |

### Method Signatures

```java
FileVisitResult preVisitDirectory(
        T dir,
        BasicFileAttributes attrs
) throws IOException;
```

```java
FileVisitResult visitFile(
        T file,
        BasicFileAttributes attrs
) throws IOException;
```

```java
FileVisitResult visitFileFailed(
        T file,
        IOException exc
) throws IOException;
```

```java
FileVisitResult postVisitDirectory(
        T dir,
        IOException exc
) throws IOException;
```

Notice that `visitFileFailed()` and `postVisitDirectory()` receive an `IOException`, not `BasicFileAttributes`.

### FileVisitResult

The visitor methods return a `FileVisitResult` enum value.

| Value | Meaning |
|---|---|
| `CONTINUE` | Continue traversal |
| `TERMINATE` | Stop traversal |
| `SKIP_SUBTREE` | Skip the current directory's descendants when returned from `preVisitDirectory()` |
| `SKIP_SIBLINGS` | Skip remaining siblings |

### Example: Skip a Directory

```java
@Override
public FileVisitResult preVisitDirectory(
        Path dir,
        BasicFileAttributes attrs) {

    if (dir.getFileName().toString().equals("archive")) {
        return FileVisitResult.SKIP_SUBTREE;
    }

    return FileVisitResult.CONTINUE;
}
```

The `archive` directory's descendants are not visited.

### SimpleFileVisitor Defaults

`SimpleFileVisitor` provides default behaviour so you can override only the methods you need.

By default:

- Successful visits continue.
- A failed visit rethrows the supplied `IOException`.
- A post-directory visit rethrows a non-null exception.

### Important Exam Trap

`SKIP_SUBTREE` has its intended effect when returned from `preVisitDirectory()`.

Returning it from `visitFile()` does not retroactively skip a directory tree.

---

## Symbolic Links

A symbolic link is a filesystem entry that refers to another path.

For example:

```text
shortcut.txt → original.txt
```

### Creating a Symbolic Link

```java
Path link = Path.of("shortcut.txt");
Path target = Path.of("original.txt");

Files.createSymbolicLink(link, target);
```

Support and permissions depend on the operating system and filesystem.

### Checking a Symbolic Link

```java
Files.isSymbolicLink(link);
```

Returns `true` if the path itself identifies a symbolic link.

### Reading the Link Target

```java
Path target = Files.readSymbolicLink(link);
```

Returns the stored target path, which may be relative.

### Following Links

Many `Files` operations follow symbolic links by default.

For example:

```java
Files.isRegularFile(link);
```

Normally checks the target file.

To inspect the link itself:

```java
Files.isRegularFile(
        link,
        LinkOption.NOFOLLOW_LINKS
);
```

For a symbolic link, this normally returns `false`, because the link itself is not a regular file.

### Reading Attributes Without Following Links

```java
BasicFileAttributes attrs =
        Files.readAttributes(
                link,
                BasicFileAttributes.class,
                LinkOption.NOFOLLOW_LINKS
        );

System.out.println(attrs.isSymbolicLink());
```

Output for a symbolic link:

```text
true
```

### Directory Traversal and Links

By default:

```java
Files.walk(path);
```

Does not follow symbolic links during traversal.

To follow them:

```java
Files.walk(
        path,
        FileVisitOption.FOLLOW_LINKS
);
```

Following symbolic links can introduce cycles.

If a cycle is detected, traversal may report a `FileSystemLoopException`.

### NOFOLLOW_LINKS vs FOLLOW_LINKS

| Option | Type | Purpose |
|---|---|---|
| `NOFOLLOW_LINKS` | `LinkOption` | Do not follow symbolic links for supported operations |
| `FOLLOW_LINKS` | `FileVisitOption` | Follow symbolic links during directory traversal |

These are different enums.

---

## Method Comparison

| Method | Return Type | Traversal | Starting Path Included? |
|---|---|---|---|
| `Files.list(path)` | `Stream<Path>` | Immediate children | No |
| `Files.walk(path)` | `Stream<Path>` | Recursive | Yes |
| `Files.walk(path, depth)` | `Stream<Path>` | Depth-limited | Yes |
| `Files.find(path, depth, matcher)` | `Stream<Path>` | Recursive search | If matched |
| `Files.walkFileTree(path, visitor)` | `Path` | Visitor-based recursion | Visited |

### Key Signatures

```java
Stream<Path> list(Path dir) throws IOException;
```

```java
Stream<Path> walk(
        Path start,
        FileVisitOption... options
) throws IOException;
```

```java
Stream<Path> walk(
        Path start,
        int maxDepth,
        FileVisitOption... options
) throws IOException;
```

```java
Stream<Path> find(
        Path start,
        int maxDepth,
        BiPredicate<Path, BasicFileAttributes> matcher,
        FileVisitOption... options
) throws IOException;
```

```java
Path walkFileTree(
        Path start,
        FileVisitor<? super Path> visitor
) throws IOException;
```

### Stream Closure

The streams returned by `Files.list()`, `Files.walk()` and `Files.find()` hold filesystem resources.

Use try-with-resources:

```java
try (Stream<Path> paths = Files.walk(path)) {
    paths.forEach(System.out::println);
}
```

`walkFileTree()` manages traversal internally and does not return a stream requiring closure.

---

## Quick Reference

### BasicFileAttributes

```java
BasicFileAttributes attrs =
        Files.readAttributes(
                path,
                BasicFileAttributes.class
        );
```

```java
attrs.size();               // long
attrs.creationTime();       // FileTime
attrs.lastModifiedTime();   // FileTime
attrs.lastAccessTime();     // FileTime

attrs.isRegularFile();      // boolean
attrs.isDirectory();        // boolean
attrs.isSymbolicLink();     // boolean
attrs.isOther();            // boolean
```

### Attribute APIs

```java
Files.size(path);                       // long
Files.getLastModifiedTime(path);        // FileTime
Files.getAttribute(path, "basic:size"); // Object
Files.readAttributes(path, "basic:*");  // Map<String, Object>
```

### Traversal

```java
Files.list(path);              // Stream<Path>
Files.walk(path);              // Stream<Path>
Files.walk(path, 2);           // Stream<Path>
Files.find(path, 2, matcher);  // Stream<Path>

Files.walkFileTree(path, visitor); // Path
```

### FileVisitResult

```text
CONTINUE
TERMINATE
SKIP_SUBTREE
SKIP_SIBLINGS
```

### Symbolic Links

```java
Files.isSymbolicLink(path);

Files.readSymbolicLink(path);

Files.createSymbolicLink(link, target);

Files.walk(path, FileVisitOption.FOLLOW_LINKS);
```

---

## Final Memory Kicks

```text
FILE ATTRIBUTES:

BasicFileAttributes
→ METADATA SNAPSHOT

size()
→ long

creationTime()
→ FileTime

lastModifiedTime()
→ FileTime
```

```text
readAttributes(path, Class)
→ ATTRIBUTES OBJECT

readAttributes(path, String)
→ MAP

getAttribute()
→ OBJECT
```

```text
TRAVERSAL:

list()
→ ONE LEVEL
→ EXCLUDES START

walk()
→ RECURSIVE
→ INCLUDES START

find()
→ RECURSIVE + BIPREDICATE
→ PATH + ATTRIBUTES

walkFileTree()
→ FILE VISITOR
→ RETURNS PATH
```

```text
STREAMS:

list()
walk()
find()

→ Stream<Path>
→ CLOSE WITH TRY-WITH-RESOURCES
```

```text
FILE VISITOR:

preVisitDirectory()
→ BEFORE DIRECTORY CONTENTS

visitFile()
→ FILE

visitFileFailed()
→ ERROR

postVisitDirectory()
→ AFTER DIRECTORY CONTENTS
```

```text
VISIT RESULTS:

CONTINUE
→ KEEP GOING

TERMINATE
→ STOP

SKIP_SUBTREE
→ SKIP DESCENDANTS

SKIP_SIBLINGS
→ SKIP REMAINING SIBLINGS
```

```text
SYMBOLIC LINKS:

NOFOLLOW_LINKS
→ LinkOption

FOLLOW_LINKS
→ FileVisitOption

walk()
→ DOES NOT FOLLOW LINKS BY DEFAULT
```

```text
EXAM TRAPS:

Files.list()
→ NOT RECURSIVE

Files.walk()
→ INCLUDES STARTING PATH

Files.find()
→ REQUIRES MAX DEPTH + BIPREDICATE

Files.walkFileTree()
→ RETURNS PATH, NOT STREAM

BasicFileAttributes
→ SNAPSHOT, NOT LIVE DATA
```