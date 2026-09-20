package rysharp.jdk21.base.chapter._06;

class Person {
     static String name;
     void setName(String q) { name = q; } }

  public class Child extends Person {
    // Since variables are only hidden, not overridden, there are two distinct name variables accessible, depending on the location and reference type
     static String name;
     void setName(String w) { name = w; }
    public static void main(String[] p) {
             final Child m = new Child(); // creates a Child instance
             final Person t = m; // which is implicitly cast to a Person reference type
              m.name = "Elysia"; //  uses the Child reference type, updating Child.name to Elysia
              t.name = "Sophia"; // uses the Person reference type, updating Person.name to Sophia
              m.setName("Webby"); // calls the overridden setName() instance method line 10 - Child.name to Webby
             t.setName("Olivia"); // calls the overridden setName() instance method line 10 - and then to Olivia
            System.out.println(m.name + " " + t.name); // Child.name and Person.name prints "Olivia Sophia"
            } }