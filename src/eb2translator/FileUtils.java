package eb2translator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static ArrayList<String> allFiles = new ArrayList<>();
    public static ArrayList<String> nonCompilableFiles = new ArrayList<>();

    // Read file to string
    public static String readFile(String path) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            reader.close();
            return stringBuilder.toString();
        }catch(Exception e) {
            throw new RuntimeException("Error reading file: " + path);
        }
    }

    // Build file list
    public static ArrayList<String> recursiveFileList(String directoyPath) {
        ArrayList<String> fileList = new ArrayList<>();

        String[] files = new File(directoyPath).list();

        if (files == null) {
            fileList.add(directoyPath);
            return fileList;
        };

        for (String file : files) {
            String target = directoyPath + File.separator + file;
            if (new File(target).isDirectory()) {
                ArrayList<String> subFileList = recursiveFileList(target);
                for (String subFile : subFileList) {
                    fileList.add(subFile);
                }
            } else {
                fileList.add(target);
            }
        }

        return fileList;
    }

    // Select files to compile
    public static void selectFiles() {

        allFiles.clear();
        nonCompilableFiles.clear();

        allFiles = recursiveFileList(InputOutputLocator.input);
        // Filter files with extensions: .ebsrc, .sh, .bash, .ebash
        for (int i = 0; i < allFiles.size(); i++) {
            String file = allFiles.get(i);
            if (!file.endsWith(".ebsrc") && !file.endsWith(".sh") && !file.endsWith(".bash") && !file.endsWith(".ebash")) {
                allFiles.remove(i);
                nonCompilableFiles.add(file);
                i--;
            }
        }
    }

    public static void writeFile(String outputPath, String string) {
        try {
            File file = new File(outputPath);
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(string);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Error writing file: " + outputPath);
        }
    }

    public static void copyNonCompilableFiles() {

        System.out.println("Copying non-compilable files...");

        for (String file : nonCompilableFiles) {
            String outputPath = InputOutputLocator.getOutputPath(file);
            System.out.println("Copying file: " + file + " to " + outputPath);
            try {
                File originalFile = new File(file);
                File outputFile = new File(outputPath);
                outputFile.getParentFile().mkdirs();
                InputStream in = new FileInputStream(originalFile);
                OutputStream out = new FileOutputStream(outputFile);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                    out.flush();
                }

                in.close();
                out.close();

            }catch(Exception e) {
                System.out.println("WARNING: Non-compilable file are note copied: " + file + " -> " + outputPath);
                e.printStackTrace();
            }
        }
    }
}
