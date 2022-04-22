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
        isValidRuntime(runtimeFiles);


    }

    // Check if the runtime is valid
    // Compare if the necessary files and the given list
    // to check if they exist in the runtime
    public static void isValidRuntime(ArrayList<String> givenRuntimeFiles) {

        String[] requiredFileList = {
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

        for(String requiredFile : requiredFileList) {

            boolean requiredFileExists = false;
            for(String runtimeFile : givenRuntimeFiles) {

                // Check if the runtime file exists
                if(runtimeFile.endsWith(requiredFile)) {

                    if (runtimeFile.contains("/lib/")) libs.add(runtimeFile);
                    else if (runtimeFile.contains("/base/")) base.add(runtimeFile);

                    requiredFileExists = true;
                    break;

                }
            }

            if(!requiredFileExists) {
                throw new RuntimeException("eBash2 runtime is not installed correctly. Missing file: " + requiredFile);
            }
        }
    }

    
}
