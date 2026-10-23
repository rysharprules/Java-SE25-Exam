import java.time.*;
public class DaylightSaving {
    public static void main(String[] args) {
        ZonedDateTime first = ZonedDateTime.of(2024,11,3,1,30,0,0,ZoneId.of("America/Chicago"));
        ZonedDateTime second = first.plusHours(1);
        System.out.println(first);  // 01:30 -05:00
        System.out.println(second); // 01:30 -06:00
        System.out.println(first.equals(second)); // false
        System.out.println(first.toInstant().equals(second.toInstant())); // false
    }
}
