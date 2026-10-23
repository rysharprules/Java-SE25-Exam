import java.io.*;
import java.nio.file.*;
import java.util.stream.*;
public class FilesLinesReduce {
    public static void main(String[] args) throws Exception {
        Path file = Files.createTempFile("java-review-", ".txt");
        try {
            Files.writeString(file, "one\ntwo\n");
            if (!Files.exists(file)) throw new FileNotFoundException(file.toString());
            try (Stream<String> lines = Files.lines(file)) {
                StringBuilder result = lines.collect(StringBuilder::new,
                    StringBuilder::append, StringBuilder::append);
                System.out.println(result); // onetwo
            }
        } finally { Files.deleteIfExists(file); }
    }
}
