package com.lbg.ib.api.retrievePartyDetails;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author 8903735
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = true, features = "src/test/resources/retrievePartyDetails", format = { "pretty",
"html:target/site/cucumber-pretty", "json:target/reports/cucumber-RetrievePartyDetails-cucumber.json" }, tags = { "~@ignore" })
public class RetrievePartyDetailsTest{

}
