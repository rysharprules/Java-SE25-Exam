package rysharp.exam.actionplan.enthuware;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class DateTimeQuestions {

    record Book(String title, LocalDate releaseDate) {
    }

    void main(){
        q1();
    }

    // Technically a streams question, but unfamiliar with IsoChronology
    static void q1(){
        var books = new ArrayList<Book>(List.of(new Book("The Outsider", LocalDate.of(2019, 1, 1)),
                new Book("Becoming", LocalDate.of(2018, 1, 1)),
                new Book("Uri", LocalDate.of(2017, 1, 1))));

        /*
        The IsoChronology class in Java (java.time.chrono.IsoChronology) represents the standard ISO-8601 calendar system
        (the de facto Gregorian/Julian-hybrid calendar used globally). It was introduced in Java 8 as part of the java.time package.
         */
        Predicate<Book> p = b -> b.releaseDate().isAfter(IsoChronology.INSTANCE.date(2018, 1, 1));
        // newBooks will be assigned a set of book titles that were released after the date indicated by the predicate p (two correct options below)
        Set<String> newBooks1 = books.stream()
                .collect(Collectors.partitioningBy(p)) .get(true).stream().map(Book::title).collect(Collectors.toCollection(TreeSet::new));
        System.out.println(newBooks1);
        Set<String> newBooks2 = books.stream()
                .collect(Collectors.filtering(p, Collectors.mapping(Book::title, Collectors.toSet())));
        System.out.println(newBooks2);
    }

}
