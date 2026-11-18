
package rysharp.base.chapter._14_Input_Output.code.serialization;

import java.io.*;

public class SerializationExample {

    public static void main(String[] args) throws Exception {
        example1_basicSerialization();
        example2_transientAndStaticFields();
        example3_recordDeserialization();
        example4_enumDeserialization();
        example5_nonSerializableObjectGraph();
    }

    // 1. Serializable subclass constructors are bypassed during deserialization.
    private static void example1_basicSerialization() throws Exception {
        System.out.println("\n1. Ordinary Serializable class");

        Child original = new Child("Alice");
        byte[] bytes = serialize(original);

        System.out.println("Deserializing:");
        Child restored = deserialize(bytes, Child.class);
        System.out.println("Restored name: " + restored.name);

        // Parent constructor runs during deserialization.
        // Child constructor does not.
    }

    static class Parent {
        Parent() {
            System.out.println("Parent constructor");
        }
    }

    static class Child extends Parent implements Serializable {
        private static final long serialVersionUID = 1L;

        String name;

        Child(String name) {
            System.out.println("Child constructor");
            this.name = name;
        }
    }

    // 2. Default serialization excludes transient and static fields.
    private static void example2_transientAndStaticFields() throws Exception {
        System.out.println("\n2. transient and static fields");

        Session.sharedCount = 10;
        Session original = new Session("Alice", "TOKEN");

        byte[] bytes = serialize(original);

        Session.sharedCount = 99;

        Session restored = deserialize(bytes, Session.class);

        System.out.println("username: " + restored.username);       // Alice
        System.out.println("token: " + restored.token);             // null
        System.out.println("sharedCount: " + Session.sharedCount);  // 99
    }

    static class Session implements Serializable {
        private static final long serialVersionUID = 1L;

        static int sharedCount;
        String username;
        transient String token;

        Session(String username, String token) {
            this.username = username;
            this.token = token;
        }
    }

    // 3. Records invoke their canonical constructor during deserialization.
    private static void example3_recordDeserialization() throws Exception {
        System.out.println("\n3. Serializable record");

        Person original = new Person(35, "Alice");
        byte[] bytes = serialize(original);

        System.out.println("Deserializing:");
        Person restored = deserialize(bytes, Person.class);

        System.out.println("Restored: " + restored);

        // The canonical constructor executes again during deserialization.
        // The additional no-arg constructor is not called.
    }

    record Person(int age, String name) implements Serializable {

        Person {
            System.out.println("Person canonical constructor");
        }

        Person() {
            this(0, "");
            System.out.println("Person no-arg constructor");
        }
    }

    // 4. Enum constants are resolved by name, not reconstructed.
    private static void example4_enumDeserialization() throws Exception {
        System.out.println("\n4. Enum serialization");

        Gender original = Gender.MALE;
        // First use initializes ALL enum constants.

        byte[] bytes = serialize(original);

        System.out.println("Deserializing:");
        Gender restored = deserialize(bytes, Gender.class);

        System.out.println("Same enum instance: " + (restored == original));
        // true

        // No enum constructors execute during readObject():
        // the enum is already initialized.
        //
        // In a fresh JVM, deserialization can trigger enum class
        // initialization, which constructs all enum constants.
    }

    enum Gender {
        MALE("M"), FEMALE("F"), OTHER;

        Gender() {
            System.out.println("Gender no-arg constructor");
        }

        Gender(String code) {
            System.out.println("Gender String constructor");
        }
    }

    // 5. Serializable objects require serializable referenced objects.
    private static void example5_nonSerializableObjectGraph() throws Exception {
        System.out.println("\n5. Non-serializable object graph");

        Order order = new Order(new Item("Book"));

        try {
            serialize(order);
            System.out.println("Unexpected success");
        } catch (NotSerializableException e) {
            System.out.println("Expected: " + e.getClass().getSimpleName());
        }
    }

    static class Order implements Serializable {
        private static final long serialVersionUID = 1L;

        Item item;

        Order(Item item) {
            this.item = item;
        }
    }

    static class Item {
        String title;

        Item(String title) {
            this.title = title;
        }
    }

    // Shared serialization helpers.
    private static byte[] serialize(Object value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(value);
        }

        return bytes.toByteArray();
    }

    private static <T> T deserialize(byte[] bytes, Class<T> type)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bytes))) {

            return type.cast(in.readObject());
        }
    }
}
