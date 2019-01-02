package com.lbg.ib.api.saml.assertion;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/samlAssertion", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-SamlAssertionFeaturesTest.json" }, tags = { "~@ignore" })
public class SamlAssertionFeaturesTest {

}
