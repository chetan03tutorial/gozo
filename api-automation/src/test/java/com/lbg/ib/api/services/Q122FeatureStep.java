package com.lbg.ib.api.services;

import static org.junit.Assert.assertEquals;

import org.springframework.test.context.ContextConfiguration;

import com.lbg.ib.api.alligator.annotations.Brand;
import com.lbg.ib.api.alligator.annotations.RequestBody;
import com.lbg.ib.api.alligator.annotations.RestInvoker;
import com.lbg.ib.api.alligator.http.BaseFeatures;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

import cucumber.api.java.en.Given;

@ContextConfiguration(value = "classpath*:cucumber.xml")
public class Q122FeatureStep extends BaseFeatures {

	@RestInvoker(service = "q122", method = HttpMethod.POST)
	@Given("^asm decision service is up and running \"([^\"]*)\" \"([^\"]*)\"$")
	public void then_hit_asm_decision_service_with_for_brand(@Brand String brand,@RequestBody(file = true) String request) throws Throwable {

	}

	@Given("^validate the asm decision code as \"([^\"]*)\"$")
	public void validate_the_asm_decision_code_as(String expectedValue) throws Throwable {
		String actualValue = (String) response.getPropValue("creditScoreResultCode");
		assertEquals(expectedValue, actualValue);
	}

}