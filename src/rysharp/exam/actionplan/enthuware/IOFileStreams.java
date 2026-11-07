
package rysharp.exam.actionplan.enthuware;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOFileStreams {

    void main() throws IOException {
        Path dir = Files.createTempDirectory("io-streams-");

        System.out.println("Test directory: " + dir);

        q1(dir);
        q2(dir);
        q3(dir);
        q4(dir);
        q5(dir);
    }

    static void q1(Path dir) throws IOException {
        System.out.println("\nQ1 - FileOutputStream truncation");

        Path file = dir.resolve("output.txt");
        System.out.println(file);

        Files.writeString(file, "ABCDEFG");
        System.out.println(Files.readString(file)); // ABCDEFG

        // Default constructor: overwrite/truncate existing file.
        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            out.write('X');
        }

        System.out.println(Files.readString(file)); // X
    }

    static void q2(Path dir) throws IOException {
        System.out.println("\nQ2 - FileOutputStream append mode");

        Path file = dir.resolve("append.txt");
        System.out.println(file.getFileName());

        Files.writeString(file, "ABC");

        // true means append; false means overwrite.
        try (FileOutputStream out =
                     new FileOutputStream(file.toFile(), true)) {
            out.write('D');
            out.write('E');
        }

        System.out.println(Files.readString(file)); // ABCDE

        // new FileOutputStream(file, false) would truncate. (false is default)
    }

    static void q3(Path dir) throws IOException {
        System.out.println("\nQ3 - Checked IOException");

        Path source = dir.resolve("source.txt");
        Path target = dir.resolve("target.txt");

        Files.writeString(source, "HELLO");

        // Both read() and write() can throw IOException.
        // FileNotFoundException alone is insufficient.
        try (FileInputStream in = new FileInputStream(source.toFile());
             FileOutputStream out = new FileOutputStream(target.toFile())) {

            int value;
            while ((value = in.read()) != -1) {
                out.write(value);
            }
        }

        System.out.println(Files.readString(target)); // HELLO

        // DOES NOT COMPILE without declaring/catching IOException:
        //
        // try {
        //     FileInputStream in = new FileInputStream("a.txt");
        //     FileOutputStream out = new FileOutputStream("b.txt");
        //     out.write(in.read());
        // } catch (FileNotFoundException e) {
        //     System.out.println("Missing file");
        // }
        //
        // FileNotFoundException extends IOException,
        // but IOException includes other checked failures.
    }

    static void q4(Path dir) throws IOException {
        System.out.println("\nQ4 - try-with-resources closing order");

        Path file = dir.resolve("close.txt");

        try (FileOutputStream out = new FileOutputStream(file.toString());
             BufferedOutputStream buffered = new BufferedOutputStream(out)) {

            buffered.write('A');
            buffered.write('B');

            System.out.println("Inside try block");
        }

        // Resources close in reverse declaration order:
        // buffered first, then out.
        // Closing buffered flushes its remaining bytes.

        System.out.println(Files.readString(file)); // AB
    }

    static void q5(Path dir) throws IOException {
        System.out.println("\nQ5 - read() and write(int)");

        Path file = dir.resolve("bytes.txt");

        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            out.write(65);
            out.write(66);
            out.write(67);
        }

        try (FileInputStream in = new FileInputStream(file.toFile())) {
            int value;

            while ((value = in.read()) != -1) {
                System.out.println(
                        value + " -> " + (char) value);
            }
        }

        // 65 -> A
        // 66 -> B
        // 67 -> C

        // InputStream.read() returns int (0..255), or -1 at EOF.
        // OutputStream.write(int) writes the low 8 bits.
    }
}
