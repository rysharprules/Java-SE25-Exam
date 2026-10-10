# Executor Services & Threads

A focused reference for Java executor services, asynchronous tasks, scheduling, thread lifecycle and virtual threads.

## Contents

- [Platform and Virtual Threads](#platform-and-virtual-threads)
- [Creating Threads](#creating-threads)
- [ExecutorService](#executorservice)
- [Executor Factory Methods](#executor-factory-methods)
- [Runnable vs Callable](#runnable-vs-callable)
- [Future](#future)
- [invokeAll and invokeAny](#invokeall-and-invokeany)
- [ScheduledExecutorService](#scheduledexecutorservice)
- [Executor Lifecycle](#executor-lifecycle)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## Platform and Virtual Threads

Java supports two thread types.

| Feature | Platform Thread | Virtual Thread |
|---|---|---|
| Introduced | Original Java threading model | Finalised in Java 21 |
| Managed by | JVM and OS | JVM schedules onto carrier platform threads |
| Cost | Relatively expensive | Lightweight |
| Typical quantity | Hundreds/thousands | Potentially millions |
| Best suited to | General tasks, CPU work | High-concurrency blocking I/O |
| Daemon status | Configurable | Always daemon |
| Thread pool | Common | Usually unnecessary |

Virtual threads do not make CPU-intensive calculations execute faster.

Their advantage is scalability when many tasks spend time waiting on I/O.

### Virtual Thread Mounting

```text
VIRTUAL THREAD
      ↓ mounted
CARRIER PLATFORM THREAD
      ↓
OS THREAD
```

When a virtual thread blocks on a supported operation, the JVM can unmount it, freeing the carrier to execute another virtual thread.

Virtual threads are still instances of `Thread`.

```java
Thread.currentThread().isVirtual();
```

Returns `true` for a virtual thread and `false` for a platform thread.

### Java 25 Pinning

Earlier virtual-thread implementations could pin a virtual thread to its carrier when blocking inside `synchronized` code.

JDK 24 removed this major restriction through JEP 491, so Java 25 virtual threads can generally unmount when blocking inside synchronized methods or blocks.

Some pinning situations remain, including certain native or foreign-function calls.

---

## Creating Threads

### Platform Thread

```java
Thread thread = new Thread(() -> {
    System.out.println("Platform: " +
            !Thread.currentThread().isVirtual());
});

thread.start();
thread.join();
```

Output:

```text
Platform: true
```

`start()` schedules the thread for execution.

Calling `run()` directly does **not** start another thread.

```java
thread.run();   // Ordinary method call
thread.start(); // Starts a new thread
```

A thread may be started only once. Starting it again throws `IllegalThreadStateException`.

### Virtual Thread

```java
Thread thread = Thread.ofVirtual().start(() -> {
    System.out.println("Virtual: " +
            Thread.currentThread().isVirtual());
});

thread.join();
```

Output:

```text
Virtual: true
```

### Thread Builder API

```java
Thread.ofPlatform()
        .name("worker")
        .start(() -> System.out.println("Running"));
```

```java
Thread.ofVirtual()
        .name("request-1")
        .start(() -> System.out.println("Running"));
```

A builder can create an unstarted thread:

```java
Thread thread = Thread.ofVirtual()
        .unstarted(() -> System.out.println("Hello"));

thread.start();
```

### Thread.sleep()

```java
Thread.sleep(1000);
```

Pauses the current thread for approximately one second.

It can throw `InterruptedException`.

Sleeping does not guarantee that the thread resumes immediately after the requested duration.

---

## ExecutorService

An `ExecutorService` manages task execution without requiring you to create and manage every thread manually.

```java
ExecutorService executor =
        Executors.newSingleThreadExecutor();

try {
    executor.execute(() -> System.out.println("Hello"));
} finally {
    executor.shutdown();
}
```

The executor controls which thread runs the submitted task.

### Executor vs ExecutorService

```java
Executor executor = command -> command.run();
```

`Executor` defines:

```java
void execute(Runnable command);
```

An `Executor` is not required to run tasks asynchronously. The example above executes the task on the calling thread.

`ExecutorService` extends `Executor` and adds:

- Task submission returning `Future`.
- Bulk execution.
- Shutdown and termination management.

### execute() vs submit()

```java
executor.execute(() -> System.out.println("A"));
```

`execute()` returns `void`.

```java
Future<?> future =
        executor.submit(() -> System.out.println("B"));
```

`submit()` returns a `Future`.

A task exception submitted through `submit()` is captured by the future and reported through `get()` as an `ExecutionException`.

---

## Executor Factory Methods

The `Executors` class provides factory methods.

### Single-Thread Executor

```java
ExecutorService executor =
        Executors.newSingleThreadExecutor();
```

Uses one worker thread, executing tasks sequentially.

```text
TASK A → TASK B → TASK C
```

If the worker fails, the executor can replace it.

### Fixed Thread Pool

```java
ExecutorService executor =
        Executors.newFixedThreadPool(3);
```

Uses up to three worker threads concurrently.

Additional tasks wait in a queue.

```text
WORKER 1 → Task A
WORKER 2 → Task B
WORKER 3 → Task C

QUEUE    → Task D
```

### Cached Thread Pool

```java
ExecutorService executor =
        Executors.newCachedThreadPool();
```

Creates threads as needed and reuses available threads.

Idle threads are eventually removed.

The pool can grow substantially under load.

### Work-Stealing Pool

```java
ExecutorService executor =
        Executors.newWorkStealingPool();
```

Creates a work-stealing pool using a `ForkJoinPool`.

Tasks may execute out of submission order.

### Virtual-Thread-Per-Task Executor

```java
ExecutorService executor =
        Executors.newVirtualThreadPerTaskExecutor();
```

Creates a new virtual thread for each submitted task.

```java
try (ExecutorService executor =
        Executors.newVirtualThreadPerTaskExecutor()) {

    Future<String> future =
            executor.submit(() -> "Hello");

    System.out.println(future.get());
}
```

Output:

```text
Hello
```

This is **not a virtual-thread pool**. Each task gets its own virtual thread.

### Factory Comparison

| Factory | Behaviour |
|---|---|
| `newSingleThreadExecutor()` | One worker; sequential tasks |
| `newFixedThreadPool(n)` | Up to `n` worker threads |
| `newCachedThreadPool()` | Creates/reuses platform threads as needed |
| `newWorkStealingPool()` | Work-stealing `ForkJoinPool` |
| `newScheduledThreadPool(n)` | Scheduled execution |
| `newSingleThreadScheduledExecutor()` | Single-worker scheduler |
| `newVirtualThreadPerTaskExecutor()` | New virtual thread per task |

Unless otherwise stated, these factories use platform threads.

---

## Runnable vs Callable

Both represent tasks, but their signatures differ.

### Runnable

```java
@FunctionalInterface
public interface Runnable {
    void run();
}
```

- Returns `void`.
- Cannot declare checked exceptions.

```java
Runnable task = () -> System.out.println("Running");
```

### Callable

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

- Returns a value.
- Can declare checked exceptions.

```java
Callable<Integer> task = () -> 42;
```

### Comparison

| Feature | Runnable | Callable |
|---|---|---|
| Abstract method | `run()` | `call()` |
| Return type | `void` | `V` |
| Checked exceptions | Cannot declare | Can declare |
| `execute()` | Yes | No |
| `submit()` | Yes | Yes |

### submit() Overloads

```java
Future<?> f1 = executor.submit(
        () -> System.out.println("Hello"));
```

`Runnable` task: successful `get()` returns `null`.

```java
Future<Integer> f2 = executor.submit(() -> 42);
```

`Callable<Integer>` task: `get()` returns `42`.

```java
Future<String> f3 = executor.submit(
        () -> System.out.println("Hello"), "DONE");
```

`Runnable` with supplied result: successful `get()` returns `"DONE"`.

The third form is easy to overlook.

---

## Future

A `Future<V>` represents the result of an asynchronous computation.

```java
try (ExecutorService executor =
        Executors.newSingleThreadExecutor()) {

    Future<Integer> future =
            executor.submit(() -> 10 + 20);

    System.out.println(future.get());
}
```

Output:

```text
30
```

### Important Methods

| Method | Behaviour |
|---|---|
| `get()` | Waits for result |
| `get(timeout, unit)` | Waits up to timeout |
| `isDone()` | Task completed, failed or cancelled |
| `isCancelled()` | Task was cancelled |
| `cancel(boolean)` | Attempts cancellation |

### get()

```java
Integer result = future.get();
```

Blocks until the task completes.

May throw:

- `InterruptedException`
- `ExecutionException`
- `CancellationException` (unchecked)

### Timed get()

```java
future.get(2, TimeUnit.SECONDS);
```

May additionally throw `TimeoutException`.

A timeout does **not** automatically cancel the task.

### isDone()

```java
future.isDone();
```

Returns `true` if the task:

- Completed successfully.
- Failed.
- Was cancelled.

**`isDone() == true` does not guarantee success.**

### Cancellation

```java
future.cancel(true);
```

Attempts cancellation and permits interruption if the task is running.

```java
future.cancel(false);
```

Attempts cancellation without interrupting a running task.

Cancellation is not a guarantee that running code immediately stops.

### Future Exceptions

```java
try (ExecutorService executor =
        Executors.newSingleThreadExecutor()) {

    Future<Integer> future = executor.submit(() -> {
        throw new IllegalStateException("Failed");
    });

    try {
        future.get();
    } catch (ExecutionException e) {
        System.out.println(e.getCause().getClass().getSimpleName());
    }
}
```

Output:

```text
IllegalStateException
```

The task's exception is wrapped in `ExecutionException` when retrieved through `get()`.

---

## invokeAll and invokeAny

Both methods accept collections of `Callable` tasks.

### invokeAll()

```java
List<Callable<Integer>> tasks = List.of(
        () -> 10,
        () -> 20,
        () -> 30
);

try (ExecutorService executor =
        Executors.newFixedThreadPool(3)) {

    List<Future<Integer>> results =
            executor.invokeAll(tasks);

    for (Future<Integer> result : results) {
        System.out.println(result.get());
    }
}
```

Output:

```text
10
20
30
```

Important:

- Returns `List<Future<T>>`.
- Waits for all tasks to complete in the untimed form.
- Results are ordered according to the **input task list**, not completion order.
- Individual tasks may fail; their futures still appear in the list.

### invokeAny()

```java
try (ExecutorService executor =
        Executors.newFixedThreadPool(3)) {

    Integer result = executor.invokeAny(List.of(
            () -> 10,
            () -> 20,
            () -> 30
    ));

    System.out.println(result);
}
```

Possible output:

```text
20
```

Important:

- Returns a single `T`, not a `Future<T>`.
- Returns the result of a successfully completed task.
- Cancels unfinished tasks after a successful result is obtained.
- The result is nondeterministic when multiple tasks can succeed.

### Comparison

| Method | Return | Waits for |
|---|---|---|
| `invokeAll()` | `List<Future<T>>` | All tasks |
| `invokeAny()` | `T` | One successful result |

Timed overloads also exist.

For timed `invokeAll()`, unfinished tasks are cancelled when the timeout expires.

---

## ScheduledExecutorService

A `ScheduledExecutorService` executes tasks after a delay or repeatedly.

```java
ScheduledExecutorService executor =
        Executors.newScheduledThreadPool(2);
```

### Schedule Once

```java
ScheduledFuture<?> future = executor.schedule(
        () -> System.out.println("Hello"),
        2,
        TimeUnit.SECONDS
);
```

Executes after approximately two seconds.

### scheduleAtFixedRate()

```java
executor.scheduleAtFixedRate(
        () -> System.out.println("Tick"),
        0,
        5,
        TimeUnit.SECONDS
);
```

Parameters:

```text
TASK
INITIAL DELAY
PERIOD
TIME UNIT
```

Schedules based on intended start times.

```text
0s → 5s → 10s → 15s
```

If a task takes longer than the period, the next execution may start late, but successive executions of the same periodic task do not overlap.

### scheduleWithFixedDelay()

```java
executor.scheduleWithFixedDelay(
        () -> System.out.println("Tick"),
        0,
        5,
        TimeUnit.SECONDS
);
```

Parameters:

```text
TASK
INITIAL DELAY
DELAY
TIME UNIT
```

Waits the specified delay **after the previous execution finishes**.

If each execution takes two seconds:

```text
Start 0s → Finish 2s
            ↓ wait 5s
Start 7s → Finish 9s
            ↓ wait 5s
Start 14s
```

### Fixed Rate vs Fixed Delay

| Method | Timing based on |
|---|---|
| `scheduleAtFixedRate()` | Planned start-to-start interval |
| `scheduleWithFixedDelay()` | Previous finish-to-next start interval |

Periodic execution stops if an execution throws an exception.

### ScheduledFuture

Scheduling methods return `ScheduledFuture`.

```java
ScheduledFuture<?> future = executor.schedule(
        () -> System.out.println("Done"),
        1,
        TimeUnit.SECONDS
);
```

`ScheduledFuture` extends `Delayed` and `Future`.

---

## Executor Lifecycle

An executor must be shut down when it is no longer needed.

### shutdown()

```java
executor.shutdown();
```

- Stops accepting new tasks.
- Allows previously submitted tasks to finish.
- Does not wait for completion.

### shutdownNow()

```java
List<Runnable> pending = executor.shutdownNow();
```

- Attempts to interrupt running tasks.
- Stops processing waiting tasks.
- Returns tasks that never commenced execution.
- Does not guarantee immediate termination.

### awaitTermination()

```java
executor.shutdown();

boolean finished =
        executor.awaitTermination(10, TimeUnit.SECONDS);
```

Waits up to the timeout for termination.

Returns:

```text
true  → terminated
false → timeout elapsed
```

May throw `InterruptedException`.

### Lifecycle States

```text
RUNNING
   ↓ shutdown()
SHUTDOWN
   ↓ tasks finish
TERMINATED
```

Or:

```text
RUNNING
   ↓ shutdownNow()
STOPPING
   ↓ tasks exit
TERMINATED
```

### AutoCloseable

Since Java 19, `ExecutorService` supports `AutoCloseable`.

```java
try (ExecutorService executor =
        Executors.newFixedThreadPool(2)) {

    executor.submit(() -> System.out.println("A"));
    executor.submit(() -> System.out.println("B"));
}
```

The `close()` method initiates orderly shutdown and waits for tasks to complete.

Unlike a bare `shutdown()` call, leaving the try-with-resources block waits for termination.

### isShutdown() vs isTerminated()

```java
executor.isShutdown();
```

True once shutdown has been initiated.

```java
executor.isTerminated();
```

True once all tasks have completed following shutdown.

```text
SHUTDOWN
≠
TERMINATED
```

---

## Quick Reference

### Thread Creation

```java
new Thread(task).start();

Thread.ofPlatform().start(task);

Thread.ofVirtual().start(task);

Thread.startVirtualThread(task);
```

### Executor Factories

```java
Executors.newSingleThreadExecutor();

Executors.newFixedThreadPool(4);

Executors.newCachedThreadPool();

Executors.newWorkStealingPool();

Executors.newScheduledThreadPool(2);

Executors.newVirtualThreadPerTaskExecutor();
```

### Task Submission

```java
executor.execute(runnable);           // void

executor.submit(runnable);            // Future<?>

executor.submit(callable);            // Future<T>

executor.submit(runnable, result);    // Future<T>
```

### Future

```java
future.get();
future.get(1, TimeUnit.SECONDS);
future.isDone();
future.isCancelled();
future.cancel(true);
```

### Bulk Tasks

```java
executor.invokeAll(tasks);  // List<Future<T>>

executor.invokeAny(tasks);  // T
```

### Scheduling

```java
schedule(task, delay, unit);

scheduleAtFixedRate(task, initialDelay, period, unit);

scheduleWithFixedDelay(task, initialDelay, delay, unit);
```

### Lifecycle

```java
executor.shutdown();
executor.shutdownNow();
executor.awaitTermination(10, TimeUnit.SECONDS);
executor.isShutdown();
executor.isTerminated();
```

---

## Final Memory Kicks

```text
THREADS:

start()
→ NEW THREAD

run()
→ ORDINARY METHOD CALL

isVirtual()
→ THREAD TYPE
```

```text
VIRTUAL THREADS:

LIGHTWEIGHT
→ GOOD FOR BLOCKING I/O

newVirtualThreadPerTaskExecutor()
→ NEW VIRTUAL THREAD PER TASK
→ NOT A THREAD POOL
```

```text
EXECUTOR FACTORIES:

Single
→ ONE WORKER

Fixed
→ N WORKERS

Cached
→ GROWS AS NEEDED

WorkStealing
→ FORKJOIN

VirtualThreadPerTask
→ ONE VIRTUAL THREAD PER TASK
```

```text
TASKS:

Runnable.run()
→ VOID

Callable.call()
→ RESULT + CHECKED EXCEPTIONS

execute()
→ VOID

submit()
→ FUTURE
```

```text
FUTURE:

get()
→ WAIT

isDone()
→ FINISHED, FAILED OR CANCELLED

cancel(true)
→ MAY INTERRUPT
```

```text
BULK:

invokeAll()
→ ALL TASKS
→ LIST OF FUTURES

invokeAny()
→ ONE SUCCESSFUL RESULT
→ VALUE
```

```text
SCHEDULING:

Fixed RATE
→ START TO START

Fixed DELAY
→ FINISH TO START
```

```text
SHUTDOWN:

shutdown()
→ NO NEW TASKS

shutdownNow()
→ ATTEMPT INTERRUPTION

awaitTermination()
→ WAIT

isShutdown()
→ SHUTDOWN STARTED

isTerminated()
→ ALL TASKS FINISHED
```