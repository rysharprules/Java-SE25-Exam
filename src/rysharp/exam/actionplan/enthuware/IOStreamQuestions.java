package rysharp.exam.actionplan.enthuware;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOStreamQuestions {

    public static void main(String[] args) throws Exception {

        Path file = Files.createTempFile("io-exam-", ".txt");

        try {
            // Q1 - Writer -> BufferedWriter
            try (Writer writer = new FileWriter(file.toFile());
                 BufferedWriter bw = new BufferedWriter(writer)) {

                bw.write("Hello");
                bw.newLine(); // BufferedWriter-specific
                bw.write("World");
            }

            // Q2 - Reader -> BufferedReader
            try (Reader reader = new FileReader(file.toFile());
                 BufferedReader br = new BufferedReader(reader)) {

                System.out.println("Q2");
                System.out.println(br.readLine());
                System.out.println(br.readLine());

                // reader.readLine(); // DOES NOT COMPILE
                // reader.read();     // Valid
            }

            // Q3 - InputStream -> InputStreamReader -> BufferedReader
            try (InputStream is = new FileInputStream(file.toFile());
                 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader br = new BufferedReader(reader)) {

                System.out.println("\nQ3");
                System.out.println(br.readLine());
            }

            // Q4 - OutputStream -> PrintWriter
            var bytes = new ByteArrayOutputStream();

            try (PrintWriter pw = new PrintWriter(
                    bytes, true, StandardCharsets.UTF_8)) {

                pw.println("Hello");
                pw.printf("Number: %d%n", 42);
            }

            System.out.println("\nQ4");
            System.out.print(bytes.toString(StandardCharsets.UTF_8));

            // Q5 - BufferedReader accepts any Reader
            try (BufferedReader br = new BufferedReader(
                    new StringReader("One\nTwo"))) {

                System.out.println("\nQ5");
                br.lines().forEach(System.out::println);
            }

            // Q6 - BufferedInputStream accepts InputStream
            try (InputStream is = new ByteArrayInputStream(
                    "ABC".getBytes(StandardCharsets.UTF_8));
                 BufferedInputStream bis = new BufferedInputStream(is)) {

                System.out.println("\nQ6");
                System.out.println((char) bis.read());

                bis.mark(10);
                System.out.println((char) bis.read());

                bis.reset();
                System.out.println((char) bis.read());
            }

        } finally {
            Files.deleteIfExists(file);
        }
    }
}