package com.lbg.ib.api.sales.gozo.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * File Utility component to load file
 */
@Component
public class ClassPathFileReader extends ApplicationFileReader {

	protected static final Log logger = LogFactory.getLog(ClassPathFileReader.class);

	public String read(String fileName) {
		String filePath = fileName + ".json";
		logger.debug("===file path is ====" + filePath);
		InputStream inputStream = loadFile(filePath);
		logger.debug("===Got file === " + filePath);
		String theString = null;
		try {
			theString = IOUtils.toString(inputStream, "UTF-8");
		} catch (IOException e) {
			logger.debug("unable to read the json request file from the path " + filePath);
			logger.debug("unable to read the json request file from the path " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.debug("unable to close the input stream to " + filePath);
				e.printStackTrace();
			}
		}
		return theString;
	}

	public static void writeFile(String filePath, String content)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		writer.println(content);
		writer.close();
	}

}
