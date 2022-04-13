package eb2translator;

import java.util.ArrayList;

import eb2translator.ebash2.BaseImplementation;
import eb2translator.ebash2.struct.EBClass;

public class Translate {
    public static void translate() {
        System.out.println("Building to string...");

        for(String scriptPath : FileUtils.allFiles) {

            System.out.println("Translating " + InputOutputLocator.getOutputPath(scriptPath) + "...");


            StringBuilder stringBuilder = new StringBuilder();
            String script[] = FileUtils.readFile(scriptPath).split("\n");

            stringBuilder.append("#!/bin/bash");
            stringBuilder.append(BaseImplementation.getBaseImplementations());

            for(String line : script) {

                // Omit comment
                if(line.trim().startsWith("#")) {
                    continue;
                }

                // Omit empty line
                if(line.trim().isEmpty()) {
                    continue;
                }

                // Detect @import
                if(line.startsWith("@import ")) {
                    // Identify the name of the package
                    String importingPackage = line.replace("@import ", "");

                    // Import packages
                    ArrayList<EBClass> importedClasses = new ArrayList<>();

                    // Import master
                    importedClasses = Classes.allFromMasterPackage(importingPackage);
                    
                    // If the length is 0, it means that the line has specified the class
                    if(importedClasses.size() == 0) {
                        // Get the specific name
                        String specificName = "/lib/" + importingPackage + ".ebsrc";

                        // Search from loaded class
                        importedClasses.add(Classes.getClass(specificName));

                        System.out.println("Importing " + specificName + "...");
                    }else{
                        System.out.println("Package included: " + importingPackage);
                    }

                    // Check if length is 0
                    if(importedClasses.size() == 0) {
                        System.err.println("Package not found: " + importingPackage);
                        System.exit(9);
                    }

                    // Build string
                    for(int i = 0; i < importedClasses.size(); i++) {
                        EBClass e = importedClasses.get(i);
                        stringBuilder.append(e.toString());
                    }

                    continue;
                }

                // Append line
                stringBuilder.append(line + "\n");
            }

            // Write to file
            FileUtils.writeFile(InputOutputLocator.getOutputPath(scriptPath), stringBuilder.toString());

            System.out.println("Translated " + InputOutputLocator.getOutputPath(scriptPath) + "...");
        }
    }
}
