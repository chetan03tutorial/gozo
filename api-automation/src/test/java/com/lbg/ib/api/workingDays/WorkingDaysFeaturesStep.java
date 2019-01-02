/**
 *
 */
package com.lbg.ib.api.workingDays;

import com.google.gson.JsonArray;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;
import com.lbg.ib.api.test.services.AddressLookupFeaturesImpl;
import com.lbg.ib.api.test.services.RecordProductArrangementQuestionnireImpl;
import com.lbg.ib.api.test.services.generic.GenericImpl;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class WorkingDaysFeaturesStep extends BaseEnvSetUp {

	private static final String ENV = "env";
	private GenericImpl genericImpl;
	Map<String, String> workingDaysMap;
	private String serviceEndPoint;


	public WorkingDaysFeaturesStep() throws Exception {
		super();
		genericImpl = new GenericImpl();
	}

	@Given("^I have a \"([^\"]*)\"$")
	public void i_have_a(String envHost)  throws Throwable {
		serviceEndPoint = new StringBuilder(this.getEnvProperty().getProperty(envHost)).toString();
		assertTrue(serviceEndPoint != null);

	}
	@When("^I try to get the response from the API$")
	public void i_try_to_get_the_response_from_the_API() throws Throwable {
		workingDaysMap = genericImpl.getResponseFromAPI(serviceEndPoint);
		assertTrue(workingDaysMap!=null);
	}

	@Then("^I should see http status code as \"([^\"]*)\"$")
	public void i_should_see_http_status_code_as(String httpStatus) throws Throwable {
		String httpStatusActual = workingDaysMap.get("StatusCode").replaceAll("\"", "");
		assertTrue(httpStatusActual.equalsIgnoreCase(httpStatus));
	}


	@Then("^I should check the number of holidays to be less than (\\d+) days$")
	public void i_should_check_the_number_of_holidays_to_be_less_than_days(int daysRange)  throws Throwable {
		String workingDays = workingDaysMap.get("StatusMessage").replaceAll("\"", "");
		String[] workingDayArr = workingDays.split(",");
		assertTrue(workingDayArr.length<daysRange);
	}

}
