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
 * Feature file for update Email.
 * @author tkhann
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/modifyPreference", glue = {"com.lbg.ib.api.automation",
        "com.lbg.ib.api.paperless"}, format = {"pretty",
        "html:target/site/cucumber-pretty",
        "json:target/reports/cucumber-UpdateEmailFeatureTest.json"}, tags = {"~@ignore"})
public class ModifyPreferenceFeatureTest {

}
