package com.lbg.ib.api.DetermineEligibilityCI;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/DetermineEligibilityCI", format = { "pretty",
"html:target/site/cucumber-pretty","json:target/reports/cucumber-DetermineEligibilityCIFeaturesTest.json" }, tags = { "~@ignore" })
public class DetermineEligibilityCIFeaturesTest {
}
