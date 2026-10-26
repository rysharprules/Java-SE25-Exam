package rysharp.exam.retake.examples;

import java.util.*;
import java.util.stream.*;
// Q12 POST_EXAM_REPORT.md
public class GathererWindows {
    // Java 25 required (Gatherers were finalized in Java 24).
    public static void main(String[] args) {
        var windows = Stream.of(1,2,3,4,5).gather(Gatherers.windowFixed(2)).toList();
        System.out.println(windows); // [[1, 2], [3, 4], [5]]
        System.out.println(windows.stream().map(w -> w.get(0)).toList()); // [1,3,5]
        // w.get(1) on final window [5] would throw IndexOutOfBoundsException.
    }
}
