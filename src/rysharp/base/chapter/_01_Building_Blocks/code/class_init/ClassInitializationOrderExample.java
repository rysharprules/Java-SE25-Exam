/*
 * ClassInitializationOrderExample
 * --------------------------------
 *
 * Demonstrates:
 *
 *   - Static field initialization
 *   - Static initializer blocks
 *   - Instance field initialization
 *   - Instance initializer blocks
 *   - Multiple levels of inheritance
 *   - super() constructor chaining
 *   - this() constructor chaining
 *   - Java 25 constructor prologues
 *   - Static initialization happening only once
 *   - Instance initialization happening for every object
 *
 *
 * BROAD INITIALIZATION ORDER
 * ==========================
 *
 * On first use:
 *
 *   STATIC:
 *
 *       Animal
 *          ↓
 *       Mammal
 *          ↓
 *       Dog
 *
 *
 * When constructing a Dog:
 *
 *       Dog constructor prologue
 *              ↓
 *       Animal instance initialization
 *              ↓
 *       Animal constructor
 *              ↓
 *       Mammal instance initialization
 *              ↓
 *       Mammal constructor
 *              ↓
 *       Dog instance initialization
 *              ↓
 *       Dog constructor body
 *
 *
 * IMPORTANT:
 *
 * Static fields and static initializer blocks execute in
 * textual order within each class.
 *
 * Instance fields and instance initializer blocks also
 * execute in textual order within each class.
 */


class Animal {

    // ---------------------------------------------------------
    // STATIC INITIALIZATION
    // ---------------------------------------------------------

    static String animalStaticField =
            log("Animal - static field");

    static {
        log("Animal - static initializer block");
    }


    // ---------------------------------------------------------
    // INSTANCE INITIALIZATION
    // ---------------------------------------------------------

    String animalInstanceField =
            log("Animal - instance field");

    {
        log("Animal - instance initializer block");
    }


    // ---------------------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------------------

    Animal() {
        log("Animal - constructor");
    }


    // Utility method used by all classes so that initialization
    // can be seen clearly in the console.
    static String log(String message) {
        System.out.println(message);
        return message;
    }
}


class Mammal extends Animal {

    // ---------------------------------------------------------
    // STATIC INITIALIZATION
    // ---------------------------------------------------------

    static String mammalStaticField =
            log("Mammal - static field");

    static {
        log("Mammal - static initializer block");
    }


    // ---------------------------------------------------------
    // INSTANCE INITIALIZATION
    // ---------------------------------------------------------

    String mammalInstanceField =
            log("Mammal - instance field");

    {
        log("Mammal - instance initializer block");
    }


    // ---------------------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------------------

    Mammal(String name) {

        /*
         * There is no explicit super() here.
         *
         * The compiler effectively supplies:
         *
         *     super();
         *
         * Therefore Animal must be constructed before
         * Mammal's own instance initialization and constructor
         * body can complete.
         */

        log("Mammal - constructor: " + name);
    }
}


class Dog extends Mammal {

    // ---------------------------------------------------------
    // STATIC INITIALIZATION
    // ---------------------------------------------------------

    static String dogStaticField =
            log("Dog - static field");

    static {
        log("Dog - static initializer block");
    }


    // ---------------------------------------------------------
    // INSTANCE INITIALIZATION
    // ---------------------------------------------------------

    String dogInstanceField =
            log("Dog - instance field");

    {
        log("Dog - instance initializer block");
    }


    // ---------------------------------------------------------
    // CONSTRUCTORS
    // ---------------------------------------------------------

    Dog() {

        /*
         * Constructor chaining.
         *
         * This delegates to Dog(String).
         *
         * Dog's instance fields and initializer blocks do NOT
         * execute once for this constructor and again for
         * Dog(String).
         *
         * They execute only ONCE for this object.
         */

        this("Unknown");

        /*
         * Dog(String) has completely finished before execution
         * returns here.
         */

        log("Dog - no-arg constructor body");
    }


    Dog(String name) {

        /*
         * =====================================================
         * JAVA 25 FLEXIBLE CONSTRUCTOR BODY
         * =====================================================
         *
         * Java 25 allows statements before an explicit
         * super(...) or this(...) constructor invocation.
         *
         * This part is called the CONSTRUCTOR PROLOGUE.
         *
         * Importantly, this code executes BEFORE super(...).
         */

        log("Dog - PROLOGUE starts");

        String cleanedName = name.trim().toUpperCase();

        log("Dog - PROLOGUE cleaned name: " + cleanedName);


        /*
         * At this point we must NOT try to use the current Dog
         * instance in ways that require it to have been
         * initialized.
         *
         * For example, this would NOT compile:
         *
         *     System.out.println(this.dogInstanceField);
         *
         * The current object is not available for normal
         * instance use in the prologue.
         */


        // -----------------------------------------------------
        // SUPERCLASS CONSTRUCTION
        // -----------------------------------------------------

        super(cleanedName);


        /*
         * super(cleanedName) causes construction to travel
         * through the superclass hierarchy.
         *
         * Mammal's constructor implicitly invokes:
         *
         *     super();
         *
         * which reaches Animal.
         *
         *
         * The resulting order is:
         *
         *     Dog prologue
         *
         *          ↓
         *
         *     Animal instance field
         *     Animal instance initializer
         *     Animal constructor
         *
         *          ↓
         *
         *     Mammal instance field
         *     Mammal instance initializer
         *     Mammal constructor
         *
         *          ↓
         *
         *     Dog instance field
         *     Dog instance initializer
         *
         *          ↓
         *
         *     execution continues HERE
         */


        log("Dog - String constructor body");
    }
}


/*
 * =============================================================
 * JAVA 25 COMPACT SOURCE FILE MAIN METHOD
 * =============================================================
 *
 * There is no explicit class surrounding main().
 *
 * Java supplies the implicit/compact class.
 */

void main() {

    System.out.println();
    System.out.println("======================================");
    System.out.println("FIRST DOG");
    System.out.println("======================================");


    /*
     * FIRST ACTIVE USE
     * ----------------
     *
     * Because this is the first active use of Dog, static
     * initialization occurs first.
     *
     * Expected static order:
     *
     *     Animal static field
     *     Animal static block
     *
     *     Mammal static field
     *     Mammal static block
     *
     *     Dog static field
     *     Dog static block
     *
     *
     * Then construction begins.
     *
     * Dog() delegates to Dog(String).
     *
     * Expected instance/construction order:
     *
     *     Dog prologue
     *
     *     Animal instance field
     *     Animal instance block
     *     Animal constructor
     *
     *     Mammal instance field
     *     Mammal instance block
     *     Mammal constructor
     *
     *     Dog instance field
     *     Dog instance block
     *
     *     Dog(String) body
     *     Dog() body
     */

    new Dog();


    System.out.println();
    System.out.println("======================================");
    System.out.println("SECOND DOG");
    System.out.println("======================================");


    /*
     * STATIC INITIALIZATION DOES NOT REPEAT.
     *
     * Animal, Mammal and Dog have already been initialized.
     *
     * Therefore we immediately begin construction of the
     * second object.
     *
     * Notice also that this directly selects Dog(String),
     * so Dog() is not involved.
     */

    new Dog("  Rex  ");


    System.out.println();
    System.out.println("======================================");
    System.out.println("THIRD DOG");
    System.out.println("======================================");


    /*
     * Again:
     *
     *     NO static initialization.
     *
     * But every new object receives its own:
     *
     *     instance field initialization
     *     instance initializer blocks
     *     constructor execution
     *
     * Because we use new Dog() again, the this() constructor
     * chain is involved again.
     */

    new Dog();


    System.out.println();
    System.out.println("======================================");
    System.out.println("FINISHED");
    System.out.println("======================================");
}


/*
 * =============================================================
 * FINAL MEMORY MODEL
 * =============================================================
 *
 *
 * FIRST EVER new Dog()
 * --------------------
 *
 * CLASS INITIALIZATION:
 *
 *     Animal static fields / blocks
 *                ↓
 *     Mammal static fields / blocks
 *                ↓
 *     Dog static fields / blocks
 *
 *
 * CONSTRUCTION:
 *
 *     Dog()
 *       |
 *       | this(...)
 *       ↓
 *     Dog(String)
 *       |
 *       | PROLOGUE
 *       |
 *       | super(...)
 *       ↓
 *
 *     Animal instance fields / blocks
 *                ↓
 *     Animal constructor
 *                ↓
 *     Mammal instance fields / blocks
 *                ↓
 *     Mammal constructor
 *                ↓
 *     Dog instance fields / blocks
 *                ↓
 *     Dog(String) constructor body
 *                ↓
 *     Dog() constructor body
 *
 *
 *
 * SUBSEQUENT new Dog()
 * --------------------
 *
 *     NO STATIC INITIALIZATION
 *
 *     Dog constructor prologue
 *                ↓
 *     Animal instance fields / blocks
 *                ↓
 *     Animal constructor
 *                ↓
 *     Mammal instance fields / blocks
 *                ↓
 *     Mammal constructor
 *                ↓
 *     Dog instance fields / blocks
 *                ↓
 *     Dog constructor body/bodies
 *
 *
 * KEY RULES:
 *
 * 1. Static initialization happens once per class initialization.
 *
 * 2. Superclass static initialization happens before subclass
 *    static initialization.
 *
 * 3. Instance fields and initializer blocks execute once for
 *    every new object.
 *
 * 4. Within a class, fields and initializer blocks execute in
 *    textual order.
 *
 * 5. Superclass instance initialization occurs before subclass
 *    instance initialization.
 *
 * 6. this(...) chains constructors in the SAME class.
 *
 * 7. super(...) invokes a constructor in the SUPERCLASS.
 *
 * 8. this(...) chaining does NOT cause instance initializers
 *    to execute multiple times.
 *
 * 9. Java 25 permits a constructor PROLOGUE before an explicit
 *    this(...) or super(...) invocation.
 *
 * 10. A prologue can execute before superclass construction,
 *     but cannot freely use the current object.
 *
 * 11. Constructors can be OVERLOADED.
 *
 * 12. Constructors cannot be OVERRIDDEN.
 */