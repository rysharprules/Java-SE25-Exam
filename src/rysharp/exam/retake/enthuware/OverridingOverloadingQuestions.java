package rysharp.exam.retake.enthuware;

public class OverridingOverloadingQuestions {
    interface Flyer {
    }

    class AirPlane implements Flyer {
    }

    static void flyIt(Flyer f) {
        System.out.println("Flyer Flying");
    }

    static void flyIt(AirPlane a) {
        System.out.println("AirPlane Flying");
    }

    void main() {
        // OVERLOADING: compile-time selection based on declared argument types.
        // OVERRIDING: runtime selection based on the actual object's type.
        // Static methods can be overloaded or hidden, but not overridden.
        Flyer f = new AirPlane();
        AirPlane a = new AirPlane();
        flyIt(f);  //1
        flyIt(a);  //2


        A o1 = new C();
        B o2 = (B) o1;
        System.out.println(o1.m1()); // 30
        System.out.println(o2.i); // 20
    }

    class A {
        int i = 10;

        int m1() {
            return i;
        }
    }

    class B extends A {
        int i = 20;

        int m1() {
            return i;
        }
    }

    class C extends B {
        int i = 30;

        int m1() {
            return i;
        }
    }
}
