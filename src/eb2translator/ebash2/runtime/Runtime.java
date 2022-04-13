package eb2translator.ebash2.runtime;

import java.io.IOException;
import java.util.ArrayList;

import eb2translator.FileUtils;
import eb2translator.Main;

public class Runtime {

    public static ArrayList<String> libs = new ArrayList<>();
    public static ArrayList<String> base = new ArrayList<>();

    public static void checkRuntime(String[] args) throws IOException {

        // Check if the parameter contains manual specification of eBash2 path
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.equals("-r") || arg.equals("--runtime")) {    // --runtime
                Main.isLastArgument(args.length, i, arg);
                Main.ebash2path = args[i + 1];
                i++;
            }
        }

        System.out.println("Runtime: " + Main.ebash2path);

        // Verify if eBash2 is installed
        ArrayList<String> runtimeFiles = FileUtils.recursiveFileList(Main.ebash2path);
        String[] requiredFiles = {
            "base/exception.ebbasesrc",
            "base/import.ebbasesrc",
            "base/include.ebbasesrc",
            "base/null.ebbasesrc",
            "base/quit.ebbasesrc",
            "base/runmodescript.ebbasesrc",
            "base/setpath.ebbasesrc",
            "base/version.ebbasesrc",
            "lib/Foundation/exception.ebsrc",
            "lib/Foundation/float.ebsrc",
            "lib/Foundation/in.ebsrc",
            "lib/Foundation/int.ebsrc",
            "lib/Foundation/out.ebsrc",
            "lib/Foundation/string.ebsrc",
        };
        isValidRuntime(requiredFiles, runtimeFiles);


    }

    public static void isValidRuntime(String[] requiredFiles, ArrayList<String> runtimeFiles) {
        for(String file : requiredFiles) {

            boolean foundFile = false;

            for(String runtimeFile : runtimeFiles) {

                if (runtimeFile.contains("/lib/")) libs.add(runtimeFile);
                else if (runtimeFile.contains("/base/")) base.add(runtimeFile);
                
                if(runtimeFile.endsWith(file)) {
                    foundFile = true;
                    break;
                }
            }

            if(!foundFile) {
                throw new RuntimeException("eBash2 runtime is not installed correctly. Missing file: " + file);
            }
        }
    }

    
}
