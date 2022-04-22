package eb2translator.ebash2.struct;

import java.util.ArrayList;

import eb2translator.exceptions.InvalidSyntaxException;

public class Function {
    private Class parentClass;
    public String name;
    public String code;
    public String returnType;
    public int requiredParameters;
    
    // Constructor
    public Function(Class parentClass, String name, String code, String returnType, int requiredParameters) {
        this.parentClass = parentClass;
        this.name = name;
        this.code = code;
        this.returnType = returnType;
        this.requiredParameters = requiredParameters;
    }

    // Get the parent class name
    public String getParentClassName() {
        return parentClass.name;
    }

    // Get the parent class path
    public String getParentClassLocation() {
        return parentClass.classFileLocation;
    }

    // Parse the function code
    // Function limits to have 255 parameters
    public static Function parse(Class parentClass, String functionName, String functionBodyWithoutSignature) {

        // Scan the parameters
        // pattern 1: $1-9
        // pattern 2: ${1 - 255}
        
        String[] chars = functionBodyWithoutSignature.split("");
        int requiredParameters = 0;

        for(int i = 0; i < chars.length; i++) {

            // Check if the current char is a parameter (starts with $)
            if (chars[i].equals("$")) {

                // Check if the is any other chars after the $
                if (i == chars.length - 1) {
                    throw new InvalidSyntaxException("Unexpected end of variable at function \"" + functionName + "\" (Not a number)");
                }

                // Check if the next char is a number
                if (chars[i + 1].matches("[0-9]")) {
                    int parameterNumber = Integer.parseInt(chars[i + 1]);
                    
                    // If the parameter number is greater than 255, throw an exception
                    if (parameterNumber > 255) {
                        throw new InvalidSyntaxException("Parameter may not be accessed over 255. Occurred at function \"" + functionName + "\"");
                    }

                    // If the parameter number is greater than the required parameters, ignore
                    // Otherwise, set to the required parameters
                    if (parameterNumber > requiredParameters) {
                        requiredParameters = parameterNumber;
                    }
                }

                // Check if the next char is an opening curly brace
                if (chars[i + 1].equals("{")) {

                    // Check if there is any closing curly brace after the opening curly brace
                    int closingCurlyBraceIndex = -1;
                    for(int j = i + 1; j < chars.length; j++) {
                        if (chars[j].equals("}")) {
                            closingCurlyBraceIndex = j;
                            break;
                        }
                    }

                    // If there is no closing curly brace, throw an exception
                    if (closingCurlyBraceIndex == -1) {
                        throw new InvalidSyntaxException("Unexpected end of variable at function \"" + functionName + "\" (No closing)");
                    }

                    // Find the length of the variable within the curly braces
                    int variableLength = 0;
                    for(int j = i + 2; j < closingCurlyBraceIndex; j++) {
                        variableLength++;
                    }

                    // Get the number of the parameter
                    String variableNumber = "";
                    for (int j = 0; j < variableLength; j++) {
                        variableNumber += chars[i + 2 + j];
                    }

                    // Try if the variable number is a number
                    // If not parsible, then it is not a parameter.
                    int parsedVariableNumber = -1;
                    try {
                        parsedVariableNumber = Integer.parseInt(variableNumber);
                    } catch (NumberFormatException ignored) {}

                    // If the variable number is greater than 255, throw an exception
                    if (parsedVariableNumber > 255) {
                        throw new InvalidSyntaxException("Parameter may not be accessed over 255. Occurred at function \"" + functionName + "\"");
                    }

                    // If the variable number is greater than the required parameters, ignore
                    // Otherwise, set to the required parameters
                    if (parsedVariableNumber > requiredParameters) {
                        requiredParameters = parsedVariableNumber;
                    }
                }
            } // End of parsing parameters

        }

        // Get the return type
        String returnType = "";
        
        // For each line
        String[] lines = functionBodyWithoutSignature.split("\n");
        for(int i = 0; i < lines.length; i++) {

            // If the line starts with "@return"
            if (lines[i].trim().startsWith("@return")) {
                
                // Get return type
                returnType = lines[i].trim().substring("@return".length());
                break;

            }
        }

        // Return the function
        return new Function(parentClass, functionName, functionBodyWithoutSignature, returnType, requiredParameters);
    }

    // Parse all functions from entire script
    public static ArrayList<Function> parseAll(Class parentClass, String script) {
        ArrayList<Function> functions = new ArrayList<>();

        // For each line
        // Detect function ____() {
        String[] lines = script.split("\n");

        for(int i = 0; i < lines.length; i++) {

            // If the line starts with "function"
            if (lines[i].trim().startsWith("function ")) {

                // Get the function name
                String functionName = lines[i].trim().substring(
                    "function ".length(), 
                    lines[i].trim().indexOf("(")
                );

                // Get the function body
                StringBuilder functionBodyBuilder = new StringBuilder();
                for(int j = i + 1; j < lines.length; j++) {
                    if (lines[j].trim().startsWith("}")) {
                        i = j;
                        break;
                    }
                    functionBodyBuilder.append(lines[j] + "\n");
                }

                // Parse the function
                Function function = Function.parse(parentClass, functionName, functionBodyBuilder.toString());

                // Add the function to the list
                functions.add(function);
            }
        }
        
        return functions;
    }
}
