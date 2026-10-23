public class InterfaceRules {
    sealed interface Action permits Music, Dance {
        default String label() { return "art"; }
        public static String kind() { return "static on interface"; }
        // final void run(); // ILLEGAL: interface method cannot be final
    }
    static final class Music implements Action {}
    static final class Dance implements Action {}
    public static void main(String[] args) {
        System.out.println(new Music().label()); // inherited default method
        System.out.println(Action.kind());       // valid static call
        // Music.kind(); // ILLEGAL: interface static method not inherited
    }
}
