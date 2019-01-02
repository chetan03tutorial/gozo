package com.lbg.ib.api.e170;

import static org.junit.Assert.*;

import org.springframework.test.context.ContextConfiguration;

import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.PathParameter;
import com.lbg.ib.api.alligator.annotations.RequestBody;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class E170FeatureStep extends BaseFeatures {
	
    @Given("^token service is up and running for the user having token \"([^\"]*)\" for brand \"([^\"]*)\"$")
	@RestInvoker(service = "token", method = HttpMethod.POST)
	public void token_service_is_up_and_running_for_the_user_having_token(@RequestBody(file = true) String request, @Brand String brand) throws Throwable {

	}

	@Given("^user info service is also up and running \"([^\"]*)\"$")
	@RestInvoker(service = "userInfo", method = HttpMethod.GET)
	public void user_info_service_is_also_up_and_running(@Brand String brand) throws Throwable {

	}

	@Given("^then hit the overdraft upfront service with the request \"([^\"]*)\" for brand \"([^\"]*)\"$")
	@RestInvoker(service = "upfrontOverdraft", method = HttpMethod.POST)
	public void then_hit_the_overdraft_upfront_service_with_the_request(@RequestBody(file = true) String request, @Brand String brand) throws Throwable {

	}

	@Then("^verify the overdraft limit as \"([^\"]*)\" for \"([^\"]*)\"$")
	public void verify_the_overdraft_limit_as_for(String maxOverdraft, @Brand String brand) throws Throwable {
		assertTrue("Unexpected value of Email", maxOverdraft.equalsIgnoreCase((String) response.getPropValue("maxOverDraftLimit")));
	}

	
	@RestInvoker(service="e160", method=HttpMethod.POST)
	@Given("^then hit the overdraft amend service \"([^\"]*)\" with \"([^\"]*)\" for brand \"([^\"]*)\"$")
	public void then_hit_the_overdraft_remove_service_with_for_brand(@PathParameter(param="operation")String arg1, @RequestBody(file = true) String request, @Brand String brand) throws Throwable {
	}
	
	@Given("^validate the featureNextReviewFlagDate as \"([^\"]*)\"$")
	public void validate_the_currency_code_as(String featureNextReviewFlagDate) throws Throwable {
	    assertEquals(featureNextReviewFlagDate, (String)response.getPropValue("featureNextReviewFlagDate"));
	}

}
