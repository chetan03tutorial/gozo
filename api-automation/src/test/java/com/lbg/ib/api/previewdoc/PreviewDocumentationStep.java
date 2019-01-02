/**
 * 
 */
package com.lbg.ib.api.previewdoc;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.test.services.PreviewDocService;
import com.lbg.ib.api.test.services.RetrieveProductFeaturesImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * @author sthak4
 *
 */
public class PreviewDocumentationStep extends BaseEnvSetUp {

	public static final String ENV = "env";
	private RetrieveProductFeaturesImpl retrieveProductFeaturesImpl;
	private PreviewDocService previewDocService;
	public Map<String, String> actualResult;
	private String host;
	private Properties envProperty;
	private String serviceEndPoint;
	private String arrangementId;

	/**
	 * @throws Exception
	 * 
	 */
	public PreviewDocumentationStep() throws Exception {
		super();
		previewDocService = new PreviewDocService();
		try {
			envProperty = this.getEnvProperty();

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	private String parseJson(String jsonMessage, String key) {

		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
		JsonElement jsonElement = o.get(key);
		if (jsonElement != null) {
			if (!jsonElement.isJsonNull()) {
				String value = jsonElement.toString();
				System.out.println("value:" + value);
				return value;
			}
		}

		return null;

	}

	public String readRequestData(String fileName) {
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		InputStream in = contextClassLoader.getResourceAsStream("jsonRequestFiles/" + fileName + ".json");
		String theString = null;
		try {
			theString = IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theString;
	}

	@Given("^I make a call to the Preview Document API \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
	public void i_make_a_call_to_the_preview_document_API(String retrieveEndpoint, String fileNetReferenceId,
			String success) throws Throwable {
		host = (String) envProperty.get(retrieveEndpoint);
		serviceEndPoint = host.concat(fileNetReferenceId);
		actualResult = previewDocService.getResponseFromAPI(serviceEndPoint, success);
		assertTrue(actualResult != null);
	}

	@Then("^I should see the valid document response \"([^\"]*)\"$")
	public void i_should_see_valid_document_response(String httpStatus) throws Throwable {
		String httpStatusActual = actualResult.get("StatusCode");
		String messageBody = actualResult.get("StatusMessage");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
		assertTrue(messageBody != null);

	}

	@Then("^I should not see the valid document response \"([^\"]*)\"$")
	public void i_should_not_see_valid_document_response(String httpStatus) throws Throwable {
		String httpStatusActual = actualResult.get("StatusCode");
		String messageBody = actualResult.get("StatusMessage");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
		assertTrue("\"600300\"".equals(parseJson(messageBody, "code")));
		assertTrue("\"FAILED_TO_RETRIEVE_FILE\"".equals(parseJson(messageBody, "message")));
	}

}
