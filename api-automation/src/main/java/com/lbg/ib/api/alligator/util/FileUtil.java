package com.lbg.ib.api.alligator.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * File Utility component to load file
 */
public class FileUtil {

	protected static final Log logger = LogFactory.getLog(FileUtil.class);

	public static InputStream loadFile(String filePath) {
		InputStream in = null;
		try {
			logger.debug("Going to read file from the path === " + filePath);
			Thread currentThread = Thread.currentThread();
			ClassLoader contextClassLoader = currentThread.getContextClassLoader();
			in = contextClassLoader.getResourceAsStream(filePath);
			logger.debug("File Input stream is  === " + in);
		} catch (Exception ex) {

		}
		return in;

	}

	public static void writeFile(String filePath, String content)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		writer.println(content);
		writer.close();
	}

}
