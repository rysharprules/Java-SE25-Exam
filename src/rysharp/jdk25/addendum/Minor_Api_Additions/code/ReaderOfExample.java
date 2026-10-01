package rysharp.jdk25.addendum.Minor_Api_Additions.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ReaderOfExample {

    public static void main(String[] args) throws IOException {

        /*
         * Java 24 added Reader.of(CharSequence).
         *
         * It creates a Reader over characters that already exist
         * in memory.
         */
        Reader reader = Reader.of("ABC");

        /*
         * Reader.read() returns an int.
         *
         * -1 indicates that the end of the input has been reached.
         */
        System.out.println((char) reader.read()); // A
        System.out.println((char) reader.read()); // B
        System.out.println((char) reader.read()); // C
        System.out.println(reader.read());        // -1

        /*
         * The returned Reader supports mark() and reset().
         */
        Reader markedReader = Reader.of("JAVA");

        System.out.println(markedReader.markSupported()); // true

        System.out.println((char) markedReader.read()); // J

        markedReader.mark(10);

        System.out.println((char) markedReader.read()); // A
        System.out.println((char) markedReader.read()); // V

        markedReader.reset();

        // We return to the marked position, before A.
        System.out.println((char) markedReader.read()); // A

        /*
         * Reader.of() accepts CharSequence, not just String.
         */
        StringBuilder builder = new StringBuilder("Hello");

        Reader builderReader = Reader.of(builder);

        System.out.println((char) builderReader.read()); // H

        /*
         * Reader.of() is NOT a general factory for Reader subclasses.
         *
         * This does NOT compile:
         *
         * BufferedReader br = Reader.of("Hello");
         *
         * Reader.of() returns Reader.
         *
         * If BufferedReader is required, wrap the Reader normally.
         */
        BufferedReader bufferedReader =
                new BufferedReader(Reader.of("Hello"));

        System.out.println(bufferedReader.readLine()); // Hello

        /*
         * Reader.of(null) compiles but throws NullPointerException
         * at runtime.
         */
    }
}