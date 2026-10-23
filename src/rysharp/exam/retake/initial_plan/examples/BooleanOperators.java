public class BooleanOperators {
    public static void main(String[] args) {
        int x = 0, y = 0;
        boolean a = (x++ > 0) & (y++ >= 0);
        System.out.printf("&  result=%b x=%d y=%d%n", a,x,y); // false 1 1
        x = y = 0;
        boolean b = (x++ > 0) && (y++ >= 0);
        System.out.printf("&& result=%b x=%d y=%d%n", b,x,y); // false 1 0
        System.out.println(6 & 3); // 2: integral bitwise AND
    }
}
