package eb2translator;

import eb2translator.ebash2.Imports;
import eb2translator.ebash2.runtime.Runtime;

public class Main {

    public static String ebash2path = "/usr/local/eBash2";

    public static void main(String[] args) throws Exception {
        System.out.println("eBash2 to Native Bash Translator");
        System.out.println("Copyright (C) 2021 _410");
        System.out.println("");

        // Check if there is eBash runtime
        Runtime.checkRuntime(args);

        // Get IO path
        InputOutputLocator.getIOPath(args);

        // Get all buildable source codes (.sh, .bash, .ebash)
        FileUtils.selectFiles();

        // Import analysis
        Imports.analyzeFromSource();

        // Translate
        Translate.translate();

        // Copy non-compilable files
        FileUtils.copyNonCompilableFiles();
    }


    public static void isLastArgument(int length, int i, String arg) {
        if(i == length - 1) throw new RuntimeException("Unexpected end of argument: " + arg);
    }

    public static void print(String s) {
        System.out.println(s);
        try{Thread.sleep(2);}catch(Exception e){}
    }

    public static void warning(String s) {
        warnings++;
        System.out.println(s);
    }

}
