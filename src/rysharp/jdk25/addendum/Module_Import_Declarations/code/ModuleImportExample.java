package rysharp.jdk25.addendum.Module_Import_Declarations.code;

// Valid: import module java.base;
// Import all exported packages from java.base
import module java.base;

// Valid: import module java.sql;
// Import all exported packages from java.sql (which requires java.base and java.logging)
import module java.sql;

public class ModuleImportExample {
    public static void main(String[] args) {
        // String is available automatically because java.lang is imported by default.
        String message = "Hello";
        System.out.println(message);

        // List is available because it's exported by java.base and imported by `import module java.base;`
        java.util.List<String> list = java.util.List.of("A", "B");
        System.out.println("List: " + list);

        // java.sql.Date is available because of `import module java.sql;`
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        System.out.println("Date: " + date);
    }
}

// --- Examples that DON'T compile (commented out) ---
/*
// ❌ Not allowed: import module cannot be inside a class
class InvalidImport {
    import module java.base;
}

// ❌ Not allowed: module does not exist
import module java.nonexistent;
*/
