package eb2translator.ebash2.struct;

import java.util.ArrayList;

import eb2translator.BasicArguments;
import eb2translator.FileUtils;

public class Script {
    public ArrayList<Class> importedClasses = new ArrayList<>();
    public ArrayList<String> importingPackages = new ArrayList<>();
    public String fileLocation = "";
    public String codeBody = "";

    public Script(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String toString() {
        ArrayList<Class> classes = new ArrayList<>();
        for (Class c : importedClasses) {
            classes.add(c);
            for(Class d : c.dependencies) {
                classes.add(d);
            }
        }

        // Remove duplicates
        ArrayList<Class> classes2 = new ArrayList<>();
        for (Class c : classes) {
            if (!classes2.contains(c)) {
                classes2.add(c);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        // Add imports
        for (Class c : classes2) {
            stringBuilder.append("# imported: " + c.packageName + "/" + c.name + "\n");
        }
        for (Class c : classes2) {
            for (Function f : c.functions) {
                stringBuilder.append(f.code + "\n");
            }
        }

        return stringBuilder.toString();
        
    }

    // Remove duplicated imported classes
    public void removeDuplicatedImports() {

        ArrayList<Class> importedClasses = new ArrayList<>();
        // For each imported class, will not copy to new arraylist if it is already in the arraylist
        for (Class importedClass : this.importedClasses) {
            if (!importedClasses.contains(importedClass)) {
                importedClasses.add(importedClass);
            }
        }
        
    }

    public static Script parseScript(String scriptFileLocation) {

        // Create a new script instance
        Script scriptObject = new Script(scriptFileLocation);

        // Get the script file
        String scriptFile = FileUtils.readFile(scriptFileLocation);
        String[] originalLines = scriptFile.split("\n");

        ArrayList<String> lines = new ArrayList<>();

        // Remove all comments (if configured)
        // While converting String[] to ArrayList<String>
        for (String line : originalLines) {
            if (BasicArguments.keepComments || !line.trim().startsWith("#")) {
                lines.add(line);
            }
        }

        // Remove all lines with @import,
        // and add the imported file to the list of imported files
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().startsWith("@import ")) {

                // Get the imported file location
                String line = lines.get(i);
                String importName = lines.get(i).substring(line.indexOf("@import ") + "@import ".length());

                // Add the imported file location to the list of imported files
                scriptObject.importingPackages.add(importName);

                // Load class
                ArrayList<Class> importedClasses = Package.loadClassesInPackage(importName);
                for (Class importedClass : importedClasses) {
                    scriptObject.importedClasses.add(importedClass);

                    // Import dependencies
                    for(Class dependency : importedClass.dependencies) {
                        scriptObject.importedClasses.add(dependency);
                    }
                }

                // Remove the line from the list
                lines.remove(i);
                i--;

            }
        }

        scriptObject.removeDuplicatedImports();

        // Build string from dependencies
        StringBuilder stringBuilder = new StringBuilder();
        for (Class c : scriptObject.importedClasses) {
            stringBuilder.append(c.toString());
        }

        // Build the code body
        StringBuilder codeBody = new StringBuilder();
        for (String line : lines) {
            codeBody.append(line + "\n");
        }

        // Set the code body
        scriptObject.codeBody = stringBuilder.toString() + codeBody.toString();

        return scriptObject;
    }

    public void optimize() {
        String[] lines = codeBody.split("\n");
        ArrayList<String> newLines = new ArrayList<>();

        // Remove all empty lines
        for (String line : lines) {
            if (line.trim().length() < 1) {
                continue;
            }else{
                newLines.add(line);
            }
        }


        // Build to string
        StringBuilder codeBody = new StringBuilder();
        for (String line : newLines) {
            codeBody.append(line + "\n");
        }

        // Set the code body
        this.codeBody = codeBody.toString();
    }
}
