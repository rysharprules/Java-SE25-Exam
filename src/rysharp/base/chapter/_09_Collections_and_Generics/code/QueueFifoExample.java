package rysharp.base.chapter._09_Collections_and_Generics.code;

import module java.base;

public class QueueFifoExample {

    public static void main(String[] args) {

        Queue<String> queue = new ArrayDeque<>();

        // Queue / FIFO:
        //
        // offer() adds at the TAIL.
        // poll() removes from the HEAD.
        // peek() examines the HEAD.
        //
        // HEAD                    TAIL
        //  ↓                        ↓
        // [A] -> [B] -> [C]

        queue.offer("A");
        queue.offer("B");
        queue.offer("C");

        System.out.println(queue);        // [A, B, C]
        System.out.println(queue.peek()); // A - examine HEAD

        System.out.println(queue.poll()); // A - remove HEAD
        System.out.println(queue.poll()); // B
        System.out.println(queue.poll()); // C

        // poll() uses the special-value behaviour.
        System.out.println(queue.poll()); // null
    }
}