package com.lbg.ib.api.conversionEligibility;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/conversionEligibility", format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-ConversionEligibilityFeaturesTest.json" }, tags = { "~@ignore" })
public class ConversionEligibilityFeaturesTest {

}
