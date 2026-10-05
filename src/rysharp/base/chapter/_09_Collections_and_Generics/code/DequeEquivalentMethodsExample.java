package rysharp.base.chapter._09_Collections_and_Generics.code;

import java.util.ArrayDeque;
import java.util.Deque;

public class DequeEquivalentMethodsExample {

    public static void main(String[] args) {

        fifoEquivalents();
        lifoEquivalents();
        emptyBehaviour();
    }

    private static void fifoEquivalents() {

        System.out.println("--- FIFO ---");

        Deque<String> deque = new ArrayDeque<>();

        // Normal Queue terminology:
        deque.offer("A");

        // Equivalent explicit Deque terminology:
        deque.offerLast("B");

        // Both inserted at the TAIL.
        System.out.println(deque); // [A, B]

        // Queue peek() examines the HEAD.
        // Deque peekFirst() explicitly says HEAD/FIRST.
        System.out.println(deque.peek());      // A
        System.out.println(deque.peekFirst()); // A

        // Both remove from the HEAD.
        System.out.println(deque.poll());      // A
        System.out.println(deque.pollFirst()); // B
    }

    private static void lifoEquivalents() {

        System.out.println("--- LIFO ---");

        Deque<String> deque = new ArrayDeque<>();

        // Stack terminology:
        deque.push("A");

        // push() is equivalent to addFirst().
        deque.addFirst("B");

        // Both inserted at the HEAD.
        //
        // addFirst("B")
        //       ↓
        //     [B, A]
        //
        System.out.println(deque); // [B, A]

        // Stack peek() is equivalent to peekFirst().
        System.out.println(deque.peek());      // B
        System.out.println(deque.peekFirst()); // B

        // pop() is equivalent to removeFirst().
        System.out.println(deque.pop());         // B
        System.out.println(deque.removeFirst()); // A
    }

    private static void emptyBehaviour() {

        System.out.println("--- EMPTY BEHAVIOUR ---");

        Deque<String> deque = new ArrayDeque<>();

        // Special-value family:
        System.out.println(deque.poll());      // null
        System.out.println(deque.pollFirst()); // null
        System.out.println(deque.peek());      // null
        System.out.println(deque.peekFirst()); // null

        // Exception family:
        //
        // deque.pop();         // NoSuchElementException
        // deque.remove();      // NoSuchElementException
        // deque.removeFirst(); // NoSuchElementException
        // deque.element();     // NoSuchElementException
        // deque.getFirst();    // NoSuchElementException
    }
}