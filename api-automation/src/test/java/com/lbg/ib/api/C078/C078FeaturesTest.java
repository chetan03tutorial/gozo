package com.lbg.ib.api.C078;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/C078", format = { "pretty",
		"html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-C078FeaturesTest.json" }, tags = { "~@ignore" })
public class C078FeaturesTest {
}
