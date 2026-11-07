package rysharp.exam.actionplan.examples;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

// Q14 POST_EXAM_REPORT.md
public class BufferedRead {
    public static void main(String[] args) throws Exception {
        byte[] buffer = new byte[5];
        try (InputStream in = new ByteArrayInputStream("xxxracecar".getBytes(StandardCharsets.UTF_8));
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            in.skip(3);// skips xxx
            int count;
            while ((count = in.read(buffer, 3, 2)) != -1) {
                System.out.println("Read " + count + " bytes: "
                        + new String(buffer, 3, count, StandardCharsets.UTF_8));
                System.out.println("Buffer (as chars): "
                        + new String(buffer, StandardCharsets.UTF_8));
                out.write(buffer, 3, count);
            }
            System.out.println(out.toString(StandardCharsets.UTF_8)); // racecar
        }
    }
}
