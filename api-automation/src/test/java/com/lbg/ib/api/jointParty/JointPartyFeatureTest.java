package com.lbg.ib.api.jointParty;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/jointParty", glue = {
		"com.lbg.ib.api.automation", "com.lbg.ib.api.jointParty" }, format = {
		"pretty", "html:target/site/cucumber-pretty",
		"json:target/reports/cucumber-jointPartyInfo.json" }, tags = { "~@ignore" })
public class JointPartyFeatureTest {

}
