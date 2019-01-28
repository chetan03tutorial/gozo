package org.gozo.funkysanta.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
	
	public String readContent(InputStream is) {
		String content = null;
		try {
			content = IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.debug("unable to close the input stream");
				e.printStackTrace();
			}
		}
		return content;
	}

	public String read(String location) {
		try {
			InputStream is;
			URL url = getClass().getResource(location);
			is = url.openConnection().getInputStream();
			return readContent(is);
		} catch (IOException e) {
			// put a warning here
			// log the warning
			// stop the processing
			// service cannot be written without its configuration
			// ignore this configuration
			e.printStackTrace();
		}
		// throw an exception here
		return null;
	}

}
