
package rysharp.base.chapter._11_Exceptions_and_Localisation.code.bundles;

import java.util.ListResourceBundle;

public class Messages extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"hello", "Hello (default)"},
                {"goodbye", "Goodbye (default)"},
                {"currency", "GBP"},
                {"numbers", new int[] {1, 2, 3}}
        };
    }
}
