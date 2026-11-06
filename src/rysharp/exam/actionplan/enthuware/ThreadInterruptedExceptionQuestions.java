
package rysharp.exam.actionplan.enthuware;

public class ThreadInterruptedExceptionQuestions {

    public static void main(String[] args)
            throws InterruptedException {

        Thread worker = new Thread(() -> {
            try {
                IO.println("Worker going to sleep...");
                Thread.sleep(10_000);

                IO.println("Worker woke up normally");
            } catch (InterruptedException e) {
                IO.println("InterruptedException caught!");

                // InterruptedException clears the interrupt flag
                IO.println("Interrupted status: "
                        + Thread.currentThread().isInterrupted());
            }

            IO.println("Worker finished");
        });

        worker.start();

        // Give the worker time to enter sleep()
        Thread.sleep(500);

        IO.println("Main interrupting worker...");
        worker.interrupt();

        worker.join();

        IO.println("Final state: " + worker.getState());
    }
}
