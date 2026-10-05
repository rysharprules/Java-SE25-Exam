package rysharp.base.chapter._09_Collections_and_Generics.code;

import module java.base;

public class DequeFifoExample {

    public static void main(String[] args) {

        Deque<String> deque = new ArrayDeque<>();

        // Explicit Deque version of FIFO:
        //
        // offerLast()  -> add at TAIL
        // pollFirst()  -> remove from HEAD
        // peekFirst()  -> examine HEAD
        //
        // These correspond to the normal Queue methods:
        //
        // offer() -> offerLast()
        // poll()  -> pollFirst()
        // peek()  -> peekFirst()

        deque.offerLast("A");
        deque.offerLast("B");
        deque.offerLast("C");

        System.out.println(deque);             // [A, B, C]
        System.out.println(deque.peekFirst()); // A

        System.out.println(deque.pollFirst()); // A
        System.out.println(deque.pollFirst()); // B
        System.out.println(deque.pollFirst()); // C

        System.out.println(deque.pollFirst()); // null
    }
}