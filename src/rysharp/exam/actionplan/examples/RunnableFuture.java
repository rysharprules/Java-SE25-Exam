package rysharp.exam.actionplan.examples;

import java.util.concurrent.*;

// Q15 POST_EXAM_REPORT.md
public class RunnableFuture {
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Future<?> a = pool.submit(() -> {});
            Future<String> b = pool.submit(() -> {}, "done");
            System.out.println(a.get()); // null
            System.out.println(b.get()); // done
        } finally { pool.shutdown(); }
        IO.println("fini");
    }
}
