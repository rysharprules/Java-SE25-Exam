package rysharp.jdk25.addendum.Minor_Api_Additions.code;

import java.nio.CharBuffer;
import java.util.Arrays;

public class CharBufferGetCharsExample {

    public static void main(String[] args) {

        CharBuffer buffer = CharBuffer.wrap("JAVA25");

        /*
         * CharBuffer has a position, limit and capacity.
         *
         * JAVA25
         * 012345
         *   ^
         *   position = 2
         */
        buffer.position(2);

        char[] result = {'A', 'B', 'C', 'D', 'E'};

        /*
         * CharBuffer implements CharSequence.
         *
         * For getChars(), source indexes are relative to the
         * current position.
         *
         * With position = 2, the CharSequence view is:
         *
         * V A 2 5
         * 0 1 2 3
         *
         * [0, 3) therefore gives V A 2.
         *
         * These are copied into result beginning at index 1.
         */
        buffer.getChars(0, 3, result, 1);

        System.out.println(Arrays.toString(result));
        // [A, V, A, 2, E]

        /*
         * IMPORTANT:
         *
         * getChars() does NOT advance the CharBuffer position.
         */
        System.out.println(buffer.position()); // 2

        /*
         * Contrast that with a relative bulk get().
         *
         * get() consumes characters beginning at the current position
         * and therefore advances the position.
         */
        char[] consumed = new char[2];

        buffer.get(consumed, 0, 2);

        System.out.println(Arrays.toString(consumed)); // [V, A]
        System.out.println(buffer.position());         // 4
    }
}