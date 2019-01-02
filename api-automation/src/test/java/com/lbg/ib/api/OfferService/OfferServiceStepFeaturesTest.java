package com.lbg.ib.api.OfferService;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/OfferService", format = { "pretty",
"html:target/site/cucumber-pretty", "json:target/reports/cucumber-OfferServiceStepFeaturesTest.json" }, tags = { "~@ignore" ,"@PCA-101"})
public class OfferServiceStepFeaturesTest {
}
