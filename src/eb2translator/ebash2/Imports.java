package eb2translator.ebash2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import eb2translator.Classes;
import eb2translator.FileUtils;
import eb2translator.InputOutputLocator;
import eb2translator.Main;
import eb2translator.ebash2.struct.EBClass;

public class Imports {
    
    public static void analyzeFromSource() throws IOException {

        System.out.println("Analyzing imports...");

        ArrayList<EBClass> c = analyze(InputOutputLocator.input);
        for(EBClass e : c) {
            Classes.add(e);
        }

        Main.print("Imported classes: ");
        for(EBClass e : Classes.classes.values()) {
            Main.print(e.classFileName.replace("/lib/", ""));
        }
        Main.print("Imported packages: ");
        for(String e : Classes.allPackages) {
            Main.print(e);
        }
        Main.print("Total imports: " + c.size());
    }

    public static ArrayList<EBClass> analyze(String file) throws IOException {
        ArrayList<String> fileList = FileUtils.recursiveFileList(file);
        ArrayList<EBClass> classes = new ArrayList<>();

        for (String f : fileList) {
            ArrayList<EBClass> classlist = parse(f);
            for (EBClass c : classlist) {
                classes.add(c);
            }
        }

        // Filter duplicates
        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                if (classes.get(i).path.equals(classes.get(j).path)) {
                    classes.remove(j);
                }
            }
        }

        return classes;
    }

    private static ArrayList<EBClass> parse(String file) throws IOException {
        String[] lines = FileUtils.readFile(file).split("\n");

        ArrayList<EBClass> classes = new ArrayList<>();
        ArrayList<String> duplicationChecker = new ArrayList<>();

        for (String line : lines) {

            // Check if the line is an importing line
            if (line.startsWith("@import ")) {
            

                // Get the package name by removing the @import
                String packageName = line.substring(line.indexOf("@import ") + "@import ".length());
                String packagePath = Main.ebash2path + "/lib/" + packageName;

                // If the package imported is a directory
                if (new File(packagePath).isDirectory()) {

                    // Recursively search the directory
                    ArrayList<String> packageFileList = FileUtils.recursiveFileList(packagePath);
                    
                    // Add the package path to the list
                    for (String packageFile : packageFileList) {

                        if (! new File(packageFile).isFile() || ! new File(packageFile).getAbsolutePath().endsWith(".ebsrc")) {
                            System.err.println("Error: " + packageFile + " does not exist, or is not a ebsrc file.");
                            System.exit(1);
                        }

                        if (duplicationChecker.contains(packageFile)) {
                            Main.warning("WARNING: " + packageFile + " is already imported.");
                            continue;
                        }

                        duplicationChecker.add(packageFile);
                        classes.add(new EBClass(packageFile));
                    }
                }

                // If not, directory, then
                else {
                    packagePath += ".ebsrc";

                    // Add the package path to the list
                    if (! new File(packagePath).isFile() || ! new File(packagePath).getAbsolutePath().endsWith(".ebsrc")) {
                        System.err.println("Error: " + packagePath + " does not exist, or is not a ebsrc file.");
                        System.exit(9);
                    }

                    if (duplicationChecker.contains(packagePath)) {
                        Main.warning("WARNING: " + packagePath + " is already imported.");
                        continue;
                    }

                    duplicationChecker.add(packagePath);
                    classes.add(new EBClass(packagePath));
                }
            }
        }
        return classes;
    }

}
