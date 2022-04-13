package eb2translator.ebash2;

import eb2translator.FileUtils;
import eb2translator.ebash2.runtime.Runtime;

public class BaseImplementation {
    public static String getBaseImplementations() {
        String baseImplementations = "";

        for(String file : Runtime.base) {
            baseImplementations += "\n" + FileUtils.readFile(file);
        }

        // Remove comments
        baseImplementations = baseImplementations.replaceAll("#.*", "");
        
        return baseImplementations;
    }
}
