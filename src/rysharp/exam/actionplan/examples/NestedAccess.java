package rysharp.exam.actionplan.examples;

// Q11 POST_EXAM_REPORT.md
public class NestedAccess {
    private int outerPrivate = 10;
    public int outerPublic = 20;
    class Inner {
        private int innerPrivate = 30;
        private int outerPrivate = 50; // would hide outerPrivate of super and print (50 20) in show()
        public int innerPublic = 40;
        // can call explicitly with // NestedAccess.this.outerPrivate + " " + NestedAccess.this.outerPublic
        void show() { System.out.println(outerPrivate + " " + outerPublic); }
    }
    public static void main(String[] args) {
        NestedAccess outer = new NestedAccess();
        NestedAccess.Inner inner = outer.new Inner();
        System.out.println(inner.innerPrivate); // allowed inside enclosing class
        inner.show();                           // accesses outer's private field
    }
}
