package com.lbg.ib.api.conversionFulfilment;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/conversionFulfilment", format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-ConversionFulfilmentFeaturesTest.json" }, tags = { "~@ignore" })
public class ConversionFulfilmentFeaturesTest {

}
