package com.lbg.ib.api.sales.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class FileUtil {
    
    /*
     * public static File readFile(File file) { StringBuilder result = new
     * StringBuilder(); try (Scanner scanner = new Scanner(file)) { while
     * (scanner.hasNextLine()) { String line = scanner.nextLine();
     * result.append(line).append("\n"); } scanner.close();
     * 
     * } catch (IOException e) { e.printStackTrace(); } }
     */
    
    public static String readFile(String completeFilePath) {
        // URL url =
        // Thread.currentThread().getClass().getResource("/com/path/to/file.txt");
        // File file = new File(url.toURI());
        Thread currentThread = Thread.currentThread();
        ClassLoader contextClassLoader = currentThread.getContextClassLoader();
        InputStream in = null;
        String result = null;
        try {
            in = contextClassLoader.getResourceAsStream(completeFilePath);
            result = IOUtils.toString(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
        
    }

    public static InputStream loadFile(String filePath) {
        InputStream in = null;
        try {
            Thread currentThread = Thread.currentThread();
            ClassLoader contextClassLoader = currentThread.getContextClassLoader();
            in = contextClassLoader.getResourceAsStream(filePath);
        } catch (Exception ex) {

        }
        return in;

    }
    
}