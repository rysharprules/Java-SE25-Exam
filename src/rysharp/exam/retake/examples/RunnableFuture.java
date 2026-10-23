import java.util.concurrent.*;
public class RunnableFuture {
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Future<?> a = pool.submit(() -> {});
            Future<String> b = pool.submit(() -> {}, "done");
            System.out.println(a.get()); // null
            System.out.println(b.get()); // done
        } finally { pool.shutdown(); }
    }
}
