package eb2translator;

import eb2translator.ebash2.BaseImplementation;
import eb2translator.ebash2.struct.Script;

public class Translate {
    public static void translate() {

        System.out.println("Generating base implementations...");
        String baseImplementation = BaseImplementation.getBaseImplementations();

        // For each compilable file
        for(String scriptPath : FileUtils.allFiles) {

            // Create a new script instance
            Script scriptObject = Script.parseScript(scriptPath);
            scriptObject.codeBody = baseImplementation + scriptObject.codeBody;
            scriptObject.optimize();

            // Write to file
            FileUtils.writeFile(InputOutputLocator.getOutputPath(scriptPath), scriptObject.codeBody);
            System.out.println("Exporting: " + InputOutputLocator.getOutputPath(scriptPath));
        }
    }
}
