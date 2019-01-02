package com.lbg.ib.api.services.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
/*import java.nio.file.Files;
import java.nio.file.Paths;*/

/**
 * 
 * File Utility component to load file
 */
public class FileUtil {

	/**
	 * @param filePath
	 * @return the file content given a filepath
	 */
	public static InputStream loadFile(String filePath) {
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		InputStream in = contextClassLoader.getResourceAsStream(filePath);
		return in;

	}

	public static void writeFile(String filePath, String content)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		System.out.println("content:" + content);
		writer.println(content);
		writer.close();
	}

	/*public static String readFile(String filePath) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(filePath)));
		System.out.println("content:" + content);
		return content;
	}*/
}

