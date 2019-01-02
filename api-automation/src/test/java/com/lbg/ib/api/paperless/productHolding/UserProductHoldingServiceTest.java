package com.lbg.ib.api.paperless.productHolding;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author cshar8
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = {"src/test/resources/paperfreeStatus"}, glue = {"com.lbg.ib.api.paperless.productHolding"}, format = {"pretty", "html:target/site/cucumber-pretty", "json:target/reports/cucumber-PaperfreeStatusTest.json"}, tags = {"~@ignore"})
public class UserProductHoldingServiceTest {

}
