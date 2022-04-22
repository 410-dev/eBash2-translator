package eb2translator.ebash2.struct;

import java.io.File;
import java.util.ArrayList;

import eb2translator.Main;

public class Package {
    public static ArrayList<Class> loadClassesInPackage(String packageName) {
        ArrayList<Class> classes = new ArrayList<>();

        // Get class files
        ArrayList<String> classFiles = listClassesInPackage(packageName);

        // For each class file
        for (String classFile : classFiles) {
            // Extract class name by getting file name only
            String className = new File(classFile).getName();

            // Create a new class instance
            Class classObject = new Class(packageName, className, classFile);

            classes.add(classObject);
        }

        return classes;
    }


    public static ArrayList<String> listClassesInPackage(String packageName) {
        ArrayList<String> classFiles = new ArrayList<>();


        // List files in the runtime
        File runtimeLibrary = new File(Main.ebash2path + "/lib/" + packageName);
        String[] list = null;
        
        if (runtimeLibrary.isDirectory()) list = runtimeLibrary.list();

        // If list is null, then it is a single file
        if (list == null) {
            classFiles.add(runtimeLibrary.getAbsolutePath() + ".ebsrc");
        }else{
            // For each file in the runtime
            for (String file : list) {
                // If the file is a directory, neglect
                if (new File(runtimeLibrary + "/" + file).isDirectory()) {
                    continue;
                }

                // If the file is not a .ebsrc file, neglect
                if (!file.endsWith(".ebsrc")) {
                    continue;
                }

                // Add to list
                File f = new File(file);
                classFiles.add(runtimeLibrary.getAbsolutePath() + File.separator + f.getName());
            }
        }

        for(String classFile : classFiles) {
            if (new File(classFile).isFile()) {
                System.out.println("Found class file: " + classFile);
            }else{
                System.out.println("Could not find class file: " + classFile);
                throw new RuntimeException("Could not find class file: " + classFile);
            }
        }

        return classFiles;
    }
}
