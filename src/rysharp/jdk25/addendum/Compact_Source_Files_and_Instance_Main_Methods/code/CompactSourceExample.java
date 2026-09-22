// This is a Compact Source File.
// NOTE: It does not have a package declaration, as required for compact source files.

void main() {
    System.out.println("--- Compact Source File & Instance Main Method Example ---");
    System.out.println("Hello from instance main method!");
    
    // Call helper method
    helper();
    
    // Create another object to show instance methods
    new Greeter().greet();
}

void main(String[] args) {
    System.out.println("Main method with args: " + java.util.Arrays.toString(args));
    // When both exist, main(String[] args) takes precedence if invoked
}

void helper() {
    System.out.println("Helper method called.");
}

class Greeter {
    void greet() {
        System.out.println("Greeter: Hello!");
    }
}

// --- Examples that DON'T compile (commented out) ---
/*
// ❌ Compact source files cannot have package declarations
package rysharp.jdk25.addendum.Compact_Source_Files_and_Instance_Main_Methods.code;

// ❌ Explicit class declaration is not allowed in a compact source file
public class NotACompactSourceFile {
    public static void main(String[] args) {
        System.out.println("This is a traditional class.");
    }
}
*/
