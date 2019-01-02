/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.paperless;
 
import org.junit.runner.RunWith;
 
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
 
/**
 * Feature file for paperless.
 * @author tkhann
 */
 
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/userMandateInfo", glue =
{"com.lbg.ib.api.automation", "com.lbg.ib.api.paperless"}, format = {"pretty",
        "html:target/site/cucumber-pretty",
        "json:target/reports/cucumber-UserMandateInfoFeatureTest.json"}, tags = {"~@ignore"})
public class UserMandateInfoFeatureTest {
 
}
