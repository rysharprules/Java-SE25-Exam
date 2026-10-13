package rysharp.base.chapter._14_Input_Output.code;

import java.io.IOException;
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
