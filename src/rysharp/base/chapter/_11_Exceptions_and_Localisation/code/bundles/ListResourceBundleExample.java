
package rysharp.base.chapter._11_Exceptions_and_Localisation.code.bundles;

import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ListResourceBundleExample {

    private static final String BASE_NAME =
            "rysharp.base.chapter._11_Exceptions_and_Localisation.code.bundles.Messages";

    public static void main(String[] args) {
        example1_localeFallback();
        example2_rootBundle();
        example3_objectValues();
        example4_missingKeys();
        example5_examTraps();
    }

    // 1. Locale fallback happens per key through the parent chain
    private static void example1_localeFallback() {
        System.out.println("\n1. Locale fallback");

        ResourceBundle bundle =
                ResourceBundle.getBundle(BASE_NAME, Locale.FRANCE);

        System.out.println(bundle.getString("hello"));
        // Salut - Messages_fr_FR

        System.out.println(bundle.getString("goodbye"));
        // Au revoir - Messages_fr

        System.out.println(bundle.getString("currency"));
        // EUR - Messages_fr_FR

        System.out.println(bundle.getLocale());
        // fr_FR

        // Selected bundle hierarchy:
        //
        // Messages_fr_FR
        //       |
        // Messages_fr
        //       |
        // Messages
        //
        // Missing keys are searched through this hierarchy.
        // They do not trigger a new default-locale lookup.
    }

    // 2. Root bundle
    private static void example2_rootBundle() {
        System.out.println("\n2. Root bundle");

        ResourceBundle bundle =
                ResourceBundle.getBundle(BASE_NAME, Locale.ROOT);

        System.out.println(bundle.getString("hello"));
        // Hello (default)

        System.out.println(bundle.getString("goodbye"));
        // Goodbye (default)

        System.out.println(bundle.getLocale());
        // Empty string for Locale.ROOT
    }

    // 3. ListResourceBundle can contain objects, not just Strings
    private static void example3_objectValues() {
        System.out.println("\n3. Object values");

        ResourceBundle bundle =
                ResourceBundle.getBundle(BASE_NAME, Locale.FRANCE);

        int[] numbers = (int[]) bundle.getObject("numbers");

        System.out.println(Arrays.toString(numbers));
        // [1, 2, 3]

        // The key "numbers" is absent from fr_FR and fr.
        // Java retrieves it from the root bundle.
        //
        // getString() expects a String value.
        // getObject() can retrieve other object types.
    }

    // 4. Missing keys
    private static void example4_missingKeys() {
        System.out.println("\n4. Missing keys");

        ResourceBundle bundle =
                ResourceBundle.getBundle(BASE_NAME, Locale.FRANCE);

        try {
            System.out.println(bundle.getString("unknown"));
        } catch (MissingResourceException e) {
            System.out.println("Missing key: unknown");
        }

        // MissingResourceException is unchecked.
        // A key must exist in the selected bundle or its parents.
    }

    // 5. Certification traps
    private static void example5_examTraps() {
        System.out.println("\n5. Exam traps");

        // ListResourceBundle:
        //
        // - Is an abstract class extending ResourceBundle.
        // - Requires overriding getContents().
        // - getContents() returns Object[][].
        // - Each inner array contains a key and a value.
        // - Keys are Strings.
        // - Values can be Strings or other Objects.
        //
        // Correct:
        //
        // {"hello", "Bonjour"}
        // {"count", 42}
        // {"values", new int[] {1, 2, 3}}
        //
        // getString("hello") -> String
        // getObject("count") -> Object (Integer)
        //
        // ResourceBundle.getBundle() returns ResourceBundle,
        // regardless of whether the backing bundle is a
        // .properties file or a ListResourceBundle subclass.
        //
        // Class-based bundles take precedence over properties
        // files with the same base name and locale.
        //
        // ResourceBundle selection happens at getBundle().
        // Key fallback happens at getString()/getObject().
    }
}
