public class NestedAccess {
    private int outerPrivate = 10;
    public int outerPublic = 20;
    class Inner {
        private int innerPrivate = 30;
        public int innerPublic = 40;
        void show() { System.out.println(outerPrivate + outerPublic); }
    }
    public static void main(String[] args) {
        NestedAccess outer = new NestedAccess();
        NestedAccess.Inner inner = outer.new Inner();
        System.out.println(inner.innerPrivate); // allowed inside enclosing class
        inner.show();                           // accesses outer's private field
    }
}
