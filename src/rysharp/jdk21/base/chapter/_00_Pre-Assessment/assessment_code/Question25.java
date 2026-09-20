package rysharp.jdk21.base.chapter._00;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class Question25 {

}

record Music(List<String> tempo) {
    final int score = 10; // a record cannot contain instance variables that are not declared as part of the record.
}
record Song(String lyrics, Music m) {
    Song {
        this.lyrics = lyrics + "Never gonna give you up"; // a compact `record` constructor does not allow modifying an instance variable with a this reference.
    } }
sealed class Dance {} // `permits` clause is optional for a sealed class if the associated classes are in the same file.
record March() {
    int roll(Song s) { // A pattern matching switch can include elements of the record and use var.
        return switch (s) {
            case null -> 2;
            case Song(var q, Music(List d)) -> 1;
            default -> 3;
        };
    }
}
sealed class Ballet extends Dance permits NewDance {
    Ballet { // missing parentheses in the constructor - only records support compact constructors.
        var d = LocalDate.of(2025, Month.OCTOBER, 20);
        if (d.isAfter(LocalDate.now()))
            System.out.print("say goodbye");
    }
}
abstract class NewDance extends Ballet {} // a class that extends sealed classes must be marked final, sealed, or non-sealed.
