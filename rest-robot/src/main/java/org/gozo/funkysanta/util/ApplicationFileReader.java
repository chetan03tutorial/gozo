package org.gozo.funkysanta.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;

public class ApplicationFileReader {

	protected static final Log logger = LogFactory.getLog(ClassPathFileReader.class);
	
	public InputStream loadFile(String filePath) {
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
