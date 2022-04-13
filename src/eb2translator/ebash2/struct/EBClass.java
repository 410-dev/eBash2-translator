package eb2translator.ebash2.struct;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import eb2translator.FileUtils;
import eb2translator.Main;
import eb2translator.ebash2.Imports;

public class EBClass {
    public Dependency dependencies = null;
    public HashMap<String, String> function = new HashMap<>();

    public String path;
    public String classFileName;

    public EBClass(String filePath) throws IOException {

        path = filePath.replace(Main.ebash2path, "");

        classFileName = new File(filePath).getName();

        // Read string of the ebash file
        String content = "";
        try {
            content = FileUtils.readFile(filePath);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }

        // Split the string into lines
        String[] lines = content.split("\n");
        

        // For every line, identify the function.
        for(int i = 0; i < lines.length; i++) {

            // Check if it has an importing line
            if(lines[i].startsWith("@import ")) {
                // Import the current file
                dependencies = new Dependency(Imports.analyze(filePath));
            }

            String line = lines[i];
            String functionName = parseFunctionName(line);

            if(functionName != null) {
                String functionCode = "";

                for(int j = i + 1; j < lines.length; j++) {
                    String nextLine = lines[j];
                    i = j;
                    if(nextLine.trim().endsWith("}")) {
                        break;
                    }
                    functionCode += nextLine + "\n";
                    // i = j;
                }
                function.put(functionName, functionCode);
            }
        }
    }


    public String parseFunctionName(String content) {
        if (content.startsWith("function") && content.contains("{")) {
            String functionName = content.substring(content.indexOf("function ") + "function ".length());
            functionName = functionName.substring(0, functionName.indexOf("()"));
            functionName = functionName.replaceAll(" ", "");

            return functionName;
        }else{
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (dependencies != null) {
            sb.append(dependencies.toString());
        }

        for (String key : function.keySet()) {
            sb.append("function " + key + "() {\n");
            sb.append(function.get(key));
            sb.append("}\n\n");
        }

        return sb.toString();
    }
}
