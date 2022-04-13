package eb2translator;

import java.util.ArrayList;
import java.util.HashMap;

import eb2translator.ebash2.struct.EBClass;

public class Classes {
    public static ArrayList<String> allPackages = new ArrayList<>();
    public static HashMap<String, EBClass> classes = new HashMap<>();

    public static void add(String name, EBClass class1) {

        if (classes.containsKey(name)) {
            return;
        }

        classes.put(name, class1);

        String masterPackageName = class1.path.replace("/lib/", "");

        // Remove the last part of the path
        if (masterPackageName.contains("/")) {
            masterPackageName = masterPackageName.substring(0, masterPackageName.lastIndexOf("/"));
        }else{
            return;
        }

        if (!allPackages.contains(masterPackageName)) {
            allPackages.add(masterPackageName);
        }
    }
    
    public static void add(EBClass class1) {
        add(class1.path, class1);
    }

    public static ArrayList<EBClass> allFromMasterPackage(String masterPackageName) {
        ArrayList<EBClass> classes = new ArrayList<>();

        for (EBClass class1 : Classes.classes.values()) {
            if (class1.path.replace("/lib/", "").startsWith(masterPackageName)) {
                classes.add(class1);
            }
        }

        if (classes.size() == 0) {
            System.err.println("No such package: " + masterPackageName);
            System.exit(9);
        }

        return classes;
    }

    public static EBClass getClass(String classPath) {
        return classes.get(classPath);
    }
}
