package com.lb.ib.api.authjourney;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author ssama1
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/authJourneyFeature", format = { "pretty", 
"html:target/site/cucumber-pretty", "json:target/reports/cucumber-AuthJourneyProductFeatureTest.json" }, tags = { "~@ignore" })
public class AuthJourneyProductFeatureTest {
}

