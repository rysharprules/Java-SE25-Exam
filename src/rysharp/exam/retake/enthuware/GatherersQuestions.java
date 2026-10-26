package rysharp.exam.retake.enthuware;

import java.util.Arrays;
import java.util.stream.Gatherers;

public class GatherersQuestions {
    void main(){
        String[] codes = {"a", "ab", "aaa"};
        Arrays.stream(codes)
                .gather(Gatherers.windowSliding(2))
                //List<String> of size 2 [a, ab]
                //List<String> of size 2 [ab, aaa]
                .gather(Gatherers.scan(()->0, (a, b)->a+b.size() ))
                // () -> 0 initializes the accumulator once.
                //a is the current accumulated integer.
                //b is the current window (List<String>).
                //b.size() is 2 for both windows.
                .forEach(System.out::println);// 2 4
        // scan() emits every updated accumulator value, whereas fold() emits only the final accumulated result.
        // So if you changed scan to fold:
        // .gather(Gatherers.fold(() -> 0, (a, b) -> a + b.size()))
        // the output would simply be: 4

    }
}

