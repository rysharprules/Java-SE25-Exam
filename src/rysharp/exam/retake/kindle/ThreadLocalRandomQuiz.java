package rysharp.exam.retake.kindle;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThreadLocalRandomQuiz {
    void main(){
        // Kindle 21.3.7 Quiz (p.1361)
        q1();
    }
    static void q1() {
        /*
        stateful lambdas because sb refers to a mutable object whose state is not managed by the stream pipeline but
        its state is accessed from stream operations. Therefore, when the stream pipeline is parallelized, multiple
        threads employed by the stream pipeline to process the elements of the stream will try to access and mutate sb simultaneously.
         */
        StringBuilder sb = new StringBuilder();
        IntStream is = ThreadLocalRandom.current().ints(0, 10);
        is
                .parallel() // sb will contain exactly 2000 numbers if the parallel operation is removed.
                .takeWhile(i->sb.length()<2001)
                .forEach(s->sb.append(s));
        System.out.println(sb.length()); // The length of sb is unpredictable.
    }
}
