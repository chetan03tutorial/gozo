package com.lbg.ib.api.saml.validation;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/tokenValidation", glue = { "com.lbg.ib.api.automation",
		"com.lbg.ib.api.saml.validation", "com.lbg.ib.api.product" }, format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-SamlValidationFeaturesTest.json" }, tags = { "~@ignore" })
public class SamlValidationFeaturesTest {

}
