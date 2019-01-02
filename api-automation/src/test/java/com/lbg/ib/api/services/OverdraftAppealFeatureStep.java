package com.lbg.ib.api.services;

import static org.junit.Assert.assertTrue;

import org.springframework.test.context.ContextConfiguration;

import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class OverdraftAppealFeatureStep extends BaseFeatures {

	
	@RestInvoker(service = "odAppeal", method = HttpMethod.GET)
	@Given("^overdraft appeal service is up and running \"([^\"]*)\"$")
	public void overdraft_appeal_service_is_up_and_running(@Brand String brand) throws Throwable {
	   
	}

	@Then("^validate the pld decision \"([^\"]*)\"$")
	public void validate_the_pld_decision(String expectedValue) throws Throwable {
		String actualValue = (String)response.getPropValue("creditScoreResultCode");
		assertTrue(actualValue.equalsIgnoreCase(expectedValue));
	}

}