/*
 * Demonstrates why calling overridable methods from constructors
 * can produce surprising results.
 */

class Parent {

    Parent() {
        System.out.println("1. Parent constructor");

        /*
         * Java uses dynamic dispatch here.
         *
         * Even though we are currently executing Parent's
         * constructor, the actual object is a Child.
         *
         * Therefore Child.printValue() is invoked.
         */
        printValue();
    }

    void printValue() {
        System.out.println("Parent printValue()");
    }
}


class Child extends Parent {

    /*
     * This initializer has NOT executed when the Parent
     * constructor calls printValue().
     *
     * Until this executes, value contains its default:
     *
     *     int default = 0
     */
    int value = initialiseValue();

    int initialiseValue() {
        System.out.println("3. Child field initialized to 10");
        return 10;
    }

    Child() {
        System.out.println("4. Child constructor");
        printValue();
    }

    @Override
    void printValue() {
        System.out.println("2/5. Child value = " + value);
    }
}


void main() {

    /*
     * Predict this carefully.
     *
     * new Child()
     *
     *      ↓
     *
     * Parent constructor starts
     *
     *      ↓
     *
     * Parent calls printValue()
     *
     *      ↓
     *
     * Dynamic dispatch calls Child.printValue()
     *
     * BUT:
     *
     * Child's field initializer has not executed yet.
     *
     * Therefore value is still its default:
     *
     *     0
     *
     * Only after Parent construction completes does:
     *
     *     int value = initialiseValue();
     *
     * execute and assign 10.
     */

    new Child();
}