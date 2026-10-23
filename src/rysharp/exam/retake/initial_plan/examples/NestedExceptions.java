public class NestedExceptions {
    static class Custom extends RuntimeException {
        Custom(String message) { super(message); }
    }
    public static void main(String[] args) {
        try {
            try { throw new Custom("First"); }
            catch (Exception e) { throw new Custom("Second"); }
        } catch (Custom e) { System.out.println(e.getMessage()); } // Second
    }
}
