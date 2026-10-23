import java.io.*;
import java.nio.charset.StandardCharsets;
public class BufferedRead {
    public static void main(String[] args) throws Exception {
        byte[] buffer = new byte[4];
        try (InputStream in = new ByteArrayInputStream("racecar".getBytes(StandardCharsets.UTF_8));
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int count;
            while ((count = in.read(buffer)) != -1) {
                System.out.println("read " + count + " bytes");
                out.write(buffer, 0, count);
            }
            System.out.println(out.toString(StandardCharsets.UTF_8)); // racecar
        }
    }
}
