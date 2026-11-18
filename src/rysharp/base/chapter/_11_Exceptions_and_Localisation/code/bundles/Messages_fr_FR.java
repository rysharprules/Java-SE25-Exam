
package rysharp.base.chapter._11_Exceptions_and_Localisation.code.bundles;

import java.util.ListResourceBundle;

public class Messages_fr_FR extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"hello", "Salut"},
                {"currency", "EUR"}
        };
    }
}
