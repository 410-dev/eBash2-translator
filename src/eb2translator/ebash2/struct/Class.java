package eb2translator.ebash2.struct;

import java.util.ArrayList;

import eb2translator.FileUtils;

public class Class {
    public String classFileLocation;
    public String name;
    public String packageName;
    public ArrayList<Function> functions = new ArrayList<>();
    public ArrayList<Class> dependencies = new ArrayList<>();

    public Class(String packageName, String name, String classFileLocation) {
        this.packageName = packageName;
        this.name = name;
        this.classFileLocation = classFileLocation;
        parseFunctions();
        listDependencies();
    }

    public void parseFunctions() {
         functions = Function.parseAll(this, FileUtils.readFile(classFileLocation));
    }

    public void listDependencies() {

        // Read file
        String file = FileUtils.readFile(classFileLocation);
        String[] lines = file.split("\n");

        // Find @import lines
        for (String line : lines) {
            if (line.trim().startsWith("@import ")) {
                String importName = line.substring(line.indexOf("@import ") + "@import ".length());
                ArrayList<Class> importedClasses = Package.loadClassesInPackage(importName);
                for (Class importedClass : importedClasses) {
                    dependencies.add(importedClass);
                }
            }
        }

        // Add dependencies recursively
        ArrayList<Class> newlyGeneratedDependencyList = new ArrayList<>();
        for (Class dependency : dependencies) {
            ArrayList<Class> dependencies = dependency.dependencies;
            for (Class dependency2 : dependencies) {
                newlyGeneratedDependencyList.add(dependency2);
            }
        }
        dependencies.addAll(newlyGeneratedDependencyList);

        // Remove duplicates
        ArrayList<Class> dependencies = new ArrayList<>();
        for (Class dependency : this.dependencies) {
            if (!dependencies.contains(dependency)) {
                dependencies.add(dependency);
            }
        }

        this.dependencies = dependencies;
    }

    public boolean equals(Class c) {
        return this.name.equals(c.name) && this.packageName.equals(c.packageName);
    }

    public String toString() {
        // Get string of dependencies
        StringBuilder dependenciesString = new StringBuilder();
        if (dependencies.size() > 0) {
            dependenciesString.append("\n");
            for (Class dependency : dependencies) {
                System.out.println("Dependency: " + dependency.name);
                dependenciesString.append("\n" + dependency.toString());
            }
        }else{
            System.out.println("No dependencies for " + name);
        }

        // Get string of functions
        StringBuilder functionsString = new StringBuilder();
        if (functions.size() > 0) {
            functionsString.append("\n");
            for (Function function : functions) {
                System.out.println("Function: " + function.name);
                functionsString.append("\n" + function.code);
            }
        }else{
            System.out.println("No functions for " + name);
        }

        String codeBody = dependenciesString.toString() + functionsString.toString();
        return codeBody;
    }
}
