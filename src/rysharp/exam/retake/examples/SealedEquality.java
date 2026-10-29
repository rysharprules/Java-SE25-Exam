package rysharp.exam.retake.examples;

import java.util.Objects;

// Q7 POST_EXAM_REPORT.md
public class SealedEquality {
    sealed static abstract class Art permits Music, Dance {
        private final double meters;
        Art(double meters) { this.meters = meters; }
        public double meters() { return meters; }

        @Override public final boolean equals(Object other) {
            return other instanceof Art a && Double.compare(meters, a.meters) == 0;
        }
        @Override public final int hashCode() { return Double.hashCode(meters); }
    }

    final static class Music extends Art { Music(double meters) { super(meters); } }
    final static class Dance extends Art { Dance(double meters) { super(meters); } }

    public static void main(String[] args) {
        Art a = new Music(3.5), b = new Dance(3.5), c = new Dance(4.0);
        System.out.println(a.equals(null)); // false
        System.out.println(a.equals(b));    // true: cross-subclass equality by meters
        System.out.println(b.equals(c));    // false
        System.out.println(a.getClass() == b.getClass()); // false
    }
}
