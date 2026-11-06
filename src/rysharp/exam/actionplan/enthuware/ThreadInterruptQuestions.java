package rysharp.exam.actionplan.enthuware;
/*
Thread State	Description & Core Causes
NEW	Created but unstarted. The Thread object has been instantiated in memory, but start() has not yet been invoked.
RUNNABLE	Executing or ready to execute. The thread is actively running inside the JVM or waiting for resources like CPU allocation from the OS scheduler.
BLOCKED	Waiting for a monitor lock. The thread is suspended, waiting to acquire a synchronized lock currently held by another thread.
WAITING	Waiting indefinitely for another thread. A thread enters this state by calling lock/wait actions with no timeout (e.g., Object.wait(), Thread.join(), or LockSupport.park()).
TIMED_WAITING	Waiting for a specified time. The thread is paused for a strict duration via methods like Thread.sleep(ms), or timed variations of wait(), join(), and park().
TERMINATED	Finished execution. The thread has completed its run() method logic entirely or aborted due to an uncaught exception. It cannot be restarted.
 */
public class ThreadInterruptQuestions {

    static volatile boolean keepRunning = true;

    static Thread worker = new Thread(() -> {
        while (keepRunning) {
            // Busy loop
        }
        // Reads and clears the current thread's interrupt flag
        IO.println("IO.println(Thread.interrupted()); 1st time " + Thread.interrupted());// true
        IO.println("IO.println(Thread.interrupted()); 2nd time " + Thread.interrupted());// false
        System.out.println("Worker finished");
    });

    public static void main(String[] args)
            throws InterruptedException {

        worker.start();

        System.out.println("Before interrupt: "
                + worker.getState() + " "
                + worker.isInterrupted());// Reads interrupt flag without clearing it

        worker.interrupt();// Requests interruption of thread, normally setting its interrupt flag to true

        System.out.println("After interrupt: "
                + worker.getState() + " "
                + worker.isInterrupted());

        Thread.sleep(2000);

        keepRunning = false;
        worker.join();

        System.out.println("Final state: "
                + worker.getState());
    }
}
