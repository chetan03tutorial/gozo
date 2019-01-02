package com.lbg.ib.api.e169;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/e169", glue = {"com.lbg.ib.api.e169"}, format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-e169.json" }, tags = { "~@ignore" })
public class E169FeatureTest {

}
