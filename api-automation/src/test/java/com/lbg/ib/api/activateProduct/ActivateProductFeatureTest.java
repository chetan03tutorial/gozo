package com.lbg.ib.api.activateProduct;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author ssama
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/activateProductFeature/", format = { "pretty",
"html:target/site/cucumber-pretty", "json:target/reports/cucumber-ActivateProductFeatureTest.json" }, tags = { "~@ignore" })
public class ActivateProductFeatureTest {

}
