package rysharp.exam.retake.examples;

public class LoopTrace {
    public static void main(String[] args) {
        int c = 1;

        for (int a = 3; a > 0; a--) {
            for (int b = 0; b < 3; b++, c--, System.out.printf("%nDECREMENT c=%d", c)) {
                System.out.println();
                System.out.printf(
                        "INNER C  | a=%d, b=%d, c=%d%n", a, b, c);

                if (a == 1) {
                    continue; // decrement does occur after continue

                } else if (b == 1) {
                    System.out.printf(
                            "BREAK     | a=%d, b=%d, c=%d%n", a, b, c); // decrement does not occur after break
                    break;

                } else {
                    System.out.printf(
                            "CHANGE C  | a=%d, b=%d, c=%d | adding %d (c is %d)",
                            a, b, c, a + b, c + a + b);
                    c += a + b;
                }
            }
        }

        System.out.println();
        System.out.println("Final c = " + c);
    }
}