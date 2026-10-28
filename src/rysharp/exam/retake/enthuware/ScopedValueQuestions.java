package rysharp.exam.retake.enthuware;

public class ScopedValueQuestions {
    final static ScopedValue<String> MSG = ScopedValue.newInstance();
    static final ScopedValue<String> USERID = ScopedValue.newInstance();
    final static ScopedValue<Integer> DISCOUNT = ScopedValue.newInstance();

    public static String getMsg() {
        return MSG.orElse("none");
    }

    void main() {
        q1();
        q2();
        q3();
    }

    static void q1() {
        System.out.println("Q1");
        StringBuilder log = new StringBuilder();
        // An exception will be thrown if an unbounded scoped value is accessed using the get() method.
        // In the first call to getMsg(), the MSG is actually unbound, but instead of MSG.get(), we are using MSG.orElse("none").
        log.append(getMsg()).append(":");
        ScopedValue.where(MSG, "a").run(() -> {
            log.append(getMsg()).append(":");
            ScopedValue.where(MSG, "b").run(() -> log.append(getMsg()).append(":"));
            log.append(getMsg()).append(":");
        });
        log.append(getMsg());
        System.out.println(log); // none:a:b:a:none
    }

    static void q2() {
        System.out.println("Q2");
        var carrier = ScopedValue.where(USERID, "guest");

        System.out.println(USERID.isBound()); // false
        System.out.println(USERID.orElse("none")); // none

        System.out.println(carrier.get(USERID)); // guest

        carrier.run(() -> {
            System.out.println(USERID.isBound()); // true
            System.out.println(USERID.get());     // guest
        });

        System.out.println(USERID.isBound()); // false
    }

    static void q3() {
        System.out.println("Q3");
        // The new thread establishes its own binding using ScopedValue.where(...).run(...).
        //Inside that scope, DISCOUNT.get() returns 8.
        new Thread(() -> {
            ScopedValue.where(DISCOUNT, 8).run(() -> {
                System.out.println(" 1 : " + DISCOUNT.get());
            });
        }).start();
        // Same example with call
        new Thread(() -> {
            System.out.println(ScopedValue.where(DISCOUNT, 8).call(() -> " 1 : " + DISCOUNT.get()));
        }).start();

        // throws NoSuchElementException: ScopedValue not bound
        // The main thread binds DISCOUNT to 10, but then creates a separate thread.
        //Ordinary Thread instances do not automatically inherit scoped-value bindings from their parent thread.
        ScopedValue.where(DISCOUNT, 10).run(() -> {
            new Thread(() -> {
                // Consequently, inside the second thread: DISCOUNT.isBound(); // false
                System.out.println(" 2 : " + DISCOUNT.get()); // get throws NoSuchElementException
            }).start();
        });
        // The binding of 10 belongs to the main thread's dynamic scope, not the new thread.
    }
}
