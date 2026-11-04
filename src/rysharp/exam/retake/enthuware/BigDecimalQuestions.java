package rysharp.exam.retake.enthuware;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalQuestions {
    void main() {
        q1();
        q2();
    }

    static void q1() {
        System.out.println("\nQ1");
        System.out.println();
        BigDecimal bd = null;
        try {
            bd = BigDecimal.valueOf(12, 3);
            System.out.println("--- Setting scale with RoundingMode");
            System.out.println(bd.setScale(2, RoundingMode.HALF_UP));
            System.out.println(bd.setScale(5, RoundingMode.HALF_UP));
            System.out.println(bd.setScale(3, RoundingMode.HALF_DOWN));
            System.out.println(bd.setScale(2, RoundingMode.CEILING));
            System.out.println(bd.setScale(2, RoundingMode.FLOOR));

            System.out.println("--- Scales");
            System.out.println(BigDecimal.valueOf(12, -1));
            System.out.println(BigDecimal.valueOf(12, 0));
            System.out.println(BigDecimal.valueOf(12, 1));
            System.out.println(BigDecimal.valueOf(12, 2));
            System.out.println(BigDecimal.valueOf(12, 3));
            System.out.println(BigDecimal.valueOf(12, 4));

            System.out.println("--- Question 1");
            String s = bd.toString();
            System.out.println(s); // 0.012
            int i = Integer.parseInt(s);// throws
            System.out.println("" + i);
        } catch (Exception e) {
            System.out.println("trouble : " + bd); // trouble : 0.012
            System.out.println(e.getClass() + ": " + e.getMessage());// class java.lang.NumberFormatException: For input string: "0.012"
        }
    }

    static void q2() {
        System.out.println("\nQ2");
        Object t = BigDecimal.valueOf(107);
        // attempts to call intValue on Object
        //int k = (BigDecimal) t.intValue() / 9;
        int k = ((BigDecimal) t).intValue()/9;
        System.out.println(k); // 11 (107/9=11.8, .8 is truncated due to int)
    }
}
