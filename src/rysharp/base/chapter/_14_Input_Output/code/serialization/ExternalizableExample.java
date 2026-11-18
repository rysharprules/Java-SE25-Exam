
package rysharp.base.chapter._14_Input_Output.code;

import java.io.*;

public class ExternalizableExample {

    public static void main(String[] args) throws Exception {
        example1_basicExternalization();
        example2_transientFields();
        example3_examTraps();
    }

    // 1. Basic Externalizable - PE4 Q22
    private static void example1_basicExternalization() throws Exception {
        System.out.println("\n1. Basic Externalizable");

        Person original = new Person("Ryan", 35);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        // Serialization calls writeExternal().
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(original);
        }

        // Deserialization calls:
        // 1. The PUBLIC no-argument constructor.
        // 2. readExternal().
        Person restored;

        try (ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bytes.toByteArray()))) {

            restored = (Person) in.readObject();
        }

        System.out.println(restored);
        // Person{name='Ryan', age=35}
    }

    public static class Person implements Externalizable {

        private String name;
        private int age;

        // REQUIRED for Externalizable deserialization:
        // An accessible PUBLIC no-argument constructor.
        public Person() {
            System.out.println("Person no-arg constructor");
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // REQUIRED: public writeExternal(ObjectOutput)
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            System.out.println("writeExternal called");

            out.writeUTF(name);
            out.writeInt(age);
        }

        // REQUIRED: public readExternal(ObjectInput)
        @Override
        public void readExternal(ObjectInput in)
                throws IOException, ClassNotFoundException {

            System.out.println("readExternal called");

            name = in.readUTF();
            age = in.readInt();
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    // 2. Externalizable and transient fields
    private static void example2_transientFields() throws Exception {
        System.out.println("\n2. Externalizable and transient");

        Account original = new Account("Alice", "SECRET");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(original);
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bytes.toByteArray()))) {

            Account restored = (Account) in.readObject();

            System.out.println(restored.username); // Alice
            System.out.println(restored.password); // SECRET
        }

        // IMPORTANT:
        // With Externalizable, YOU control which fields are written.
        //
        // Even a transient field can be serialized explicitly
        // using writeExternal().
        //
        // In a real application, avoid writing secrets like passwords.
    }

    public static class Account implements Externalizable {

        private String username;
        private transient String password;

        public Account() {
        }

        public Account(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(username);
            out.writeUTF(password);
        }

        @Override
        public void readExternal(ObjectInput in)
                throws IOException, ClassNotFoundException {

            username = in.readUTF();
            password = in.readUTF();
        }
    }

    // 3. Common certification traps - PE4 Q22
    private static void example3_examTraps() {
        System.out.println("\n3. Exam traps");

        // Externalizable extends Serializable.
        //
        // Serializable:
        // - Marker interface (no abstract methods).
        // - Fields are serialized automatically by default.
        // - Can customize using private writeObject/readObject.
        // - Does not require a public no-arg constructor.
        //
        // Externalizable:
        // - Requires public writeExternal(ObjectOutput).
        // - Requires public readExternal(ObjectInput).
        // - Requires a public no-arg constructor for deserialization.
        // - Fields are NOT serialized automatically.
        // - You decide exactly what to write and read.
        //
        // IMPORTANT:
        // private writeObject/readObject are NOT replacements
        // for writeExternal/readExternal.
        //
        // The order of reading must match the order of writing.

        // This does NOT compile:
        //
        // class Bad implements Externalizable {
        //
        //     private void writeExternal(ObjectOutput out) { }
        //
        //     private void readExternal(ObjectInput in) { }
        //
        // }
        //
        // Interface methods must be implemented as public.

        // This CAN compile, but deserialization will fail:
        //
        // public class Bad implements Externalizable {
        //
        //     public Bad(String value) { }
        //
        //     public void writeExternal(ObjectOutput out)
        //             throws IOException { }
        //
        //     public void readExternal(ObjectInput in)
        //             throws IOException, ClassNotFoundException { }
        //
        // }
        //
        // Missing public no-arg constructor.

        // PE4 Q22:
        //
        // When Externalizable is implemented, the relevant
        // serialization hooks are writeExternal/readExternal,
        // NOT private writeObject/readObject.
    }
}
