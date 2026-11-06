package rysharp.exam.actionplan.enthuware;

public class InterfaceQuestions implements IInt {

    public static void main(String[] args) {
        InterfaceQuestions s = new InterfaceQuestions();

        // Interface fields
        int j = s.thevalue;       // Valid
        int k = IInt.thevalue;    // Valid
        int l = thevalue;         // Valid

        // Interface static methods
        IInt.printValue();        // Valid

        // s.printValue();        // Compilation error
        // printValue();          // Compilation error
        // InterfaceQuestions.printValue(); // Compilation error
    }
}

interface IInt {
    int thevalue = 0; // Implicitly public static final

    static void printValue() {
        System.out.println(thevalue);
    }
}
