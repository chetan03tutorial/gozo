package com.lbg.ib.api.overdraftFulfillment;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/overdraftFulfillment", glue = {"com.lbg.ib.api.overdraftFulfillment"}, format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-e169.json" }, tags = { "~@ignore" })
public class OverdraftFulfillmentFeatureTest {

}
