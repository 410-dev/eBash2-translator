package eb2translator;

import java.io.File;

public class InputOutputLocator {

    public static boolean isDirectory = false;
    public static String input = "";
    public static String output = "";

    public static String getOutputPath(String file) {
        return output + File.separator + file.substring(input.length() + 1);
    }

    public static void getIOPath(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-i") || arg.equals("--input")) {    // --input
                Main.isLastArgument(args.length, i, arg);
                input = args[i + 1];
                i++;
            } else if (arg.equals("-o") || arg.equals("--output")) {    // --output
                Main.isLastArgument(args.length, i, arg);
                output = args[i + 1];
                i++;
            }
        }

        // TODO: DELETE THIS AFTER THE TEST IS COMPLETE
        input = "./sample";

        if (input.equals("")) {
            throw new RuntimeException("Input file is not specified.");
        }

        File f = new File(input);
        if (f.isDirectory()) {
            isDirectory = true;
        }

        if (output.equals("")) {
            if (isDirectory) {
                output = input + ".native";
            } else {
                output = f.getAbsolutePath() + ".native.bash";
            }
        }

        System.out.println("Input file: " + input);
        System.out.println("Output file: " + output);
        
    }
}
