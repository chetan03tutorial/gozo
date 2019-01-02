/**
 * 
 */
package com.lbg.ib.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.jayway.restassured.RestAssured;
import org.apache.commons.io.IOUtils;

/**
 * @author ssama1
 *
 */

public class BaseEnvSetUp {
	
	private static String sessionId;
	
	private Properties envProperties;
	private static Map<String, String> cookieMap;

	public static Map<String, String> getCookieMap() {
		return cookieMap;
	}

	public static void setCookieMap(Map<String, String> cookieMap) {
		BaseEnvSetUp.cookieMap = cookieMap;
	}

	static {
		RestAssured.useRelaxedHTTPSValidation();
	}
	public void setEnvProperty() throws IOException {
		InputStream in = null;
		try {
			envProperties = new Properties();
			Thread currentThread = Thread.currentThread();
			ClassLoader contextClassLoader = currentThread.getContextClassLoader();
			in = contextClassLoader.getResourceAsStream("app.properties");
			envProperties.load(in);
		} finally {
			if (null != in) {
				in.close();
			}

		}

	}

	public Properties getEnvProperty() throws IOException {
		if (null == envProperties) {
			setEnvProperty();
		}
		return this.envProperties;
	}
	
	public String readRequestData(String filePath , String fileName)
	{
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		InputStream in = contextClassLoader.getResourceAsStream(filePath+"/"+fileName+".json");
		String theString = null;
		try {
			 theString = IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theString;
	}

	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		BaseEnvSetUp.sessionId = sessionId;
	}


	public String readRequestDataFromDir(String directory,String fileName)
	{
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		InputStream in = contextClassLoader.getResourceAsStream("jsonRequestFiles/C078/"+fileName+".json");
		String theString = null;
		try {
			theString = IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theString;
	}

	
}

