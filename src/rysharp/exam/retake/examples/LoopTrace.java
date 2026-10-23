public class LoopTrace {
    public static void main(String[] args) {
        int c = 1;
        for (int a = 3; a > 0; a--) {
            for (int b = 0; b < 3; b++, c--) {
                if (a == 1) {
                    System.out.printf("CONTINUE a=%d b=%d c=%d%n", a,b,c);
                    continue;
                } else if (b == 1) {
                    System.out.printf("BREAK    a=%d b=%d c=%d%n", a,b,c);
                    break;
                } else {
                    System.out.printf("CHANGE   a=%d b=%d c=%d (+%d)%n", a,b,c,a+b);
                    c += a + b;
                }
            }
        }
        System.out.println("Final c = " + c); // 1
    }
}
