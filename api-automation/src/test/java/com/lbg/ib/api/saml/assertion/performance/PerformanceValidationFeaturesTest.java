package com.lbg.ib.api.saml.assertion.performance;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/performanceSSO", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-PerformanceValidationFeaturesTest.json" }, tags = { "~@ignore" })
public class PerformanceValidationFeaturesTest {

}
