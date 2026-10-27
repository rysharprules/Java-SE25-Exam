package rysharp.exam.retake.examples;

public class MathMethods {
    public static void main(String[] args) {
        System.out.println(Math.round(2.5f));   // 3 (int)
        System.out.println(Math.round(-2.5));   // -2 (long)
        System.out.println(Math.floor(-2.1));   // -3.0
        System.out.println(Math.ceil(-2.1));    // -2.0
        System.out.println(Math.max(3, 7));     // 7
        System.out.println(Math.abs(-4));       // 4
        System.out.println(Math.pow(2, 3));     // 8.0
        System.out.println(Math.round(2.5));   // 3 (long)

        round();
        addExact();
        random();
    }

    /*
    The Math.round() method in Java rounds a floating-point number to the nearest integer. It handles midpoints (e.g., .5) by rounding towards positive infinity

    float	-- int	    public static int round(float a)
    double	-- long	    public static long round(double a)
     */
    static void round(){
        System.out.println(Math.round(3.5f));// 4 (int)
        System.out.println(Math.round(3.49));// 3 (long)
    }

    /*
    The Math.addExact() method in Java returns the sum of its arguments, throwing an exception if the result overflows the capacity of the data type.

	Throws Exception On Overflow? - Yes (ArithmeticException)
    int x, int y	-- int
    long x, long y	-- long
     */
    static void addExact(){
        System.out.println(Math.addExact(45, 34L)); // 79
    }

    static void random(){
        System.out.println("\nMath.random");
        System.out.println(Math.random());
        System.out.println(Math.random());
        System.out.println((int) (Math.random() * 11)); // Generates a whole number from 0 to 10
    }
}
