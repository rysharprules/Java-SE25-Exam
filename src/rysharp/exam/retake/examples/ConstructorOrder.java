public class ConstructorOrder {
    static class Parent {
        static { System.out.println("Parent static"); }
        { System.out.println("Parent instance"); }
        Parent() { System.out.println("Parent constructor"); }
    }
    static class Child extends Parent {
        static { System.out.println("Child static"); }
        { System.out.println("Child instance"); }
        Child() { this(1); System.out.println("Child no-arg body"); }
        Child(int x) { super(); System.out.println("Child int body"); }
    }
    public static void main(String[] args) { new Child(); new Child(); }
}
