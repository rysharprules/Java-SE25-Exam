package rysharp.base.chapter._11_Exceptions_and_Localisation.code;

public class FinallyExample {

    public static void main(String[] args) {
        example1_finallyAfterReturn();
        example2_finallyOverridesReturn();
        example3_finallySuppressesException();
        example4_finallyThrowsException();
        example5_checkedExceptionSuppressed();
        example6_checkedExceptionPropagates();
    }

    private static void example1_finallyAfterReturn() {
        System.out.println("1. " + calculate1());
        // Finally
        // 1. 10
    }

    private static int calculate1() {
        try {
            return 10;
        } finally {
            System.out.println("Finally");
        }
    }

    private static void example2_finallyOverridesReturn() {
        System.out.println("2. " + calculate2());
        // 2. 20
    }

    private static int calculate2() {
        try {
            return 10;
        } finally {
            return 20;
        }
    }

    private static void example3_finallySuppressesException() {
        System.out.println("3. " + calculate3());
        // 3. 42
    }

    private static int calculate3() {
        try {
            throw new IllegalStateException("Try exception");
        } finally {
            return 42;
        }
    }

    private static void example4_finallyThrowsException() {
        try {
            calculate4();
        } catch (Exception e) {
            System.out.println("4. " + e.getMessage());
            // 4. Finally exception
        }
    }

    private static void calculate4() {
        try {
            throw new IllegalArgumentException("Try exception");
        } finally {
            throw new IllegalStateException("Finally exception");
        }
    }


    private static void example5_checkedExceptionSuppressed() {
        System.out.println("5. " + calculate5());
        // 5. 42
    }

    private static int calculate5() {
        try {
            throw new java.io.IOException("Checked exception");
        } finally {
            return 42;
        }
        // No throws declaration required: finally always returns.
    }

    private static void example6_checkedExceptionPropagates() {
        try {
            calculate6();
        } catch (java.io.IOException e) {
            System.out.println("6. " + e.getMessage());
            // 6. Checked exception
        }
    }

    private static int calculate6() throws java.io.IOException {
        try {
            throw new java.io.IOException("Checked exception");
        } finally {
            System.out.println("Finally executed");
            // No return here, so the IOException propagates.
        }
    }

}
