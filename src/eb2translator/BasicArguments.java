package eb2translator;

public class BasicArguments {
    public static void parseParam(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("No arguments are specified.");
        }

        if (args[0].equals("-h") || args[0].equals("--help")) {
            System.out.println("Usage: java -jar eb2native.jar [options]");
            System.out.println("Options:");
            System.out.println("  -i, --input <file>  Input file.");
            System.out.println("  -o, --output <file> Output file.");
            System.out.println("  -r, --runtime <path> eBash2 runtime path.");
            System.out.println("  -h, --help           Show this help.");
            System.out.println("  -v, --version        Show version.");
            System.exit(0);
        }

        if (args[0].equals("-v") || args[0].equals("--version")) {
            System.out.println("eBash2 Native Translator v" + Main.version);
            System.exit(0);
        }
    }
}
