package eb2translator.ebash2;

import eb2translator.FileUtils;
import eb2translator.ebash2.runtime.Runtime;

public class BaseImplementation {

    // Select all of the files that ends with .ebbasesrc
    public static String getBaseImplementations() {
        String baseImplementations = "";

        for(String file : Runtime.base) {
            baseImplementations += "\n" + FileUtils.readFile(file);
        }

        // Remove comments
        baseImplementations = baseImplementations.replaceAll("#.*", "");
        
        baseImplementations = "#!/bin/bash" + baseImplementations;

        return baseImplementations;
    }
}
