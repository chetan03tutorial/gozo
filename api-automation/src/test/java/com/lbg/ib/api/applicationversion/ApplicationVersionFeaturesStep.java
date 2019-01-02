/**
 * 
 */
package com.lbg.ib.api.applicationversion;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.RetrieveProductArrangementQuestionnireImpl;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class ApplicationVersionFeaturesStep extends BaseEnvSetUp {
	
	private RetrieveProductArrangementQuestionnireImpl retrieveImpl;
	public Map<String, String> actualResult;	
	
	public ApplicationVersionFeaturesStep() throws Exception {
		super();
		retrieveImpl = new RetrieveProductArrangementQuestionnireImpl();
	}
	
	@Given("^Perform the application version operation using enpoint \"([^\"]*)\"$")
	public void fetchApplicationVersion(String endpoint) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		actualResult = retrieveImpl.getResponseFromAPI(url);
		assertTrue(actualResult != null);
	}	
	
	@Then("^verify ApplicationName equals \"([^\"]*)\"$")
	public void validateApplicationName(String applicationName) {
		
		String messageBody = actualResult.get("StatusMessage");
		String fetchedApplicationName = JsonUtils.parseJsonRecursive(messageBody, "ApplicationName").replaceAll("\"", "");
		assertTrue(applicationName.equals(fetchedApplicationName));
	}
	
	@Then("^verify ActiveXFlag equals \"([^\"]*)\"$")
	public void validateActiveXFlag(String authorized) {
		String messageBody = actualResult.get("StatusMessage");
		String fetchedActiveXFlag = JsonUtils.parseJsonRecursive(messageBody, "ActiveXFlag").replaceAll("\"", "");
		assertTrue(authorized.equals(fetchedActiveXFlag));
	}



}
