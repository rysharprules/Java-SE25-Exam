
# Synchronization, Locks & CyclicBarrier

[🔙 Back](README.md)

## 1. synchronized

Java's built-in locking mechanism. Only one thread can hold a particular monitor at a time.

### Instance vs Static

| Declaration | Lock acquired |
|---|---|
| `synchronized void method()` | Current object (`this`) |
| `static synchronized void method()` | Class object (`MyClass.class`) |
| `synchronized (obj)` | Specified object's monitor |
| `synchronized (MyClass.class)` | Class monitor |

```java
class Counter {
    synchronized void increment() {
        // Locks this Counter instance
    }

    static synchronized void reset() {
        // Locks Counter.class
    }
}
```

- Two threads calling synchronized methods on the **same instance** cannot execute those methods simultaneously.
- Different instances have different monitors.
- Static synchronized methods share one class monitor.
- Instance and static synchronized methods use different monitors.
- A thread can reacquire a monitor it already owns (reentrant).
- Monitor release is automatic when the synchronized method/block exits, including on exception.
- `synchronized` does not guarantee fairness.

### synchronized Blocks

```java
synchronized (lock) {
    // Critical section
}
```

- Lock expression must be a reference type, not a primitive.
- A null lock reference causes `NullPointerException`.
- `wait()`, `notify()` and `notifyAll()` require ownership of that object's monitor.
- `wait()` releases the monitor while waiting; `sleep()` does not.
- Acquiring an intrinsic monitor is not interruptible via `InterruptedException`.

---

## 2. ReentrantLock

`java.util.concurrent.locks.ReentrantLock`

An explicit, reentrant mutual-exclusion lock.

```java
ReentrantLock lock = new ReentrantLock();

lock.lock();
try {
    // Critical section
} finally {
    lock.unlock();
}
```

**Always release in `finally`.** Unlike `synchronized`, unlocking is manual.

### Important Methods

| Method | Behaviour |
|---|---|
| `lock()` | Waits until acquired; not interruptible while waiting |
| `lockInterruptibly()` | Interruptible lock acquisition; throws `InterruptedException` |
| `tryLock()` | Attempts immediately; returns `boolean` |
| `tryLock(timeout, unit)` | Waits up to timeout; interruptible |
| `unlock()` | Releases one hold; must be owned by current thread |
| `isLocked()` | Whether any thread holds the lock |
| `isHeldByCurrentThread()` | Whether calling thread owns it |
| `getHoldCount()` | Current thread's reentrant hold count |

### tryLock()

```java
if (lock.tryLock()) {
    try {
        // Critical section
    } finally {
        lock.unlock();
    }
}
```

Never call `unlock()` unless the current thread successfully acquired the lock.

### Reentrancy

```java
lock.lock();
lock.lock();

System.out.println(lock.getHoldCount()); // 2

lock.unlock();
lock.unlock();
```

Each successful acquisition requires a corresponding release.

### Fairness

```java
new ReentrantLock(true); // Fairness requested
new ReentrantLock();     // Nonfair (default)
```

- Fair mode generally favours longest-waiting threads under contention.
- Does not guarantee strict scheduling order.
- Untimed `tryLock()` can acquire a fair lock even when other threads are waiting.

---

## 3. ReadWriteLock

`java.util.concurrent.locks.ReadWriteLock`

Allows multiple readers or one writer.

The usual implementation is `ReentrantReadWriteLock`.

```java
ReadWriteLock rw = new ReentrantReadWriteLock();

Lock read = rw.readLock();
Lock write = rw.writeLock();
```

### Compatibility

| Existing lock | New reader | New writer |
|---|---|---|
| No lock | Allowed | Allowed |
| Read lock | Allowed | Blocked |
| Write lock | Blocked | Blocked |

The owning writer can also acquire read or write locks reentrantly.

### Read Lock

```java
read.lock();
try {
    // Read shared data
} finally {
    read.unlock();
}
```

Multiple threads can hold read locks concurrently.

### Write Lock

```java
write.lock();
try {
    // Modify shared data
} finally {
    write.unlock();
}
```

A writer requires exclusive access.

### Key Rules

- Multiple readers are allowed.
- A writer excludes other threads' readers and writers.
- Read and write locks must be released manually.
- Write-to-read downgrading is supported.
- Read-to-write upgrading is not supported directly; attempting to acquire the write lock while retaining a read lock can block indefinitely.
- `ReentrantReadWriteLock` is reentrant.
- Default fairness is nonfair; a fair constructor is available.

---

## 4. CyclicBarrier

`java.util.concurrent.CyclicBarrier`

Coordinates a fixed number of threads at a common meeting point.

```java
CyclicBarrier barrier = new CyclicBarrier(3);
```

Three participants must reach `await()` before they can proceed.

### Example

```java
CyclicBarrier barrier = new CyclicBarrier(3);

Runnable task = () -> {
    try {
        System.out.println("Before barrier");
        barrier.await();
        System.out.println("After barrier");
    } catch (InterruptedException | BrokenBarrierException e) {
        Thread.currentThread().interrupt();
    }
};
```

Run three instances of the task concurrently to release the barrier.

### Barrier Flow

```text
THREAD A ───── await() ──┐
THREAD B ───── await() ──┼── ALL 3 ARRIVE ── CONTINUE
THREAD C ───── await() ──┘
```

- `await()` blocks until all parties arrive.
- The barrier is reusable after successful release.
- `getParties()` returns the fixed number of participants.
- `getNumberWaiting()` returns the current number waiting.
- `isBroken()` reports whether the barrier is broken.
- `reset()` resets the barrier; existing waiters receive `BrokenBarrierException`.

### Barrier Action

```java
CyclicBarrier barrier = new CyclicBarrier(
        3,
        () -> System.out.println("All arrived")
);
```

The optional barrier action executes once per successful cycle, by the last thread to arrive, before waiting threads are released.

### Important Exceptions

| Exception | Cause |
|---|---|
| `InterruptedException` | Waiting thread interrupted |
| `BrokenBarrierException` | Barrier broken by interruption, timeout, reset or barrier-action failure |
| `TimeoutException` | Timed `await()` expires |

A timeout or interruption can break the barrier, causing other waiting threads to receive `BrokenBarrierException`.

### Thread Pool Trap

```java
ExecutorService executor = Executors.newFixedThreadPool(2);
CyclicBarrier barrier = new CyclicBarrier(3);
```

If three tasks are submitted but only two workers exist, the first two may block at `await()`, preventing the third task from starting.

```text
WORKER 1 → await() → BLOCKED
WORKER 2 → await() → BLOCKED

QUEUE    → TASK 3 → NEVER STARTS
```

This can deadlock indefinitely without timeout, interruption or other intervention.

Calling `shutdown()` does not interrupt the blocked workers.

---

## 5. Quick Comparison

| Feature | synchronized | ReentrantLock | ReadWriteLock | CyclicBarrier |
|---|---|---|---|---|
| Purpose | Mutual exclusion | Explicit mutual exclusion | Shared reads, exclusive writes | Thread coordination |
| Multiple readers | No special support | No | Yes | N/A |
| Reentrant | Yes | Yes | Yes (implementation) | N/A |
| Manual unlock | No | Yes | Yes | No |
| Timed acquisition | No | Yes | Yes | Timed `await()` |
| Interruptible waiting | Not for monitor entry | `lockInterruptibly()` | Supported by lock implementations | `await()` |
| Reusable | Yes | Yes | Yes | Yes |

## 6. Final Memory Kicks

- **Instance synchronized** → locks `this`.
- **Static synchronized** → locks `ClassName.class`.
- **ReentrantLock** → explicit lock; `unlock()` in `finally`.
- **tryLock()** → returns `boolean`, does not automatically block.
- **ReadWriteLock** → many readers OR one writer.
- **CyclicBarrier** → all participants must arrive.
- **Barrier interruption/timeout** → barrier may break and release others with exceptions.
- **Too few pool workers for barrier participants** → potential deadlock.
