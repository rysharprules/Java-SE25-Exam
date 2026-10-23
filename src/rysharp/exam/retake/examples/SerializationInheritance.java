import java.io.*;
public class SerializationInheritance {
    static class Game implements Serializable {
        private static final long serialVersionUID = 1L;
        String title;
        Game(String title) { this.title = title; }
    }
    static final class Chess extends Game {
        private static final long serialVersionUID = 1L;
        int players;
        Chess(String title, int players) { super(title); this.players = players; }
        @Override public String toString() { return title + " " + players; }
    }
    public static void main(String[] args) throws Exception {
        Game p = new Chess("Chess", 2);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) { out.writeObject(p); }
        Game restored;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            restored = (Game) in.readObject();
        }
        System.out.println(restored); // Chess 2: virtual toString
        System.out.println(((Chess) restored).players); // 2: cast needed for field
    }
}
