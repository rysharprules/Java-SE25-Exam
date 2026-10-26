package rysharp.exam.retake.examples;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

// Q16 POST_EXAM_REPORT.md
public class FilesLinesReduce {
    public static void main(String[] args) throws Exception {
        Path file = Files.createTempFile("java-review-", ".txt");
        try {
            if (!Files.exists(file)) throw new FileNotFoundException(file.toString());
            Files.writeString(file, "one\ntwo\n");
            try (Stream<String> lines = Files.lines(file)) {
                StringBuilder result = lines.collect(
                        StringBuilder::new, StringBuilder::append, StringBuilder::append);
                System.out.println(result); // onetwo
            }
        } finally { Files.deleteIfExists(file); }
    }
}
