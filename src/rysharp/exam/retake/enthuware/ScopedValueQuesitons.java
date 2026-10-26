package rysharp.exam.retake.enthuware;

public class ScopedValueQuesitons {
    final static ScopedValue<String> MSG = ScopedValue.newInstance();

    public static String getMsg() {
        return MSG.orElse("none");
    }

    static void main(String[] args) {
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
}
