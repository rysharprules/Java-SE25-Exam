
package rysharp.exam.actionplan.enthuware;

public class InterfaceInheritanceRules {

    public static void main(String[] args) {
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();
    }

    static void Q1() {
        // Abstract and default methods are inherited.
        // Static methods are NOT inherited by subinterfaces.
        // Interface fields ARE inherited, despite being static.

        System.out.println(Child.VALUE); // 10
        System.out.println(Child.STEP);  // 20

        Child child = new ChildImpl();
        child.abstractMethod(); // Abstract implemented
        child.defaultMethod();  // Parent default inherited

        Parent.staticMethod(); // Valid
        // Child.staticMethod(); // DOES NOT COMPILE
    }

    static void Q2() {
        // A subinterface may redeclare an inherited default
        // method as abstract, forcing implementations to provide it.

        AbstractAgain obj = new AbstractAgainImpl();
        obj.getLength(); // 50
    }

    static void Q3() {
        // A default method CANNOT be replaced by a static method
        // with the same signature in a subinterface.

        DefaultParent obj = new DefaultChildImpl();
        obj.calculate(); // 10

        // INVALID:
        // interface BadChild extends DefaultParent {
        //     static int calculate() { return 20; }
        // }
    }

    static void Q4() {
        // Private interface methods are NOT inherited.
        // A subinterface may declare its own private method
        // with exactly the same signature.

        HelperChild obj = new HelperChildImpl();
        obj.run(); // Parent helper / Child helper
    }

    static void Q5() {
        // Interface fields are always public static final.
        // Subinterfaces can HIDE inherited fields.

        System.out.println(FieldParent.VALUE); // 10
        System.out.println(FieldChild.VALUE);  // 20

        // Variables are public final static
        // INVALID:
        // interface BadFields {
        //     private final int STEP = 10;
        // }

        // Interface methods cannot be final.
        // INVALID:
        // interface BadMethods {
        //     default final int getUnit() { return 100; }
        // }
    }

    interface Parent {
        int VALUE = 10;

        void abstractMethod();

        default void defaultMethod() {
            System.out.println("Parent default");
        }

        static void staticMethod() {
            System.out.println("Parent static");
        }
    }

    interface Child extends Parent {
        int STEP = 20;
    }

    static class ChildImpl implements Child {
        public void abstractMethod() {
            System.out.println("Abstract implemented");
        }
    }

    interface Measurement {
        default int getLength() {
            return 10;
        }
    }

    interface AbstractAgain extends Measurement {
        int getLength(); // Valid: default becomes abstract
    }

    static class AbstractAgainImpl implements AbstractAgain {
        public int getLength() {
            System.out.println("Abstract again: 50");
            return 50;
        }
    }

    interface DefaultParent {
        default int calculate() {
            return 10;
        }
    }

    interface DefaultChild extends DefaultParent {
        // Static calculate() here would be illegal.
        //static int calculate() {}
    }

    static class DefaultChildImpl implements DefaultChild {
    }

    interface HelperParent {
        private void helper() {
            System.out.println("Parent helper");
        }

        default void runParentHelper() {
            helper();
        }
    }

    interface HelperChild extends HelperParent {
        private void helper() {
            System.out.println("Child helper");
        }

        default void run() {
            runParentHelper();
            helper();
        }
    }

    static class HelperChildImpl implements HelperChild {
    }

    interface FieldParent {
        int VALUE = 10;
    }

    interface FieldChild extends FieldParent {
        int VALUE = 20; // Valid field hiding
    }
}
