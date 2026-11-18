# Concurrency

[🔙 Back](../../../../../README.md)

## 📚 Chapter Objectives
As per the [OCP Oracle Certified Professional Java SE 21 Developer Study Guide: Exam 1z0-830](https://www.amazon.co.uk/Oracle-Certified-Professional-Developer-Study/dp/1394286619) book:

>- **Managing Concurrent Code Execution**
>  - Create both platform and virtual threads. Use both Runnable and Callable objects, manage the thread lifecycle, and use different Executor services and concurrent API to run tasks.
>  - Develop thread-safe code, using locking mechanisms and concurrent API.
>  - Process Java collections concurrently and utilize parallel streams.
>- **Working with Streams and Lambda expressions**
>  - Perform decomposition, concatenation, and reduction, and grouping and partitioning on sequential and parallel streams.

## 🎯 Targeted Revision
AI generated guides based on areas I deem benefit from further explanation or JDK25 amendments.

Code packages match the naming of the md.

| Title | File |
|---|---|
| Synchronization, Locks & CyclicBarrier | [locks.md](locks.md) |
| Concurrent Collections | [concurrent_collections.md](concurrent_collections.md) |
| Executor Services & Threads | [executor_services_threads.md](executor_services_threads.md) |

## </> Code Examples
AI generated code examples with detailed comments.

| Example file | Support |
|---|---|
| [NewVirtualThreadPerTaskExecutorExample](code/NewVirtualThreadPerTaskExecutorExample.java) | [Support](code_support/NewVirtualThreadPerTaskExecutorExample.md) |
| [ExecutorServiceExample](code/ExecutorServiceExample.java) | - |

## Additional runnable examples

The following examples are organised by Java feature and can be opened directly in the source tree.

| Example | Source |
|---|---|
| `AtomicIntegerQuestions` | [code/practice/AtomicIntegerQuestions.java](code/practice/AtomicIntegerQuestions.java) |
| `RunnableFuture` | [code/practice/RunnableFuture.java](code/practice/RunnableFuture.java) |
| `ThreadInterruptQuestions` | [code/practice/ThreadInterruptQuestions.java](code/practice/ThreadInterruptQuestions.java) |
| `ThreadInterruptedExceptionQuestions` | [code/practice/ThreadInterruptedExceptionQuestions.java](code/practice/ThreadInterruptedExceptionQuestions.java) |
| `ThreadLockQuestions` | [code/practice/ThreadLockQuestions.java](code/practice/ThreadLockQuestions.java) |
| `ThreadStateQuestions` | [code/practice/ThreadStateQuestions.java](code/practice/ThreadStateQuestions.java) |
