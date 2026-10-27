package rysharp.exam.retake.enthuware;

public class ScopedValueQuesitons {
    final static ScopedValue<String> MSG = ScopedValue.newInstance();
    static final ScopedValue<String> USERID = ScopedValue.newInstance();

    public static String getMsg() {
        return MSG.orElse("none");
    }

    void main(){
        q1();
        q2();
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
}
