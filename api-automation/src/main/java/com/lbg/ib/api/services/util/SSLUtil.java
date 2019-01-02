package com.lbg.ib.api.services.util;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.lbg.ib.api.automation.CertLoader;
import javax.net.ssl.*;

/**
 * @author Sapient
 *
 */
public class SSLUtil {

	
	public static Map<String, String> getCookies(String apiURL) {
		CertLoader loader = new CertLoader();
		KeyStore keystorePath = null;
		Response response = null;
		try {
			keystorePath = loader.loadCertificatesToKeyStore();
			HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
			public boolean verify(String string,SSLSession ssls) {
				return true;
			}
			});
					RestAssured.useRelaxedHTTPSValidation();

			//RestAssured.trustStore(keystorePath);
			response = given().get(apiURL).then()
					.contentType(ContentType.JSON).extract().response();
			
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.getCookies();
	}
	
	
	public static Map<String, String> getCookieMap(String apiURL, Map<String, String> cookieMap) {
		CertLoader loader = new CertLoader();
		KeyStore keystorePath = null;
		Response response = null;
		try {
			keystorePath = loader.loadCertificatesToKeyStore();
					RestAssured.useRelaxedHTTPSValidation();

			//RestAssured.trustStore(keystorePath);
			response = given().cookies(cookieMap).get(apiURL).then()
					.contentType(ContentType.JSON).extract().response();
			response.getSessionId();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.getCookies();
	}

}
