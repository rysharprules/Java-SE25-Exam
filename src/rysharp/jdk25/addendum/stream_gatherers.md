# Stream Gatherers (Introduced in Java 24)

Stream Gatherers (`java.util.stream.Gatherer`) provide a way to define **custom intermediate operations** in a Stream pipeline.

> Think of `Stream::gather(Gatherer)` as the **intermediate** equivalent to `Stream::collect(Collector)`.

## Why use Stream Gatherers?

Existing intermediate operations (`map`, `filter`, `flatMap`) are powerful but can be limited. Gatherers allow for:
*   **More flexible transformations:** One-to-one, one-to-many, many-to-one, or many-to-many.
*   **Stateful operations:** Can track previously seen elements to influence later ones.
*   **Infinite stream support:** Can convert infinite streams into finite ones (short-circuiting).
*   **Parallel execution:** Designed to work well with parallel streams.

## Key Concepts

*   **`Stream::gather(Gatherer)`:** The new intermediate operation.
*   **`java.util.stream.Gatherers`:** A factory class containing common built-in gatherers (e.g., `windowFixed`, `windowSliding`).

## Built-in Gatherers

The `Gatherers` class provides common implementations:

### `windowFixed(int size)`
Groups stream elements into lists of a fixed size. Useful for batch processing.

```java
// Example: Batch processing
customers.stream()
    .gather(Gatherers.windowFixed(100))
    .forEach(batch -> database.insertBatch(batch));
```

### `windowSliding(int size)`
Similar to `windowFixed`, but windows overlap.

## Exam Tips

*   **Intermediate vs Terminal:** Gatherers are **intermediate operations**.
*   **Not for Primitives:** Similar to `Collectors`, Gatherers are not available for primitive streams (`IntStream`, `LongStream`, `DoubleStream`).
*   **Chainability:** They can be chained using `andThen()` or multiple `.gather()` calls.
*   **Parallelism:** Unlike some custom operations, Gatherers are built to handle `parallel()` streams effectively.
