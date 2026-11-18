package rysharp.base.chapter._14_Input_Output.code;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

public class FilesExample {

    public static void main(String[] args) throws IOException {

        Path directory = Files.createTempDirectory("files-example-");

        Path source = directory.resolve("source.txt");
        Path copy = directory.resolve("copy.txt");
        Path moved = directory.resolve("moved.txt");

        try {
            // 1. Create and write a file
            Files.writeString(source, "Java\nPython\nKotlin");

            // 2. Read the entire file as a String
            String content = Files.readString(source);
            System.out.println("readString: " + content);

            // 3. Read all lines into a List<String>
            List<String> lines = Files.readAllLines(source);
            System.out.println("readAllLines: " + lines);

            // 4. Stream lines lazily
            try (Stream<String> stream = Files.lines(source)) {
                System.out.println("lines count: " + stream.count());
            }

            // 5. Read and write raw bytes
            byte[] bytes = Files.readAllBytes(source);
            System.out.println("readAllBytes length: " + bytes.length);

            Path bytesFile = directory.resolve("bytes.txt");
            Files.write(bytesFile, bytes);

            // 6. Write a collection of lines
            Path listFile = directory.resolve("list.txt");
            Files.write(listFile, List.of("One", "Two", "Three"));

            // 7. Copy a file
            Files.copy(source, copy);
            System.out.println("source exists: " + Files.exists(source));// true
            System.out.println("copy exists: " + Files.exists(copy));// true

            // 8. Move / rename a file
            Files.move(copy, moved);
            System.out.println("copy exists: " + Files.exists(copy));// false
            System.out.println("moved exists: " + Files.exists(moved));// true

            // 9. Inspect paths and attributes
            System.out.println("exists: " + Files.exists(source));// true
            System.out.println("isRegularFile: " + Files.isRegularFile(source));// true
            System.out.println("isDirectory: " + Files.isDirectory(directory));// true
            System.out.println("size: " + Files.size(source));// 18

            // 10. List direct directory entries
            try (Stream<Path> entries = Files.list(directory)) {
                System.out.println("entry count: " + entries.count());// 4
            }

            // 11. Walk recursively through a directory
            try (Stream<Path> paths = Files.walk(directory)) {
                System.out.println("walk count: " + paths.count());// 5
            }

            // 12. Find files recursively
            try (Stream<Path> matches = Files.find(
                    directory,
                    2,
                    (path, attributes) -> path.toString().endsWith(".txt"))) {

                System.out.println("txt count: " + matches.count());// 4
            }

            // 13. Compare file content
            System.out.println("mismatch: " + Files.mismatch(source, moved));// -1
            // -1 means identical

            // 14. Delete a file
            Files.delete(moved);
            System.out.println("after delete: " + Files.exists(moved));// false


            // 15. Files.copy() overloads - PE4 Q50
            System.out.println("\n15. Files.copy() overloads");

            // Path -> Path
            // Supports CopyOption arguments such as REPLACE_EXISTING.
            Path replacement = directory.resolve("replacement.txt");

            Files.writeString(replacement, "Old content");

            Files.copy(source, replacement, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(Files.readString(replacement));
            // Java
            // Python
            // Kotlin

            // Path -> OutputStream
            // Copies file contents into an already-open output stream.
            Path outputFile = directory.resolve("output.txt");

            try (OutputStream out = Files.newOutputStream(outputFile)) {
                Files.copy(source, out);
            }

            System.out.println(Files.readString(outputFile));
            // Java
            // Python
            // Kotlin

            // This does NOT compile:
            // Files.copy(source, out, StandardCopyOption.REPLACE_EXISTING);
            //
            // There is no Files.copy(Path, OutputStream, CopyOption...)
            // overload. The output stream is already open.

            // InputStream -> Path
            // This overload DOES support CopyOption arguments.
            Path inputCopy = directory.resolve("input-copy.txt");

            try (InputStream in = Files.newInputStream(source)) {
                Files.copy(in, inputCopy, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println(Files.mismatch(source, inputCopy)); // -1

            // Exam reminder:
            //
            // Files.copy(Path, Path, CopyOption...)
            // Files.copy(Path, OutputStream)
            // Files.copy(InputStream, Path, CopyOption...)
            //
            // Only the overload accepting an OutputStream has no
            // CopyOption parameter.


            // 16. Path.relativize() versus resolve() - PE4 Q24
            System.out.println("\n16. relativize() versus resolve()");

            Path from = Path.of("a/b");
            Path to = Path.of("a/c");

            // relativize() calculates how to navigate FROM one
            // path TO another.
            Path relative = from.relativize(to);

            System.out.println(relative); // ../c

            // Starting at a/b:
            // ..  -> move up to a
            // c   -> move down into c

            // resolve() appends a relative path to a base path.
            Path resolved = from.resolve(relative);

            System.out.println(resolved);             // a/b/../c
            System.out.println(resolved.normalize()); // a/c

            // resolve() does not automatically normalize the path.

            // More examples:
            System.out.println(
                    Path.of("a/b/c").relativize(Path.of("a/b/d"))
            ); // ../d

            System.out.println(
                    Path.of("a/b").relativize(Path.of("a/b/c"))
            ); // c

            System.out.println(
                    Path.of("a/b/c").relativize(Path.of("a/b"))
            ); // ..

            // Exam reminders:
            //
            // relativize() -> calculates a relative route.
            // resolve()    -> combines paths.
            // normalize()  -> removes redundant "." and "..".
            //
            // A relative path and an absolute path cannot be relativized against each other.
//            System.out.println(
//                    Path.of("/a/b/c").relativize(Path.of("a/b"))
//            ); // IllegalArgumentException: 'other' is different type of Path

            // 17. Files.copy() does not always replace existing files
            System.out.println("\n17. Copying and existing targets");

            Path existing = directory.resolve("existing.txt");
            Files.writeString(existing, "Original");

            // Without REPLACE_EXISTING, copying to an existing
            // target Path throws FileAlreadyExistsException.
            try {
                Files.copy(source, existing);
            } catch (java.nio.file.FileAlreadyExistsException e) {
                System.out.println("Target already exists");
            }

            System.out.println(Files.readString(existing)); // Original

            // With REPLACE_EXISTING, the target is replaced.
            Files.copy(source, existing, StandardCopyOption.REPLACE_EXISTING);

            System.out.println(Files.mismatch(source, existing)); // -1

            // Contrast with Files.newOutputStream():
            //
            // By default, opening an OutputStream using
            // Files.newOutputStream(path) creates the file if
            // absent or truncates an existing regular file.
            //
            // This behaviour is controlled when opening the stream,
            // not by passing REPLACE_EXISTING to Files.copy().

        } finally {
            // Clean up the temporary directory and its contents
            try (Stream<Path> paths = Files.walk(directory)) {
                for (Path path : paths.sorted(
                        java.util.Comparator.reverseOrder()).toList()) {
                    Files.deleteIfExists(path);
                }
            }
        }
    }
}
