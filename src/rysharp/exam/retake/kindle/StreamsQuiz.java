package rysharp.exam.retake.kindle;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

// Kindle 21.1.6 Quiz (p.1298)
public class StreamsQuiz {
    void main() {
        q1();
        q2();
        q3();
        q4();
    }

    static void q1() {
        // Remember that sorted is an intermediate operation. It does not trigger the execution of the stream pipeline.
        System.out.println(marks.get().sorted());// java.util.stream.SortedOps$OfRef@a09ee92

        //System.out.println(marks.get().map(i -> "" + i).max()); // max and min require a Comparator object to be passed as an argument.

        // If you expected the peek operations of the given pipeline to produce any output, you are in for a surprise.
        // The stream execution is smart enough to figure out that none of the intermediate operations in the given pipeline
        // change the count of the elements in the stream and so, it short circuits all of them and returns the count based
        // on the initial stream itself. Thus, it will just print 5. Observe that count() does not return an Optional.
        System.out.println(marks.get().peek(System.out::println).sorted().peek(System.out::println).count());// 5

        // This has the same issue as the previous statement but with a slight difference. Instead of optimizing away the intermediate
        // operations altogether, here, the stream pipeline execution optimizes away the processing of all but the first element of the
        // stream because to generate the result of findAny, it requires just one element to process. Therefore, only one element passes
        // through the pipeline. Thus, peek prints 55 and findAny results in Optional[55].
        // These two statements highlight the unreliability of the peek operation. It is good only for debugging purpose and must not be relied
        // upon for any side affect.
        System.out.println(marks.get().peek(System.out::println).distinct().findAny());// 55 Optional[55]

        // The sorted operation requires all the elements to be processed before it can generate the first element of the outgoing stream.
        // Therefore, all the elements will pass through peek first then through sorted. The findFirst operation will return Optional[55] at the end.
        System.out.println(marks.get().peek(System.out::println).sorted().findFirst());// 55 65 70 80 70 Optional[55]

        // Here, the number of elements in the last outgoing stream cannot be determined without executing the filter operation for each
        // incoming element. Therefore, each element must pass through peek and then through filter before count can generate the result.
        // The given peek operation prints each element. Since no element satisfies the predicate given to the filter operation, the outgoing
        // stream from filter will be empty. Hence, count will return 0.
        System.out.println(marks.get().peek(System.out::println).filter(i -> i < 0).count());// 55 65 70 80 70 0

        /*
        Remember that count() can skip peek(), map() and other size-preserving intermediate operations when the size is known.
        Also remember that short-circuiting operations can prevent later elements from being processed.
         */
    }

    static void q2() {
        // limit(2) will limit the stream to the first two elements, i.e., 55, 65. anyMatch > 70 will return false
        System.out.println(marks.get().limit(2).anyMatch(i -> i > 70));
        System.out.println(marks.get().limit(10).noneMatch(i -> i > 70));// false
        // allMatch() is a terminal operation and no other operation can further be chained to it.
        //System.out.println(marks.get().allMatch(i -> i > 70).count());
        // Note that the limit method ends the outgoing stream only in two conditions - if the limit is reached or if the incoming stream ends before the limit is reached.
        System.out.println(marks.get().filter(i -> i > 90).limit(2).findFirst());// Optional.empty
        System.out.println(marks.get().filter(i -> i > 70).limit(2).count());// 1
    }

    static void q3() {
        System.out.println(words.get().reduce(0, (c, w) -> targetWord.equals(w) ? c + 1 : c, (c1, c2) -> c1 + c2));// 2
        System.out.println(words.get().map(s -> targetWord.equals(s) ? 1 : 0).reduce((a, b) -> a + b));// Optional[2]
        System.out.println(words.get().map(s -> targetWord.equals(s) ? 1 : 0).reduce(Integer::sum));// Optional[2]
        System.out.println(words.get().filter(targetWord::equals).count());// 2
    }

    static void q4() {
//        Integer count1 = words.get().collect(
//                ()->0,
//                (oldCount, word) -> { if(targetWord.equals(word)) oldCount++; },
//                (c1, c2) -> c1 + c2 );
//        System.out.println("ccc ="+count);

        /*
        Remember that collect performs a mutable reduction. It means, the object supplied by the Supplier function acts as container that stores the
        result and the same object is supposed to be updated after each element is processed. Here, an Integer object is being supplied by the Supplier
        function but Integer is immutable. You can do oldCount++ but that won't update oldCount. Furthermore, the collect method accepts
        BiConsumers as the second and the third arguments. A BiConsumer just consumes two values, it desn't return anything. So, the third lambda doesn't
        implement BiConsumer correctly.

        The following is how this should be done:
         */
        AtomicInteger count2 = words.get().collect(
                AtomicInteger::new,
                (oldCount, word) -> { if(targetWord.equals(word)) oldCount.incrementAndGet(); },
                (c1, c2) -> c1.getAndAdd(c2.get()));
        System.out.println(count2.get());
    }

    static Supplier<Stream<String>> words = () -> Stream.of( "a", "b", "c", "a", "b");
    static Supplier<Stream<Integer>> marks = () -> Stream.of(55, 65, 70, 80, 70);
    static String targetWord = "a";
}
