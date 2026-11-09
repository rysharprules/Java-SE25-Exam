
package rysharp.exam.actionplan.enthuware;

import java.util.*;

public class GenericsOverrideQuestions {

    void main() {
        Q1();
        Q2();
        Q3();
        Q4();
    }

    // Q1 - Covariant return types.
    // An overriding method can return a MORE SPECIFIC type.

    static void Q1() {
        class Base {
            public List<Number> transform(Set<Integer> values) {
                return new ArrayList<>(List.of(10, 20));
            }
        }

        class Derived extends Base {
            @Override
            public ArrayList<Number> transform(Set<Integer> values) {
                return new ArrayList<>(List.of(30, 40));
            }
        }

        Base obj = new Derived();
        System.out.println("Q1: " + obj.transform(Set.of(1)));
        // [30, 40]
    }


    // Q2 - Invalid covariant return (ME13 Q38).
    // ArrayList<Number> -> List<Number> is WIDENING, not narrowing.

    static void Q2() {
        class Base {
            public ArrayList<Number> transform(Set<Integer> values) {
                return new ArrayList<>();
            }
        }

        class Derived extends Base {

            // Uncomment to see the compilation error.

            /*
            @Override
            public List<Number> transform(Set<Integer> values) {
                return new ArrayList<>();
            }
            */
        }

        System.out.println("Q2: List is not a subtype of ArrayList");
    }


    // Q3 - Raw parameter overriding (ME13 Q38).
    // Set and Set<Integer> have the same erasure.
    // A raw Set parameter can override the parameterized version.

    static void Q3() {
        class Base {
            public ArrayList<Number> transform(Set<Integer> values) {
                return new ArrayList<>(List.of(1, 2));
            }
        }

        class Derived extends Base {
            @Override
            public ArrayList<Number> transform(Set values) {
                return new ArrayList<>(List.of(3, 4));
            }
        }

        Base obj = new Derived();
        System.out.println("Q3: " + obj.transform(Set.of(10)));
        // [3, 4]
    }


    // Q4 - Overloading vs overriding (ME13 Q34).
    // Changing Collection<T> to List<T> changes the parameter type.
    // This is an OVERLOAD, not an override.

    static void Q4() {
        class Base {
            public <T extends CharSequence>
            Collection<String> transform(Collection<T> values) {
                return List.of("Base");
            }
        }

        class Derived extends Base {

            public <T extends CharSequence>
            Collection<T> transform(List<T> values) {
                return values;
            }

            // INVALID: super cannot be used in a type parameter bound.
            //
            // public <T super String>
            // Collection<T> transform(List<String> values) {
            //     return new ArrayList<>();
            // }

            // VALID wildcard syntax (not added as another overload):
            //
            // List<? super String> values;
        }

        Derived obj = new Derived();

        Collection<String> collection = new ArrayList<>(List.of("A"));
        List<String> list = new ArrayList<>(List.of("B"));

        System.out.println("Q4 Collection: " + obj.transform(collection));
        // [Base]

        System.out.println("Q4 List: " + obj.transform(list));
        // [B]

        collection = list;
        System.out.println("Q4 Collection with ArrayList ref: " + obj.transform(collection));

        Collection collection2 = List.of(1,2,3,4,5);
        System.out.println("Q4 Collection List (non-generic) Integer: " + obj.transform(collection2));

//        Collection<Integer> collection3 = Set.of(1,2,3,4,5);
//        System.out.println("Q4 Collection Set<Integer>: " + obj.transform(collection3));// Cannot resolve method 'transform(Collection<Integer>)'
//
//        ArrayList<Double> arrayList = new ArrayList<>();
//        arrayList.add(1.0);
//        System.out.println("Q4 ArrayList <Double>: " + obj.transform(arrayList));// Cannot resolve method 'transform(ArrayList<Double>)'
        Base base = new Derived();
        // Although base refers to a Derived, the compiler only sees the methods available through Base, so the List<T> overload isn't considered.
        List<String> values = new ArrayList<>(List.of("Hello"));
        System.out.println(base.transform(values)); // [Base]
    }
}
