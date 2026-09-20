# Module Import Declarations

`import module`

Java 25 allows:

```
import module java.base;
```

This makes the **accessible types exported by that module** available for simple-name use.

For example:

```
import module java.base;

class Test {
List<String> names = new ArrayList<>();
LocalDate date = LocalDate.now();
}
```

No need for:

```
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
```

because those packages are exported by `java.base`.

## The important catch

A module import **doesn't magically make every type in the module available**.

It is subject to the module's **exports**.

## `java.base` exports

| Package                | Think of it as          | Common types                                                       |
| ---------------------- | ----------------------- | ------------------------------------------------------------------ |
| `java.lang`            | Core Java               | `String`, `Object`, `Math`, `Integer`, `System`, `Exception`       |
| `java.util`            | Collections & utilities | `List`, `Set`, `Map`, `ArrayList`, `HashMap`, `Arrays`, `Optional` |
| `java.util.function`   | Functional interfaces   | `Function`, `Predicate`, `Consumer`, `Supplier`, `UnaryOperator`   |
| `java.util.stream`     | Streams                 | `Stream`, `IntStream`, `Collectors`                                |
| `java.io`              | Traditional I/O         | `File`, `InputStream`, `OutputStream`, `Reader`, `Writer`          |
| `java.nio.file`        | Modern file/path API    | `Path`, `Paths`, `Files`                                           |
| `java.time`            | Date/time               | `LocalDate`, `LocalTime`, `LocalDateTime`, `Duration`, `Period`    |
| `java.util.concurrent` | Concurrency utilities   | `ExecutorService`, `Executors`, `Future`, `ConcurrentHashMap`      |
| `java.util.regex`      | Regular expressions     | `Pattern`, `Matcher`                                               |
| `java.lang.annotation` | Annotations             | `@Override`, `@Deprecated`, `Annotation`                           |
| `java.lang.reflect`    | Reflection              | `Method`, `Field`, `Constructor`                                   |

