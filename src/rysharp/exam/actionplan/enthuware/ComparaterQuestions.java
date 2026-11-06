package rysharp.exam.actionplan.enthuware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ComparaterQuestions {

    public static void main(String[] args) {
        ArrayList<Person> al = new ArrayList<>();
        al.add(new Person("Paul", "01012000"));
        al.add(new Person("Peter", "01011990"));
        al.add(new Person("Patrick", "01012002"));
        System.out.println("original: " + al);

        java.util.Collections.sort(al, (p1, p2)->p1.dob.compareTo(p2.dob));
        System.out.println("sorted: " + al);

        Collections.shuffle(al);
        System.out.println("shuffled: " + al);

        java.util.Collections.sort(al, Comparator.comparing(p -> p.dob)); // cleaner version of above
        System.out.println("sorted: " + al);

        Collections.shuffle(al);

        java.util.Collections.sort(al, SortTest::diff);// correct method inferred
        System.out.println("sorted: " + al);

        Collections.shuffle(al);

        java.util.Collections.sort(al, new MySorter()::compare);// this is a valid method reference
        System.out.println("sorted: " + al);

        Collections.shuffle(al);

        Collections.sort(al, new MySorter2());// MySorter2 implements Comparator
        System.out.println("sorted: " + al);

        Collections.shuffle(al);

        al.sort(new MySorter2());// as above but using List.sort
        System.out.println("sorted: " + al);

        //java.util.Arrays.sort(al, SortTest::diff); // Arrays.sort works on arrays and not lists.
    }

    static class Person {
        String name;
        String dob;

        public Person(String name, String dob) {
            this.name = name;
            this.dob = dob;
        }

        public String toString() {
            return name + " " + dob;
        }
    }

    static class MySorter {
        public int compare(Person p1, Person p2) {
            return p1.dob.compareTo(p2.dob);
        }
    }

    static class MySorter2 implements Comparator<Person> {
        @Override
        public int compare(Person p1, Person p2) {
            return p1.dob.compareTo(p2.dob);
        }
    }

    public class SortTest {
        public static int diff(Person p1, Person p2) {
            return p1.dob.compareTo(p2.dob);
        }

        public static int diff(Date d1, Date d2) {
            return d1.compareTo(d2);
        }
    }
}
