package rysharp.exam.actionplan.enthuware;

import java.nio.file.Path;
import java.nio.file.Paths;

public class IOPathQuestions {
    void main (){
        q1();
    }

    static void q1() {
        // normalize() operates on the path's syntax. It doesn't check whether the file exists.

        // On Windows, normalize() removes the .. immediately after the drive root because there's nowhere further up the path to go.
        System.out.println("C:\\..\\..\\temp > " + Paths.get("C:\\..\\..\\temp").normalize());
        // C:\temp

        System.out.println("/..\\../temp > " + Paths.get("/..\\../temp").normalize());
        // \temp

        System.out.println("\\..\\..\\temp > " + Paths.get("\\..\\..\\temp").normalize());
        // \temp

        //Paths.get("\\..\\C:\\..\\..\\temp");
        // java.nio.file.InvalidPathException: Illegal char <:> at index 6: \..\C:\..\..\temp (thrown by Paths.get)

        System.out.println("../../temp > " + Paths.get("../../temp"));
        // ..\..\temp

        System.out.println("..\\..\\temp > " + Paths.get("..\\..\\temp").normalize());
        // ..\..\temp

        System.out.println("C:\\doesNotExist\\..\\file.txt > "
                + Paths.get("C:\\doesNotExist\\..\\file.txt").normalize());
        // C:\file.txt

        Path p1 = Path.of("c:\\..\\temp\\test.txt");
        // toUri() converts the path into a URI; it doesn't require the target file to exist.
        System.out.println("c:\\..\\temp\\test.txt > " + p1.normalize().toUri()); // file:///c:/temp/test.txt
    }
}
