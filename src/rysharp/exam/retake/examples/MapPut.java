import java.util.*;
public class MapPut {
    public static void main(String[] args) {
        Map<String,Double> map = new HashMap<>();
        System.out.println(map.put("cookie", 2.99)); // null
        System.out.println(map.put("cookie", 3.99)); // 2.99
        System.out.println(map.get("cookie"));       // 3.99
        System.out.println(map.size());              // 1
    }
}
