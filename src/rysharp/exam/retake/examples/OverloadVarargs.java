public class OverloadVarargs {
    static String choose(int a, int b) { return "int,int"; }
    static String choose(int a, String b) { return "int,String"; }
    static String choose(float a, float b) { return "float,float"; }
    static String choose(double... values) { return "double..."; }
    public static void main(String[] args) {
        System.out.println(choose(1, 2.0)); // double...
        System.out.println(choose(1, 2));   // int,int
    }
}
