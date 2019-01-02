package com.lbg.ib.api.e170;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author ssama1
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/e170", glue = {"com.lbg.ib.api.e170"}, format = { "pretty",
				"html:target/site/cucumber-pretty",
				"json:target/reports/cucumber-e170.json" }, tags = { "~@ignore" })
public class E170FeatureTest {

}
