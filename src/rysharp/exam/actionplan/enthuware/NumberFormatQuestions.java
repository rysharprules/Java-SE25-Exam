package rysharp.exam.actionplan.enthuware;

import java.util.*;
import java.text.*;

public class NumberFormatQuestions {

    public static void main(String[] args) throws ParseException {
        double amount = 123456.789;

        Locale fr = new Locale("fr", "FR");
        NumberFormat formatter = NumberFormat.getInstance(fr);// French formatting uses a comma as the decimal separator and a grouping space.

        String s = formatter.format(amount);

        formatter = NumberFormat.getInstance();
        Number amount2 = formatter.parse(s); // NumberFormat.parse() throws the checked ParseException

        System.out.println(amount + " " + amount2);// 123456.789 123
    }
}
