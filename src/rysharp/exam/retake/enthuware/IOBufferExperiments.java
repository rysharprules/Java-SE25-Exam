package rysharp.exam.retake.enthuware;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOBufferExperiments {

    public static void main(String[] args) throws IOException {
        Path directory = Files.createTempDirectory("io-experiments-");
        System.out.println("Test files: " + directory);

        fileReaderWriter(directory);
        correctedReaderWriter(directory);
        byteStreams(directory);
        bufferedStreams(directory);
        singleCharacterReaderWriter(directory);
    }

    // Experiment 1: ME5 Q8 - the incorrect copying pattern
    /*
    Given that a file named test1.txt contains the following data: 01234567 and the following code snippet:

    char[] buf = new char[5];
    try(FileReader fr = new FileReader("c:\\temp\\test1.txt");
        FileWriter fw = new FileWriter("c:\\temp\\test2.txt")){
            while( fr.read(buf) != -1){
                fw.write(buf);
            }
     }

     What will be the contents of test2.txt after the above code is run?
     */
    static void fileReaderWriter(Path dir) throws IOException {
        System.out.println("--- fileReaderWriter");
        Path source = dir.resolve("input.txt");
        Path target = dir.resolve("incorrect.txt");

        Files.writeString(source, "01234567");

        char[] buffer = new char[5];

        try (FileReader reader = new FileReader(source.toFile());
             FileWriter writer = new FileWriter(target.toFile())) {

            int count;
            while ((count = reader.read(buffer)) != -1) {
                System.out.println("Read " + count + " characters: " + new String(buffer));
                writer.write(buffer); // Writes ALL 5 characters!
            }
        }

        System.out.println("Experiment 1: " + Files.readString(target));
        /*
        change:
        char[] buffer = new char[5];
        to:
        char[] buffer = new char[3];

        Read 3 characters: 012
        Read 3 characters: 345
        Read 2 characters: 675
        Experiment 1: 012345675
         */
    }

    // Experiment 2: Correct copying using the number of characters read
    static void correctedReaderWriter(Path dir) throws IOException {
        System.out.println("--- correctedReaderWriter");
        Path source = dir.resolve("input.txt");
        Path target = dir.resolve("correct.txt");

        char[] buffer = new char[5];

        try (FileReader reader = new FileReader(source.toFile());
             FileWriter writer = new FileWriter(target.toFile())) {

            int count;
            while ((count = reader.read(buffer)) != -1) {
                System.out.println("Read " + count + " characters: " + new String(buffer));
                writer.write(buffer, 0, count);
            }
        }

        System.out.println("Experiment 2: " + Files.readString(target));
    }

    // Experiment 3: The same trap using byte streams
    static void byteStreams(Path dir) throws IOException {
        System.out.println("--- byteStreams");
        Path source = dir.resolve("input.txt");
        Path target = dir.resolve("bytes.txt");

        byte[] buffer = new byte[5];

        try (FileInputStream input = new FileInputStream(source.toFile());
             FileOutputStream output = new FileOutputStream(target.toFile())) {

            int count;
            while ((count = input.read(buffer)) != -1) {
                System.out.println("Read " + count + " bytes");
                output.write(buffer); // Same mistake!
            }
        }

        System.out.println("Experiment 3: " + Files.readString(target));
    }

    // Experiment 4: BufferedReader / BufferedWriter
    static void bufferedStreams(Path dir) throws IOException {
        System.out.println("--- bufferedStreams");
        Path source = dir.resolve("input.txt");
        Path target = dir.resolve("buffered.txt");

        try (BufferedReader reader = Files.newBufferedReader(source);
             BufferedWriter writer = Files.newBufferedWriter(target)) {

            char[] buffer = new char[5];
            int count;

            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
        }

        System.out.println("Experiment 4: " + Files.readString(target));
    }


    // Experiment 5: Read and write one character at a time
    static void singleCharacterReaderWriter(Path dir) throws IOException {
        System.out.println("--- singleCharacterReaderWriter");
        Path source = dir.resolve("input.txt");
        Path target = dir.resolve("single-char.txt");

        try (FileReader fr = new FileReader(source.toFile());
             FileWriter fw = new FileWriter(target.toFile())) {

            int ch;

            while ((ch = fr.read()) != -1) {
                System.out.println("Read character: " + (char) ch + " (numeric value: " + ch + ")");
                fw.write(ch);
            }
        }

        System.out.println("Experiment 5: " + Files.readString(target));
    }

}
