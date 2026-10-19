void main() {

    Deque<String> stack = new ArrayDeque<>();

    // Deque / LIFO stack:
    //
    // push() -> addFirst()    -> HEAD
    // pop()  -> removeFirst() -> HEAD
    // peek() -> peekFirst()   -> HEAD
    //
    // Everything happens at the HEAD.

    stack.push("A");
    // [A]

    stack.push("B");
    // [B, A]

    stack.push("C");
    // [C, B, A]
    //  ↑
    // HEAD / TOP

    System.out.println(stack);        // [C, B, A]
    System.out.println(stack.peek()); // C

    System.out.println(stack.pop());  // C
    System.out.println(stack.pop());  // B
    System.out.println(stack.pop());  // A

    // Unlike poll(), pop() does NOT return null when empty.
    // It delegates conceptually to removeFirst().
    //
    // stack.pop(); // NoSuchElementException
}