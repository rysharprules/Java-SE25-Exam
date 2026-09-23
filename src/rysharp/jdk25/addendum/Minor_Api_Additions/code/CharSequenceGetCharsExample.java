package rysharp.jdk25.addendum.Minor_Api_Additions.code;

import java.util.Arrays;

public class CharSequenceGetCharsExample {

    public static void main(String[] args) {

        /*
         * Java 25 adds getChars() to the CharSequence interface.
         *
         * String already had a getChars() method, but it can now be
         * called directly through a CharSequence reference.
         */
        CharSequence sequence = "ORACLE";

        char[] result = {'A', 'B', 'C', 'D', 'E', 'F'};

        /*
         * Source range is [1, 4).
         *
         * ORACLE
         * 012345
         *
         * Indexes 1, 2 and 3 = R A C
         *
         * A useful shortcut for half-open ranges:
         *
         *     end - start = number of elements
         *
         *     4 - 1 = 3 characters
         *
         * dstBegin = 2, so RAC overwrites destination indexes
         * 2, 3 and 4.
         */
        sequence.getChars(1, 4, result, 2);

        System.out.println(Arrays.toString(result));
        // [A, B, R, A, C, F]

        /*
         * getChars() modifies the supplied array.
         *
         * It does NOT:
         * - return a new array
         * - insert characters
         * - increase the array size
         *
         * Its return type is void.
         */
    }
}