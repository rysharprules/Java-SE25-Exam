package rysharp.exam.actionplan.examples;

// Q10 POST_EXAM_REPORT.md
public class OverloadVarargs {
    static String choose(int a, int b) { return "int,int"; }
    static String choose(Integer a, int b) { return "Integer,int"; }
    static String choose(int a, Integer b) { return "int,Integer"; }
    static String choose(int a, String b) { return "int,String"; }
    static String choose(float a, float b) { return "float,float"; }
    static String choose(double... values) { return "double..."; }
    static String choose(double a, Double b) { return "double,Double"; }
    public static void main(String[] args) {
        System.out.println(choose(1, 2.0)); // double...
        System.out.println(choose(1, 2));   // int,int
        IO.println(choose(1, Integer.valueOf(1))); // int,Integer
//        IO.println(choose(Integer.valueOf(1), Integer.valueOf(1))); // java: reference to choose is ambiguous
        IO.println(choose(2.0, Integer.valueOf(1)));// double...
        IO.println(choose(5, Double.valueOf(1)));// double,Double
        IO.println(choose(5.2, 6));// double...
        IO.println(choose(5.2f, 6));
    }
}
