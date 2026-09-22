package rysharp.jdk25.addendum.Flexible_Constructor_Bodies.code;

public class FlexibleConstructorExample {

    // super() example
    static class Parent {
        Parent(String s) {
            System.out.println("Parent constructor: " + s);
        }
    }

    static class Child extends Parent {
        private final String instanceField = "Instance Field";

        Child(String param) {
            // PROLOGUE
            // Allowed:
            System.out.println("Performing allowed work before super():");
            String local = "Local Variable";
            System.out.println("Processing: " + param + ", " + local);
            int length = param.length();
            
            // Calling super(...) marks the end of the prologue
            super(param + " - " + length);
            
            // Epilogue
            System.out.println("Epilogue: " + this.instanceField);
        }
    }

    // this() example
    static class ThisExample {
        private int x;

        ThisExample(int x) {
            this.x = x;
        }

        ThisExample() {
            // Allowed: call another constructor using this(...)
            int y = check(10);
            this(y);
        }

        private static int check(int val) {
            return val * 2;
        }

        // --- Examples that DON'T compile (commented out) ---
        /*
        ThisExample(String s) {
            // ❌ Not allowed: cannot access instance field before this(...)
            System.out.println(this.x); 
            this(1);
        }

        ThisExample(double d) {
            // ❌ Not allowed: cannot access this before this(...)
            this.toString(); 
            this(1);
        }
        */
    }

    record FlexibleRecord(int x, int y) {
        // Constructor with prologue before delegating to canonical constructor
        FlexibleRecord(int x, int y, String name) {
            var shoutyName = name.toUpperCase();
            System.out.println("Record prologue: " + shoutyName);
            this(x, y);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Flexible Constructor Bodies Examples ---");
        new Child("Test");
        new ThisExample();
        new FlexibleRecord(1, 2, "Record Example");
    }
}
