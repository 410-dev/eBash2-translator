package eb2translator;

import eb2translator.ebash2.runtime.Runtime;

public class Main {

    private static int warnings = 0;
    private static long msec = 0;

    public static String ebash2path = "/usr/local/eBash2";

    public static String currentClass = "";
    public static String version = "1.0beta";

    public static void main(String[] args) throws Exception {
        System.out.println("eBash2 to Native Bash Translator");
        System.out.println("Copyright (C) 2022 _410");
        System.out.println("");


        // Time measure start
        long start = System.currentTimeMillis();

        // Parameters
        BasicArguments.parseParam(args);

        // Check if there is eBash runtime
        Runtime.checkRuntime(args);

        // Get IO path
        InputOutputLocator.getIOPath(args);

        // Get all buildable source codes (.sh, .bash, .ebash)
        FileUtils.selectFiles();

        // Translate
        Translate.translate();

        // Copy non-compilable files
        FileUtils.copyNonCompilableFiles();

        // Time measure end
        long end = System.currentTimeMillis();
        msec = end - start;

        // Build success
        printBuildSuccess();
    }


    public static void isLastArgument(int length, int i, String arg) {
        if(i == length - 1) throw new RuntimeException("Unexpected end of argument: " + arg);
    }

    public static void print(String s) {
        System.out.println(currentClass + s);
        try{Thread.sleep(2);}catch(Exception e){}
    }

    public static void warning(String s) {
        warnings++;
        System.out.println(s);
    }

    private static void printBuildSuccess() {
        String msg = "                                  " + "\n" +
"                               &@@@@@@@@@@@@(               " + "\n" +
"                                      %@@@@@@@@@            " + "\n" +
"                                        #@@@@@@@@@          " + "\n" +
"                                       @@@@@@@@@@@@@        " + "\n" +
"                                    ( @@@@@@@@@@@@@@(       " + "\n" +
"                                  @@@@@ .@@@@   ,@@@@@@@,   " + "\n" +
"                                @@@@@@@@@@        .@@@@@@   " + "\n" +
"                             ,@@@@@@@@@             (@/     " + "\n" +
"                           @@@@@@@@@#                       " + "\n" +
"                        ,@@@@@@@@@                          " + "\n" +
"                      @@@@@@@@@*                            " + "\n" +
"                   #@@@@@@@@@                               " + "\n" +
"                .@@@@@@@@@/                                 " + "\n" +
"              @@@@@@@@@@                                    " + "\n" +
"           @@@@@@@@@@@                                      " + "\n" +
"        @@@@@@@@@@@#                                        " + "\n" +
"     %@@@@@@@@@@@                                           " + "\n" +
"      @@@@@@@@@                                             " + "\n" +
"        @@@@@                                               ";

        String msg2 = "                                  " + "\n" +
"______       _ _     _                                  "+ "\n" +
"| ___ \\     (_) |   | |                                  "+ "\n" +
"| |_/ /_   _ _| | __| |  ___ _   _  ___ ___ ___  ___ ___ "+ "\n" +
"| ___ \\ | | | | |/ _` | / __| | | |/ __/ __/ _ \\/ __/ __|"+ "\n" +
"| |_/ / |_| | | | (_| | \\__ \\ |_| | (_| (_|  __/\\__ \\__ \\"+ "\n" +
"\\____/ \\__,_|_|_|\\__,_| |___/\\__,_|\\___\\___\\___||___/___/"+ "\n";
        System.out.println(msg + "\n" + msg2);


        System.out.println("Program build succeeded with " + warnings + " warnings.");
        System.out.println("Build time: " + msec + " ms (" + convertToMinutes(msec) + ")");
    }

    private static String convertToMinutes(long msec) {
        long minutes = msec / 60000;
        long seconds = (msec % 60000) / 1000;
        return minutes + "m " + seconds + " s";
    }
}
