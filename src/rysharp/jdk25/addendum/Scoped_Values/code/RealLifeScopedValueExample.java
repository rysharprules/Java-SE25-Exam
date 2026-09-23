package rysharp.jdk25.addendum.Scoped_Values.code;

import java.lang.ScopedValue;

/**
 * A more real-life example demonstrating how ScopedValues can be used to pass
 * context (like a Request ID) down a call chain without explicitly passing 
 * it as a method parameter.
 */
public class RealLifeScopedValueExample {
    // The ScopedValue holding the context
    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    public static void main(String[] args) {
        System.out.println("Starting request handling...");
        
        // Establish the scope with a binding for the Request ID
        handleRequest("abc-123");
        
        System.out.println("Request handling finished.");
    }

    // This method establishes the scope for the request
    public static void handleRequest(String requestId) {
        ScopedValue.where(REQUEST_ID, requestId)
                   .run(RealLifeScopedValueExample::authenticate);
    }

    // These methods do NOT need to receive requestId as a parameter
    private static void authenticate() {
        System.out.println("Authenticating request: " + REQUEST_ID.get());
        loadAccount();
    }

    private static void loadAccount() {
        System.out.println("Loading account for: " + REQUEST_ID.get());
        databaseCall();
    }

    private static void databaseCall() {
        // Here we access the context directly
        System.out.println("Executing database query for Request ID: " + REQUEST_ID.get());
    }
}
