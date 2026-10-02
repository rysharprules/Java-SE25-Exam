/*
 * Demonstrates constructor chaining.
 *
 * Constructors are OVERLOADED, not overridden.
 *
 * A this() chain eventually has to reach a constructor
 * that invokes a superclass constructor.
 */

class Parent {

    {
        System.out.println("4. Parent instance initializer");
    }

    Parent(String message) {
        System.out.println("5. Parent constructor: " + message);
    }
}


class Child extends Parent {

    {
        System.out.println("6. Child instance initializer");
    }

    /*
     * Called first by main(), but its BODY executes last.
     */
    Child() {
        this(10);

        System.out.println("9. Child() body");
    }

    /*
     * Child() delegates here.
     *
     * Again, this constructor's BODY does not execute yet.
     * It first delegates to Child(String).
     */
    Child(int number) {
        this("Value = " + number);

        System.out.println("8. Child(int) body");
    }

    /*
     * This is where the this() chain finally reaches super().
     */
    Child(String message) {
        super(message);

        System.out.println("7. Child(String) body");
    }
}


void main() {

    /*
     * Invocation travels:
     *
     * Child()
     *    ↓
     * Child(int)
     *    ↓
     * Child(String)
     *    ↓
     * Parent(String)
     *
     *
     * Execution then works back:
     *
     * Parent instance initializer
     * Parent(String)
     *    ↓
     * Child instance initializer
     * Child(String)
     *    ↓
     * Child(int)
     *    ↓
     * Child()
     *
     *
     * IMPORTANT:
     *
     * Child's instance initializer runs ONCE.
     *
     * Calling three Child constructors through this()
     * does NOT mean the object is initialized three times.
     */

    new Child();
}