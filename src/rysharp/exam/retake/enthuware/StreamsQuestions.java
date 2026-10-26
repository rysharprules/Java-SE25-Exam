package rysharp.exam.retake.enthuware;
import module java.base;

public class StreamsQuestions {
    void main() {
        Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8};
        Arrays.stream(intArray)
                // 1. The takeWhile method passes the elements to the next operation in the stream pipeline as long as the element satisfies the given predicate.
                // As soon as an element fails the predicate, that element and the rest are dropped.
                // In this case, the predicate is i<10. Since the predicate never fails for the given stream of elements, all the elements go to the next operation.
                .takeWhile(i -> i < 10)
                // The map operation adds 1 to each element before passing them on to the next stage. Thus, the stream now contains 2, 3, 4, 5, 6, 7, 8, 9
                .map(j -> j + 1)
                // The dropWhile method keeps dropping the elements from stream pipeline until it reaches an element that fails the predicate.
                // Once the predicate fails, that element and the rest of the elements are passed to the next operation.
                // In this case, the condition is k%5 = 0, but this condition fails for the first element itself. Thus, no element is dropped.
                .dropWhile(k -> k % 5 == 0)
                // limit(3) passes on first 3 elements to the next operation. Thus, forEach will get only three elements 2, 3, and 4.
                .limit(3)
                // Thus, forEach will get only three elements 2, 3, and 4.
                .forEach(System.out::print);
    }
}
